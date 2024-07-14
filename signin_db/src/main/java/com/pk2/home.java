package com.pk2;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/displaytable")
public class home extends HttpServlet {
	private static final long serialVersionUID = 1L;
   	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	   response.setContentType("text/html");
	   PrintWriter out= response.getWriter();
	   
	   String url="jdbc:oracle:thin:@//localhost:1521/orcl";
	   String dbuser="SYS as SYSDBA";
	   String dbpass="Parag@123";
	   String dispmaskaccno="";
	   Connection con=null;
	   PreparedStatement ps=null;
	   ResultSet rs=null;
	   
	   try {
		   Class.forName("oracle.jdbc.driver.OracleDriver");
			  con=DriverManager.getConnection(url,dbuser,dbpass);
			  String sql="select t1.LNAME,t1.EXCLUSIVECUST,t1.IFSCCODE,t1.STATUSCODE,t1.STATUSDESC,t1.FVADDR,t1.MNAME,t1.MASKACCNO,t1.APPSTATUS,ta.ENRACCNO from test1 t1 inner join test_accno ta on t1.MASKACCNO=ta.ACCNO";
			  ps=con.prepareStatement(sql);
			  rs=ps.executeQuery();
			  
			  out.println("<html><head><title>Home Page</title>");
			  out.println("<link rel='stylesheet' href='home2.css'>");
			  out.println("<link rel='stylesheet' href='https://cdn.datatables.net/1.11.5/css/jquery.dataTables.min.css'>");
			  out.println("<script src='https://code.jquery.com/jquery-3.6.0.min.js'></script>");
			  out.println("<script src='https://cdn.datatables.net/1.11.5/js/jquery.dataTables.min.js'></script>");
			  out.println("</head>");
			  out.println("<body>");
			  out.println("<h2>Bank Account Details</h2>");
			  out.println("<div class='head-con' style='margin-bottom:20px; margin-right:50px;'><a href='search.jsp'><button>Search User</button></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='add.html'><button>Add New</button></a>&nbsp;&nbsp;&nbsp;&nbsp;<button class='back-button' style='margin-left:800px;' onclick=\"redirectTo('/signin_db/')\">Logout</button><form action='download' method='get' style='margin-left:900px;'><button>Download Data</button></form></div>");
			  out.println("<table id='myTable' class='display' border='2'>");
			  out.println("<thead>");
			  out.println("<tr><th>LNAME</th><th>EXCLUSIVECUST</th><th>IFSCCODE</th><th>STATUSCODE</th><th>STATUSDESC</th><th>FVADDR</th><th>MASKACCNO</th><th>APPSTATUS</th><th>Actions</th></tr></thead>");
			  out.println("<tbody>");
			  while(rs.next()) {
				  String maskaccno=rs.getString("MASKACCNO");
				  String enr=rs.getString("ENRACCNO");
				  if(enr.length()==52 && ("1".compareTo(maskaccno)<=0 || maskaccno.length()==2))
				  {
					  SecretKey sk=AESUtil.getSecretKey();
					  String dnr=AESUtil.decrypt(enr, sk);
					  dispmaskaccno=dnr.replaceAll(".(?=.{4})","X");
				  }
				      out.println("<tr>");
				      out.println("<td>"+rs.getString("LNAME")+"</td>");
				      out.println("<td>"+rs.getString("EXCLUSIVECUST")+"</td>");
				      out.println("<td>"+rs.getString("IFSCCODE")+"</td>");
				      out.println("<td>"+rs.getString("STATUSCODE")+"</td>");
				      out.println("<td>"+rs.getString("STATUSDESC")+"</td>");
				      out.println("<td>"+rs.getString("FVADDR")+"</td>");
				      out.println("<td>"+dispmaskaccno+"</td>");
				      out.println("<td>"+rs.getString("APPSTATUS")+"</td>");
				      out.println("<td><a href='edit.jsp?id="+maskaccno+"'><button>Edit</button></a></td>");
				      out.println("</tr>");
			  }
			  out.println("</tbody>");
			  out.println("</table>");
			  out.println("<script src='home1.js'></script>");
			  out.println("<script>function redirectTo(link){window.location.href=link;}</script>");
			  out.println("</body></html>");
	   } catch(Exception e){
			  e.printStackTrace();
			  out.println("Error: "+e.getMessage());
		  } finally{
			  try {
			  if(rs!=null) rs.close();
			  if(ps!=null) ps.close();
			  if(con!=null) con.close();
		     }
			 catch(SQLException e) {
				 e.printStackTrace();
			 }
		 }
	}
}
