package com.healthcare.herplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query(value="Select * from messages where messages.sender_name=?1 OR messages.receiver_name=?2 ORDER BY created_at ASC",nativeQuery=true) // Native query or SQL
	public List<Message> getMessageHistoryListByName(String senderName, String receiverName);
}

