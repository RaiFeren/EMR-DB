/* Paul McCormack
 * 010186829 - PO
 * CS 133 PS 6
 * 2/29/2012
 */

// Invoke it like this: http://localhost:8080/pjmps6/products

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

// displays page and performs various requests about electronics products
public class ProductQuery extends HttpServlet {

    private static final String TITLE = "Product Finder";

    private static final String HTML_FORM_BEGIN = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n" + "<HTML>\n" + "<HEAD><TITLE>" + TITLE + "</TITLE></HEAD>" + "<BODY>" + "<H1 ALIGN=\"CENTER\">" + TITLE + "</H1> <p> Paul McCormack, 010186829, for CS 133 PS 6, 2/29/2012 </p>";

    private static final String HTML_FORM_BODY = "<h2> Find a Product from our selection. </h2><table border=1 cellpadding=12px><tr><td VALIGN=\"top\"><!-- part a --><h4> a) Find a PC by price. </h4><FORM METHOD=POST>	Desired Price: $<input type=\"text\" name=\"price\" size=10 value=\"\"> <br>	<input type=\"submit\" name=\"submit_a\" value=\"Query\"></FORM></td><td VALIGN=\"top\"><!-- part b --><h4> b) Find a laptop by minimum specifications. </h4><FORM METHOD=POST>	<table>	<tr> <td> Minimum Speed: </td> <td> <input type=\"text\" name=\"speed\" size=10 value=\"\"> GHz </td> </tr>	<tr> <td> Minimum RAM: </td> <td><input type=\"text\" name=\"ram\" size=10 value=\"\"> GB </td> </tr>	<tr> <td> Minimum Hard Disk: </td> <td><input type=\"text\" name=\"hd\" size=10 value=\"\"> GB </td> </tr>	<tr> <td> Minimum Screen Size: </td> <td><input type=\"text\" name=\"screen\" size=10 value=\"\"> inches </td> </tr>	<tr> <td> <input type=\"submit\" name=\"submit_b\" value=\"Query\"> </td> </tr>	</table></FORM></td><td VALIGN=\"top\"><!-- part c --><h4> c) Find a product by manufacturer. </h4><FORM METHOD=POST>	<table>	<tr> <td> Manufacturer: </td> <td> <input type=\"text\" name=\"maker\" size=10 value=\"\"> </td> </tr>	<tr> <td> <input type=\"submit\" name=\"submit_c\" value=\"Query\"> </td> </tr>	</table></FORM></td></tr><tr><td VALIGN=\"top\"><!-- part d --><h4> d) Find a PC and printer by budget. </h4><FORM METHOD=POST>	<table>	<tr> <td> Budget: </td> <td> <input type=\"text\" name=\"budget\" size=10 value=\"\"> </td> </tr>	<tr> <td> Minimum Speed: </td> <td> <input type=\"text\" name=\"speed\" size=10 value=\"\"> GHz </td> </tr>	<tr> <td> <input type=\"submit\" name=\"submit_d\" value=\"Query\"> </td> </tr>	</table></FORM></td><td VALIGN=\"top\"><!-- part e --><h4> e) Insert a PC into the database. </h4><FORM METHOD=POST>	<table>	<tr> <td> Manufacturer: </td> <td> <input type=\"text\" name=\"maker\" size=10 value=\"\"> </td> </tr>	<tr> <td> Model Number: </td> <td> <input type=\"text\" name=\"model\" size=10 value=\"\"> </td> </tr>	<tr> <td> Speed: </td> <td> <input type=\"text\" name=\"speed\" size=10 value=\"\"> GHz </td> </tr>	<tr> <td> RAM: </td> <td><input type=\"text\" name=\"ram\" size=10 value=\"\"> GB </td> </tr>	<tr> <td> Hard Disk Size: </td> <td><input type=\"text\" name=\"hd\" size=10 value=\"\"> GB </td> </tr>	<tr> <td> Price ($): </td> <td><input type=\"text\" name=\"price\" size=10 value=\"\"> </td> </tr>	<tr> <td> <input type=\"submit\" name=\"submit_e\" value=\"Insert\"> </td> </tr>	</table></FORM></td></tr></table>";

    private static final String HTML_FORM_END = "</BODY></HTML>";

