package com.event.source.svc.db.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import com.event.source.svc.db.types.JsonBinaryType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "events")
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TypeDef(name = "jsonbType",typeClass = JsonBinaryType.class)
public class EventEntity {

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID",strategy = "uuid2")
	private String id;
	private String name;
	private String status;
	private String correlationId;
	private long createdTime;
	@Column(columnDefinition = "jsonb")
	@Type(type = "jsonbType")
	private Object payload;
}
