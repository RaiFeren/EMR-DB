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
public class DoctorServlet extends HttpServlet {

	// main page (not template page) for patient portal
	public static final String DOCTOR_TEMPLATE = "../webapps/emr/res/doctor_template.html";
	public static final String DOCTOR_MAIN = "../webapps/emr/res/doctors.html";

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

		// send the DOCTOR_MAIN page back
        out.println(readFileAsString(DOCTOR_MAIN));
    }

	// respond to a post by interpreting form information and printing requested output
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// see which form was submitted based on state of submit button
		if(request.getParameter("login") != null)				// log in to an account
			doLogin(request, out);
		else if(request.getParameter("update") != null)			// update treatments known
			updateTreatmentsKnown(request, out);

        // Set up the response
        response.setContentType("text/html");
    }


	// log in to a doctors's page
    public void doLogin(HttpServletRequest request, PrintWriter out){
		try{
			// get login ID
			int doctorID = Integer.parseInt(request.getParameter("did"));

			out.println(generate_doctor_page(doctorID, doctorName, symptoms, date, patient, loc_id, loc_name, condition, treatment, appt_id, loc_addr_1, loc_addr_2,
                all_loc_id, allLocations, locationAvailable, treat_id, treatName, treatKnown));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// update list of treatments known
	public void updateTreatmentsKnown(HttpServletRequest request, PrintWriter out){
		try{
			// get symptoms and decide on condition
			out.println(generate_doctor_page(doctorID, doctorName, symptoms, date, patient, loc_id, loc_name, condition, treatment, appt_id, loc_addr_1, loc_addr_2,
                all_loc_id, allLocations, locationAvailable, treat_id, treatName, treatKnown));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	private String generate_doctor_page(int doctorID, String doctorName, String[][] symptoms, String[] date, String[] patient, int[] loc_id, String[] loc_name, String[] condition, String[] treatment, int[] appt_id, String[] loc_addr_1, String[] loc_addr_2, int[] all_loc_id, String[] allLocations, boolean[] locationAvailable, int[] treat_id, String[] treatName, boolean[] treatKnown) throws IOException {

        String html = readFileAsString(DOCTOR_TEMPLATE);
        String appointmentRows = "";
        // concatenated into comma-separated lists
        String[] symptomStrings = new String[symptoms.length];
        String allLocationRows = "";
        String locationDividers = "";
        String treatmentsKnown = "<tr>";


        for (int row = 0; row < symptoms.length; row++) {
            symptomStrings[row] = "";
            for (int i = 0; i < symptoms[row].length; i++) {
                if (i != 0)
                    symptomStrings[row] += ", ";
                symptomStrings[row] += symptoms[row][i];
            }
        }

        for (int appt = 0; appt < appt_id.length; appt++) {
            appointmentRows += String.format("<tr> <td> %s </td> <td> %s </td> <td> <a href=\"#\" onclick=\"showhide('location_%d');\">%s</a> </td> <td> %s </td> <td> %s </td> <td> %s </td> </tr>", date[appt], patient[appt], loc_id[appt], loc_name[appt], symptomStrings[appt], condition[appt], treatment[appt]);
        }

        for (int loc = 0; loc < loc_id.length; loc++)
            locationDividers += String.format("<div id=\"location_%d\" style=\"display: none;\">" + "<br>\n" + "<b> %s </b>\n" + "<ul>\n" + "%s <br>\n" + "%s\n" + "</ul>\n" + "</div>\n\n", loc_id[loc], loc_name[loc], loc_addr_1[loc], loc_addr_2[loc]);

        for (int i = 0; i < all_loc_id.length; i++) {
            allLocationRows += String.format("<tr><td> <input type=\"checkbox\" name=\"loc_%d\" %s> %s </td> </tr>\n", all_loc_id[i], locationAvailable[i] ? "checked" : "", allLocations[i]);
        }

        for (int i = 0; i < treat_id.length; i++) {
            treatmentsKnown += String.format("\t<td> <input type=\"checkbox\" name=\"treat_%d\" %s> %s </td>", treat_id[i], treatKnown[i] ? "checked": "", treatName[i]);
            if (i > 0 && i < treat_id.length - 1 && i % 4 == 3)
                treatmentsKnown += "</tr>\n<tr>";
        }
        treatmentsKnown += "</tr>";

        html = html.replace("%DOCTOR_ID%", doctorID + "");
        html = html.replace("%DOCTOR_NAME%", doctorName);
        html = html.replace("%APPOINTMENT_ROWS%", appointmentRows);
        html = html.replace("%LOCATION_DIVIDERS%", locationDividers);
        html = html.replace("%AVAILABLE_LOCATIONS%", allLocationRows);
        html = html.replace("%TREATMENTS_KNOWN%", treatmentsKnown);

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
