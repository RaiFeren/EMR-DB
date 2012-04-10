/* Paul McCormack and Rai Feren
 * CS 133 Final Project
 * April 10, 2012
 */

First execute emr.sql to create the necessary tables.  Not closing the sql terminal window sometimes caused my servlet's delete statements to freeze, since some locks were not released.

The .class files are in the directory already, but you can compile it again from within the directory using
	javac -classpath servlet-api.jar *.java

After starting the java web server, open the servlet from index.txt.  From here are links to the portals:
	http://localhost:8080/emr/patients
	http://localhost:8080/emr/doctors
	http://localhost:8080/emr/admins
	





