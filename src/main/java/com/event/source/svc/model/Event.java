package com.event.source.svc.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    private String id;
	@NotBlank(message = "Nombre de Evento no puede ser nulo o vacio.")
	private String typeEvent;
	private String status;
	/*@NotBlank(message = "EntityId cannot be null or empty.")
	private String entityId;*/
	private String codAgenda;
	private long createdTime;
	@NotNull(message = "Agenda básica no puede ser nulo o vacío.")
	private Object agendaBasica;
	@NotNull(message = "Agenda detalle no puede ser nulo o vacío.")
	private Object agendaDetalle;
	private Date createDate;
	private Date updateDate;
	private String codUser;

}
