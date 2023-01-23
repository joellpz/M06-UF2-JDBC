-- PostgreSQL Script generated by OpenAI
-- dom 22 ene 2023 14:12:39
-- Model: New Model Version: 1.0

-- No es necesario establecer los valores de UNIQUE_CHECKS o FOREIGN_KEY_CHECKS en PostgreSQL.

-- Schema nbaReference

-- DROP SCHEMA IF EXISTS nbaReference CASCADE;

-- Schema nbaReference

-- CREATE SCHEMA IF NOT EXISTS nbaReference;

-- Table Players

DROP TABLE IF EXISTS Players CASCADE;

CREATE TABLE IF NOT EXISTS Players (
idPlayer SERIAL NOT NULL,
name VARCHAR(45) DEFAULT 'noData',
age INT DEFAULT 0,
position VARCHAR(100) DEFAULT 'noData',
collegue VARCHAR(45) DEFAULT 'noData',
draftTeam VARCHAR(45) DEFAULT 'noData',
draftPosition INT DEFAULT 0,
draftYear INT DEFAULT 0000,
born DATE,
experience INT DEFAULT 0,
PRIMARY KEY (idPlayer)
);

-- Table Teams

DROP TABLE IF EXISTS Teams CASCADE;

CREATE TABLE IF NOT EXISTS Teams (
idTeam SERIAL NOT NULL,
name VARCHAR(45),
location VARCHAR(45),
games INT,
wins INT,
loses INT,
playoff INT,
confChampions INT,
nbaChampions INT,
conference VARCHAR(45),
PRIMARY KEY (idTeam)
);

-- Table Games

DROP TABLE IF EXISTS Games CASCADE;

CREATE TABLE IF NOT EXISTS Games (
idGame SERIAL NOT NULL,
date DATE,
visitor INT,
local INT,
pointsVisitor INT,
pointsLocal INT,
arena VARCHAR(45),
PRIMARY KEY (idGame),
FOREIGN KEY (visitor) REFERENCES Teams(idTeam) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (local) REFERENCES Teams(idTeam) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table Seasons

DROP TABLE IF EXISTS Seasons CASCADE;

CREATE TABLE IF NOT EXISTS Seasons (
idSeason SERIAL NOT NULL,
years VARCHAR(45),
league VARCHAR(45),
champion INT,
mvp varchar(45),
roty varchar(45),
pointsLeader VARCHAR(45),
reboundsLeader VARCHAR(45),
assistsLeader VARCHAR(45),
winSharesLeader VARCHAR(45),
PRIMARY KEY (idSeason),
FOREIGN KEY (champion) REFERENCES Teams(idTeam) ON UPDATE CASCADE ON DELETE CASCADE
);

-- Table PlayerPerGame

DROP TABLE IF EXISTS PlayerPerGame CASCADE;

CREATE TABLE IF NOT EXISTS PlayerPerGame (
idPlayer INT NOT NULL,
idGame INT NOT NULL,
idTeam INT NOT NULL,
minutesPlayed TIMESTAMP NULL,
fieldGoals INT DEFAULT 0,
fgAttempts INT DEFAULT 0,
fgPerc FLOAT DEFAULT 0.0,
"3PTSFG" INT DEFAULT 0,
"3PFGAttempts" INT DEFAULT 0,
"3PFGPerc" FLOAT DEFAULT 0.0,
freeThrows INT DEFAULT 0,
freeThorwsAttemps INT,
FTPerc FLOAT DEFAULT 0.0,
offRbds INT DEFAULT 0,
defRbds INT DEFAULT 0,
totalRbds INT DEFAULT 0,
assists INT DEFAULT 0,
steals INT DEFAULT 0,
blocks INT DEFAULT 0,
turnovers INT DEFAULT 0,
fouls INT DEFAULT 0,
points INT DEFAULT 0,
punctuation INT DEFAULT 0,
PRIMARY KEY (idPlayer, idGame),
FOREIGN KEY (idPlayer) REFERENCES Players(idPlayer) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (idGame) REFERENCES Games(idGame) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (idTeam) REFERENCES Teams(idTeam) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--CREATE INDEX fk_PlayerPerGame_Games1_idx ON PlayerPerGame (idGame);
--CREATE INDEX fk_PlayerPerGame_Teams1_idx ON PlayerPerGame (idTeam);

-- Table PLAYERSEASON;

DROP TABLE IF EXISTS PlayerSeasons CASCADE;

CREATE TABLE IF NOT EXISTS PlayerSeasons (
idPlayer INT NOT NULL,
idSeason INT NOT NULL,
idTeam INT NULL,
age INT NULL,
league VARCHAR(45) NULL,
position VARCHAR(45) NULL,
games INT NULL,
gamesStarter INT NULL,
minutesPlayed INT NULL,
fieldGoals INT NULL,
FGAttempts INT NULL,
FGPerc FLOAT NULL,
"3PTSFG" INT NULL,
"3PFGAttempts" INT NULL,
"3PFGPerc" FLOAT NULL,
"2PtsFG" INT NULL,
"2PFGAttempts" INT NULL,
"2PFGPerc" FLOAT NULL,
effectiveGoalPerc FLOAT NULL,
freeThrows INT NULL,
freeThrowsAttempts INT NULL,
FTPerc FLOAT NULL,
offRbds INT NULL,
defRbds INT NULL,
totalRbds INT NULL,
assists INT NULL,
steals INT NULL,
blocks INT NULL,
turnovers INT NULL,
fouls INT NULL,
points INT NULL,
triplesDouble INT NULL,
PRIMARY KEY (idPlayer, idSeason),
FOREIGN KEY (idPlayer) REFERENCES Players (idPlayer) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (idSeason) REFERENCES Seasons (idSeason) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (idTeam) REFERENCES Teams (idTeam) ON DELETE NO ACTION ON UPDATE NO ACTION
);

--CREATE INDEX fk_PlayerSeasons_Seasons1_idx ON PlayerSeasons (idSeason);
--CREATE INDEX fk_PlayerSeasons_Teams1_idx ON PlayerSeasons (idTeam);

DROP TABLE IF EXISTS TeamPerSeason CASCADE;

CREATE TABLE IF NOT EXISTS TeamPerSeason (
idSeason INT NOT NULL,
idTeam INT NOT NULL,
position INT DEFAULT 0,
wins INT DEFAULT 0,
loses INT DEFAULT 0,
winRate FLOAT DEFAULT 0.0,
gamesBehind FLOAT DEFAULT 0.0,
ptsGame FLOAT DEFAULT 0.0,
defPTSGame FLOAT DEFAULT 0.0,
teamRating FLOAT DEFAULT 0.0,
conference VARCHAR(15) DEFAULT 'noData',
PRIMARY KEY (idSeason, idTeam),
FOREIGN KEY (idSeason) REFERENCES Seasons (idSeason) ON DELETE NO ACTION ON UPDATE NO ACTION,
FOREIGN KEY (idTeam) REFERENCES Teams (idTeam) ON DELETE NO ACTION ON UPDATE NO ACTION
);

-- Por favor tenga en cuenta que algunas cosas son diferentes en PostgreSQL, como el uso de SERIAL en lugar de AUTO_INCREMENT, y la sintaxis para las claves externas.