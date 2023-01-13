-- MySQL Script generated by MySQL Workbench
-- jue 12 ene 2023 19:16:43
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema nbaReference
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `nbaReference` ;

-- -----------------------------------------------------
-- Schema nbaReference
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `nbaReference` DEFAULT CHARACTER SET utf8 ;
SHOW WARNINGS;
USE `nbaReference` ;

-- -----------------------------------------------------
-- Table `nbaReference`.`Players`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`Players` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`Players` (
  `idPlayer` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT 'noData',
  `age` INT NULL DEFAULT 0,
  `position1` VARCHAR(45) NULL DEFAULT 'noData',
  `position2` VARCHAR(45) NULL DEFAULT 'noData',
  `position3` VARCHAR(45) NULL DEFAULT 'noData',
  `collegue` VARCHAR(45) NULL DEFAULT 'noData',
  `draftTeam` VARCHAR(45) NULL DEFAULT 'noData',
  `draftPosition` INT NULL DEFAULT 0,
  `draftYear` YEAR(4) NULL DEFAULT 0000,
  `born` DATE NULL,
  `experience` INT NULL DEFAULT 0,
  PRIMARY KEY (`idPlayer`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`Games`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`Games` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`Games` (
  `idGame` INT NOT NULL AUTO_INCREMENT,
  `date` DATE NULL,
  `visitor` VARCHAR(45) NULL DEFAULT 'noData',
  `local` VARCHAR(45) NULL DEFAULT 'noData',
  `pointsVisitor` INT NULL DEFAULT 0,
  `pointsLocal` INT NULL DEFAULT 0,
  `arena` VARCHAR(45) NULL DEFAULT 'noData',
  PRIMARY KEY (`idGame`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`Seasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`Seasons` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`Seasons` (
  `idSeason` INT NOT NULL AUTO_INCREMENT,
  `years` VARCHAR(45) NULL DEFAULT 'noData',
  `league` VARCHAR(45) NULL DEFAULT 'noData',
  `champion` INT NULL DEFAULT 0 COMMENT 'idTeam',
  `mvp` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `roty` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `pointsLeader` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `reboundsLeader` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `assistsLeader` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `winShareLeader` INT NULL DEFAULT 0 COMMENT 'idPlayer',
  `pointsMax` INT NULL DEFAULT 0,
  `reboundsMax` INT NULL DEFAULT 0,
  `assistsMax` INT NULL DEFAULT 0,
  `winSharesMax` INT NULL DEFAULT 0,
  PRIMARY KEY (`idSeason`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`Teams`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`Teams` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`Teams` (
  `idTeam` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL DEFAULT 'noData',
  `location` VARCHAR(45) NULL DEFAULT 'noData',
  `games` VARCHAR(45) NULL DEFAULT 'noData',
  `wins` VARCHAR(45) NULL DEFAULT 'noData',
  `loses` VARCHAR(45) NULL DEFAULT 'noData',
  `playoff` VARCHAR(45) NULL DEFAULT 'noData',
  `confChampions` VARCHAR(45) NULL DEFAULT 'noData',
  `nbaChampions` VARCHAR(45) NULL DEFAULT 'noData',
  `conference` VARCHAR(45) NULL DEFAULT 'noData',
  PRIMARY KEY (`idTeam`))
ENGINE = InnoDB;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`PlayerPerGame`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`PlayerPerGame` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`PlayerPerGame` (
  `idPlayer` INT NOT NULL,
  `idGame` INT NOT NULL,
  `idTeam` INT NOT NULL,
  `minutesPlayed` DATETIME NULL,
  `fieldGoals` INT NULL DEFAULT 0,
  `fgAttempts` INT NULL DEFAULT 0,
  `fgPerc` FLOAT NULL DEFAULT 0.0,
  `3PtsFG` INT NULL DEFAULT 0,
  `3PFGAttempts` INT NULL DEFAULT 0,
  `3PFGPerc` FLOAT NULL DEFAULT 0.0,
  `freeThrows` INT NULL DEFAULT 0,
  `freeThorwsAttemps` INT NULL DEFAULT 0,
  `FTPerc` FLOAT NULL DEFAULT 0.0,
  `offRbds` INT NULL DEFAULT 0,
  `defRbds` INT NULL DEFAULT 0,
  `totalRbds` INT NULL DEFAULT 0,
  `assists` INT NULL DEFAULT 0,
  `steals` INT NULL DEFAULT 0,
  `blocks` INT NULL DEFAULT 0,
  `turnovers` INT NULL DEFAULT 0,
  `fouls` INT NULL DEFAULT 0,
  `points` INT NULL DEFAULT 0,
  `punctuation` INT NULL DEFAULT 0,
  PRIMARY KEY (`idPlayer`, `idGame`),
  CONSTRAINT `fk_PlayerPerGame_Players`
    FOREIGN KEY (`idPlayer`)
    REFERENCES `nbaReference`.`Players` (`idPlayer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlayerPerGame_Games1`
    FOREIGN KEY (`idGame`)
    REFERENCES `nbaReference`.`Games` (`idGame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlayerPerGame_Teams1`
    FOREIGN KEY (`idTeam`)
    REFERENCES `nbaReference`.`Teams` (`idTeam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_PlayerPerGame_Games1_idx` ON `nbaReference`.`PlayerPerGame` (`idGame` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `fk_PlayerPerGame_Teams1_idx` ON `nbaReference`.`PlayerPerGame` (`idTeam` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`PlayerSeasons`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`PlayerSeasons` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`PlayerSeasons` (
  `idPlayer` INT NOT NULL,
  `idSeason` INT NOT NULL,
  `idTeam` INT NULL DEFAULT 0,
  `age` INT NULL DEFAULT 0,
  `league` VARCHAR(45) NULL DEFAULT 'noData',
  `position` VARCHAR(45) NULL,
  `games` INT NULL DEFAULT 0,
  `gamesStarter` INT NULL DEFAULT 0,
  `minutesPlayed` INT NULL DEFAULT 0,
  `fieldGoals` INT NULL DEFAULT 0,
  `FGAttempts` INT NULL DEFAULT 0,
  `FGPerc` FLOAT NULL DEFAULT 0.0,
  `3PtsFG` INT NULL DEFAULT 0,
  `3PFGAttempts` INT NULL DEFAULT 0,
  `3PFGPerc` FLOAT NULL DEFAULT 0.0,
  `2PtsFG` INT NULL DEFAULT 0,
  `2PFGAttempts` INT NULL DEFAULT 0,
  `2PFGPerc` FLOAT NULL DEFAULT 0.0,
  `effectiveGoalPerc` FLOAT NULL DEFAULT 0.0,
  `freeThrows` INT NULL DEFAULT 0,
  `freeThorwsAttemps` INT NULL DEFAULT 0,
  `FTPerc` FLOAT NULL DEFAULT 0.0,
  `offRbds` INT NULL DEFAULT 0,
  `defRbds` INT NULL DEFAULT 0,
  `totalRbds` INT NULL DEFAULT 0,
  `assists` INT NULL DEFAULT 0,
  `steals` INT NULL DEFAULT 0,
  `blocks` INT NULL DEFAULT 0,
  `turnovers` INT NULL DEFAULT 0,
  `fouls` INT NULL DEFAULT 0,
  `points` INT NULL DEFAULT 0,
  `tripleDouble` INT NULL DEFAULT 0,
  PRIMARY KEY (`idPlayer`, `idSeason`),
  CONSTRAINT `fk_PlayerSeasons_Players1`
    FOREIGN KEY (`idPlayer`)
    REFERENCES `nbaReference`.`Players` (`idPlayer`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlayerSeasons_Seasons1`
    FOREIGN KEY (`idSeason`)
    REFERENCES `nbaReference`.`Seasons` (`idSeason`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_PlayerSeasons_Teams1`
    FOREIGN KEY (`idTeam`)
    REFERENCES `nbaReference`.`Teams` (`idTeam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_PlayerSeasons_Seasons1_idx` ON `nbaReference`.`PlayerSeasons` (`idSeason` ASC) VISIBLE;

SHOW WARNINGS;
CREATE INDEX `fk_PlayerSeasons_Teams1_idx` ON `nbaReference`.`PlayerSeasons` (`idTeam` ASC) VISIBLE;

SHOW WARNINGS;

-- -----------------------------------------------------
-- Table `nbaReference`.`TeamPerSeason`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `nbaReference`.`TeamPerSeason` ;

SHOW WARNINGS;
CREATE TABLE IF NOT EXISTS `nbaReference`.`TeamPerSeason` (
  `idSeason` INT NOT NULL,
  `idTeam` INT NOT NULL,
  `position` INT NULL DEFAULT 0,
  `wins` INT NULL DEFAULT 0,
  `loses` INT NULL DEFAULT 0,
  `winRate` FLOAT NULL DEFAULT 0.0,
  `gamesBehind` FLOAT NULL DEFAULT 0.0,
  `ptsGame` FLOAT NULL DEFAULT 0.0,
  `defPTSGame` FLOAT NULL DEFAULT 0.0,
  `teamRating` FLOAT NULL DEFAULT 0.0,
  `conference` VARCHAR(15) NULL DEFAULT 'noData',
  PRIMARY KEY (`idSeason`, `idTeam`),
  CONSTRAINT `fk_TeamPerSeason_Seasons1`
    FOREIGN KEY (`idSeason`)
    REFERENCES `nbaReference`.`Seasons` (`idSeason`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_TeamPerSeason_Teams1`
    FOREIGN KEY (`idTeam`)
    REFERENCES `nbaReference`.`Teams` (`idTeam`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

SHOW WARNINGS;
CREATE INDEX `fk_TeamPerSeason_Teams1_idx` ON `nbaReference`.`TeamPerSeason` (`idTeam` ASC) VISIBLE;

SHOW WARNINGS;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;