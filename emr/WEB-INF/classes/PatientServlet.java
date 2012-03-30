/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */

// Invoke it like this: http://localhost:8080/emr/patients

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

// displays page and performs various requests about electronics products
public class PatientServlet extends HttpServlet {

	// main page (not template page) for patient portal
	public static final String PATIENT_TEMPLATE = "../webapps/emr/res/patient_template.html";
	public static final String PATIENT_MAIN = "../webapps/emr/res/patients.html";

	// just for testing
	int patientID = 67;
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


	// respond to a GET request by just writing the page to the output
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set up the response
        response.setContentType("text/html");

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// send the PATIENT_MAIN page back
        out.println(readFileAsString(PATIENT_MAIN));
    }

	// respond to a post by interpreting form information and printing requested output
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// see which form was submitted based on state of submit button
		if(request.getParameter("login") != null)				// log in to an account
			doLogin(request, out);
		else if(request.getParameter("register") != null)		// create a new account
			registerPatient(request, out);
		else if(request.getParameter("create") != null)			// create an appointment
			createAppointment(request, out);
		else 													// canceling any appointment
			cancelAppointment(request, out);

        // Set up the response
        response.setContentType("text/html");
    }


	// log in to a patient's page
    public void doLogin(HttpServletRequest request, PrintWriter out){
		try{
			// get login ID
			int ID = Integer.parseInt(request.getParameter("pid"));

			out.println(generate_patient_page(ID, patientName, symptoms, date, doctor, loc_id, loc_name, appt_id, loc_addr_1, loc_addr_2, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// register a new patient
    public void registerPatient(HttpServletRequest request, PrintWriter out){
		try{
			// get name, dob, budget
			String patientName = request.getParameter("pname2");
			String dob = request.getParameter("dob");
			double budget = Double.parseDouble(request.getParameter("budget"));

			out.println(generate_patient_page(patientID, patientName, symptoms, date, doctor, loc_id, loc_name, appt_id, loc_addr_1, loc_addr_2, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// create an appointment by id (note that patient id is in hidden html field)
	public void createAppointment(HttpServletRequest request, PrintWriter out){
		try{
			// get symptoms and decide on condition

			out.println(generate_patient_page(patientID, patientName, symptoms, date, doctor, loc_id, loc_name, appt_id, loc_addr_1, loc_addr_2, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// cancel an appointment by id (note that patient id is in hidden html field)
	public void cancelAppointment(HttpServletRequest request, PrintWriter out){
		try{
			// cancel appointment
			out.println(generate_patient_page(patientID, patientName, symptoms, date, doctor, loc_id, loc_name, appt_id, loc_addr_1, loc_addr_2, sym_id, symptomNames));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	private String generate_patient_page(int patientID, String patientName, String[][] symptoms, String[] date, String[] doctor, int[] loc_id, String[] loc_name, int[] appt_id, String[] loc_addr_1, String[] loc_addr_2, int[] sym_id, String[] symptomNames) throws IOException {

	        String html = readFileAsString(PATIENT_TEMPLATE);
	        String appointmentRows = "";
	        // concatenated into comma-separated lists
	        String[] symptomStrings = new String[symptoms.length];
	        String allowedSymptomRows = "<tr>";
	        String locationDividers = "";

	        for (int row = 0; row < symptoms.length; row++) {
	            symptomStrings[row] = "";
	            for (int i = 0; i < symptoms[row].length; i++) {
	                if (i != 0)
	                    symptomStrings[row] += ", ";
	                symptomStrings[row] += symptoms[row][i];
	            }
	        }

	        for (int appt = 0; appt < appt_id.length; appt++) {
	            appointmentRows += String.format("<tr>" + "<td> %s </td>" + "<td> %s </td>" + "<td> <a href=\"#\" onclick=\"showhide('location_%d');\">%s</a> </td>" + "<td> %s </td>" + "<td> <input type=\"submit\" name=\"cancel_%d\" value=\"Cancel\"> </td> </tr>\n", date[appt], doctor[appt], loc_id[appt], loc_name[appt], symptomStrings[appt], appt_id[appt]);
	        }

	        for (int loc = 0; loc < loc_id.length; loc++)
	            locationDividers += String.format("<div id=\"location_%d\" style=\"display: none;\">" + "	<br>\n" + "	<b> %s </b>\n" + "	<ul>\n" + "		%s <br>\n" + "		%s\n" + "	</ul>\n" + "</div>\n\n", loc_id[loc], loc_name[loc], loc_addr_1[loc], loc_addr_2[loc]);

	        for (int i = 0; i < sym_id.length; i++) {
	            allowedSymptomRows += String.format("\t<td> <input type=\"checkbox\" name=\"sym_%d\"> %s </td>", sym_id[i], symptomNames[i]);
	            if (i > 0 && i < sym_id.length - 1 && i % 4 == 3)
	                allowedSymptomRows += "</tr>\n<tr>";
	        }
	        allowedSymptomRows += "</tr>";

	        html = html.replace("%PATIENT_ID%", patientID + "");
	        html = html.replace("%PATIENT_NAME%", patientName);
	        html = html.replace("%APPOINTMENT_ROWS%", appointmentRows);
	        html = html.replace("%LOCATION_DIVIDERS%", locationDividers);
	        html = html.replace("%POSSIBLE_SYMPTOMS%", allowedSymptomRows);

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
