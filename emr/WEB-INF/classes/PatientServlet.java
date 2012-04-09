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
import java.util.ArrayList;

// displays page and performs various requests about electronics products
public class PatientServlet extends HttpServlet {

	// main page (not template page) for patient portal
	public static final String PATIENT_TEMPLATE = "../webapps/emr/res/patient_template.html";
	public static final String PATIENT_MAIN = "../webapps/emr/res/patients.html";

/*
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
	*/


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

		// Set up the response
        response.setContentType("text/html");

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
    }


	// log in to a patient's page
    public void doLogin(HttpServletRequest request, PrintWriter out){
		try{
			// get login ID
			int patientID = Integer.parseInt(request.getParameter("pid"));

			// get records from database or else reject input
			ArrayList<ArrayList<Object>> result = DB.executeQuery("SELECT name FROM Patients WHERE pid=" + patientID + ";", 1);
			if (result.isEmpty()){
				// send the PATIENT_MAIN page back
				String errorPage = readFileAsString(PATIENT_MAIN);
				errorPage = errorPage.replace("<div id=\"bad_id\" style=\"display: none;\">", "<div id=\"bad_id\" style=\"display: block;\">");
				errorPage = errorPage.replace("%BAD_ID%", patientID + "");
        		out.println(errorPage);
        		return;
			}
			else{
				out.println(generate_patient_page(patientID));
			}

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
			String insurance = request.getParameter("insurance");
			double weight = Double.parseDouble(request.getParameter("weight"));
			int patientID = 0;

			// create a new pid for this patient by finding maximum of all current IDs
			Object result = DB.executeQuery("SELECT MAX(pid) FROM Patients", 1).get(0).get(0);
			if (result == null)
				patientID = 1;
			else
				patientID = Integer.parseInt(result.toString()) + 1;

			// insert patient into database
			DB.executeUpdate("INSERT INTO Patients VALUES(" + patientID + ", \"" + patientName + "\", \"" + dob  + "\", " + weight + ");");

			// insert information into insurance and uses table

			// will not remove existing row by primary key constraint
			DB.executeUpdate("INSERT INTO Insurance VALUES(\"" + insurance + "\", 0);");
			DB.executeUpdate("UPDATE Insurance SET num_users = num_users + 1 WHERE name LIKE \"" + insurance + "\";");
			DB.executeUpdate("INSERT INTO Uses VALUES(" + patientID + ", \"" + insurance + "\");");

			out.println(generate_patient_page(patientID));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// create an appointment by id (note that patient id is in hidden html field)
	public void createAppointment(HttpServletRequest request, PrintWriter out){
		try{
			// get patient ID
			int patientID = Integer.parseInt(request.getParameter("pid"));

			// get symptoms and decide on condition
			ArrayList<ArrayList<Object>> possibleSymptoms = DB.executeQuery("SELECT sid FROM Symptoms", 1);
			ArrayList<Integer> symptoms = new ArrayList<Integer>();
			for (int i = 0; i < possibleSymptoms.size(); i++)
				if (request.getParameter("sym_" + possibleSymptoms.get(i).get(0)) != null)
					symptoms.add((Integer) possibleSymptoms.get(i).get(0));

			// create a new appointment
			// get a new appointment id
			Integer aid = 1;
			Object result = DB.executeQuery("SELECT MAX(aid) FROM Appointments", 1).get(0).get(0);
			if (result != null)
				aid = Integer.parseInt(result.toString()) + 1;
			Integer did = 1;
			Integer fid = 1;
			String date = "NEW APPT";

			// add appointment
			DB.executeUpdate("INSERT INTO Appointments VALUES("+aid+", "+patientID+", "+did+", "+fid+", \""+date+"\");");

			// add all symptoms (will need to turn off autocommit)
			// get symptoms and decide on condition
			for (int i = 0; i < symptoms.size(); i++)
				DB.executeUpdate("INSERT INTO SymptomList VALUES("+aid+", "+symptoms.get(i)+");");

			out.println(generate_patient_page(patientID));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// cancel an appointment by id (note that patient id is in hidden html field)
	public void cancelAppointment(HttpServletRequest request, PrintWriter out){
		try{
			// get patient ID
			int patientID = Integer.parseInt(request.getParameter("pid"));
			int apptID = -1;

			// get a list of all appointment ids for this person
			ArrayList<ArrayList<Object>> appts = DB.executeQuery("SELECT A.aid FROM Appointments A WHERE A.pid = " + patientID + ";", 1);

			// check all buttons with ids "cancel_i", where i is aid
			for (int i = 0; i < appts.size(); i++)
				if (request.getParameter("cancel_" + appts.get(i).get(0)) != null){
					apptID = (Integer) appts.get(i).get(0);
					break;
				}

			if (apptID != -1)
				DB.executeUpdate("DELETE FROM Appointments WHERE aid = " + apptID + ";");

			out.println(generate_patient_page(patientID));

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	private String generate_patient_page(int patientID) throws IOException {

	        String html = readFileAsString(PATIENT_TEMPLATE);
	        String appointmentRows = "";
	        String prescriptionRows = "";
	        String allowedSymptomRows = "<tr>";
	        String locationDividers = "";

	        // get patient name
	        String patientName = (String) DB.executeQuery("SELECT name FROM Patients WHERE pid = " + patientID + ";", 1).get(0).get(0);

			// get all appointment information from database (except symptoms, which will get later)
			ArrayList<ArrayList<Object>> appointmentInfo = DB.executeQuery("SELECT A.date, D.name, F.fid, F.name, A.aid FROM Patients P, Appointments A, Doctors D, Facilities F WHERE P.pid=" + patientID + " and A.pid = P.pid and A.did = D.did and A.fid = F.fid;", 5);

			// concatenated into comma-separated lists
	        String[] symptomStrings = new String[appointmentInfo.size()];
	        for (int row = 0; row < appointmentInfo.size(); row++) {
				// get symptom info for this appointment
				ArrayList<ArrayList<Object>> sympList = DB.executeQuery("SELECT S.name FROM Symptoms S, SymptomList L WHERE L.aid = " + appointmentInfo.get(row).get(4) + " and L.sid = S.sid;", 1);
	            symptomStrings[row] = "";
	            for (int i = 0; i < sympList.size(); i++) {
	                if (i != 0)
	                    symptomStrings[row] += ", ";
	                symptomStrings[row] += (String) sympList.get(i).get(0);
	            }
	        }

			// print appointment info
	        for (int appt = 0; appt < appointmentInfo.size(); appt++) {
	            appointmentRows += String.format("<tr> <td> %s </td> <td> %s </td> <td> <a href=\"#\" onclick=\"showhide('location_%d');\">%s</a> </td> <td> %s </td> <td> <input type=\"submit\" name=\"cancel_%d\" value=\"Cancel\"> </td> </tr>\n",
	            (String) appointmentInfo.get(appt).get(0), (String) appointmentInfo.get(appt).get(1), (Integer) appointmentInfo.get(appt).get(2),
	            (String) appointmentInfo.get(appt).get(3), symptomStrings[appt], (Integer) appointmentInfo.get(appt).get(4));
	            //date[appt], doctor[appt], loc_id[appt], loc_name[appt], symptomStrings[appt], appt_id[appt]);
	        }

	        // get all prescriptions
	        ArrayList<ArrayList<Object>> prescriptions = DB.executeQuery("SELECT T.name, D.name, T.cost, R.howlong FROM Treatments T, TakesPrescriptions R, Patients P, Doctors D WHERE P.pid = " + patientID + " and R.pid = P.pid and T.tid = R.tid and D.did = R.did;", 4);
			for (int i = 0; i < prescriptions.size(); i++) {
				prescriptionRows += String.format("<tr> <td> %s </td> <td> %s </td> <td> %s </td> <td> %s </td> </tr>\n",
				(String) prescriptions.get(i).get(0), (String) prescriptions.get(i).get(1), (Double) prescriptions.get(i).get(2), (String) prescriptions.get(i).get(3));
	        }

			// get all location addresses for dividers
			ArrayList<ArrayList<Object>> facilityAddrs = DB.executeQuery("SELECT F.fid, F.name, F.addr1, F.addr2 FROM Facilities F", 4);
	        for (int loc = 0; loc < facilityAddrs.size(); loc++)
	            locationDividers += String.format("<div id=\"location_%d\" style=\"display: none;\"> <br>\n <b> %s </b>\n <ul>\n %s <br>\n %s\n </ul>\n </div>\n\n",
	            (Integer) facilityAddrs.get(loc).get(0), (String) facilityAddrs.get(loc).get(1), (String) facilityAddrs.get(loc).get(2), (String) facilityAddrs.get(loc).get(3));
	            //loc_id[loc], loc_name[loc], loc_addr_1[loc], loc_addr_2[loc]);

			// get all allowed symptoms from database
			ArrayList<ArrayList<Object>> symptomList = DB.executeQuery("SELECT sid, name FROM Symptoms", 2);
	        for (int i = 0; i < symptomList.size(); i++) {
	            allowedSymptomRows += String.format("\t<td> <input type=\"checkbox\" name=\"sym_%d\"> %s </td>", (Integer) symptomList.get(i).get(0), (String) symptomList.get(i).get(1));
	            if (i > 0 && i < symptomList.size() - 1 && i % 4 == 3)
	                allowedSymptomRows += "</tr>\n<tr>";
	        }
	        allowedSymptomRows += "</tr>";

	        html = html.replace("%PATIENT_ID%", patientID + "");
	        html = html.replace("%PATIENT_NAME%", patientName);
	        html = html.replace("%APPOINTMENT_ROWS%", appointmentRows);
	        html = html.replace("%LOCATION_DIVIDERS%", locationDividers);
	        html = html.replace("%POSSIBLE_SYMPTOMS%", allowedSymptomRows);
	        html = html.replace("%PRESCRIPTION_ROWS%", prescriptionRows);

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



}
