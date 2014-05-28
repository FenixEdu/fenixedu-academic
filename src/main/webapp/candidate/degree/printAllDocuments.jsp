<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom"%>
<%@page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />



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
