/* Paul McCormack and Rai Feren
 * CS 133 Final Project
 * April 10, 2012
 */

First execute emr.sql to create the necessary tables.  

First execute these commands:
	DROP DATABASE IF EXISTS emr;
	CREATE DATABASE emr;
and then run the emr_backup.txt script.
mysql -u root -p --database=emr < "C:\Documents and Settings\pmccorma\emr_full_backup.sql"

To backup the sql tables to a script, use
	mysqldump -p --user=root emr > "C:\Documents and Settings\pmccorma\emr_full_backup.sql"

The .class files are in the directory already, but you can compile it again from within the directory using
	javac -classpath servlet-api.jar *.java

After starting the java web server, open the servlet from index.txt.  From here are links to the portals:
	http://localhost:8080/emr/patients
	http://localhost:8080/emr/doctors
	http://localhost:8080/emr/admins
	





