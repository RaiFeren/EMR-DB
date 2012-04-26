-- Paul McCormack and Rai Feren
-- 010186829 - PO and 40152662 - HMC
-- SQL script for EMR program
-- CS 133 Final Project, Due 5/1/2012


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

drop database if exists emr;
create database emr;
use emr;

set autocommit=off;

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `appointments` (
  `aid` int(11) NOT NULL,
  `pid` int(11) DEFAULT NULL,
  `did` int(11) DEFAULT NULL,
  `fid` int(11) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  `date` char(11) NOT NULL,
  PRIMARY KEY (`aid`),
  KEY `pid` (`pid`),
  KEY `did` (`did`),
  KEY `fid` (`fid`),
  KEY `cid` (`cid`),
  CONSTRAINT `appointments_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `patients` (`pid`) ON DELETE CASCADE,
  CONSTRAINT `appointments_ibfk_2` FOREIGN KEY (`did`) REFERENCES `doctors` (`did`) ON DELETE CASCADE,
  CONSTRAINT `appointments_ibfk_3` FOREIGN KEY (`fid`) REFERENCES `facilities` (`fid`) ON DELETE CASCADE,
  CONSTRAINT `appointments_ibfk_4` FOREIGN KEY (`cid`) REFERENCES `conditionstreats` (`cid`) ON DELETE CASCADE 
  -- Can have unknown condition, thus change in conditions shouldn't stop an appointment.
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `appointments`
--

LOCK TABLES `appointments` WRITE;
/*!40000 ALTER TABLE `appointments` DISABLE KEYS */;
INSERT INTO `appointments` VALUES (1,3,7,1,5,'2012-06-31'),(3,5,3,1,7,'Drop In'),(4,1,5,5,3,'Drop In'),(5,5,2,4,9,'Drop In'),(6,4,4,3,10,'Drop In'),(8,4,3,1,4,'Drop In'),(9,4,4,3,10,'Drop In');
/*!40000 ALTER TABLE `appointments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `conditionstreats`
--

DROP TABLE IF EXISTS `conditionstreats`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `conditionstreats` (
  `cid` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `info` char(100) DEFAULT NULL,
  `probability` double DEFAULT NULL,
  `tid` int(11) NOT NULL,
  PRIMARY KEY (`cid`),
  KEY `tid` (`tid`),
  CONSTRAINT `conditionstreats_ibfk_1` FOREIGN KEY (`tid`) REFERENCES `treatments` (`tid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `conditionstreats`
--

LOCK TABLES `conditionstreats` WRITE;
/*!40000 ALTER TABLE `conditionstreats` DISABLE KEYS */;
INSERT INTO `conditionstreats` VALUES (1,'Intestinal Obstruction','partial or complete',0.14,1),(2,'Food Poisoning','eating bad food',0.2,2),(3,'Pneumonia','fluid in the lungs',0.07,3),(4,'Ear Infection','infection of middle ear',0.3,3),(5,'Giardiasis','giardia is an amoeba',0.3,3),(6,'Pink Eye','eye infection',0.3,4),(7,'Myopia','nearsightedness',0.6,5),(8,'Arthritis','swelling of joints',0.3,6),(9,'Common Cold','virus',0.8,7),(10,'Influenza','common flu',0.5,7),(11,'Poison Ivy Rash','the plant',0.13,8),(12,'Strep Throat','Streptococcus',0.1,3),(13,'Kidney Infection','infection',0.04,3);
/*!40000 ALTER TABLE `conditionstreats` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctors`
--

DROP TABLE IF EXISTS `doctors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `doctors` (
  `did` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `degree` char(10) DEFAULT NULL,
  PRIMARY KEY (`did`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctors`
--

LOCK TABLES `doctors` WRITE;
/*!40000 ALTER TABLE `doctors` DISABLE KEYS */;
INSERT INTO `doctors` VALUES (1,'Dr. Baumgartner','MD'),(2,'Dr. Sica','DDS'),(3,'Dr. McCormack','MD'),(4,'Dr. Graham','MD'),(5,'Dr. Moritz','MD, FAAFP'),(6,'Dr. Weisman','MD'),(7,'Dr. Levine','MD'),(8,'Sue Rudy','NP'),(9,'Nurse Rill','RN');
/*!40000 ALTER TABLE `doctors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `facilities`
--

DROP TABLE IF EXISTS `facilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `facilities` (
  `fid` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `addr1` char(30) NOT NULL,
  `addr2` char(30) DEFAULT NULL,
  PRIMARY KEY (`fid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `facilities`
--

LOCK TABLES `facilities` WRITE;
/*!40000 ALTER TABLE `facilities` DISABLE KEYS */;
INSERT INTO `facilities` VALUES (1,'Boulder Medical Center','2750 Broadway','Boulder, CO 80304'),(2,'Boulder Community Hospital','110 Balsam Ave.','Boulder, CO 80304'),(3,'St. Luke\'s Medical Center','1719 East 19th Avev','Denver, CO 80218'),(4,'North Boulder Dental','1001 North Street','Boulder, CO 80304'),(5,'The Allergy Stop','695 South Colorado Blvd','Denver, CO 80246');
/*!40000 ALTER TABLE `facilities` ENABLE KEYS */;
UNLOCK TABLES;

-- ' to fix dumb syntax highlighting...

--
-- Table structure for table `implies`
--

DROP TABLE IF EXISTS `implies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `implies` (
  `sid` int(11) DEFAULT NULL,
  `cid` int(11) DEFAULT NULL,
  `probability` double DEFAULT NULL,
  KEY `sid` (`sid`),
  KEY `cid` (`cid`),
  CONSTRAINT `implies_ibfk_1` FOREIGN KEY (`sid`) REFERENCES `symptoms` (`sid`) ON DELETE CASCADE,
  CONSTRAINT `implies_ibfk_2` FOREIGN KEY (`cid`) REFERENCES `conditionstreats` (`cid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `implies`
--

LOCK TABLES `implies` WRITE;
/*!40000 ALTER TABLE `implies` DISABLE KEYS */;
INSERT INTO `implies` VALUES (1,1,70),(2,1,90),(3,1,0),(4,1,0),(5,1,40),(6,1,0),(7,1,0),(8,1,20),(9,1,0),(10,1,0),(11,1,0),(12,1,30),(13,1,0),(14,1,0),(15,1,0),(16,1,0),(17,1,0),(1,2,70),(2,2,30),(3,2,0),(4,2,0),(5,2,80),(6,2,0),(7,2,0),(8,2,60),(9,2,30),(10,2,0),(11,2,0),(12,2,70),(13,2,0),(14,2,0),(15,2,0),(16,2,0),(17,2,0),(1,3,0),(2,3,0),(3,3,85),(4,3,0),(5,3,0),(6,3,0),(7,3,0),(8,3,70),(9,3,80),(10,3,0),(11,3,60),(12,3,5),(13,3,0),(14,3,40),(15,3,0),(16,3,0),(17,3,30),(1,4,0),(2,4,0),(3,4,0),(4,4,80),(5,4,0),(6,4,96),(7,4,0),(8,4,60),(9,4,0),(10,4,0),(11,4,30),(12,4,0),(13,4,0),(14,4,0),(15,4,0),(16,4,0),(17,4,0),(1,5,80),(2,5,90),(3,5,0),(4,5,0),(5,5,50),(6,5,0),(7,5,0),(8,5,0),(9,5,0),(10,5,0),(11,5,0),(12,5,20),(13,5,0),(14,5,0),(15,5,10),(16,5,0),(17,5,0),(1,6,0),(2,6,0),(3,6,0),(4,6,0),(5,6,0),(6,6,0),(7,6,98),(8,6,20),(9,6,30),(10,6,0),(11,6,0),(12,6,0),(13,6,10),(14,6,0),(15,6,0),(16,6,60),(17,6,0),(1,7,0),(2,7,0),(3,7,0),(4,7,0),(5,7,0),(6,7,0),(7,7,0),(8,7,0),(9,7,0),(10,7,0),(11,7,0),(12,7,0),(13,7,0),(14,7,0),(15,7,0),(16,7,100),(17,7,0),(1,8,0),(2,8,0),(3,8,0),(4,8,0),(5,8,0),(6,8,0),(7,8,0),(8,8,0),(9,8,0),(10,8,90),(11,8,0),(12,8,0),(13,8,0),(14,8,0),(15,8,0),(16,8,0),(17,8,0),(1,9,0),(2,9,0),(3,9,50),(4,9,80),(5,9,0),(6,9,0),(7,9,40),(8,9,10),(9,9,0),(10,9,0),(11,9,90),(12,9,30),(13,9,0),(14,9,80),(15,9,0),(16,9,0),(17,9,6),(1,10,10),(2,10,0),(3,10,20),(4,10,0),(5,10,40),(6,10,6),(7,10,0),(8,10,80),(9,10,60),(10,10,70),(11,10,20),(12,10,40),(13,10,0),(14,10,7),(15,10,0),(16,10,0),(17,10,0),(1,11,0),(2,11,0),(3,11,0),(4,11,0),(5,11,0),(6,11,0),(7,11,20),(8,11,0),(9,11,0),(10,11,0),(11,11,0),(12,11,0),(13,11,99),(14,11,3),(15,11,0),(16,11,0),(17,11,0),(1,12,0),(2,12,0),(3,12,6),(4,12,0),(5,12,10),(6,12,0),(7,12,0),(8,12,40),(9,12,20),(10,12,0),(11,12,0),(12,12,0),(13,12,0),(14,12,90),(15,12,0),(16,12,0),(17,12,2),(1,13,0),(2,13,0),(3,13,0),(4,13,0),(5,13,30),(6,13,0),(7,13,0),(8,13,50),(9,13,0),(10,13,0),(11,13,0),(12,13,0),(13,13,0),(14,13,0),(15,13,95),(16,13,0),(17,13,0);
/*!40000 ALTER TABLE `implies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `insurance`
--

DROP TABLE IF EXISTS `insurance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `insurance` (
  `name` char(30) DEFAULT NULL,
  `description` char(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `insurance`
--

LOCK TABLES `insurance` WRITE;
/*!40000 ALTER TABLE `insurance` DISABLE KEYS */;
INSERT INTO `insurance` VALUES ('Cigna',""),('AIG',""),('State Farm',""),('Kaiser',"");
/*!40000 ALTER TABLE `insurance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `knows`
--

DROP TABLE IF EXISTS `knows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `knows` (
  `did` int(11) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  KEY `did` (`did`),
  KEY `tid` (`tid`),
  CONSTRAINT `knows_ibfk_1` FOREIGN KEY (`did`) REFERENCES `doctors` (`did`) ON DELETE CASCADE,
  CONSTRAINT `knows_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `treatments` (`tid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `knows`
--

LOCK TABLES `knows` WRITE;
/*!40000 ALTER TABLE `knows` DISABLE KEYS */;
INSERT INTO `knows` VALUES (1,0),(1,1),(1,2),(1,3),(1,5),(1,6),(6,0),(6,2),(6,5),(6,7),(2,5),(2,6),(3,2),(3,3),(3,4),(4,0),(4,1),(4,2),(4,5),(4,6),(5,2),(5,3),(5,5),(5,6),(7,0),(7,1),(7,2),(7,6),(8,1),(8,2),(8,5),(8,6),(8,7),(9,1),(9,5),(9,6);
/*!40000 ALTER TABLE `knows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patients`
--

DROP TABLE IF EXISTS `patients`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `patients` (
  `pid` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `dob` char(30) DEFAULT NULL,
  `weight` double DEFAULT NULL,
  PRIMARY KEY (`pid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patients`
--

LOCK TABLES `patients` WRITE;
/*!40000 ALTER TABLE `patients` DISABLE KEYS */;
INSERT INTO `patients` VALUES (1,'Paul McCormack','2/18/1991',140),(2,'Marie von Chek','3/12/1940',136),(3,'Elaine Rich','1/19/1965',120),(4,'Little Bobby','4/30/2007',70),(5,'Monsieur Gilbert','5/20/78',240);
/*!40000 ALTER TABLE `patients` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `symptomlist`
--

DROP TABLE IF EXISTS `symptomlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `symptomlist` (
  `aid` int(11) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  KEY `aid` (`aid`),
  KEY `sid` (`sid`),
  CONSTRAINT `symptomlist_ibfk_1` FOREIGN KEY (`aid`) REFERENCES `appointments` (`aid`) ON DELETE CASCADE,
  CONSTRAINT `symptomlist_ibfk_2` FOREIGN KEY (`sid`) REFERENCES `symptoms` (`sid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symptomlist`
--

LOCK TABLES `symptomlist` WRITE;
/*!40000 ALTER TABLE `symptomlist` DISABLE KEYS */;
INSERT INTO `symptomlist` VALUES (1,1),(1,2),(1,5),(1,12),(1,15),(3,16),(4,3),(4,8),(4,9),(4,17),(5,3),(5,11),(6,8),(8,6),(9,5),(9,13),(9,17);
/*!40000 ALTER TABLE `symptomlist` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `symptoms`
--

DROP TABLE IF EXISTS `symptoms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `symptoms` (
  `sid` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `descr` char(30) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `symptoms`
--

LOCK TABLES `symptoms` WRITE;
/*!40000 ALTER TABLE `symptoms` DISABLE KEYS */;
INSERT INTO `symptoms` VALUES (1,'Abdominal pain',''),(2,'Constipation',''),(3,'Cough',''),(4,'Decreased hearing',''),(5,'Diarrhea',''),(6,'Earache',''),(7,'Eye discomfort and redness',''),(8,'Fever',''),(9,'Headaches',''),(10,'Joint pain or muscle pain',''),(11,'Nasal congestion',''),(12,'Nausea or vomiting',''),(13,'Skin rashes',''),(14,'Sore throat',''),(15,'Urinary problems',''),(16,'Vision problems',''),(17,'Wheezing','');
/*!40000 ALTER TABLE `symptoms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `takesprescriptions`
--

DROP TABLE IF EXISTS `takesprescriptions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `takesprescriptions` (
  `pid` int(11) DEFAULT NULL,
  `tid` int(11) DEFAULT NULL,
  `did` int(11) DEFAULT NULL,
  `howlong` char(30) DEFAULT NULL,
  KEY `pid` (`pid`),
  KEY `tid` (`tid`),
  KEY `did` (`did`),
  CONSTRAINT `takesprescriptions_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `patients` (`pid`) ON DELETE CASCADE,
  CONSTRAINT `takesprescriptions_ibfk_2` FOREIGN KEY (`tid`) REFERENCES `treatments` (`tid`) ON DELETE CASCADE,
  CONSTRAINT `takesprescriptions_ibfk_3` FOREIGN KEY (`did`) REFERENCES `doctors` (`did`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `takesprescriptions`
--

LOCK TABLES `takesprescriptions` WRITE;
/*!40000 ALTER TABLE `takesprescriptions` DISABLE KEYS */;
INSERT INTO `takesprescriptions` VALUES (5,4,3,'indefinitely'),(1,2,5,'indefinitely'),(5,6,2,'indefinitely'),(4,6,4,'indefinitely'),(4,6,1,'indefinitely'),(4,2,3,'indefinitely'),(4,6,4,'indefinitely');
/*!40000 ALTER TABLE `takesprescriptions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `treatments`
--

DROP TABLE IF EXISTS `treatments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `treatments` (
  `tid` int(11) NOT NULL,
  `name` char(30) NOT NULL,
  `cost` double DEFAULT NULL,
  `info` char(100) DEFAULT NULL,
  `maker` char(10) DEFAULT NULL,
  `fid` int(11) DEFAULT NULL,
  `sideeffects` char(30) DEFAULT NULL,
  PRIMARY KEY (`tid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `treatments`
--

LOCK TABLES `treatments` WRITE;
/*!40000 ALTER TABLE `treatments` DISABLE KEYS */;
INSERT INTO `treatments` VALUES (1,'Hospital Visit',0,'You need medical attention at the hospital.','NULL',NULL,''),(2,'Drinking Fluids',0,'to replace lost electrolytes and flush your system','NULL',NULL,''),(3,'Antibiotics',30,'antibiotics will kill bacteria causing the infection','NULL',NULL,''),(4,'Eye Drops',25,'to be administered in the eye','NULL',NULL,''),(5,'Glasses',180,'get prescription glasses from your eye doctor','NULL',NULL,''),(6,'NSAID',15,'non-steroidal anti-inflammatory drugs','NULL',NULL,'thinning of blood'),(7,'Rest',0,'just rest','NULL',NULL,''),(8,'Corticosteroid',40,'only sometimes, usually goes away','NULL',NULL,'');
/*!40000 ALTER TABLE `treatments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `uses`
--

DROP TABLE IF EXISTS `uses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `uses` (
  `pid` int(11) DEFAULT NULL,
  `name` char(30) DEFAULT NULL,
  PRIMARY KEY (`pid`),
  CONSTRAINT `uses_ibfk_1` FOREIGN KEY (`pid`) REFERENCES `patients` (`pid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `uses`
--

LOCK TABLES `uses` WRITE;
/*!40000 ALTER TABLE `uses` DISABLE KEYS */;
INSERT INTO `uses` VALUES (1,'Cigna'),(2,'AIG'),(3,'Cigna'),(4,'State Farm'),(5,'Kaiser');
/*!40000 ALTER TABLE `uses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worksin`
--

DROP TABLE IF EXISTS `worksin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worksin` (
  `did` int(11) DEFAULT NULL,
  `fid` int(11) DEFAULT NULL,
  KEY `did` (`did`),
  KEY `fid` (`fid`),
  CONSTRAINT `worksin_ibfk_1` FOREIGN KEY (`did`) REFERENCES `doctors` (`did`) ON DELETE CASCADE,
  CONSTRAINT `worksin_ibfk_2` FOREIGN KEY (`fid`) REFERENCES `facilities` (`fid`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worksin`
--

LOCK TABLES `worksin` WRITE;
/*!40000 ALTER TABLE `worksin` DISABLE KEYS */;
INSERT INTO `worksin` VALUES (1,1),(1,2),(2,4),(3,1),(3,2),(4,3),(5,5),(7,1),(7,2),(7,3),(8,1),(9,5);
/*!40000 ALTER TABLE `worksin` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- now create all triggers and procedures to check
-- for declaring errors
create table error(msg CHAR(100) PRIMARY KEY);
insert into error values("Cannot perform operations on Symptoms"), ("Condition and Symptoms must be changed simultaneously."), ("empty");

-- NO INSERT, UPDATE, DELETE ON SYMPTOMS TABLE
create trigger reject_symptom_insert before insert on Symptoms
for each row insert into error values("Cannot perform operations on Symptoms");

create trigger reject_symptom_delete before delete on Symptoms
for each row insert into error values("Cannot perform operations on Symptoms");

create trigger reject_symptom_update before update on Symptoms
for each row insert into error values("Cannot perform operations on Symptoms");

-- MAKE SURE IMPLIES TABLE IS COMPLETELY FILLED OUT (EACH CONDITION HAS CORRESPONDING SYMPTOMS) ON INSERT
-- so clearly (num_symptoms)*(num_conditions) = (num_implies) will check the condition since the foreign key constraints prevent
-- fake data

-- CREATE PROCEDURE check_CST()
-- update error
-- set msg = IF((select count(*) from symptoms)*(select count(*) from conditionstreats) = (select count(*) from implies), "empty", "Condition and Symptoms must be changed simultaneously.")
-- WHERE msg = "empty";

-- create trigger cond_symp_1 after insert on ConditionsTreats
-- for each row call check_CST();

-- create trigger cond_symp_2 after insert on Implies
-- for each row call check_CST();

-- create trigger cond_symp_3 after delete on Implies
-- for each row call check_CST();

-- UPDATE 

-- CREATE TRIGGER insurancecountup BEFORE INSERT on `Uses`
--     FOR EACH ROW
--        BEGIN
--            UPDATE Insurance
--            SET num_users = num_users + 1
--            WHERE name = NEW.name;
--        END;
-- |

-- CREATE TRIGGER insurancecountdown BEFORE DELETE on `Uses`
--        FOR EACH ROW
--        BEGIN
--            UPDATE Insurance
--            SET num_users = num_users - 1
--            WHERE name = OLD.name;
--        END;
-- |


-- CREATE INDEXES (see google doc)
CREATE INDEX patients ON Patients(pid,name) USING BTREE;
CREATE INDEX doctors ON Doctors(did,name) USING BTREE;
CREATE INDEX takesprescriptions_pid ON TakesPrescriptions(pid) USING HASH;
CREATE INDEX appointments_pid ON Appointments(pid) USING HASH;
CREATE INDEX appointments_did ON Appointments(did) USING HASH;
CREATE INDEX appointments_aid ON Appointments(aid) USING HASH;
CREATE INDEX symptomlist_aid ON SymptomList(aid) USING HASH;
CREATE INDEX implies_cid ON Implies(cid,sid, probability) USING BTREE;
CREATE INDEX worksin_did ON WorksIn(did,fid) USING BTREE;
CREATE INDEX knows_tid ON Knows(tid,did) USING BTREE;

commit;
set autocommit=on;