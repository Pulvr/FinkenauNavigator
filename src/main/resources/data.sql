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
VALUES (1,1,'Haupteingang','1','Entrance'),
       (2,1,'Eingang E57','1','Entrance'),
       (3,1,'Fakultätsservicebüro E33','1','Room'),
       (4,1,'Erste Hilfe E38','1','Room'),
       (5,1,'E59','1','Room'),
       (6,1,'E62','1','Room'),
       (7,1,'E39','1','Room'),
       (8,1,'E48','1','Room'),
       (9,1,'Toilette E50','1','Room'),
       (10,1,'Toilette E60','1','Room'),
       (11,1,'Toilette E37','1','Room'),
       (12,0,'Treppenhaus bei E58','1','Stairway'),
       (13,0,'Treppenhaus bei Haupteingang','1','Stairway'),
       (14,0,'Flur E27 - E37','1','Floor'),
       (15,0,'Flur E58 - E68','1','Floor');

--bidirektionale Verbindung zwischen allen Räumen festlegen, Indikator dafür die ID
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (1, 14), (14, 1);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (2, 14), (14, 2), (2, 15), (15,2);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (3, 14), (14, 3);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (4, 14), (14, 4);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (5, 15), (15, 5);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (6, 15), (15, 6);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (7, 14), (14, 7);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (8, 14), (14, 8);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (9, 14), (14, 9);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (10, 15), (15, 10);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (11, 14), (14, 11);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (12, 15), (15, 12);
INSERT INTO ROOM_CONNECTION (FROM_ID, TO_ID) VALUES (13, 14), (14, 13);