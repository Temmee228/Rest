DROP TABLE IF EXISTS Vine CASCADE;
DROP TABLE IF EXISTS Store CASCADE;
DROP TABLE IF EXISTS Distillery CASCADE;
DROP TABLE IF EXISTS DistilleryVine CASCADE;


CREATE TABLE IF NOT EXISTS Distillery
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS Store
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Vine
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    storeID BIGINT REFERENCES Store (id)
);

CREATE TABLE IF NOT EXISTS DistilleryVine
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    vineID BIGINT REFERENCES Vine (id),
    distilleryID BIGINT REFERENCES Distillery (id)
);


INSERT INTO Distillery (name)
VALUES ('I'),
       ('It');

INSERT INTO STORE (name)
VALUES ('K&B'),
       ('Norman'),
       ('Clock');

INSERT INTO Vine (name, storeID)
VALUES ('Красный бленд', 1),
       ('Белое каберне', 1),
       ('Bodega Aleanna', 1),
       ('Полусухой Рислинг', 1),
       ('Brigaldara', 1),
       ('Gigondas LaCave', 2),
       ('Sisters Run', 2),
       ('Кнудсен Блан де Бланс', 3),
       ('Château Cissac', 3),
       ('Mazzei Ser Lapo', 3),
       ('Каберне Фран', 3),
       ('Рислинг Great Western', 3);

INSERT INTO DistilleryVine (vineID, distilleryID)
VALUES (1, 1),
       (2, 1),
       (3, 1),
       (4, 1),
       (5, 1),
       (6, 1),
       (7, 1),
       (8, 1),
       (9, 2),
       (10, 2),
       (11, 2),
       (12, 2),
       (1, 2),
       (2, 2),
       (3, 2),
       (4, 2);