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
    XCOORDINATE DOUBLE NOT NULL,
    YCOORDINATE DOUBLE NOT NULL,
    FOREIGN KEY (BUILDING_ID) REFERENCES BUILDING(ID)
);

CREATE TABLE IF NOT EXISTS ROOM_CONNECTION (
    FROM_NAME varchar(100) NOT NULL,
    TO_NAME   varchar(100) NOT NULL,
    PRIMARY KEY (FROM_NAME, TO_NAME),
    FOREIGN KEY (FROM_NAME) REFERENCES ROOM(NAME),
    FOREIGN KEY (TO_NAME) REFERENCES ROOM(NAME)
);