// Paul McCormack and Rai Feren
// 010186829 - PO and 40152662 - HMC
// CS 133 Final Project, Due 5/1/2012
// DB.java manages the database connection and
//   facilitates transaction management and
//   query processing

import java.sql.*;
import java.util.ArrayList;

public class DB {

	// the connection and transaction records
	private static Connection connection = null;
	private static boolean inTransaction = false;
	private static Statement statement = null;


	// begin a transaction by opening a connection and disabling autocommit (equiv to BEGIN TRANSACTION according to JDBC docs)
	public static void beginTransaction(){
		try{
			// make sure another transaction is not in progress.  if so, close it
			if (inTransaction)
				endTransaction();

			// open new connection to database
			connection = DB.openConnection();
			connection.setAutoCommit(false);
			statement = connection.createStatement();
			inTransaction = true;

		} catch (SQLException sqle){
			throw new RuntimeException("Error accessing database: " + sqle);
		}
	}

	// end current transaction
	public static void endTransaction(){
		try{
			// make sure a transaction is in progress
			if (!inTransaction)
				return;

			// commit changes
			connection.commit();
			connection.close();
			inTransaction = false;

		} catch (SQLException sqle){
			throw new RuntimeException("Error accessing database: " + sqle);
		}
	}


    // execute a query, which returns the result set in a nested arraylist
	public static ArrayList<ArrayList<Object>> executeQuery(String query, int num_cols) {
		// if not currently in a transaction, just run the single query
		boolean singleStatement = !inTransaction;
		if (singleStatement)
			beginTransaction();


		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();

		try {

			// Send query to database and receive result
			ResultSet resultSet = statement.executeQuery(query);

			// Compose the rows.
			while (resultSet.next()) {
				ArrayList<Object> row = new ArrayList<Object>(num_cols);
				result.add(row);

				for (int i = 1; i < num_cols+1; i++)
					row.add(resultSet.getObject(i));
			}
		} catch (SQLException sqle){
			throw new RuntimeException("Error accessing database: " + sqle);
		}

		if (singleStatement)
			endTransaction();

		return result;
	}

	// execute an update, returns an integer that sql returns
	public static int executeUpdate(String query) {
		// if not currently in a transaction, just run the single query
		boolean singleStatement = !inTransaction;
		if (singleStatement)
			beginTransaction();

		int result = -1;

		try {
			// Send query to database and receive result
			result =  statement.executeUpdate(query);
		} catch (SQLException sqle){
			throw new RuntimeException("Error accessing database: " + sqle);
		}

		if (singleStatement)
			endTransaction();

		return result;

    }

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
            ("jdbc:mysql://localhost:3306/emr",
	     	"root",
	     	"bassoon");
    }

}
