-- testdb.dbo.plant definition

-- Drop table

-- DROP TABLE testdb.dbo.plant;

CREATE TABLE testdb.dbo.plant (
	id bigint IDENTITY(1,1) NOT NULL,
	created_at datetime2 NULL,
	family varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	name varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	scientific_name varchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
	CONSTRAINT PK_plant_id PRIMARY KEY (id),
	CONSTRAINT UK_plant_name_scientficName UNIQUE (name,scientific_name)
--	CONSTRAINT UK_plant_name UNIQUE (name),
--	CONSTRAINT UK_plant_scientific_name UNIQUE (scientific_name)
);
