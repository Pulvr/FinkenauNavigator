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
    TYPE        varchar(100) NOT NULL,
    FOREIGN KEY (BUILDING_ID) REFERENCES BUILDING(ID)
);
--alle bidirektionalen Verbindungen vom Räumen zu Einander müssen einmalig gesetzt werden
CREATE TABLE IF NOT EXISTS ROOM_CONNECTION (
   FROM_ID INTEGER NOT NULL,
   TO_ID   INTEGER NOT NULL,
   PRIMARY KEY (FROM_ID, TO_ID),
   FOREIGN KEY (FROM_ID) REFERENCES ROOM(ID),
   FOREIGN KEY (TO_ID)   REFERENCES ROOM(ID)
);

INSERT INTO BUILDING (name)
VALUES ('Finkenau');

INSERT INTO ROOM(building_id,SELECTABLE,name,floor, type)
VALUES (1,1,'Haupteingang','1','ENTRANCE'),
       (1,1,'Eingang E57','1','ENTRANCE'),
       (1,1,'Fakultätsservicebüro E33','1','ROOM'),
       (1,1,'Erste Hilfe E38','1','ROOM'),
       (1,1,'E59','1','ROOM'),
       (1,1,'E62','1','ROOM'),
       (1,1,'E39','1','ROOM'),
       (1,1,'E48','1','ROOM'),
       (1,1,'Toilette E50','1','ROOM'),
       (1,1,'Toilette E60','1','ROOM'),
       (1,1,'Toilette E37','1','ROOM'),
       (1,0,'Treppenhaus bei E58','1','STAIRWAY'),
       (1,0,'Treppenhaus bei Haupteingang','1','STAIRWAY'),
       (1,0,'Flur E27 - E37','1','FLOOR'),
       (1,0,'Flur E58 - E68','1','FLOOR');

--bidirektionale Verbindung zwischen allen Räumen festlegen, Indikator dafür die ID
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID)
VALUES  (1, 14), (14, 1),
        (2, 14), (14, 2), (2, 15), (15,2),
        (3, 14), (14, 3),
        (4, 14), (14, 4),
        (5, 15), (15, 5),
        (6, 15), (15, 6),
        (7, 14), (14, 7),
        (8, 14), (14, 8),
        (9, 14), (14, 9),
        (10, 15), (15, 10),
        (11, 14), (14, 11),
        (12, 15), (15, 12),
        (13, 14), (14, 13),
        (15, 14), (14, 15);