DROP TABLE IF EXISTS detaleOplat
DROP TABLE IF EXISTS przelicznikiOplat
DROP TABLE IF EXISTS kontrahentDane
DROP TABLE IF EXISTS samochody
DROP TABLE IF EXISTS logowanie
DROP TABLE IF EXISTS przeliczniki
GO

DROP TABLE IF EXISTS kod_poczt_1
DROP TABLE IF EXISTS kraj
DROP TABLE IF EXISTS kontakt_do_kontrahenta
DROP TABLE IF EXISTS uzytkownik
DROP TABLE IF EXISTS grupa_uprawnienia
DROP TABLE IF EXISTS grupa_uzytkownikow
DROP TABLE IF EXISTS urzadzenie

GO





--false = 0
CREATE TABLE logowanie(
id INT PRIMARY KEY IDENTITY,
login VARCHAR(20) UNIQUE,
haslo VARCHAR(20),
perms SMALLINT DEFAULT 0
)


CREATE TABLE urzadzenie(
id VARCHAR(255) PRIMARY KEY NOT NULL,
ip_stacji VARCHAR(255)
)


CREATE TABLE grupa_uzytkownikow(
id BIGINT IDENTITY,
nazwa VARCHAR(255) NOT NULL,
urzadzenie_id VARCHAR(255),
CONSTRAINT grupa_uzytkownikow_pkey PRIMARY KEY(id),
FOREIGN KEY (urzadzenie_id) REFERENCES urzadzenie(id) ON DELETE NO ACTION ON UPDATE NO ACTION 
)


CREATE TABLE uzytkownik(
id INT PRIMARY KEY IDENTITY,
skasowany BIT DEFAULT 0 NOT NULL,
imie VARCHAR(255) NOT NULL,
nazwisko VARCHAR(255) NOT NULL,
login VARCHAR(255) UNIQUE NOT NULL,
haslo VARCHAR(255) NOT NULL,
grupa_id SMALLINT NOT NULL,
urzadzenie_id VARCHAR(255) REFERENCES urzadzenie(id),
grupauzytkownikow_id BIGINT DEFAULT 1 NOT NULL REFERENCES grupa_uzytkownikow(id),
spedytor BIT DEFAULT 0 NOT NULL
)


CREATE TABLE kraj(
id VARCHAR(255) NOT NULL PRIMARY KEY,
nazwa VARCHAR(255),
kraj_id VARCHAR(255) NOT NULL UNIQUE
)

CREATE TABLE kod_poczt_1(
id VARCHAR(255) NOT NULL PRIMARY KEY,
kraj_id VARCHAR(255),
kp1_kod VARCHAR(255) NOT NULL,
FOREIGN KEY (kraj_id) REFERENCES kraj(kraj_id)
)


CREATE TABLE kontakt_do_kontrahenta(
id  BIGINT PRIMARY KEY IDENTITY,
pozycja INTEGER NOT NULL,
telefon VARCHAR(255),
telefon_ekr VARCHAR(255),
email VARCHAR(255),
uzytkownik_id INTEGER,
nowy BIT DEFAULT 0,
kierowca_id INTEGER,
czas_ost_wykorz DATE DEFAULT GETDATE() NOT NULL,
FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(id),
)

CREATE TABLE grupa_uprawnienia(
id SMALLINT PRIMARY KEY IDENTITY,
nazwa VARCHAR(255) NOT NULL,
polisa_oc BIT DEFAULT 0 NOT NULL,
licencja BIT DEFAULT 0 NOT NULL,
przew_kj_stala BIT DEFAULT 0 NOT NULL,
przew_kj_tymcz BIT DEFAULT 0 NOT NULL,
przew_prior_grozny BIT DEFAULT 0 NOT NULL,
limit_staly BIT DEFAULT 0 NOT NULL,
limit_tymcz BIT DEFAULT 0 NOT NULL,
kontrola_aut BIT DEFAULT 0 NOT NULL,
term_zal_rozl BIT DEFAULT 0 NOT NULL,
bank_ksieg BIT DEFAULT 0 NOT NULL,
autoryz_zlecenia BIT DEFAULT 0 NOT NULL,
kli_chroniony BIT DEFAULT 0 NOT NULL,
kli_prior BIT DEFAULT 0 NOT NULL,
kli_kj BIT DEFAULT 0 NOT NULL,
biala_lista BIT DEFAULT 0 NOT NULL,
)




INSERT INTO kraj VALUES
(12, 'Grecja', 'GR'),
(11, 'USA', 'US'),
(10, 'Polska', 'PL'),
(9, 'ROSJA', 'RU')

INSERT INTO  kod_poczt_1 VALUES
(12, 'PL', 12345),
(11, 'US', 11111),
(10, 'RU', 45678)
	

INSERT INTO urzadzenie VALUES
(1, 9),
(2, 8),
(3, 7),
(4, 6),
(5, 5),
(6, 4)


