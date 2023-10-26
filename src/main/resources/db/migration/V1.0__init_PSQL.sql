CREATE TABLE events(
	id varchar(50) NOT NULL,
	type_event varchar(50) NOT NULL,
	status varchar(10) NOT NULL,
	cod_agenda varchar(50) NOT NULL,
	created_time bigint NOT NULL,
	agenda_basica jsonb NOT NULL,
	agenda_detalle jsonb NOT NULL,
	create_date timestamp NOT NULL ,
    update_date timestamp,
    cod_user varchar(3)  NOT NULL ,
	PRIMARY KEY (id)
)