<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom"%>
<%@page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=<%= net.sourceforge.fenixedu._development.PropertiesManager.DEFAULT_CHARSET %>" />



</head>



<body>

<%--<div>
	<jsp:include page="printUnder23TransportsDeclaration.jsp" />
</div>--%>

<div>
	<jsp:include page="printRegistrationDeclaration.jsp" />
</div>

<logic:notEmpty name="candidacy" property="registration.measurementTestRoom">
	<div style="page-break-before: always;">
		<jsp:include page="printMeasurementTestDate.jsp" />
	</div>
</logic:notEmpty>

<div style="page-break-before: always;">
	<jsp:include page="/commons/student/timeTable/classTimeTable.jsp" />
</div>

<div style="page-break-before: always;">
	<jsp:include page="printGratuityPaymentCodes.jsp" />
</div>



<style media="all">
* {
font-family: Arial, sans-serif !important;
}

table.timetable * {
border-color: #000 !important;
background: none !important;
}

p.mvert05 {
margin-top: 0em; margin-bottom: 0em;
}
</style>



</body>
</html>
