-- SQLINES DEMO *** rated by MySQL Workbench
-- SQLINES DEMO *** 9:16:43
-- SQLINES DEMO ***    Version: 1.0
-- SQLINES DEMO *** orward Engineering

-- SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0; */
-- SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0; */
-- SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION'; */

-- SQLINES DEMO *** ------------------------------------
-- Schema nbaReference
-- SQLINES DEMO *** ------------------------------------
-- DROP SCHEMA IF EXISTS nbareference CASCADE;

-- SQLINES DEMO *** ------------------------------------
-- Schema nbaReference
-- SQLINES DEMO *** ------------------------------------
-- CREATE SCHEMA IF NOT EXISTS nbareference;
-- SHOW WARNINGS;
-- SET SCHEMA 'nbareference' ;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`Players`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS Players CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
DROP SEQUENCE Players_seq;
CREATE SEQUENCE Players_seq;

CREATE TABLE IF NOT EXISTS Players (
  idPlayer INT NOT NULL DEFAULT NEXTVAL ('Players_seq'),
  name VARCHAR(45) NULL DEFAULT 'noData',
  age INT NULL DEFAULT 0,
  position VARCHAR(100) NULL DEFAULT 'noData',
  college VARCHAR(100) NULL DEFAULT 'noData',
  draftTeam VARCHAR(45) NULL DEFAULT 'noData',
  draftPosition INT NULL DEFAULT 0,
  draftYear INT NULL DEFAULT 0000,
  born DATE NULL,
  experience INT NULL DEFAULT 0,
  PRIMARY KEY (idPlayer))
;

-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`Games`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS Games CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
DROP SEQUENCE Games_seq;
CREATE SEQUENCE Games_seq;

CREATE TABLE IF NOT EXISTS Games (
  idGame INT NOT NULL DEFAULT NEXTVAL ('Games_seq'),
  date DATE NULL,
  visitor VARCHAR(45) NULL DEFAULT 'noData',
  local VARCHAR(45) NULL DEFAULT 'noData',
  pointsVisitor INT NULL DEFAULT 0,
  pointsLocal INT NULL DEFAULT 0,
  arena VARCHAR(45) NULL DEFAULT 'noData',
  PRIMARY KEY (idGame))
;

-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`Seasons`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS Seasons CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
DROP SEQUENCE Seasons_seq;
CREATE SEQUENCE Seasons_seq;

CREATE TABLE IF NOT EXISTS Seasons (
  idSeason INT NOT NULL DEFAULT NEXTVAL ('Seasons_seq'),
  years VARCHAR(45) NULL DEFAULT 'noData',
  league VARCHAR(45) NULL DEFAULT 'noData',
  champion INT NULL,
  mvp VARCHAR(45) NULL DEFAULT 0,
  roty VARCHAR(45) NULL DEFAULT 0,
  pointsLeader VARCHAR(45) NULL DEFAULT 'noData',
  reboundsLeader VARCHAR(45) NULL DEFAULT 'noData',
  assistsLeader VARCHAR(45) NULL DEFAULT 'noData',
  winSharesLeader VARCHAR(45) NULL DEFAULT 'noData',
  PRIMARY KEY (idSeason),
  CONSTRAINT fk_seasons_idteams1 FOREIGN KEY (champion)
         REFERENCES teams (idteam)
         ON UPDATE NO ACTION
         ON DELETE NO ACTION)
;


-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`Teams`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS Teams CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
DROP SEQUENCE Teams_seq;
CREATE SEQUENCE Teams_seq;

CREATE TABLE IF NOT EXISTS Teams (
  idTeam INT NOT NULL DEFAULT NEXTVAL ('Teams_seq'),
  name VARCHAR(45) NULL DEFAULT 'noData',
  location VARCHAR(45) NULL DEFAULT 'noData',
  games INT NULL DEFAULT 0,
  wins INT NULL DEFAULT 0,
  loses INT NULL DEFAULT 0,
  playoff INT NULL DEFAULT 0,
  confChampions INT NULL DEFAULT 0,
  nbaChampions INT NULL DEFAULT 0,
  conference VARCHAR(45) NULL DEFAULT 'noData',
  PRIMARY KEY (idTeam))
;

