<%@ page import="com.pk2.AESUtil" %>
<%@ page import="javax.crypto.SecretKey" %>
<%@ page import="java.sql.*" %>
<%
	  String url="jdbc:oracle:thin:@//localhost:1521/orcl";
	  String dbuser="SYS as SYSDBA";
	  String dbpass="Parag@123";
	  String id=request.getParameter("id");
	  
	  Connection con=null;
	  PreparedStatement ps=null;
	  ResultSet rs=null;
	  
	  String lname="",excl="",ifsc="",statc="",statd="",fvaddr="",mname="",appst="",enr="",dnr="";
	  
	  try{
		  Class.forName("oracle.jdbc.driver.OracleDriver");
		  String sql="Select t1.*,ta.* from test1 t1 inner join test_accno ta on t1.MASKACCNO=ta.ACCNO where t1.MASKACCNO=?";
		  con=DriverManager.getConnection(url,dbuser,dbpass);
		  ps=con.prepareStatement(sql);
		  ps.setString(1,id);
		  rs=ps.executeQuery();
		  if(rs.next()){
			  lname=rs.getString("LNAME");
			  excl=rs.getString("EXCLUSIVECUST");
			  ifsc=rs.getString("IFSCCODE");
			  statc=rs.getString("STATUSCODE");
			  statd=rs.getString("STATUSDESC");
			  fvaddr=rs.getString("FVADDR");
			  mname=rs.getString("MNAME");
			  appst=rs.getString("APPSTATUS");
			  enr=rs.getString("ENRACCNO");
		  }
		  SecretKey sk=AESUtil.getSecretKey();
		  dnr=AESUtil.decrypt(enr,sk);
	  } catch(Exception e){
		 e.printStackTrace();
	  } finally{
		 if(rs!=null) rs.close();
		 if(ps!=null) ps.close();
		 if(con!=null) con.close();
	  }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Edit Data</title>
<link rel="stylesheet" type="text/css" href="edit1.css">
<style>
.back-button{
  padding: 10px 20px;
  border:none;
  border-radius: 4px;
  background-color: #007BFF;
  color: #fff;
  cursor:pointer;
  margin-left:500px;
}
.back-button:hover{
  background-color:#0056b3;
}
</style>
</head>
<body>
<h2>Edit Record</h2>
<button class="back-button" onclick="redirectTo('/signin_db/displaytable')">Cancel</button>
<form action="editRecord" method="post">
<table>
<tr>
<td><input type="hidden" id="ID" name="ID" value="<%= id%>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="LNAME">LNAME:</label></td>
<td><input type="text" id="LNAME" name="LNAME" value="<%=lname %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="EXCLUSIVECUST">EXCLUSIVECUST(Y/N):</label></td>
<td><input type="text" id="EXCLUSIVECUST" name="EXCLUSIVECUST" value="<%=excl %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="IFSCCODE">IFSCCODE:</label></td>
<td><input type="text" id="IFSCCODE" name="IFSCCODE" value="<%=ifsc %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="STATUSCODE">STATUSCODE:</label></td>
<td><input type="text" id="STATUSCODE" name="STATUSCODE" value="<%=statc %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="STATUSDESC">STATUSDESC:</label></td>
<td><input type="text" id="STATUSDESC" name="STATUSDESC" value="<%=statd %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="FVADDR">FVADDR:</label></td>
<td><input type="text" id="FVADDR" name="FVADDR" value="<%=fvaddr %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="MNAME">MNAME:</label></td>
<td><input type="text" id="MNAME" name="MNAME" value="<%=mname %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="MASKACCNO">ACCNO:</label></td>
<td><input type="text" id="MASKACCNO" name="MASKACCNO" value="<%=dnr %>" autocomplete="off"></td>
</tr>
<tr>
<td><label for="APPSTATUS">APPSTATUS:</label></td>
<td><input type="text" id="APPSTATUS" name="APPSTATUS" value="<%=appst %>" autocomplete="off"></td>
</tr>
<tr>
<td><input type="submit" value="Update"></td>
<td><input type="reset" value="Reset"></td>
</tr>
</table>
</form>
<script>
function redirectTo(link){
	window.history.back();
}
</script>
</body>
</html>