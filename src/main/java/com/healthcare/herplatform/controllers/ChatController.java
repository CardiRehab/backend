package com.healthcare.herplatform.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import com.healthcare.herplatform.entity.Message;
//import com.healthcare.herplatform.exception.ResourceNotFoundException;
import com.healthcare.herplatform.repository.MessageRepository;

//@CrossOrigin(origins = {"https://mbzjku.csb.app", "https://www.cardirehab.com:8444", "https://cardirehab.com:8444", "https://preprod.cardirehab.com:8444", "https://www.cardirehab.com", "https://cardirehab.com", "https://preprod.cardirehab.com", "http://cardirehab.com:9595", "http://www.cardirehab.com:9595", "http://preprod.cardirehab.com:9595", "http://195.35.20.166:9595", "http://localhost:3000", "http://localhost:3002"} , allowCredentials="true" , maxAge = 3600)

@Controller
public class ChatController {

	@Autowired
	MessageRepository messageRepository;
	
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

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
        //System.out.println(message.toString());
        return message;
    }
    public void saveMessage(@RequestBody Message message) {
    	messageRepository.save(message);
    }
}
