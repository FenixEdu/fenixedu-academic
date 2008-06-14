<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<body class="registration" id="pagewrapper_registration">

<div style="font-size: 85%; line-height: 200%;">

	<div class="registration" style="text-align: right;" width="100%">
	<h2 class="registration"><bean:message key="print.declaration.academicOffice"/></h2>
	<hr size="3" width="70%" align="right" noshade="true">
	<h2 class="registration"><bean:message key="print.declaration.section"/></h2>
	</div>

	<br /><br /><br /><br /><br />
	<h3 class="registration" align="center"><bean:message key="print.declaration"/></h3>
	<br /><br /><br />

	<div class="registration" width="100%" style="text-align: justify;">
	<p><bean:message key="print.declaration.technique"/></p>
	<p>
		<%= request.getAttribute("partOne1") %>
		<%= request.getAttribute("partTwo1") %>
		<%= request.getAttribute("partThree1") %>
		<%= request.getAttribute("partFour1") %>
		<%= request.getAttribute("partFive1") %>
		<%= request.getAttribute("partSix1") %>
		<%= request.getAttribute("partSeven1") %>
	</p>
	</div>	

	<br /><br /><br />
	<%= request.getAttribute("partEight1") %>
	<p align="center"><bean:message key="print.declaration.techniqueSignature"/></p>

</div>

</body>
</html:html>