package com.healthcare.herplatform.services;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Normalizes voice-message clips to AAC/.m4a so they play natively on iOS
 * (AVPlayer), Android (MediaPlayer) and the web.
 *
 * <p>Browsers (Chrome/Firefox) record WebM/Opus, which iOS cannot decode at all
 * and Android plays unreliably. Clips that are already AAC — mobile recordings
 * and Safari — are left untouched, so we only spend CPU when there is an actual
 * compatibility gap. Transcoding is best-effort: if ffmpeg is missing or fails,
 * the original bytes are returned unchanged so uploads never break.
 */
@Component
public class VoiceTranscoder {

    private static final Logger log = LoggerFactory.getLogger(VoiceTranscoder.class);

    // Containers/codecs iOS + Android both play natively; never re-encode these.
    private static final List<String> IOS_SAFE_EXTENSIONS = Arrays.asList("m4a", "mp4", "aac");

    // Path to the ffmpeg binary; override with app.ffmpeg.path if it is not on PATH.
    @Value("${app.ffmpeg.path:ffmpeg}")
    private String ffmpegPath;

    // Hard cap so a stuck ffmpeg can't hang the upload request thread.
    @Value("${app.ffmpeg.timeout-seconds:60}")
    private long timeoutSeconds;

    /** Outcome of a (possibly skipped) transcode. */
    public static class Result {
        public final String fileName;
        public final String contentType;
        public final byte[] data;

        public Result(String fileName, String contentType, byte[] data) {
            this.fileName = fileName;
            this.contentType = contentType;
            this.data = data;
        }
    }

    /**
     * Returns AAC/.m4a bytes for a web-recorded voice clip, or the original
     * input unchanged when transcoding is unnecessary or unavailable.
     */
    public Result transcodeIfNeeded(String fileName, String contentType, byte[] data) {
        Result original = new Result(fileName, contentType, data);
        if (data == null || data.length == 0 || fileName == null) {
            return original;
        }

        String lowerName = fileName.toLowerCase(Locale.ROOT);
        String ext = extensionOf(lowerName);

        // Only touch our own voice clips (named "voice-<ts>.<ext>"), never the
        // arbitrary files a user attaches through the paperclip.
        if (!lowerName.startsWith("voice-")) {
            return original;
        }
        // Already iOS/Android-friendly (mobile m4a, Safari mp4) -> leave as-is.
        if (IOS_SAFE_EXTENSIONS.contains(ext)) {
            return original;
        }

        Path in = null;
        Path out = null;
        File logFile = null;
        try {
            in = Files.createTempFile("voicein-", "." + (ext.isEmpty() ? "webm" : ext));
            out = Files.createTempFile("voiceout-", ".m4a");
            logFile = File.createTempFile("ffmpeg-", ".log");
            Files.write(in, data);

            ProcessBuilder pb = new ProcessBuilder(
                    ffmpegPath, "-y", "-hide_banner", "-loglevel", "error",
                    "-i", in.toAbsolutePath().toString(),
                    "-vn", "-ac", "1", "-c:a", "aac", "-b:a", "96k",
                    "-movflags", "+faststart",
                    out.toAbsolutePath().toString());
            pb.redirectErrorStream(true);
            pb.redirectOutput(logFile);

            Process p = pb.start();
            boolean finished = p.waitFor(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                p.destroyForcibly();
                log.warn("ffmpeg timed out transcoding voice clip {} after {}s; storing original",
                        fileName, timeoutSeconds);
                return original;
            }

            int code = p.exitValue();
            byte[] transcoded = (code == 0 && Files.exists(out)) ? Files.readAllBytes(out) : null;
            if (code != 0 || transcoded == null || transcoded.length == 0) {
                log.warn("ffmpeg failed (exit={}) on voice clip {}; storing original. ffmpeg: {}",
                        code, fileName, readLog(logFile));
                return original;
            }

            String newName = stripExtension(fileName) + ".m4a";
            log.info("Transcoded voice clip {} ({} bytes) -> {} ({} bytes, AAC)",
                    fileName, data.length, newName, transcoded.length);
            return new Result(newName, "audio/mp4", transcoded);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("Interrupted while transcoding voice clip {}; storing original", fileName);
            return original;
        } catch (Exception e) {
            // ffmpeg not installed, IO error, etc. — never block the upload.
            log.warn("Could not transcode voice clip {} ({}); storing original", fileName, e.toString());
            return original;
        } finally {
            deleteQuietly(in);
            deleteQuietly(out);
            if (logFile != null) {
                // best-effort cleanup
                if (!logFile.delete()) {
                    logFile.deleteOnExit();
                }
            }
        }
    }

    private static String extensionOf(String lowerName) {
        int dot = lowerName.lastIndexOf('.');
        if (dot < 0 || dot == lowerName.length() - 1) {
            return "";
        }
        return lowerName.substring(dot + 1);
    }

    private static String stripExtension(String name) {
        int dot = name.lastIndexOf('.');
        return dot < 0 ? name : name.substring(0, dot);
    }

    private static String readLog(File f) {
        try {
            byte[] b = Files.readAllBytes(f.toPath());
            String s = new String(b, StandardCharsets.UTF_8).trim();
            return s.length() > 500 ? s.substring(0, 500) : s;
        } catch (Exception e) {
            return "(no log)";
        }
    }

    private static void deleteQuietly(Path p) {
        if (p == null) {
            return;
        }
        try {
            Files.deleteIfExists(p);
        } catch (Exception ignored) {
            // best-effort
        }
    }
}
