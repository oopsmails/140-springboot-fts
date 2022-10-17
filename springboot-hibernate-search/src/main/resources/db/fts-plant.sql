-- ref: https://learn.microsoft.com/en-us/sql/t-sql/statements/create-fulltext-index-transact-sql?view=sql-server-ver16

--CREATE TABLE Persons (
--    PersonID int,
--    LastName varchar(255),
--    FirstName varchar(255),
--    Address varchar(255),
--    City varchar(255)
--);


--Create FULLTEXT CATALOG ftCat_testdb as default

--CREATE FULLTEXT INDEX ON Persons (LastName, FirstName) KEY INDEX person_ln_fn


DROP TABLE testdb.dbo.plant;

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


INSERT INTO plant (created_at,family,name,scientific_name) VALUES
	 ('2022-09-29 09:52:51.2451819',N'pinaceae',N'subalpine fir',N'abies lasiocarpa'),
	 ('2022-09-29 09:52:51.2451896',N'rosaceae',N'sour cherry',N'prunus cerasus'),
	 ('2022-09-29 09:52:51.2451945',N'rosaceae2',N'asian pear',N'pyrus pyrifolia'),
	 ('2022-09-29 09:52:51.2451965',N'rosaceae3',N'asiab pear',N'pyrus pyrifolia3'),
	 ('2022-09-29 09:52:51.2452010',N'hamamelidaceae',N'chinese witch hazel',N'hamamelis mollis'),
	 ('2022-09-29 09:52:51.2452061',N'sapindaceae',N'silver maple',N'acer saccharinum'),
	 ('2022-09-29 09:52:51.2452780',N'magnoliaceae',N'cucumber tree',N'magnolia acuminata'),
	 ('2022-09-29 09:52:51.2452874',N'ericaceae',N'korean rhododendron',N'rhododendron mucronulatum'),
	 ('2022-09-29 09:52:51.2452924',N'araceae',N'water lettuce',N'pistia'),
	 ('2022-09-29 09:52:51.2452962',N'fagaceae',N'sessile oak',N'quercus petraea');
INSERT INTO plant (created_at,family,name,scientific_name) VALUES
	 ('2022-09-29 09:52:51.2453007',N'moraceae',N'common fig',N'ficus carica');
INSERT INTO plant (created_at,family,name,scientific_name) VALUES
	 ('2022-09-29 09:52:51.2453007',N'moraceae2',N'common fig2',N'ficus carica2');


SELECT * FROM plant

CREATE FULLTEXT CATALOG plant_catalog;

CREATE FULLTEXT INDEX ON testdb.dbo.plant
 (
--  family
--     Language 1033,
  name
     Language 1033,
  scientific_name
     Language 1033
 )
  KEY INDEX PK_plant_id
      ON plant_catalog;


ALTER FULLTEXT INDEX ON testdb.dbo.plant SET CHANGE_TRACKING AUTO;

SELECT * FROM plant

SELECT *
FROM testdb.dbo.plant
WHERE CONTAINS(name, '"asian"') -- has to be whole word!!!

SELECT *
FROM testdb.dbo.plant
WHERE CONTAINS(scientific_name, 'pyrus')

SELECT id, created_at, family, name, scientific_name
FROM testdb.dbo.plant
WHERE
CONTAINS (name, 'asian')
OR CONTAINS (scientific_name, 'pyrus')

SELECT id, created_at, family, name, scientific_name
FROM testdb.dbo.plant
WHERE
CONTAINS (name, 'asian')
AND CONTAINS (scientific_name, 'pyrus')

