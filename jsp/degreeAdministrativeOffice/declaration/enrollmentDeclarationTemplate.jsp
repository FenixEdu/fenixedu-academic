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

<div style="font-size: 85%; line-height: 200%;">

<% for(int iter=0; iter < allStudentsData.size(); iter++) {
	List studentData = (List) allStudentsData.get(iter); 
	for(int nrpags=0; nrpags < 2; nrpags++) { %>
	
	<div class="registration" width="100%">
	<h2 class="registration" align="right"><bean:message key="print.declaration.academicOffice"/></h2>
	<hr size=3 width=70% align="right" noshade="true">
	<h2 class="registration" align="right"><bean:message key="print.declaration.section"/></h2>
	</div>

	<br /><br /><br /><br /><br />
	<h3 class="registration" align="center"><bean:message key="print.declaration"/></h3>
	<br /><br /><br />

	<div class="registration" width="100%" style="text-align: justify;">
	<p><bean:message key="print.declaration.technique"/></p>
	<p>
		<%= studentData.get(0) %>
		<%= studentData.get(1) %>
		<%= studentData.get(2) %>
		<%= studentData.get(3) %>
		<%= studentData.get(4) %>
		<%= studentData.get(5) %>
		<%= studentData.get(6) %>
	</p>
	</div>	


	<br/><br/><br/>
	<%= studentData.get(7) %>
	<p align="center"><bean:message key="print.declaration.techniqueSignature"/></p>
	<div class="break-before">
	<% 		}
		} %>
	</body>

</html:html>