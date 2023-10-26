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

import java.util.Date;

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
	@Column(name = "type_event")
	private String typeEvent;
	private String status;
	@Column(name = "cod_agenda")
	private String codAgenda;
	@Column(name = "created_time")
	private long createdTime;
	@Column(name = "agenda_basica",columnDefinition = "jsonb")
	@Type(type = "jsonbType")
	private Object agendaBasica;
	@Column(name = "agenda_detalle", columnDefinition = "jsonb")
	@Type(type = "jsonbType")
	private Object agendaDetalle;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "update_date")
	private Date updateDate;
	@Column(name = "cod_user")
	private String codUser;
}
