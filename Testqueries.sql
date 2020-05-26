/*SELECT * FROM grupa_uprawnienia
SELECT * FROM grupa_uzytkownikow
SELECT * FROM kod_poczt_1
SELECT * FROM kontakt_do_kontrahenta
SELECT * FROM kraj
SELECT * FROM urzadzenie
SELECT * FROM uzytkownik

DELETE FROM kod_poczt_1


DELETE FROM kontakt_do_kontrahenta WHERE uzytkownik_id = (SELECT id FROM uzytkownik WHERE imie = 'Adam' AND nazwisko = 'Rudnicki')
DELETE FROM uzytkownik WHERE imie = 'Adam' AND nazwisko = 'Rudnicki'
*/

INSERT INTO kod_poczt_1 VALUES
(1, 1, 3),
(2, 3, 6),
(3, 5, 9),
(4, 7, 1)


INSERT INTO uzytkownik VALUES
(0, 'Xavier', 'Placeholder', 'x', 'x', 1, '1', 1, 0),
(1, 'Tomasz', 'Rudy', 'a', 'a', 0, '2', 2, 1),
(1, 'Adam', 'Rudnicki', 'b', 'b', 0, '3', 2, 1)



INSERT INTO kontakt_do_kontrahenta VALUES
(123, '1', '2', 't', 10, 0, 12, GETDATE()),
(234, '3', '4', 'x', 11, 1, 23, GETDATE()),
(345, '5', '6', 'y', 11, 0, 34, GETDATE())



INSERT INTO grupa_uzytkownikow VALUES
('test_grupa_uzytkownikow', '1'),
('test_grupa_uzytkownikow2', '2')



INSERT INTO grupa_uprawnienia VALUES
('c', 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 ,1 ,1 ,1 ,1),
('z', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)




INSERT INTO uzytkownik VALUES
(0, 'Adam', 'Rudnicki', '1', 'user', 1, 1, 1, 1),
(1, 'Tomasz', 'Rudy', '2', 'user2', 2, 2, 2, 0)

CREATE TABLE logowanie(
id INT PRIMARY KEY IDENTITY,
login VARCHAR(30) UNIQUE,
haslo VARCHAR(30),
perms SMALLINT
)

INSERT INTO logowanie VALUES
('admin', 'admin', 2),
('user', 'user', 1),
('user2', 'user2', 1),
('guest', 'guest', 0)


--SELECT * FROM uzytkownik

--DELETE FROM kod_poczt_1

SELECT * FROM kraj

SELECT * FROM kod_poczt_1

DROP TABLE kraj
CREATE TABLE kraj(
id VARCHAR(255) PRIMARY KEY NOT NULL,
nazwa VARCHAR(255) DEFAULT NULL,
kraj_id VARCHAR(255) NOT NULL	
)

SELECT id FROM kod_poczt_1

/*
DROP TABLE kod_poczt_1

CREATE TABLE kod_poczt_1(
id VARCHAR(255) PRIMARY KEY NOT NULL,
kraj_id VARCHAR(255) FOREIGN KEY REFERENCES kraj(id) DEFAULT NULL,
kp1_kod VARCHAR(255) NOT NULL
)*/

SELECT * FROM kraj

INSERT INTO kraj VALUES
(1, 'Polska', 'PL'),
(2, 'Rosja', 'RU'),
(3, 'USA', 'US'),
(4, 'Uganda', 'UG')