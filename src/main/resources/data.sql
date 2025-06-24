INSERT INTO BUILDING (name)
VALUES ('Finkenau');

-- Sortierung der Tabelle wird manuell anhand ihrer Position in Relation zum Haupteingang hergestellt und so wird die Tabelle ebenfalls befüllt.
-- Ist ein Raum gemäß der Übersichtskarte rechts vom Haupteingang, so ist die ID höher. Ist der Raum links vom Haupteingang, so ist die ID niedriger.
-- Da die IDs automatisch erhöht werden, reicht es einen Raum also anhand der Relation auf der Karte zu platzieren, eine manuelle Änderung darf nicht gemacht werden!
-- Flure werden immer vor allen an die verbundenen Räume gesetzt und haben damit die kleinste ID in Bezug auf alle angeschlossenen Räume
INSERT INTO ROOM(building_id,selectable,name,floor, type, on_left_side, on_right_side)
VALUES (1,0,'Treppenhaus bei Haupteingang','1','STAIRWAY',1,0),
       (1,1,'Haupteingang','1','ENTRANCE',1,0),
       --Alle Räume, die an Flur E27 - E57 angeschlossen sind
       (1,0,'Flur E27 - E57','1','FLOOR',null,null),
       (1,1,'Fakultätsservicebüro E33','1','ROOM',1,0),
       (1,1,'Toilette E57','1','ROOM',1,0),
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
VALUES ('Haupteingang', 'Flur E27 - E57'), ('Flur E27 - E57', 'Haupteingang'),
       ('Eingang E57', 'Flur E27 - E57'), ('Flur E27 - E57', 'Eingang E57'), ('Eingang E57', 'Flur E58 - E68'), ('Flur E58 - E68','Eingang E57'),
       ('Fakultätsservicebüro E33', 'Flur E27 - E57'), ('Flur E27 - E57', 'Fakultätsservicebüro E33'),
       ('Erste Hilfe E38', 'Flur E27 - E57'), ('Flur E27 - E57', 'Erste Hilfe E38'),
       ('E59', 'Flur E58 - E68'), ('Flur E58 - E68', 'E59'),
       ('E62', 'Flur E58 - E68'), ('Flur E58 - E68', 'E62'),
       ('E39', 'Flur E27 - E57'), ('Flur E27 - E57', 'E39'),
       ('E48', 'Flur E27 - E57'), ('Flur E27 - E57', 'E48'),
       ('Toilette E50', 'Flur E27 - E57'), ('Flur E27 - E57', 'Toilette E50'),
       ('Toilette E60', 'Flur E58 - E68'), ('Flur E58 - E68', 'Toilette E60'),
       ('Toilette E57', 'Flur E27 - E57'), ('Flur E27 - E57', 'Toilette E57'),
       ('Treppenhaus bei E58', 'Flur E58 - E68'), ('Flur E58 - E68', 'Treppenhaus bei E58'),
       ('Treppenhaus bei Haupteingang', 'Flur E27 - E57'), ('Flur E27 - E57', 'Treppenhaus bei Haupteingang'),
       ('Flur E58 - E68', 'Flur E27 - E57'), ('Flur E27 - E57', 'Flur E58 - E68');