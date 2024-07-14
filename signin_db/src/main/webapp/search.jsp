<%@ page import="com.pk2.AESUtil" %>
<%@ page import="javax.crypto.SecretKey" %>
<%@ page import="java.sql.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search User</title>
<link rel="stylesheet" type="text/css" href="search1.css">
<style>
.container{
 width:380px;
}
.back-button{
  padding: 10px 20px;
  border:none;
  border-radius: 4px;
  background-color: #007BFF;
  color: #fff;
  cursor:pointer;
}
.back-button:hover{
  background-color:#0056b3;
}
.edit-button{
  padding: 10px 20px;
  border:none;
  border-radius: 4px;
  background-color: #007BFF;
  color: #fff;
  cursor:pointer;
}
.edit-button:hover{
  background-color:#0056b3;
}
</style>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
<div class="container">
<h2>Search for User</h2>
<form id="searchForm">
<table>
<tr>
<td><label for="MASKACCNO">ACCNO:</label></td>
<td><input type="text" id="MASKACCNO" name="MASKACCNO" required autocomplete="off"></td>
</tr>
<tr>
<td><input type="submit" value="Search"></td>
<td><input type="reset" value="Reset"></td>
<td><button class="back-button" onclick="redirectTo('/signin_db/displaytable')">Back</button></td>
</tr>
</table>
</form>
</div>
<div class="result" style="display:none;">
<%
if(request.getMethod().equalsIgnoreCase("POST")){
	String accno=request.getParameter("MASKACCNO");
	String lname="",excl="",ifsc="",statc="",statd="",fvaddr="",mname="",appst="",enr="",dnr="",maskaccno="";
	if(accno!=null && !accno.isEmpty()){
		  String url="jdbc:oracle:thin:@//localhost:1521/orcl";
		  String dbuser="SYS as SYSDBA";
		  String dbpass="Parag@123";
		  String dispmaskaccno=accno.replaceAll(".(?=.{4})","X");
		  SecretKey sk=AESUtil.getSecretKey();
		  Connection con=null;
		  PreparedStatement ps=null;
		  ResultSet rs=null;
		  int flag=0;
		  
		  try{
			  Class.forName("oracle.jdbc.driver.OracleDriver");
			  con=DriverManager.getConnection(url,dbuser,dbpass);
			  String sql="Select t1.*,ta.* from test1 t1 inner join test_accno ta on t1.MASKACCNO=ta.ACCNO";
			  ps=con.prepareStatement(sql);
			  rs=ps.executeQuery();
			  while(rs.next()){
				  lname=rs.getString("LNAME");
				  excl=rs.getString("EXCLUSIVECUST");
				  ifsc=rs.getString("IFSCCODE");
				  statc=rs.getString("STATUSCODE");
				  statd=rs.getString("STATUSDESC");
				  maskaccno=rs.getString("MASKACCNO");
				  fvaddr=rs.getString("FVADDR");
				  mname=rs.getString("MNAME");
				  appst=rs.getString("APPSTATUS");
				  enr=rs.getString("ENRACCNO");
				  dnr=AESUtil.decrypt(enr,sk);
				  if(dnr.equals(accno)){
					  flag=1;
					  break;
				  }
			  }
			  if(flag==1)
			  {
				  out.println("<h3 style='color: #333;'>Account Details</h3>");
				  out.println("<table class='tablest' border='2'>");
				  out.println("<tr><th>LNAME</th><th>EXCLUSIVECUST</th><th>IFSCCODE</th><th>STATUSCODE</th><th>STATUSDESC</th>"+"<th>FVADDR</th><th>MNAME</th><th>MASKACCNO</th><th>APPSTATUS</th><th>Actions</th></tr>");
				  out.println("<tr>");
				  out.println("<td>"+lname+"</td>");
				  out.println("<td>"+excl+"</td>");
				  out.println("<td>"+ifsc+"</td>");
				  out.println("<td>"+statc+"</td>");
				  out.println("<td>"+statd+"</td>");
				  out.println("<td>"+fvaddr+"</td>");
				  out.println("<td>"+mname+"</td>");
				  out.println("<td>"+dispmaskaccno+"</td>");
				  out.println("<td>"+appst+"</td>");
				  out.println("<td><a href='edit.jsp?id="+maskaccno+"'><button class='edit-button'>Edit</button></a></td>");
				  out.println("</tr>");
				  out.println("</table>");
			  }
			  else
			  {
				  out.println("<p>Sorry,No data found.</p>");
			  }
		  } catch(Exception e){
			  e.printStackTrace();
		  } finally{
			  if(rs!=null) rs.close();
			  if(ps!=null) ps.close();
			  if(con!=null) con.close();
		  }
	}
}
%>
</div>
<script>
$(document).ready(function(){
  $('#searchForm').on('submit',function(event){
	 event.preventDefault();
	 
	 $.ajax({
		 type:'POST',
		 url:'search.jsp',
		 data:$(this).serialize(),
		 success: function(response) {
			 var resultHtml=$(response).filter('.result').html();
			 $('.result').html(resultHtml).show();
		 },
	 });
  });
});
</script>
<script>
function redirectTo(link){
	window.history.back();
}
</script>
</body>
</html>