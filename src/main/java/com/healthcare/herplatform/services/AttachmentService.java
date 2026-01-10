package com.healthcare.herplatform.services;

import org.springframework.web.multipart.MultipartFile;
import com.healthcare.herplatform.entity.Attachment;

public interface AttachmentService {
	Attachment saveAttachment(MultipartFile file) throws Exception;
    Attachment getAttachment(String fileId) throws Exception;
    String deleteAttachment(String fileId) throws Exception;
}
