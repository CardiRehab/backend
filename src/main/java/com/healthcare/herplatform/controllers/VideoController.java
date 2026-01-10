package com.healthcare.herplatform.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
//import com.healthcare.herplatform.models.ResponseData;
import com.healthcare.herplatform.services.VideoService;
//import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@RestController
@RequestMapping("/api/video")
public class VideoController {
	private VideoService videoService;
 
    public VideoController(VideoService videoService) {
		super();
		this.videoService = videoService;
	}

    @PreAuthorize("hasAnyRole('CRSPL', 'ADMIN')")
	@PostMapping("/upload")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            videoService.uploadVideo(file);
            return new ResponseEntity<>("File uploaded successfully!", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Failed to upload file.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
	
	@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP', 'ADMIN')")
	@GetMapping("/play/{id}")
    public ResponseEntity<byte[]> downloadVideo(@PathVariable Long id) {
    	try {
	        return videoService.getVideo(id);
    	}
        catch(Exception e) {
        	e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }
}






