--ein Gebäude hat eine ID und einen Namen
CREATE TABLE IF NOT EXISTS BUILDING
(
    ID      INTEGER PRIMARY KEY AUTO_INCREMENT,
    NAME    varchar(100) NOT NULL
);
--ein Raum hat einen namen, stockwerk und ist über foreign key mit einem Gebäude verknüpft
--manche Räume sollten nicht auswählbar sein, wie bspw der Flur
--die Bits ON_LEFT_SIDE und ON_RIGHT_SIDE gehen davon aus, dass man den Flur in die Richtung durchquert, in welcher die Raumnummern größer werden. Flure erhalten bei beiden Werten null
CREATE TABLE IF NOT EXISTS ROOM (
    ID          INTEGER PRIMARY KEY AUTO_INCREMENT,
    BUILDING_ID INTEGER NOT NULL,
    SELECTABLE  BIT NOT NULL,
    NAME        varchar(100) NOT NULL UNIQUE,
    FLOOR       varchar(100) NOT NULL,
    TYPE        varchar(100) NOT NULL,
    ON_LEFT_SIDE BIT,
    ON_RIGHT_SIDE BIT,
    FOREIGN KEY (BUILDING_ID) REFERENCES BUILDING(ID)
);

CREATE TABLE IF NOT EXISTS ROOM_CONNECTION (
   FROM_NAME varchar(100) NOT NULL,
   TO_NAME   varchar(100) NOT NULL,
   PRIMARY KEY (FROM_NAME, TO_NAME),
   FOREIGN KEY (FROM_NAME) REFERENCES ROOM(NAME),
   FOREIGN KEY (TO_NAME) REFERENCES ROOM(NAME)
);

INSERT INTO BUILDING (name)
VALUES ('Finkenau');

-- Sortierung der Tabelle wird manuell anhand ihrer Position in Relation zum Haupteingang hergestellt und so wird die Tabelle ebenfalls befüllt.
-- Ist ein Raum gemäß der Übersichtskarte rechts vom Haupteingang, so ist die ID höher. Ist der Raum links vom Haupteingang, so ist die ID niedriger.
-- Da die IDs automatisch erhöht werden, reicht es einen Raum also anhand der Relation auf der Karte zu platzieren, eine manuelle Änderung darf nicht gemacht werden!
-- Flure werden immer vor allen an sie verbundenen Räume gesetzt und haben damit die kleinste ID in Bezug auf alle angeschlossenen Räume
INSERT INTO ROOM(building_id,selectable,name,floor, type, on_left_side, on_right_side)
VALUES (1,0,'Treppenhaus bei Haupteingang','1','STAIRWAY',1,0),
       (1,1,'Haupteingang','1','ENTRANCE',1,0),
       --Alle Räume, die an Flur E27 - E37 angeschlossen sind
       (1,0,'Flur E27 - E37','1','FLOOR',null,null),
       (1,1,'Fakultätsservicebüro E33','1','ROOM',1,0),
       (1,1,'Toilette E37','1','ROOM',1,0),
       (1,1,'Erste Hilfe E38','1','ROOM',0,1),
       (1,1,'E39','1','ROOM',0,1),
       (1,1,'E48','1','ROOM',0,1),
       (1,1,'Toilette E50','1','ROOM',1,0),
       (1,1,'Eingang E57','1','ENTRANCE',0,1),
       --Alle Räume, die an Flur E58 - E68 angeschlossen sind
       (1,0,'Flur E58 - E68','1','FLOOR',null,null),
       (1,0,'Treppenhaus bei E58','1','STAIRWAY',1,0),
       (1,1,'E59','1','ROOM',0,1),
       (1,1,'Toilette E60','1','ROOM',1,0),
       (1,1,'E62','1','ROOM',0,1);

--bidirektionale Verbindung zwischen allen Räumen via Raumnamen
INSERT INTO ROOM_CONNECTION (FROM_NAME, TO_NAME)
VALUES  ('Haupteingang', 'Flur E27 - E37'), ('Flur E27 - E37', 'Haupteingang'),
        ('Eingang E57', 'Flur E27 - E37'), ('Flur E27 - E37', 'Eingang E57'), ('Eingang E57', 'Flur E58 - E68'), ('Flur E58 - E68','Eingang E57'),
        ('Fakultätsservicebüro E33', 'Flur E27 - E37'), ('Flur E27 - E37', 'Fakultätsservicebüro E33'),
        ('Erste Hilfe E38', 'Flur E27 - E37'), ('Flur E27 - E37', 'Erste Hilfe E38'),
        ('E59', 'Flur E58 - E68'), ('Flur E58 - E68', 'E59'),
        ('E62', 'Flur E58 - E68'), ('Flur E58 - E68', 'E62'),
        ('E39', 'Flur E27 - E37'), ('Flur E27 - E37', 'E39'),
        ('E48', 'Flur E27 - E37'), ('Flur E27 - E37', 'E48'),
        ('Toilette E50', 'Flur E27 - E37'), ('Flur E27 - E37', 'Toilette E50'),
        ('Toilette E60', 'Flur E58 - E68'), ('Flur E58 - E68', 'Toilette E60'),
        ('Toilette E37', 'Flur E27 - E37'), ('Flur E27 - E37', 'Toilette E37'),
        ('Treppenhaus bei E58', 'Flur E58 - E68'), ('Flur E58 - E68', 'Treppenhaus bei E58'),
        ('Treppenhaus bei Haupteingang', 'Flur E27 - E37'), ('Flur E27 - E37', 'Treppenhaus bei Haupteingang'),
        ('Flur E58 - E68', 'Flur E27 - E37'), ('Flur E27 - E37', 'Flur E58 - E68');