-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`PlayerPerGame`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS PlayerPerGame CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS PlayerPerGame (
  idPlayer INT NOT NULL,
  idGame INT NOT NULL,
  idTeam INT NOT NULL,
  minutesPlayed TIMESTAMP(0) NULL,
  fieldGoals INT NULL DEFAULT 0,
  fgAttempts INT NULL DEFAULT 0,
  fgPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  threePtsFG INT NULL DEFAULT 0,
  threePFGAttempts INT NULL DEFAULT 0,
  threePFGPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  freeThrows INT NULL DEFAULT 0,
  freeThorwsAttemps INT NULL DEFAULT 0,
  FTPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  offRbds INT NULL DEFAULT 0,
  defRbds INT NULL DEFAULT 0,
  totalRbds INT NULL DEFAULT 0,
  assists INT NULL DEFAULT 0,
  steals INT NULL DEFAULT 0,
  blocks INT NULL DEFAULT 0,
  turnovers INT NULL DEFAULT 0,
  fouls INT NULL DEFAULT 0,
  points INT NULL DEFAULT 0,
  punctuation INT NULL DEFAULT 0,
  PRIMARY KEY (idPlayer, idGame),
  CONSTRAINT fk_PlayerPerGame_Players
    FOREIGN KEY (idPlayer)
    REFERENCES Players (idPlayer)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_PlayerPerGame_Games1
    FOREIGN KEY (idGame)
    REFERENCES Games (idGame)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_PlayerPerGame_Teams1
    FOREIGN KEY (idTeam)
    REFERENCES Teams (idTeam)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
-- CREATE INDEX fk_PlayerPerGame_Games1_idx ON PlayerPerGame (idGame ASC) VISIBLE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
-- CREATE INDEX fk_PlayerPerGame_Teams1_idx ON PlayerPerGame (idTeam ASC) VISIBLE;

-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`PlayerSeasons`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS PlayerSeasons CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS PlayerSeasons (
  idPlayer INT NOT NULL,
  idSeason INT NOT NULL,
  idTeam INT NULL DEFAULT 0,
  age INT NULL DEFAULT 0,
  league VARCHAR(45) NULL DEFAULT 'noData',
  position VARCHAR(45) NULL,
  games INT NULL DEFAULT 0,
  gamesStarter INT NULL DEFAULT 0,
  minutesPlayed INT NULL DEFAULT 0,
  fieldGoals INT NULL DEFAULT 0,
  FGAttempts INT NULL DEFAULT 0,
  FGPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  threePtsFG INT NULL DEFAULT 0,
  threePFGAttempts INT NULL DEFAULT 0,
  threePFGPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  twoPtsFG INT NULL DEFAULT 0,
  twoPFGAttempts INT NULL DEFAULT 0,
  twoPFGPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  effectiveGoalPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  freeThrows INT NULL DEFAULT 0,
  freeThorwsAttemps INT NULL DEFAULT 0,
  FTPerc DOUBLE PRECISION NULL DEFAULT 0.0,
  offRbds INT NULL DEFAULT 0,
  defRbds INT NULL DEFAULT 0,
  totalRbds INT NULL DEFAULT 0,
  assists INT NULL DEFAULT 0,
  steals INT NULL DEFAULT 0,
  blocks INT NULL DEFAULT 0,
  turnovers INT NULL DEFAULT 0,
  fouls INT NULL DEFAULT 0,
  points INT NULL DEFAULT 0,
  tripleDouble INT NULL DEFAULT 0,
  PRIMARY KEY (idPlayer, idSeason),
  CONSTRAINT fk_PlayerSeasons_Players1
    FOREIGN KEY (idPlayer)
    REFERENCES Players (idPlayer)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_PlayerSeasons_Seasons1
    FOREIGN KEY (idSeason)
    REFERENCES Seasons (idSeason)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_PlayerSeasons_Teams1
    FOREIGN KEY (idTeam)
    REFERENCES Teams (idTeam)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
-- CREATE INDEX fk_PlayerSeasons_Seasons1_idx ON PlayerSeasons (idSeason ASC) VISIBLE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
-- CREATE INDEX fk_PlayerSeasons_Teams1_idx ON PlayerSeasons (idTeam ASC) VISIBLE;

-- SHOW WARNINGS;

-- SQLINES DEMO *** ------------------------------------
-- SQLINES DEMO *** ce`.`TeamPerSeason`
-- SQLINES DEMO *** ------------------------------------
DROP TABLE IF EXISTS TeamPerSeason CASCADE;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE IF NOT EXISTS TeamPerSeason (
  idSeason INT NOT NULL,
  idTeam INT NOT NULL,
  position INT NULL DEFAULT 0,
  wins INT NULL DEFAULT 0,
  loses INT NULL DEFAULT 0,
  winRate DOUBLE PRECISION NULL DEFAULT 0.0,
  gamesBehind DOUBLE PRECISION NULL DEFAULT 0.0,
  ptsGame DOUBLE PRECISION NULL DEFAULT 0.0,
  defPTSGame DOUBLE PRECISION NULL DEFAULT 0.0,
  teamRating DOUBLE PRECISION NULL DEFAULT 0.0,
  conference VARCHAR(15) NULL DEFAULT 'noData',
  PRIMARY KEY (idSeason, idTeam),
  CONSTRAINT fk_TeamPerSeason_Seasons1
    FOREIGN KEY (idSeason)
    REFERENCES Seasons (idSeason)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT fk_TeamPerSeason_Teams1
    FOREIGN KEY (idTeam)
    REFERENCES Teams (idTeam)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
;

-- SHOW WARNINGS;
-- SQLINES LICENSE FOR EVALUATION USE ONLY
-- CREATE INDEX fk_TeamPerSeason_Teams1_idx ON TeamPerSeason (idTeam ASC) VISIBLE;

-- SHOW WARNINGS;

-- SET SQL_MODE=@OLD_SQL_MODE; */
-- SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS; */
-- SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS; */