	// respond to a GET request by just writing the page to the output
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set up the response
        response.setContentType("text/html");

        // Begin composing the response
        PrintWriter out = response.getWriter();

		// just send the html page back
        out.println(HTML_FORM_BEGIN + HTML_FORM_BODY + HTML_FORM_END);
    }

	// respond to a post by interpreting form information and printing requested output
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Begin composing the response
        PrintWriter out = response.getWriter();

        out.println(HTML_FORM_BEGIN);

		// see which form was submitted based on state of submit button
		if(request.getParameter("submit_a") != null)
			performPartA(request, out);
		else if(request.getParameter("submit_b") != null)
			performPartB(request, out);
		else if(request.getParameter("submit_c") != null)
			performPartC(request, out);
		else if(request.getParameter("submit_d") != null)
			performPartD(request, out);
		else if(request.getParameter("submit_e") != null)
			performPartE(request, out);

        // Set up the response
        response.setContentType("text/html");

		// clicking on clear will just perform a get (clear the input)
        out.println("<p> Make another query below or <a href src=\"#\">clear</a> result: </p>");
        out.println(HTML_FORM_BODY + HTML_FORM_END);
    }

	// get a PC from database with price closest to input
    void performPartA(HttpServletRequest request, PrintWriter out){
		try{
			// Get posted request price
			double price = Double.parseDouble(request.getParameter("price"));

			// find item from database
			out.format("<h2> Result for PC with closest price to $%.2f: </h2> <br>", price);


			out.println("<table border=1 cellpadding=3px> <tr> <td> Maker </td> <td> Model # </td> <td> Speed (GHz) </td> <td> Price ($) </td></tr>");
			printResultSet(out,
							"select prod.maker,P1.model,P1.speed,P1.price from Products prod, PCS P1 where prod.model = P1.model AND abs(P1.price - " + price + ") < ALL (SELECT abs(P2.price - " + price + ") from PCS P2 where P1.model != P2.model);",
							4);
			out.println("</table>");
		} catch(java.lang.NumberFormatException ex){
			out.println("<h2> You entered an invalid price! </h2> <br>");
		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
	}

	// find laptop with specified minimum specs
	void performPartB(HttpServletRequest request, PrintWriter out){
		try{
			// Get posted request price
			double min_speed = Double.parseDouble(request.getParameter("speed"));
			double min_ram = Double.parseDouble(request.getParameter("ram"));
			double min_hd = Double.parseDouble(request.getParameter("hd"));
			double min_screen = Double.parseDouble(request.getParameter("screen"));

			// find item from database
			out.println("<h2> Results for laptops with minimum specs: </h2> <br>");

			out.println("<table border=1 cellpadding=3px> <tr> <td> Maker </td> <td> Model # </td> <td> Speed (GHz) </td> <td> RAM (GB) </td> <td> Hard Disk (GB) </td> <td> Screen Size (in) </td> <td> Price ($) </td></tr>");
			printResultSet(out,
							"select p.maker, l.model, l.speed, l.ram, l.hd, l.screen, l.price from Laptops l, Products p where l.model = p.model AND l.speed >= " + min_speed +
							"AND l.ram >= "+ min_ram +" AND l.hd > "+ min_hd +" AND l.screen >= "+ min_screen +";",
							7);
			out.println("</table>");
		} catch(java.lang.NumberFormatException ex){
			out.println("<h2> You entered an invalid number! </h2> <p>"+ ex.getMessage() + "</p> <br>");
		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}

	}

	// find products by manufacturer
	void performPartC(HttpServletRequest request, PrintWriter out){
		try{
			// Get posted request price
			String maker = request.getParameter("maker");

			// find item from database
			out.println("<h2> Results for products made by " + maker +": </h2> <br>");

			out.println("<h3> PCs </h3>");
			out.println("<table border=1 cellpadding=3px> <tr> <td> Model # </td> <td> Speed (GHz) </td> <td> RAM (GB) </td> <td> Hard Disk (GB) </td> <td> Price ($) </td></tr>");
			printResultSet(out, "select pcs.model, pcs.speed, pcs.ram, pcs.hd, pcs.price from PCs pcs, Products p where pcs.model = p.model AND p.maker = \"" + maker +"\";", 5);
			out.println("</table>");

			out.println("<h3> Laptops </h3>");
			out.println("<table border=1 cellpadding=3px> <tr> <td> Model # </td> <td> Speed (GHz) </td> <td> RAM (GB) </td> <td> Hard Disk (GB) </td> <td> Screen Size (in) </td> <td> Price ($) </td></tr>");
			printResultSet(out, "select l.model, l.speed, l.ram, l.hd, l.screen, l.price from Laptops l, Products p where l.model = p.model AND p.maker = \"" + maker +"\";", 6);
			out.println("</table>");

			out.println("<h3> Printers </h3>");
			out.println("<table border=1 cellpadding=3px> <tr> <td> Model # </td> <td> Is Color? </td> <td> Type </td> <td> Price ($) </td></tr>");
			printResultSet(out, "select print.model, print.color, print.type, print.price from Printers print, Products p where print.model = p.model AND p.maker = \"" + maker +"\";", 4);
			out.println("</table>");

		} catch(java.lang.NumberFormatException ex){
			out.println("<h2> You entered an invalid character! </h2> <p>"+ ex.getMessage() + "</p> <br>");
		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}

	}

	// find best PC+printer package for budget and computer speed and color printer
	void performPartD(HttpServletRequest request, PrintWriter out){
		try{
			// Get posted request price
			double min_speed = Double.parseDouble(request.getParameter("speed"));
			double budget = Double.parseDouble(request.getParameter("budget"));

			// find item from database
			out.format("<h2> Best match for budget of $%.2f with minimum speed of %.1f GHz: </h2> <br>", budget, min_speed);

			// I order first by !color and then by price.
			// the LIMIT takes the first entry, which will be the cheapest under the budget and with a color printer if possible
			out.println("<table border=1 cellpadding=3px> <tr> <td> PC Model </td> <td> Printer Model </td> <td> Total Price ($) </td></tr>");
			printResultSet(out,
							"select pc.model, pr.model, pc.price+pr.price from Printers pr, PCs pc where pc.speed > " + min_speed + " AND pc.price+pr.price < " + budget + " order by !pr.color, (pc.price+pr.price) LIMIT 1;",
							3);
			out.println("</table>");
		} catch(java.lang.NumberFormatException ex){
			out.println("<h2> You entered an invalid number! </h2> <p>"+ ex.getMessage() + "</p> <br>");
		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}

	}


	// insert a new PC into database
	void performPartE(HttpServletRequest request, PrintWriter out){
		Connection connection = null;

		try{
			// Get posted info
			String maker = request.getParameter("maker");
			int model = Integer.parseInt(request.getParameter("model"));
			double speed = Double.parseDouble(request.getParameter("speed"));
			double ram = Double.parseDouble(request.getParameter("ram"));
			double hd = Double.parseDouble(request.getParameter("hd"));
			double price = Double.parseDouble(request.getParameter("price"));

			String insert1 = "insert into products(maker,model,type) values(\""+maker+"\","+model+",\"PC\");";
			String insert2 = "insert into pcs(model,speed,ram,hd,price) values("+model+","+speed+","+ram+","+hd+","+price+");";

			// Establish network connection to database
			connection = DB.openConnection();
			connection.setAutoCommit(false);

			// Create a statement for executing the query/insert
			Statement statement = connection.createStatement();

			// remove it from products (removed from other table automatically) if it exists
			int num_removed = statement.executeUpdate("delete from products where model = " + model + ";");
			if (num_removed != 0)
				out.println("<h2> Warning: replacing existing entry with model number " + model + ". </h2> <br>");

			// Send query to database and receive result
			statement.executeUpdate(insert1);
			statement.executeUpdate(insert2);

			// find item from database
			out.println("<h2> New product inserted successfully! </h2> <br>");

			connection.commit();
			connection.close();

		} catch(java.lang.NumberFormatException ex){
			out.println("<h2> You entered an invalid number! </h2> <p>"+ ex.getMessage() + "</p> <br>");
		} catch (java.lang.Exception ex2){
			out.println("<h2> Exception: </h2> <p>"+ ex2.getMessage() +"</p> <br>");
		}
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
