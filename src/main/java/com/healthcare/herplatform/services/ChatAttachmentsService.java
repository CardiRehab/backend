package com.healthcare.herplatform.services;

import org.springframework.web.multipart.MultipartFile;

import com.healthcare.herplatform.entity.ChatAttachments;

public interface ChatAttachmentsService {
	ChatAttachments saveAttachment(MultipartFile file) throws Exception;
    ChatAttachments getAttachment(String fileId) throws Exception;
    String deleteAttachment(String fileId) throws Exception;
}
