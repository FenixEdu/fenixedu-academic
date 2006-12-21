<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />

</head>

<body class="registration" id="pagewrapper_registration">

<bean:define id="person" name="registration" property="student.person" />
<bean:define id="studentName" name="person" property="name" />
<bean:define id="nameOfMother" name="person" property="nameOfMother" />
<bean:define id="nameOfFather" name="person" property="nameOfFather" />



<table border="0" style="width: 90%;height: 100%;">
<tr><td>

<div style="font-size: 95%; line-height: 200%;">


	<div class="registration" style="text-align: right;width: 90%;height: 100%;">
		<h2 class="registration" align="right"><bean:message  key="label.candidacy.registration.declaration.academicServicesOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
		<hr size=3 width="70%" noshade="true" align="right"/>
		<h2 class="registration" align="right"><bean:message  key="label.candidacy.registration.declaration.graduationSection" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	</div>


	<br/><br/><br/><br/><br/>
	<h3 class="registration" align="center"><bean:message  key="label.candidacy.registration.declaration" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<br/><br/><br/>

	<div class="registration" style="text-align: justify;width: 100%;">
	<bean:define id="studentName" name="registration" property="student.person.name" />
	<p><b><bean:message  key="label.candidacy.registration.declaration.institution.responsible" bundle="ACADEMIC_OFFICE_RESOURCES"/></b></p>
	
	<p><bean:message key="label.candidacy.registration.declaration.section1" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="registration" property="student.number"/>, <%= studentName.toString().toUpperCase() %>,
	<bean:message key="label.candidacy.registration.declaration.section2" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message name="person" property="idDocumentType.name" bundle="ENUMERATION_RESOURCES"/>
	<bean:write name="person" property="documentIdNumber"/>, 
	<bean:message key="label.candidacy.registration.declaration.section3" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:write name="person" property="parishOfBirth"/>, 
	<bean:write name="person" property="districtOfBirth"/>, 
	<bean:message key="label.candidacy.registration.declaration.section4" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
	<%= nameOfFather.toString().toUpperCase() %> 
	<bean:message key="label.candidacy.registration.declaration.section5" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
	<%= nameOfMother.toString().toUpperCase() %>,
	<bean:message key="label.candidacy.registration.declaration.section6" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
	<bean:write name="registration" property="studentCandidacy.executionDegree.executionYear.year"/> 
	<bean:message key="label.candidacy.registration.declaration.section7" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
	<bean:write name="registration" property="lastStudentCurricularPlan.degree.name"/> 
	<bean:message key="label.candidacy.registration.declaration.section8" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</p>
	</div>
		
<bean:message  key="label.candidacy.registration.declaration.academicServicesOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/>, <%= new java.text.SimpleDateFormat("dd MMMM yyyy", new java.util.Locale("PT","pt")).format(new java.util.Date()) %>

</div>

</td></tr></table>


</body>

</html:html>