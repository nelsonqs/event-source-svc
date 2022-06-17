CREATE TABLE events(
	id varchar(50) NOT NULL,
	name varchar(50) NOT NULL,
	status varchar(10) NOT NULL,
	createdTime bigint NOT NULL,
	correlationId varchar(36) NOT NULL,
	payload jsonb NOT NULL,
	PRIMARY KEY (id)
 )