package com.healthcare.herplatform.controllers;

import com.healthcare.herplatform.models.ResponseData;
import com.healthcare.herplatform.repository.MessageRepository;
import com.healthcare.herplatform.entity.ChatAttachments;
import com.healthcare.herplatform.entity.Message;
import com.healthcare.herplatform.services.ChatAttachmentsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@RestController
@RequestMapping("/checkfileattachalgo")
public class ChatAttachmentsController {
	@Autowired
	MessageRepository messageRepository;
	
    private ChatAttachmentsService chatAttachmentsService;

    public ChatAttachmentsController(ChatAttachmentsService chatAttachmentsService) {
        this.chatAttachmentsService = chatAttachmentsService;
    }

//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity uploadFile(@RequestParam MultipartFile[] files) {
//        for (int i = 0; i < files.length; i++) {
//            logger.info(String.format("File name '%s' uploaded successfully.", files[i].getOriginalFilename()));
//        }
//        return ResponseEntity.ok().build();
//    }
    
    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @PostMapping("/uploadca")
    public ResponseData uploadFile(@RequestParam("file") MultipartFile file) throws Exception {
        ChatAttachments chatAttachments = null;
        String downloadURl = "";
        chatAttachments = chatAttachmentsService.saveAttachment(file);
        downloadURl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadca/")
                .path(chatAttachments.getId())
                .toUriString();

        return new ResponseData(chatAttachments.getFileName(),
                downloadURl,
                file.getContentType(),
                file.getSize());
    }

    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @GetMapping("/downloadca/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        ChatAttachments chatAttachments = null;
        chatAttachments = chatAttachmentsService.getAttachment(fileId);
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(chatAttachments.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + chatAttachments.getFileName()
                + "\"")
                .body(new ByteArrayResource(chatAttachments.getData()));
    }
       
    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @DeleteMapping("/deleteca/{fileId}")
    public String deleteFile(@PathVariable String fileId) throws Exception {
        String chatAttachments = null;
        chatAttachments = chatAttachmentsService.deleteAttachment(fileId);
        return chatAttachments; 
    }
    
    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @GetMapping("/getuserchathistory/{userName}")
   	public List<Message> getUserChatHistoryByName(@PathVariable("userName") String userName) throws Exception {
   		List<Message> getMessageHistoryList =  messageRepository.getMessageHistoryListByName(userName,userName);
   		
//   	System.out.println("\n\n==================================");
//   	getMessageHistoryList.forEach((s) -> System.out.println(s));
//		System.out.println("\n\n==================================");
		
   		return getMessageHistoryList;
   	}  
}
