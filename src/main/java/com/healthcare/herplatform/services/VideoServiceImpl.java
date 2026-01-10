package com.healthcare.herplatform.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.healthcare.herplatform.entity.Video;
import com.healthcare.herplatform.repository.VideoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@Service
public class VideoServiceImpl implements VideoService {
	private VideoRepository videoRepo;

	public VideoServiceImpl(VideoRepository videoRepo) {
		super();
		this.videoRepo = videoRepo;
	}

	@Override
	public ResponseEntity<String> uploadVideo(MultipartFile file) throws Exception {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (fileName.contains("..")) {
				throw new Exception("Filename contains invalid path sequence " + fileName);
			}

			Video video = new Video();
			video.setName(file.getOriginalFilename());
			video.setData(file.getBytes());

			videoRepo.save(video);
			return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);

		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ResponseEntity<byte[]> getVideo(Long id) throws Exception {
		Optional<Video> videoOptional = videoRepo.findById(id);
		try {
			if (videoOptional.isPresent()) {
				Video video = videoOptional.get();

				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + video.getName() + "\"")
						.contentType(MediaType.valueOf("video/mp4")).body(video.getData());
			}else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.notFound().build();
		}
	}

//	@Override
//    public String deleteAttachment(String fileId) throws Exception {
//	    try {		
//	        attachmentRepository.deleteById(fileId);
//	        return "Attachment Deleted Successfully!";
//	        
//	    } catch (Exception e) {
//	        throw new Exception("Could not delete the file with fileId : " + fileId);
//	    }       
//    }
}
