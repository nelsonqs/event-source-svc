package com.event.source.svc.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.event.source.svc.db.entity.EventEntity;

public interface EventsRepository extends JpaRepository<EventEntity, String> {
	
	List<EventEntity> findByEntityId(String entityId);
	
	List<EventEntity> findByEntityIdAndType(String entityId,String type);

}
