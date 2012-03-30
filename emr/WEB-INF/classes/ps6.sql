-- Paul McCormack and Rai Feren
-- CS 133 Final Project
-- Spring 2012
 
DROP DATABASE IF EXISTS emr;

CREATE DATABASE emr;

--GRANT ALL PRIVILEGES ON emr.* to root@localhost IDENTIFIED BY 'root';

USE emr;

CREATE TABLE Products (
	maker CHAR(50),
	model INT PRIMARY KEY,
	type CHAR(7)
);

CREATE TABLE PCs(
	model INT PRIMARY KEY,
	speed DOUBLE,
	ram INT,
	hd INT,
	price DOUBLE,
	FOREIGN KEY (model) REFERENCES Products(model) ON DELETE CASCADE
);

CREATE TABLE Laptops(
	model INT PRIMARY KEY,
	speed DOUBLE,
	ram INT,
	hd INT,
	screen DOUBLE,
	price DOUBLE,
	FOREIGN KEY (model) REFERENCES Products(model) ON DELETE CASCADE
);

CREATE TABLE Printers(
	model INT PRIMARY KEY,
	color BOOL,
	type CHAR(7),
	price DOUBLE,
	FOREIGN KEY (model) REFERENCES Products(model) ON DELETE CASCADE
);

-- so it goes faster
SET autocommit = 0;

-- ALL PRODUCTS
INSERT INTO Products(maker, model, type) VALUES("Panasonic",0,"PC");


COMMIT;
SET autocommit = 1;