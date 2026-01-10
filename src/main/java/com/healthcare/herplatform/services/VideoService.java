package com.healthcare.herplatform.services;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;

public interface VideoService {
	ResponseEntity<String> uploadVideo(MultipartFile file) throws Exception;
	ResponseEntity<byte[]> getVideo(Long id) throws Exception;
    //String deleteVideo(Long fileId) throws Exception;
}






