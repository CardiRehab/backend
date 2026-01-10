package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.ChatAttachments;

@Repository
public interface ChatAttachmentsRepository extends JpaRepository<ChatAttachments, String> {

}

