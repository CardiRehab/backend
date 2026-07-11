package com.healthcare.herplatform.controllers;

import com.healthcare.herplatform.models.ResponseData;
import com.healthcare.herplatform.repository.MessageRepository;
import com.healthcare.herplatform.entity.ChatAttachments;
import com.healthcare.herplatform.entity.Message;
import com.healthcare.herplatform.entity.Status;
import com.healthcare.herplatform.services.ChatAttachmentsService;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@RestController
@RequestMapping("/checkfileattachalgo")
public class ChatAttachmentsController {
	@Autowired
	MessageRepository messageRepository;

	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	// How recent (hours) a message may still be deleted by its sender. Edit the
	// single property `chat.message.delete-window-hours` to change it everywhere.
	@Value("${chat.message.delete-window-hours:24}")
	private long deleteWindowHours;

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

        // Report the stored (possibly transcoded) metadata, not the upload's —
        // a WebM clip may have been normalized to AAC/.m4a during save.
        long storedSize = chatAttachments.getData() != null
                ? chatAttachments.getData().length
                : file.getSize();
        return new ResponseData(chatAttachments.getFileName(),
                downloadURl,
                chatAttachments.getFileType(),
                storedSize);
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

    /** The configurable recency window (hours) within which a sender may still
     *  delete a message. Clients read this to gate the delete option. */
    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @GetMapping("/messagedeletewindowhours")
    public Map<String, Object> getMessageDeleteWindowHours() {
        Map<String, Object> body = new HashMap<>();
        body.put("hours", deleteWindowHours);
        return body;
    }

    /** Delete a message for everyone. Only the original sender may delete, and
     *  only within the configured recency window. Broadcasts a realtime DELETE
     *  event to both parties so any open chat updates live. */
    @PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @DeleteMapping("/deletemessage/{id}")
    public ResponseEntity<?> deleteMessage(@PathVariable("id") Long id, Authentication authentication) {
        Optional<Message> opt = messageRepository.findById(id);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
        }
        Message message = opt.get();

        // Only the original sender may delete their own message.
        String currentUser = authentication != null ? authentication.getName() : null;
        if (currentUser == null || !currentUser.equals(message.getSenderName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own messages");
        }

        // Enforce the configurable recency window.
        Date createdAt = message.getCreatedAt();
        long windowMs = deleteWindowHours * 60L * 60L * 1000L;
        if (createdAt == null || (System.currentTimeMillis() - createdAt.getTime()) > windowMs) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("This message is too old to delete (limit " + deleteWindowHours + "h)");
        }

        // Purge any attached file blob first (best-effort), then the message row.
        String fileId = message.getFileId();
        if (fileId != null && !fileId.trim().isEmpty()) {
            try {
                chatAttachmentsService.deleteAttachment(fileId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        messageRepository.deleteById(id);

        // Realtime "removed" event to both parties so any open chat updates live.
        Message event = new Message();
        event.setId(id);
        event.setSenderName(message.getSenderName());
        event.setReceiverName(message.getReceiverName());
        event.setStatus(Status.DELETE);
        try {
            simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", event);
            simpMessagingTemplate.convertAndSendToUser(message.getSenderName(), "/private", event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String, Object> body = new HashMap<>();
        body.put("deleted", true);
        body.put("id", id);
        return ResponseEntity.ok(body);
    }
}
