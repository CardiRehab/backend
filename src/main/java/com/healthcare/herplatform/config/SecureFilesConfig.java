package com.healthcare.herplatform.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration for the shared patient-education file store served by
 * {@code GET /api/auth/files/{filename}}.
 *
 * <p>{@code basePath} is where the files live on disk. {@code catalog} is the closed set of items
 * the endpoint will serve (V-02) — a filename absent from it is refused without the filesystem
 * being touched at all, so the endpoint no longer serves "whatever happens to be in the directory".
 *
 * <p>The catalogue is configuration rather than code so that the Board can publish new rehab
 * material by editing properties, without a code change. It is declared in
 * {@code application.properties} and applies to every environment; individual entries can still be
 * overridden per environment, or externally at the server, through the usual Spring property
 * sources.
 */
@Component
@ConfigurationProperties(prefix = "secure.files")
public class SecureFilesConfig {
    private String basePath;

    private List<CatalogItem> catalog = new ArrayList<>();

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public List<CatalogItem> getCatalog() {
        return catalog;
    }

    public void setCatalog(List<CatalogItem> catalog) {
        this.catalog = catalog;
    }

    /**
     * One publishable item of patient-education material.
     *
     * <p>{@code filename} is the on-disk name and the value the clients put in the URL — the web
     * bundle and the shipped mobile app both hard-code these strings, so they are the contract.
     * {@code contentType} is served verbatim and is never probed from the file, so a file whose
     * bytes disagree with its declared type cannot talk the browser into a different handler.
     * {@code id} is a stable handle for logs and for any future listing endpoint; {@code title} is
     * the human label for the same.
     */
    public static class CatalogItem {
        private String id;
        private String filename;
        private String contentType;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getContentType() {
            return contentType;
        }

        public void setContentType(String contentType) {
            this.contentType = contentType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
