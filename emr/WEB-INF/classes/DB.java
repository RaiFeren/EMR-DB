/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */
 // from store example in class
import java.sql.*;


public class DB {


    // Load the database driver once
    static {
	try {
            System.out.println("About to load the driver");
            Class.forName("com.mysql.jdbc.Driver");
            //            Class.forName("org.gjt.mm.mysql.Driver");
            System.out.println("The driver is loaded");
	}
	catch (ClassNotFoundException cnfe) {
	    throw new RuntimeException(cnfe);
	}
    }



    // Creates connections to the database.

    public static Connection openConnection () throws SQLException {
	return DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/pjmps6",
	     	"root",
	     	"bassoon");
    }

}
