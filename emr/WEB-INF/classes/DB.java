/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */
 // from store example in class
import java.sql.*;
import java.util.ArrayList;

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

// will want to optmize connecting/disconnecting later

    // execute a query, which returns the result set
	public static ArrayList<ArrayList<Object>> executeQuery(String query, int num_cols) {

		Connection connection = null;
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();

		try {

			// Establish network connection to database
			connection = DB.openConnection();

			// Create a statement for executing the query
			Statement statement = connection.createStatement();

			// Send query to database and receive result
			ResultSet resultSet = statement.executeQuery(query);

			// Compose the rows.
			while (resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>(num_cols);
				result.add(row);

				for (int i = 1; i < num_cols+1; i++)
					row.add(resultSet.getObject(i));
			}
			connection.close();
		} catch (SQLException sqle) {
			throw new RuntimeException("Error accessing database: " + sqle);
		}
		return result;
	}

	// execute an update, returns an integer that sql returns
	public static int executeUpdate(String query) {

		Connection connection = null;
		int result = -1;

		try {

			// Establish network connection to database
			connection = DB.openConnection();

			// Create a statement for executing the query
			Statement statement = connection.createStatement();

			// Send query to database and receive result
			result = statement.executeUpdate(query);

			connection.close();
		} catch (SQLException sqle) {
			throw new RuntimeException("Error accessing database: " + sqle);
		}
		return result;
    }



    // Creates connections to the database.

    public static Connection openConnection () throws SQLException {
	return DriverManager.getConnection
            ("jdbc:mysql://localhost:3306/emr",
	     	"root",
	     	"bassoon");
    }

}
