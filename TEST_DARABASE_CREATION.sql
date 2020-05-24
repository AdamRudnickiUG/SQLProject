DROP TABLE IF EXISTS kod_poczt_1
DROP TABLE IF EXISTS kraj
DROP TABLE IF EXISTS kontakt_do_kontrahenta
DROP TABLE IF EXISTS uzytkownik
DROP TABLE IF EXISTS grupa_uprawnienia
DROP TABLE IF EXISTS grupa_uzytkownikow
DROP TABLE IF EXISTS urzadzenie

GO


--false = 0
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
login VARCHAR UNIQUE NOT NULL,
haslo VARCHAR(255) NOT NULL,
grupa_id SMALLINT NOT NULL,
urzadzenie_id VARCHAR(255),
grupauzytkownikow_id BIGINT DEFAULT 1 NOT NULL,
spedytor BIT DEFAULT 0 NOT NULL,
FOREIGN KEY (grupauzytkownikow_id) REFERENCES grupa_uzytkownikow(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (urzadzenie_id) REFERENCES urzadzenie(id) ON DELETE NO ACTION ON UPDATE NO ACTION
)


CREATE TABLE kraj(
id VARCHAR NOT NULL PRIMARY KEY,
nazwa VARCHAR,
kraj_id VARCHAR NOT NULL UNIQUE
)

CREATE TABLE kod_poczt_1(
id VARCHAR NOT NULL PRIMARY KEY,
kraj_id VARCHAR,
kp1_kod VARCHAR NOT NULL,
FOREIGN KEY (kraj_id) REFERENCES kraj(kraj_id)
)


CREATE TABLE kontakt_do_kontrahenta(
id  BIGINT PRIMARY KEY IDENTITY,
pozycja INTEGER NOT NULL,
telefon VARCHAR,
telefon_ekr VARCHAR,
email VARCHAR,
uzytkownik_id INTEGER,
nowy BIT DEFAULT 0,
kierowca_id INTEGER,
czas_ost_wykorz DATE DEFAULT GETDATE() NOT NULL,
FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(id),
)

CREATE TABLE grupa_uprawnienia(
id SMALLINT PRIMARY KEY IDENTITY,
nazwa VARCHAR NOT NULL,
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




/*
SELECT * FROM kod_poczt_1
SELECT * FROM kraj*/

/*DROP TABLE IF EXISTS kod_poczt_1
DROP TABLE IF EXISTS kraj
DROP TABLE IF EXISTS kontakt_do_kontrahenta
DROP TABLE IF EXISTS uzytkownik
DROP TABLE IF EXISTS grupa_uprawnienia
DROP TABLE IF EXISTS grupa_uzytkownikow
DROP TABLE IF EXISTS urzadzenie
*/

/*
INSERT INTO kraj VALUES
(12, 'Grecja', 12),
(11, 'Falklandy', 11),
(10, 'Boœnia', 10),
(9, 'Hercegowina', 9)

INSERT INTO  kod_poczt_1 VALUES
(12, 12, 12),
(11, 11, 11),
(10, 10, 10)
*/

INSERT INTO urzadzenie VALUES
(1, 9),
(2, 8),
(3, 7),
(4, 6),
(5, 5),
(6, 4)