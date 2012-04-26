// Paul McCormack and Rai Feren
// 010186829 - PO and 40152662 - HMC
// CS 133 Final Project, Due 5/1/2012

// The administrator portal allows administrators to
// add doctors, facilities, conditions, and treatments.

// Invoke it like this: http://localhost:8080/emr/admins

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.ArrayList;

// displays page and performs various requests about electronics products
public class AdminServlet extends HttpServlet {

	// main page for admin portal
	public static final String ADMIN_TEMPLATE = "../webapps/emr/res/admins_template.html";

	// respond to a GET request by just writing the page to the output
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set up the response
        response.setContentType("text/html");

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// send the adminstrative page back
        out.println(generate_admin_page());
    }

	// respond to a post by interpreting form information and printing requested output
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 // Set up the response
        response.setContentType("text/html");

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// see which form was submitted based on state of submit button
		if(request.getParameter("add_condition") != null)			// add a condition
			addCondition(request, out);
		else if(request.getParameter("add_facility") != null)			// add a facility
			addFacility(request, out);
		else if(request.getParameter("add_doctor") != null)				// add a doctor
			addDoctor(request, out);
    }


	// add a condition
    public void addCondition(HttpServletRequest request, PrintWriter out){
		try{
			String treat_name = request.getParameter("treatname");
			// get selected treatment id
			int treat_id = Integer.parseInt(request.getParameter("treat_select"));

			DB.beginTransaction();

			// if a new treatment is being added
			if (treat_id == 0) {
				Double cost = Double.parseDouble(request.getParameter("treatcost"));
				String info = request.getParameter("treatinfo");
				String sideeffects = request.getParameter("sideeffects");
				String maker="NULL";
				String fid = "NULL";

				// add new treatment
				Object result = DB.executeQuery("SELECT MAX(tid) FROM Treatments", 1).get(0).get(0);
				if (result != null)
					treat_id = Integer.parseInt(result.toString()) + 1;

				DB.executeUpdate("INSERT INTO Treatments VALUES(" + treat_id + ", \"" + treat_name + "\", " + cost  + ", \"" + info + "\", \"" + maker + "\", "+ fid + ", \""+ sideeffects + "\");");

			}

			// create a new cid for this facility by finding maximum of all current IDs
			int cid = 1;
			Object result = DB.executeQuery("SELECT MAX(cid) FROM ConditionsTreats", 1).get(0).get(0);
			if (result != null)
				 cid = Integer.parseInt(result.toString()) + 1;

			String cond_name = request.getParameter("condname");
			String cond_info= request.getParameter("condinfo");
			Double probability = Double.parseDouble(request.getParameter("probability"))/100;

			// create a new condition
			DB.executeUpdate("INSERT INTO ConditionsTreats VALUES(" + cid + ", \"" + cond_name + "\", \"" + cond_info + "\", " + probability + ", "+ treat_id + ");");

			// add all the symptom probabilities
			ArrayList<ArrayList<Object>> possibleSymptoms = DB.executeQuery("SELECT sid FROM Symptoms", 1);
			for (int i = 0; i < possibleSymptoms.size(); i++){
				int symID = (Integer) possibleSymptoms.get(i).get(0);
				double probSymptom = Double.parseDouble(request.getParameter("prob_" + symID));
				DB.executeUpdate("INSERT INTO Implies VALUES("+symID+", "+cid+"," +probSymptom+");");
			}

			DB.endTransaction();
			out.println(generate_admin_page());

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// add a facility
    public void addFacility(HttpServletRequest request, PrintWriter out){
		try{
			// create a new fid for this facility by finding maximum of all current IDs
			int facilityID = 1;
			DB.beginTransaction();
			Object result = DB.executeQuery("SELECT MAX(fid) FROM Facilities", 1).get(0).get(0);
			if (result != null)
				facilityID = Integer.parseInt(result.toString()) + 1;

			String name = request.getParameter("facility_name");
			String addr1 = request.getParameter("facility_addr_1");
			String addr2 = request.getParameter("facility_addr_2");

			// insert facility into database
			DB.executeUpdate("INSERT INTO Facilities VALUES(" + facilityID + ", \"" + name + "\", \"" + addr1  + "\", \"" + addr2 + "\");");
			DB.endTransaction();

			out.println(generate_admin_page());

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// add a doctor
    public void addDoctor(HttpServletRequest request, PrintWriter out){
		try{
			// create a new did for this doctor by finding maximum of all current IDs
			int did = 1;
			DB.beginTransaction();
			Object result = DB.executeQuery("SELECT MAX(did) FROM Doctors", 1).get(0).get(0);
			if (result != null)
				did = Integer.parseInt(result.toString()) + 1;

			String name = request.getParameter("doctor_name");
			String degree = request.getParameter("degree");

			// insert facility into database
			DB.executeUpdate("INSERT INTO Doctors VALUES(" + did + ", \"" + name + "\", \"" + degree + "\");");
			DB.endTransaction();

			String adminPage = generate_admin_page();
			adminPage = adminPage.replace("doctor_success\" style=\"display: none", "doctor_success\" style=\"display: block");
			adminPage = adminPage.replace("%DOCTOR_NAME%", name + "");
			adminPage = adminPage.replace("%DOCTOR_ID%", did + "");
			out.println(adminPage);

		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}


	// generate admin web page to return
	public String generate_admin_page() throws IOException {

		String html = readFileAsString(ADMIN_TEMPLATE);
		String symptomProbabilities = "";
		String treatmentRows = "";

		DB.beginTransaction();

		// get all symptoms from database
		ArrayList<ArrayList<Object>> possibleSymptoms = DB.executeQuery("SELECT name, sid FROM Symptoms", 2);
		for (int i = 0; i < possibleSymptoms.size(); i++) {
			// javascript will ensure a number is typed in the boxes
			symptomProbabilities += String.format("<tr> <td> %s </td><td><input type=\"text\" name=\"prob_%d\" size=1 value = \"0.0\" onblur=\"if (isNaN(Number(this.value))) this.value = '0.0'\"> </td> </tr>\n", (String) possibleSymptoms.get(i).get(0), (Integer) possibleSymptoms.get(i).get(1));
		}

		// get all treatments from database
		ArrayList<ArrayList<Object>> treatments = DB.executeQuery("SELECT tid, name FROM Treatments", 2);
		DB.endTransaction();

		for (int i = 0; i < treatments.size(); i++) {
			treatmentRows += "<option value=\"" + treatments.get(i).get(0) + "\">"+(String) treatments.get(i).get(1)+"</option>\n";
		}

		html = html.replace("%SYMPTOM_LIST%", symptomProbabilities);
		html = html.replace("%TREATMENT_ROWS%", treatmentRows);

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
