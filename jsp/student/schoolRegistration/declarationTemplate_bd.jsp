<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<body class="registration" id="pagewrapper_registration">

<html:form action="/declaration.do">

<bean:define id="studentName" name="infoPerson" property="nome"/>
<bean:define id="nameOfFather" name="infoPerson" property="nomePai"/>
<bean:define id="nameOfMother" name="infoPerson" property="nomeMae"/>

<div style="font-size: 95%; line-height: 200%;">
<% int pageNumber = 4;
for(int iter=0; iter < pageNumber; iter++) { %>

	<div class="registration" style="text-align: right;" width="100%">
	<h2 class="registration" align="right"><bean:message key="schoolRegistration.declaration.academicOffice"/></h2>
	<hr size=3 width=70% align="right" noshade="true">
	<h2 class="registration" align="right"><bean:message key="schoolRegistration.declaration.section"/></h2>
	</div>


	<br/><br/><br/><br/><br/>
	<h3 class="registration" align="center"><bean:message key="schoolRegistration.declaration"/></h3>
	<br/><br/><br/>

	<div class="registration" width="100%" style="text-align: justify;">
	<p><bean:message key="schoolRegistration.declaration.technique"/></p>
	<p>
		<bean:message key="schoolRegistration.declaration.declares"/> <bean:write name="studentNumber"/>, <%= studentName.toString().toUpperCase() %>, 
		<bean:message key="schoolRegistration.declaration.ID"/> <bean:write name="infoPerson" property="numeroDocumentoIdentificacao"/>, <bean:message key="schoolRegistration.declaration.naturality"/>
		 <bean:write name="schoolRegistrationForm" property="parishOfBirth"/>, <bean:write name="infoPerson" property="distritoNaturalidade"/>, <bean:message key="schoolRegistration.declaration.sonOf"/> 
		 <%= nameOfFather.toString().toUpperCase() %> <bean:message key="schoolRegistration.declaration.andSonOf"/> <%= nameOfMother.toString().toUpperCase() %>,
		<bean:message key="schoolRegistration.declaration.inLectiveYear"/> <bean:write name="lectiveYear"/> <bean:message key="schoolRegistration.declaration.enrolledInDegree"/>
		<bean:write name="degreeName"/> <bean:message key="schoolRegistration.declaration.thisInstitue"/>
	</p>
	
<br/><br/><br/>
<bean:message key="schoolRegistration.declaration.academicOfficeIST"/> <bean:write name="day"/> <bean:message key="schoolRegistration.declaration.Of"/>
<bean:write name="month"/> <bean:message key="schoolRegistration.declaration.Of"/> <bean:write name="year"/>

<p align="center"><bean:message key="schoolRegistration.declaration.techniqueSignature"/></p>

<% if(iter != (pageNumber-1)) { %>
<div class="break-before">
<% } 
 } %>

</div>
</html:form>
</body>

</html:html>