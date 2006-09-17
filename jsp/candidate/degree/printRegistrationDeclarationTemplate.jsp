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

<bean:define id="person" name="registration" property="student.person" />
<bean:define id="studentName" name="person" property="name" />
<bean:define id="nameOfMother" name="person" property="nameOfMother" />
<bean:define id="nameOfFather" name="person" property="nameOfFather" />


<table width="100%" height="100%" border="0">
<tr><td>

<div style="font-size: 95%; line-height: 200%;">


	<div class="registration" style="text-align: right;" width="100%" height="100%">
	<h2 class="registration" align="right"><bean:message  key="label.candidacy.registration.declaration.academicServicesOffice"/></h2>
	<hr size=3 width="70%" noshade="true" align="right"/>
	<h2 class="registration" align="right"><bean:message  key="label.candidacy.registration.declaration.graduationSection"/></h2>
	</div>


	<br/><br/><br/><br/><br/>
	<h3 class="registration" align="center"><bean:message  key="label.candidacy.registraction.declaration"/></h3>
	<br/><br/><br/>

	<div class="registration" width="100%" style="text-align: justify;">
	<bean:define id="studentName" name="registration" property="student.person.name" />
	<p><b><bean:message  key="label.candidacy.registration.declaration.institution.responsible"/></b></p>
	
	<p><bean:message key="label.candidacy.registration.declaration.section1"/> <bean:write name="registration" property="student.number"/>, <%= studentName.toString().toUpperCase() %>,
	<bean:message key="label.candidacy.registration.declaration.section2"/> <bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/>
	<bean:write name="person" property="documentIdNumber"/>, 
	<bean:message key="label.candidacy.registration.declaration.section3"/> <bean:write name="person" property="parishOfBirth"/>, 
	<bean:write name="person" property="districtOfBirth"/>, 
	<bean:message key="label.candidacy.registration.declaration.section4"/> 
	<%= nameOfFather.toString().toUpperCase() %> 
	<bean:message key="label.candidacy.registration.declaration.section5"/> 
	<%= nameOfMother.toString().toUpperCase() %>,
	<bean:message key="label.candidacy.registration.declaration.section6"/> 
	<bean:write name="executionYear" property="year"/> 
	<bean:message key="label.candidacy.registration.declaration.section7"/> 
	<bean:write name="registration" property="lastStudentCurricularPlan.degree.name"/> 
	<bean:message key="label.candidacy.registration.declaration.section8"/>
	</p>
	</div>
		
<bean:message  key="label.candidacy.registration.declaration.academicServicesOffice"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("PT","pt")).format(new java.util.Date()) %>

</div>

</td></tr></table>
<br/><br/>

</body>

</html:html>