--0 = false
--BIT = BOOLEAN

DROP TABLE IF EXISTS kraj
DROP TABLE IF EXISTS kod_pocz_1
DROP TABLE IF EXISTS kontakt_do_kontrahenta
DROP TABLE IF EXISTS uzytkownik
DROP TABLE IF EXISTS grupa_uzytkownikow
DROP TABLE IF EXISTS grupa_uprawnienia
DROP TABLE IF EXISTS urzadzenie

DROP TABLE IF EXISTS test_table


-------------------------------------------------------------------------------------------------------------------------------------------------------
CREATE TABLE kraj(
id VARCHAR NOT NULL PRIMARY KEY,
nazwa VARCHAR,
kraj_id VARCHAR NOT NULL UNIQUE
)


CREATE TABLE kod_pocz_1(
id VARCHAR NOT NULL PRIMARY KEY,
kraj_id VARCHAR NOT NULL UNIQUE,
kp1_kod VARCHAR NOT NULL
)


CREATE TABLE urzadzenie(
id VARCHAR(255) NOT NULL PRIMARY KEY, --Nie bylo PRIMARY KEY, ale oba wywoalania urzadzenie(id) wymagaj¹ zeby bylo PRIMARY KEY
ip_stacji VARCHAR(255)
)
	

CREATE TABLE grupa_uzytkownikow( 
id BIGINT PRIMARY KEY,
nazwa VARCHAR(255) NOT NULL,
urzadzenie_id VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES urzadzenie(id) ON DELETE NO ACTION ON UPDATE NO ACTION 
--CONSTRAINT grupa_uzytkownikow_pkey PRIMARY KEY(id),
--CONSTRAINT fk1_urzadzemoe FOREIGN KEY (urzadzenie_id) REFERENCES PUBLIC.urzadzenie(id)   ON DELETE NO ACTION   ON UPDATE NO ACTION 
)


CREATE TABLE uzytkownik(
id INT PRIMARY KEY,
skasowany BIT DEFAULT 0 NOT NULL,
imie VARCHAR(255) NOT NULL,
nazwisko VARCHAR(255) NOT NULL,
login VARCHAR NOT NULL UNIQUE,
haslo VARCHAR(255) NOT NULL,
grupa_id SMALLINT NOT NULL,
urzadzenie_id VARCHAR(255) NOT NULL FOREIGN KEY REFERENCES urzadzenie(id) ON DELETE NO ACTION   ON UPDATE NO ACTION ,
grupauzytkownikow_id BIGINT DEFAULT 1 NOT NULL FOREIGN KEY REFERENCES grupa_uzytkownikow(id) ON DELETE NO ACTION   ON UPDATE NO ACTION ,
spedytor BIT DEFAULT 0 NOT NULL,
--CONSTRAINT uzytkownik_grupauzytk FOREIGN KEY (grupauzytkownikow_id) REFERENCES grupa_uzytkownikow(id)  ON DELETE NO ACTION ON UPDATE NO ACTION, --Bylo PUBLIC.grupa_uzytkownikow(id)
--CONSTRAINT uzytkownik_urzadzenie FOREIGN KEY (urzadzenie_id) REFERENCES urzadzenie(id) ON DELETE NO ACTION ON UPDATE NO ACTION --Bylo PUBLIC.urzadzenie(id)
)


CREATE TABLE kontakt_do_kontrahenta(
id BIGINT PRIMARY KEY,
pozycja INTEGER NOT NULL,
telefon VARCHAR,
telefon_ekr VARCHAR,
email VARCHAR,
uzytkownik_id INTEGER,
nowy BIT DEFAULT 0,
kierowca_id INTEGER,
czas_ost_wykorz DATE NOT NULL DEFAULT CURRENT_TIMESTAMP,
CONSTRAINT kontakt_do_kontrah_tel_kierowca UNIQUE(telefon, kierowca_id),
CONSTRAINT kontakt_do_kontrahenta_id_key UNIQUE(id, pozycja),
kdk_uzytkownik_fk INT REFERENCES uzytkownik(id) ON DELETE CASCADE
--CONSTRAINT kontakt_do_kontrahenta_chk CHECK (((NOT (kontrahent_id IS NULL)) AND (uzytkownik_id IS NULL) AND (kierowca_id IS NULL)) OR ((kontrahent_id IS NULL) AND (NOT (uzytkownik_id IS NULL)) AND (kierowca_id IS NULL)) OR ((kontrahent_id IS NULL) AND (uzytkownik_id IS NULL) AND (NOT (kierowca_id IS NULL)))) ,
--CONSTRAINT kdk_uzytkownik_fk FOREIGN KEY (uzytkownik_id) REFERENCES uzytkownik(id) ON DELETE CASCADE ON UPDATE NO ACTION
)

CREATE TABLE grupa_uprawnienia(
id SMALLINT PRIMARY KEY,
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
biala_lista BIT DEFAULT 0 NOT NULL
)


CREATE TABLE test_table(
id INT PRIMARY KEY IDENTITY,
imie VARCHAR(20) NOT NULL,
nazwisko VARCHAR(30),
wiek INT
)


INSERT INTO test_table VALUES
('Adam','Rudnicki', 21),
('Tomasz','Rudnicki', 21),
('temporary','filler', 420)

SELECT * FROM test_table