CREATE TABLE kontrahentDane(
id INT PRIMARY KEY IDENTITY,
imie VARCHAR(20),
nazwisko VARCHAR(30),
typ SMALLINT
)


CREATE TABLE przelicznikiOplat(
nazwa VARCHAR(20) PRIMARY KEY,
cena1 INT,
cena2 INT,
cena3 INT
)


CREATE TABLE detaleOplat(
id INT PRIMARY KEY IDENTITY,
id_kontrahenta INT FOREIGN KEY REFERENCES kontrahentDane(id),
przelicznik FLOAT DEFAULT 1.0,
id_uzytkownika INT FOREIGN KEY REFERENCES uzytkownik(id),
kwotaBazowa INT
)

CREATE TABLE samochody(
id INT PRIMARY KEY IDENTITY,
typ VARCHAR(30),
kolor VARCHAR(20),
model VARCHAR(30)
)



INSERT INTO kontrahentDane VALUES
('Ewa', 'Test', 2),
('Lorem', 'Ipsum', 1),
('Todd', 'Howard', 2),
('Test', 'Account', 1)


INSERT INTO przelicznikiOplat VALUES
('tanio', 3, 3, 3),
('drogie3', 1, 1, 3),
('drogie', 3, 3, 3),
('normalne', 2, 2, 2)


INSERT INTO grupa_uzytkownikow VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4)


INSERT INTO uzytkownik VALUES
(0, 'adam', 'rudnicki', 'a', 'a', 3,  1, 1, 0),
(0, 'tomasz', 'rucdnicki', 't', 't', 1, 2, 1, 0),
(1, 'temporary', 'user', 'tu', 'tu', 2, 1, 2, 1)


INSERT INTO kontakt_do_kontrahenta VALUES
(123, '888999777', '888999776', 'testmail1@gmail.com', 1, 0, 12, GETDATE()),
(234, '988999777', '988999776', 'testmail2@gmail.com', 2, 1, 23, GETDATE()),
(345, '108999777', '108999776', 'testmail3@gmail.com', 3, 0, 34, GETDATE())

	
INSERT INTO detaleOplat VALUES
(1, 1.0, 1, 1000),
(2, 0.75, 3, 2000),
(3, 0.9, 2, 3000)

INSERT INTO samochody VALUES
('ciezarowy', 'czerwony', 'opel astra'),
('osobowy', 'czarny', 'skoda octavia'),
('ciezarowy', 'czarny', 'toyota corolla')

INSERT INTO logowanie VALUES
('guest', 'guest', 0),
('user', 'user', 1),
('user2', 'user2', 1),
('admin', 'admin', 2)

SELECT * FROM kod_poczt_1
SELECT * FROM kraj
SELECT * FROM kontakt_do_kontrahenta
SELECT * FROM uzytkownik
--SELECT * FROM grupa_uprawnienia
SELECT * FROM grupa_uzytkownikow
SELECT * FROM urzadzenie
SELECT * FROM kontrahentDane
SELECT * FROM przelicznikiOplat
SELECT * FROM detaleOplat
SELECT * FROM logowanie
SELECT * FROM samochody

/*
SELECT id_kontrahenta, kwotaBazowa FROM detaleOplat
--SELECT 

--SELECT kwotaBazowa * (SELECT przelicznik FROM przeliczniki WHERE id = (SELECT id FROM detaleOplat)) FROM detaleOplat AS kwotaKoncowa

--SELECT kwotaBazowa  FROM (SELECT * FROM detaleOplat INNER JOIN przelicznikiOplat ON detaleOplat(przelicznik) = przelicznikiOplat(przelicznik))

SELECT kwotaBazowa*cena2 AS kwotaKoncowa FROM detaleOplat INNER JOIN przelicznikiOplat ON przelicznikiOplat.nazwa = detaleOplat.przelicznik

SELECT * FROM detaleOplat INNER JOIN przelicznikiOplat ON detaleOplat.przelicznik = przelicznikiOplat.nazwa

--SELECT * FROM detaleOplat INNER JOIN (SELECT * FROM przeliczniki INNER JOIN przelicznikiOplat ON przelicznikiOplat.nazwa = przeliczniki.id) ON detaleOplat.przelicznik = przelicznikiOplat.nazwa*/

SELECT *, kwotaBazowa*przelicznik AS kwotaKoncowa FROM detaleOplat

SELECT id FROM uzytkownik WHERE imie = 'adam' AND nazwisko = 'rudnicki'

UPDATE detaleOplat
SET przelicznik = 0.8, kwotaBazowa = 800
WHERE id = 2



INSERT INTO detaleOplat VALUES
(1, 2.0, (SELECT id FROM uzytkownik WHERE imie = 'adam' AND nazwisko = 'rudnicki'), 1000)

SELECT *, kwotaBazowa*przelicznik AS kwotaKoncowa FROM detaleOplat

SELECT * FROM samochody
DELETE FROM samochody WHERE id = 1
SELECT * FROM samochody