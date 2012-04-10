-- Paul McCormack and Rai Feren
-- CS 133 Final Project
-- Spring 2012
 
DROP DATABASE IF EXISTS emr;

CREATE DATABASE emr;

-- GRANT ALL PRIVILEGES ON emr.* to root@localhost IDENTIFIED BY 'root';

USE emr;

CREATE TABLE Patients(pid INT PRIMARY KEY, name CHAR(30) NOT NULL, dob CHAR(30), weight REAL);

CREATE TABLE Insurance(name CHAR(30), num_users INT);

CREATE TABLE Doctors(did INT PRIMARY KEY, name CHAR(30) NOT NULL, degree CHAR(10));

CREATE TABLE Facilities(fid INT PRIMARY KEY, name CHAR(30) NOT NULL, addr1 CHAR(30) NOT NULL, addr2 CHAR(30));

CREATE TABLE Appointments(	aid INT PRIMARY KEY, 
				pid INT, 
				did INT, 
				fid INT, 
				date CHAR(11) NOT NULL,
				FOREIGN KEY (pid) REFERENCES Patients (pid),
				FOREIGN KEY (did) REFERENCES Doctors (did),
				FOREIGN KEY (fid) REFERENCES Facilities (fid));

CREATE TABLE Symptoms(sid INT PRIMARY KEY, name CHAR(30) NOT NULL, descr CHAR(30));

CREATE TABLE SymptomList(	aid INT, 
				sid INT, 
				FOREIGN KEY (aid) REFERENCES Appointments (aid) ON DELETE CASCADE, 
				FOREIGN KEY (sid) REFERENCES Symptoms (sid) ON DELETE CASCADE);

CREATE TABLE Treatments(tid INT PRIMARY KEY, name CHAR(30) NOT NULL, cost REAL, info CHAR(100), maker CHAR(10), fid INT, sideeffects CHAR(30));

CREATE TABLE TakesPrescriptions(pid INT, 
				tid INT, 
				did INT, 
				howlong CHAR(10),
				FOREIGN KEY (pid) REFERENCES Patients (pid), 
				FOREIGN KEY (tid) REFERENCES Treatments (tid), 
				FOREIGN KEY (did) REFERENCES Doctors (did));

CREATE TABLE ConditionsTreats(	cid INT PRIMARY KEY, 
				name CHAR(30) NOT NULL, 
				info CHAR(100), 
				probability REAL, 
				tid INT,
				FOREIGN KEY (tid) REFERENCES Treatments (tid));

CREATE TABLE WorksIn(	did INT, 
			fid INT, 
			FOREIGN KEY (did) REFERENCES Doctors (did), 
			FOREIGN KEY (fid) REFERENCES Facilities (fid));
			
			
CREATE TABLE Knows(	did INT, 
			cid INT, 
			FOREIGN KEY (did) REFERENCES Doctors (did), 
			FOREIGN KEY (cid) REFERENCES ConditionsTreats (cid));
			
CREATE TABLE Implies(	sid INT, 
			cid INT, 
			probability REAL,
			FOREIGN KEY (sid) REFERENCES Symptoms (sid), 
			FOREIGN KEY (cid) REFERENCES ConditionsTreats (cid));
			
-- FOREIGN KEY (name) REFERENCES Insurance (name)
CREATE TABLE Uses(	pid INT, 
			name CHAR(30),
			FOREIGN KEY (pid) REFERENCES Patients (pid));

-- trigger to change insurance uses number on delete/insert from uses


-- so it goes faster
SET autocommit = 0;

insert into patients values(1, "Paul McCormack", "3/45/56", 140);

insert into doctors values(1, "Dr. Nobody", "DDS");

insert into facilities values(1, "Meadows Branch", "2983 Lincoln Ave.", "Redwood, CA 97876");

insert into Appointments values(1, 1, 1, 1, "1/1/12");

insert into treatments(tid, name, cost) values (1, "Sleep", 0);
insert into treatments(tid, name, cost) values (2, "Oral Rehydration Salts", 7.75);
insert into takesprescriptions values(1, 1, 1, "long time");

insert into symptoms values(1, "Nausea", "");
insert into symptoms values(2, "Vomiting", "");
insert into symptoms values(3, "Headache", "");
insert into symptoms values(4, "Runny Nose", "");
insert into symptoms values(5, "Sore Muscles", "");
insert into symptoms values(6, "Itchy Eyes", "");
insert into symptoms values(7, "Rashes", "");

insert into SymptomList values(1, 4);
insert into SymptomList values(1, 6);

insert into worksin values(1, 1);

insert into conditionstreats values(1, "Allergies", "", 0.6, 1);
insert into conditionstreats values(2, "Cholera", "from contaminated food/water", 0.003, 2);

insert into knows values(1, 1);

insert into insurance values("Cigna", 1);
insert into uses values(1,"Cigna");

COMMIT;
SET autocommit = 1;