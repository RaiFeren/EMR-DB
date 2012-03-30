/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */

Running on Windows XP SP3, not using Eclipse.

Unzip the folder pjmps6 into the webapps subdirectory of the apache folder.

First execute p6.sql to create the necessary tables.  Not closing the sql terminal window sometimes caused my servlet's delete statements to freeze, since some locks were not released.

The .class files are in the directory already, but you can compile it again from within the directory using
	javac -classpath servlet-api.jar *.java

After starting the java web server, open the servlet using:
	http://localhost:8080/emr/patients
	http://localhost:8080/emr/doctors
	http://localhost:8080/emr/admins
	
From this page you can make all queries. The screenshots are located in /pjmps6/screenshots/




