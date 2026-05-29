package com.healthcare.herplatform.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.healthcare.herplatform.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
	@Query(value="Select * from messages where messages.sender_name=?1 OR messages.receiver_name=?2 ORDER BY created_at ASC",nativeQuery=true) // Native query or SQL
	public List<Message> getMessageHistoryListByName(String senderName, String receiverName);

	@Modifying
	@Query(value="DELETE FROM messages WHERE sender_name=?1 OR receiver_name=?1", nativeQuery=true)
	void deleteByUsername(String username);

	// Attachment ids referenced by this user's messages, so their stored file
	// blobs can be purged too (messages.fileId holds the chat_attachments.id).
	@Query(value="SELECT file_id FROM messages WHERE (sender_name=?1 OR receiver_name=?1) AND file_id IS NOT NULL AND file_id <> ''", nativeQuery=true)
	List<String> findFileIdsByUsername(String username);
}

