package com.pk2;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement; 
import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet("/addRecord")
public class AddRecord extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private int cnt;
	private static final String CNT_FILE="./cnt.txt";
	private void writeCntToFile(int cnt) {
		try(BufferedWriter writer= new BufferedWriter(new FileWriter(CNT_FILE))){
			writer.write(Integer.toString(cnt));
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}
	private int readcntfromFile() {
		File file=new File(CNT_FILE);
		if(!file.exists()) {
			writeCntToFile(0);
			return 0;
		}
		try(BufferedReader reader= new BufferedReader(new FileReader(file))){
			String line=reader.readLine();
			return line!=null? Integer.parseInt(line):0;
		} catch(IOException | NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public void init() throws ServletException{
		super.init();
		cnt= readcntfromFile();
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  synchronized(this) {
		  cnt++;
		  writeCntToFile(cnt);
	  }
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
		  stmt.executeUpdate("Insert into test1 values('"+lname+"','"+excl+"','"+ifsc+"','"+statc+"','"+statd+"','"+fvaddr+"','"+mname+"','"+cnt+"','"+appst+"')");
		  SecretKey sk=AESUtil.getSecretKey();
		  String enr=AESUtil.encrypt(maskaccno,sk);
		  Statement stmt2=con.createStatement();
		  stmt2.executeUpdate("Insert into test_accno values('"+cnt+"','"+enr+"')");
	  }
	  catch(Exception e) {
		  e.printStackTrace();
	  }
	  response.sendRedirect("displaytable");
	}
}
