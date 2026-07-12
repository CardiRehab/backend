package com.healthcare.herplatform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthcare.herplatform.entity.SecondOpinionAttachment;

@Repository
public interface SecondOpinionAttachmentRepository extends JpaRepository<SecondOpinionAttachment, Long> {
}
