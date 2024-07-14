package com.pk2;

import java.io.*;
import java.sql.*;

import javax.crypto.SecretKey;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
@WebServlet("/download")
public class Download extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private void createHeaderRow(XSSFSheet sheet) {
		Row header= sheet.createRow(0);
		Cell cell1=header.createCell(0);
		cell1.setCellValue("LNAME");
		Cell cell2=header.createCell(1);
		cell2.setCellValue("EXCLUSIVECUST");
		Cell cell3=header.createCell(2);
		cell3.setCellValue("IFSCCODE");
		Cell cell4=header.createCell(3);
		cell4.setCellValue("STATUSCODE");
		Cell cell5=header.createCell(4);
		cell5.setCellValue("STATUSDESC");
		Cell cell6=header.createCell(5);
		cell6.setCellValue("FVADDR");
		Cell cell7=header.createCell(6);
		cell7.setCellValue("MASKACCNO");
		Cell cell8=header.createCell(7);
		cell8.setCellValue("APPSTATUS");
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=data.xlsx");
        try {
        	
        	@SuppressWarnings("resource")
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Data");
        	createHeaderRow(sheet);
        	
        	String url="jdbc:oracle:thin:@//localhost:1521/orcl";
     	   String dbuser="SYS as SYSDBA";
     	   String dbpass="Parag@123";
     	    String dispmaskaccno="";
     	    Statement stmt=null;
     	    ResultSet rs=null;
     	    try(Connection con=DriverManager.getConnection(url,dbuser,dbpass)){
     	    	stmt=con.createStatement();
     	    	rs=stmt.executeQuery("select t1.LNAME,t1.EXCLUSIVECUST,t1.IFSCCODE,t1.STATUSCODE,t1.STATUSDESC,t1.FVADDR,t1.MNAME,t1.MASKACCNO,t1.APPSTATUS,ta.ENRACCNO from test1 t1 inner join test_accno ta on t1.MASKACCNO=ta.ACCNO");
     	    	int rowIndex=1;
     	    	while(rs.next()) {
     	    	  String maskaccno=rs.getString("MASKACCNO");
  				  String enr=rs.getString("ENRACCNO");
  				  if(enr.length()==52 && ("1".compareTo(maskaccno)<=0 || maskaccno.length()==2))
  				  {
  					  SecretKey sk=AESUtil.getSecretKey();
  					  String dnr=AESUtil.decrypt(enr, sk);
  					  dispmaskaccno=dnr.replaceAll(".(?=.{4})","X");
  				  }
  				      Row row=sheet.createRow(rowIndex++);
  				      row.createCell(0).setCellValue(rs.getString("LNAME"));
  				      row.createCell(1).setCellValue(rs.getString("EXCLUSIVECUST"));
  				      row.createCell(2).setCellValue(rs.getString("IFSCCODE"));
  				      row.createCell(3).setCellValue(rs.getString("STATUSCODE"));
  				      row.createCell(4).setCellValue(rs.getString("STATUSDESC"));
  				      row.createCell(5).setCellValue(rs.getString("FVADDR"));
  				      row.createCell(6).setCellValue(dispmaskaccno);
  				      row.createCell(7).setCellValue(rs.getString("APPSTATUS"));
     	    	}
     	    } catch(Exception e)
     	    {
     	    	e.printStackTrace();
     	    }
     	    try(OutputStream out=response.getOutputStream()){
     	    	workbook.write(out);
     	    }
        } catch(Exception e) {
        	e.printStackTrace();
        }
	}
}
