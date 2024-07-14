package com.pk2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement; 
import javax.crypto.SecretKey;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/editRecord")
public class EditRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  String id=request.getParameter("ID");
		  String lname=request.getParameter("LNAME");
		  String excl=request.getParameter("EXCLUSIVECUST");
		  String ifsc=request.getParameter("IFSCCODE");
		  String statc=request.getParameter("STATUSCODE");
		  String statd=request.getParameter("STATUSDESC");
		  String fvaddr=request.getParameter("FVADDR");
		  String mname=request.getParameter("MNAME");
		  String maskaccno=request.getParameter("MASKACCNO");
		  String appst=request.getParameter("APPSTATUS");
		  
		   String url="jdbc:oracle:thin:@//localhost:1521/orcl";
		   String dbuser="SYS as SYSDBA";
		   String dbpass="Parag@123";
		  
		  Connection con=null;
		  try {
			  Class.forName("oracle.jdbc.driver.OracleDriver");
			  con=DriverManager.getConnection(url,dbuser,dbpass);
			  Statement stmt=con.createStatement();
			  stmt.executeUpdate("Update test1 set LNAME='"+lname+"',EXCLUSIVECUST='"+excl+"',IFSCCODE='"+ifsc+"',STATUSCODE='"+statc+"',STATUSDESC='"+statd+"',FVADDR='"+fvaddr+"',MNAME='"+mname+"',MASKACCNO='"+id+"',APPSTATUS='"+appst+"' where MASKACCNO='"+id+"'");
			  SecretKey sk=AESUtil.getSecretKey();
			  String enr=AESUtil.encrypt(maskaccno,sk);
			  Statement stmt2=con.createStatement();
			  stmt2.executeUpdate("Update test_accno set ACCNO='"+id+"',ENRACCNO='"+enr+"' where ACCNO='"+id+"'");
		  }
		  catch(Exception e) {
			  e.printStackTrace();
		  }
		  response.sendRedirect("displaytable");
	}
}
