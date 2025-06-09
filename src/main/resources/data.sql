--ein Gebäude hat eine ID und einen Namen
CREATE TABLE IF NOT EXISTS BUILDING
(
    ID      INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME    varchar(100) NOT NULL
);
--ein Raum hat einen namen, stockwerk und ist über foreign key mit einem Gebäude verknüpft
--manche Räume sollten nicht auswählbar sein, wie bspw der Flur
CREATE TABLE IF NOT EXISTS ROOM(
    ID          INTEGER PRIMARY KEY AUTO_INCREMENT,
    BUILDING_ID INTEGER NOT NULL,
    SELECTABLE  BIT NOT NULL,
    NAME        varchar(100) NOT NULL,
    FLOOR       varchar(100) NOT NULL,
    FOREIGN KEY (BUILDING_ID) REFERENCES BUILDING(ID)
);

INSERT INTO BUILDING (name)
VALUES ('Finkenau');

INSERT INTO ROOM(building_id,SELECTABLE,name,floor)
VALUES (1,1,'Haupteingang','1'),
       (1,1,'Eingang E57','1'),
       (1,1,'Fakultätsservicebüro E33','1'),
       (1,1,'Erste Hilfe E38','1'),
       (1,1,'E59','1'),
       (1,1,'E62','1'),
       (1,1,'E39','1'),
       (1,1,'E48','1'),
       (1,1,'Toilette E50','1'),
       (1,1,'Toilette E60','1'),
       (1,1,'Toilette E37','1'),
       (1,0,'Treppenhaus bei E58','1'),
       (1,0,'Treppenhaus bei Haupteingang','1'),
       (1,0,'Flur E27 - E37','1'),
       (1,0,'Flur E58 - E68','1');
