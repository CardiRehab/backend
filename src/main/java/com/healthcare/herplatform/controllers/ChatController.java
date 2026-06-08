package com.healthcare.herplatform.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import com.healthcare.herplatform.entity.Message;
import com.healthcare.herplatform.entity.Status;
//import com.healthcare.herplatform.exception.ResourceNotFoundException;
import com.healthcare.herplatform.repository.MessageRepository;
import com.healthcare.herplatform.services.PushNotificationService;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@Controller
public class ChatController {

	@Autowired
	MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private PushNotificationService pushNotificationService;

    //@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @MessageMapping("/message") // /app/message
    @SendTo("/chatroom/public")
    public Message receivePublicMessage(@Payload Message message){
        return message;
    }

    //@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @MessageMapping("/private-joining-message") // /app/private-joining-message
    public Message receivePrivateJoiningMessage(@Payload Message message){
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/privatejoining",message); // /user/David/privatejoining
        return message;
    }

    //@PreAuthorize("hasAnyRole('PATIENT', 'CRSPL', 'LHCP')")
    @MessageMapping("/private-message") // /app/private-message
    public Message receivePrivateMessage(@Payload Message message){
    	/* Storing message to database*/
    	try {
    	   this.saveMessage(message);
    	}catch(Exception e) {
    		e.printStackTrace();
    	}

        simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(),"/private",message); // /user/David/private

        /* Push notification to the recipient's devices (best-effort; never fails the message) */
        try {
            sendChatPushNotification(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println(message.toString());
        return message;
    }

    private void sendChatPushNotification(Message message) {
        if (message == null) {
            return;
        }
        Status status = message.getStatus();
        if (status != Status.MESSAGE && status != Status.MSG_ATTACH) {
            return; // skip join/leave system events
        }
        String sender = message.getSenderName() == null ? "" : message.getSenderName();
        String body;
        if (status == Status.MSG_ATTACH) {
            String fileName = message.getFileName();
            if (isAudioFileName(fileName)) {
                body = "🎤 Voice message";
            } else {
                body = (fileName != null && !fileName.trim().isEmpty()) ? ("📎 " + fileName) : "Sent an attachment";
            }
        } else {
            String text = message.getMessage();
            body = (text != null && !text.trim().isEmpty()) ? text : "New message";
        }
        Map<String, String> data = new HashMap<>();
        data.put("type", "chat");
        data.put("sender", sender);
        pushNotificationService.sendToUser(message.getReceiverName(), sender.isEmpty() ? "New message" : sender, body, data);
    }

    public void saveMessage(@RequestBody Message message) {
    	messageRepository.save(message);
    }

    /** Voice clips are uploaded as attachments named "voice-<ts>.<ext>" or with an audio extension. */
    private boolean isAudioFileName(String fileName) {
        if (fileName == null) {
            return false;
        }
        String lower = fileName.trim().toLowerCase();
        if (lower.isEmpty()) {
            return false;
        }
        if (lower.startsWith("voice-")) {
            return true;
        }
        return lower.endsWith(".webm") || lower.endsWith(".m4a") || lower.endsWith(".mp4")
                || lower.endsWith(".mp3") || lower.endsWith(".ogg") || lower.endsWith(".oga")
                || lower.endsWith(".opus") || lower.endsWith(".wav") || lower.endsWith(".aac");
    }
}
