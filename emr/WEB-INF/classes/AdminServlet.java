/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */

// Invoke it like this: http://localhost:8080/emr/doctors

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

// displays page and performs various requests about electronics products
public class AdminServlet extends HttpServlet {

	// main page (not template page) for patient portal
	public static final String ADMIN_TEMPLATE = "../webapps/emr/res/admins_template.html";

	// just for testing
	int patientID = 89;
	String patientName = "Maurice";
	String[][] symptoms = { { "cold", "drowsy" }, { "none" } };
	String[] date = { "05/31/12", "09/45/23" };
	String[] doctor = { "Dr. One", "Dr. Two" };
	int[] loc_id = { 5, 8 };
	String[] loc_name = { "Meadows Branch", "Eisenhower" };
	int[] appt_id = { 7, 12 };
	String[] loc_addr_1 = { "45 Utica", "9824 Linden Park Dr." };
	String[] loc_addr_2 = { "San Antonio, TX", "Boulder, CO" };
	int[] sym_id = { 6, 1, 3, 4, 7, 68, 23 };
	String[] symptomNames = { "nausea", "vomiting", "headache", "runny nose", "sore muscles", "itchy eyes", "rashes" };

	int doctorID = 293;
	String doctorName = "Dr. Leadville";
	String[] patient = {"Watson", "Clarke"};
	String[] condition = {"Flu", "Cholera"};
	String[] treatment = {"Rest", "Death"};
	int[] all_loc_id = {5,8,7,9};
	String[] allLocations = {"Loc 5", "Loc 8", "Loc 7", "Loc 9"};
	boolean[] locationAvailable = {true, true, false, true};
	int[] treat_id = {1,2,3,4,5,6,7};
	String[] treatName = {"treat 1", "treat 2", "treat 3", "treat 4", "treat 5", "treat 6", "treat 7"};
	boolean[] treatKnown = {false, false, true, true, false, false, true};

	int[] cond_id = {1,2,3,4,5,6};
	String[] condName = {"Diptheria", "Cholera", "Giardia", "Flu", "Tiredness", "Exhaustion"};


	// respond to a GET request by just writing the page to the output
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set up the response
        response.setContentType("text/html");

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// send the adminstrative page back
        out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));
    }

	// respond to a post by interpreting form information and printing requested output
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// see which form was submitted based on state of submit button
		if(request.getParameter("add_treatment") != null)				// add a treatment
			addTreatment(request, out);
		else if(request.getParameter("add_condition") != null)			// add a condition
			addCondition(request, out);
		else if(request.getParameter("add_facility") != null)			// add a facility
			addFacility(request, out);
		else if(request.getParameter("add_doctor") != null)				// add a doctor
			addDoctor(request, out);

        // Set up the response
        response.setContentType("text/html");
    }


	// add a treatment
    public void addTreatment(HttpServletRequest request, PrintWriter out){
		try{

			out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// add a condition
    public void addCondition(HttpServletRequest request, PrintWriter out){
		try{

			out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// add a facility
    public void addFacility(HttpServletRequest request, PrintWriter out){
		try{

			out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// add a doctor
    public void addDoctor(HttpServletRequest request, PrintWriter out){
		try{

			out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}


	public String generate_admin_page(int[] cond_id, String[] condName, int[] sym_id, String[] symName) throws IOException {

		String html = readFileAsString(ADMIN_TEMPLATE);
		String symptomProbabilities = "";
		String treatsConditionsList = "<tr>";

		for (int i = 0; i < sym_id.length; i++) {
			symptomProbabilities += String.format("<tr> <td> %s </td><td><input type=\"text\" name=\"prob_%d\" size=1> </td> </tr>\n", symName[i], sym_id[i]);
		}

		for (int i = 0; i < cond_id.length; i++) {
			treatsConditionsList += String.format("<td> <input type=\"checkbox\" name=\"cond_%d\"> %s </td>", cond_id[i], condName[i]);
			if (i > 0 && i < cond_id.length - 1 && i % 4 == 3)
				treatsConditionsList += "</tr>\n<tr>";
		}
		treatsConditionsList += "</tr>";

		html = html.replace("%SYMPTOM_LIST%", symptomProbabilities + "");
		html = html.replace("%CONDITION_LIST%", treatsConditionsList);

		return html;
    }



	// read a file as a string
	// from http://snippets.dzone.com/posts/show/1335
	private String readFileAsString(String filePath) {
		byte[] buffer = new byte[(int) new File(filePath).length()];
		try{
			BufferedInputStream f = null;
			try {
				f = new BufferedInputStream(new FileInputStream(filePath));
				f.read(buffer);
			} finally {
				if (f != null)
					try {
						f.close();
					} catch (IOException ignored) {
					}
			}
		} catch (IOException e) {
			return e.getMessage() + "\n" + System.getProperty("user.dir");
		}
		return new String(buffer);

    }

    // Displays a table row for each item
    private void printResultSet(PrintWriter out, String query, int num_cols) {

        Connection connection = null;

        try {

            // Establish network connection to database
            connection = DB.openConnection();

            // Create a statement for executing the query
            Statement statement = connection.createStatement();

            // Send query to database and receive result
            ResultSet resultSet = statement.executeQuery(query);

            // Compose the rows.
            while (resultSet.next()) {
                out.println("<TR>");
                for (int i = 1; i < num_cols+1; i++)
	                out.println("<TD>" + resultSet.getObject(i) + "</TD>");
                out.println("</TR>");
            }
			connection.close();
        } catch (SQLException sqle) {
            throw new RuntimeException("Error accessing database: " + sqle);
		}


    }

}
