<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.util.List" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<% List allStudentsData = (List) request.getAttribute("allStudentsData");%>

<body class="registration" id="pagewrapper_registration">
<% for(int iter=0; iter < allStudentsData.size(); iter++) {
	List studentData = (List) allStudentsData.get(iter); 
	for(int nrpags=0; nrpags < 2; nrpags++) { %>
<table class="registration" width="100%">
<tr>
	<td>		
		<h2 class="registration" align="right"><bean:message key="print.declaration.academicOffice"/></p>
		<hr size=3 width=70% align="right" noshade="true">
		<h2 class="registration" align="right"><bean:message key="print.declaration.section"/></p>
	</td>
</tr>
</table>

<br><br><br><br><br><br>
<h3 class="registration" align="center"><bean:message key="print.declaration"/></h3>
<br><br><br>

<table class="registration" width="100%">
<tr>
	<td align="justify"><bean:message key="print.declaration.technique"/></td>
</tr>
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(0) %></td>
</tr>
<tr><td> </td></tr>	
<tr>
	<td align="justify"><b><%= studentData.get(1) %></b></td>
</tr>
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(2) %></td>
</tr>
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(3) %></td>
</tr>	
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(4) %></td>
</tr>	
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(5) %></td>
</tr>	
<tr><td> </td></tr>	
<tr>
	<td align="justify"><%= studentData.get(6) %></td>
</tr>
</table>	


<br><br><br><br>
<%= studentData.get(7) %>
<p align="center"><bean:message key="print.declaration.techniqueSignature"/></p>
<div class="break-before">
<% 		}
	} %>
</body>

</html:html>