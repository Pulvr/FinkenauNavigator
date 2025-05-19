CREATE TABLE IF NOT EXISTS USERACCOUNTS(
    id      long PRIMARY KEY AUTO_INCREMENT,
    name    varchar(100) NOT NULL,
    passw   varchar(100) NOT NULL
);