import java.io.*;

public class PageGenerator {

    public static final String PATIENT_TEMPLATE = "patient_template.html";
    public static final String DOCTOR_TEMPLATE = "doctor_template.html";
    public static final String ADMIN_TEMPLATE = "admins_template.html";

    // from http://snippets.dzone.com/posts/show/1335
    private static String readFileAsString(String filePath) throws java.io.IOException {
        byte[] buffer = new byte[(int) new File(filePath).length()];
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
        return new String(buffer);
    }
    
    public static String generate_admin_page(int[] cond_id, String[] condName, int[] sym_id, String[] symName) throws IOException {

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


    public static String generate_doctor_page(int doctorID, String doctorName, String[][] symptoms, String[] date, String[] patient, int[] loc_id, String[] loc_name, String[] condition, String[] treatment, int[] appt_id, String[] loc_addr_1, String[] loc_addr_2, int[] all_loc_id, String[] allLocations, boolean[] locationAvailable, int[] treat_id, String[] treatName, boolean[] treatKnown) throws IOException {

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

    public static String generate_patient_page(int patientID, String patientName, String[][] symptoms, String[] date, String[] doctor, int[] loc_id, String[] loc_name, int[] appt_id, String[] loc_addr_1, String[] loc_addr_2, int[] sym_id, String[] symptomNames) throws IOException {

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

    public static void main(String[] args) throws IOException {
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
        
        

        //System.out.println(generate_patient_page(patientID, patientName, symptoms, date, doctor, loc_id, loc_name, appt_id, loc_addr_1, loc_addr_2, sym_id, symptomNames));
        //System.out.println(generate_doctor_page(doctorID, doctorName, symptoms, date, patient, loc_id, loc_name, condition, treatment, appt_id, loc_addr_1, loc_addr_2, 
        //        all_loc_id, allLocations, locationAvailable, treat_id, treatName, treatKnown));
        
        System.out.println(generate_admin_page(cond_id, condName, sym_id, symptomNames));
                

    }
}