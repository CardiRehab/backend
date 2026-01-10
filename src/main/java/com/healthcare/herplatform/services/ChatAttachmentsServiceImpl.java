package com.healthcare.herplatform.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.healthcare.herplatform.entity.ChatAttachments;
import com.healthcare.herplatform.repository.ChatAttachmentsRepository;

@Service
public class ChatAttachmentsServiceImpl implements ChatAttachmentsService{

    private ChatAttachmentsRepository chatAttachmentsRepository;

    public ChatAttachmentsServiceImpl(ChatAttachmentsRepository chatAttachmentsRepository) {
        this.chatAttachmentsRepository = chatAttachmentsRepository;
    }

    @Override
    public ChatAttachments saveAttachment(MultipartFile file) throws Exception {
       String fileName = StringUtils.cleanPath(file.getOriginalFilename());
       try {
            if(fileName.contains("..")) {
                throw  new Exception("Filename contains invalid path sequence "
                + fileName);
            }

            ChatAttachments chatAttachments
                    = new ChatAttachments(fileName,
                    file.getContentType(),
                    file.getBytes());
            return chatAttachmentsRepository.save(chatAttachments);

       } catch (Exception e) {
            throw new Exception("Could not save File: " + fileName);
       }
    }

    @Override
    public ChatAttachments getAttachment(String fileId) throws Exception {
        return chatAttachmentsRepository
                .findById(fileId)
                .orElseThrow(
                        () -> new Exception("File not found with Id: " + fileId));
    }
    
    @Override
    public String deleteAttachment(String fileId) throws Exception {
	    try {		
	        chatAttachmentsRepository.deleteById(fileId);
	        return "Attachment Deleted Successfully!";
	        
	    } catch (Exception e) {
	        throw new Exception("Could not delete the file with fileId : " + fileId);
	    }       
    }
}
