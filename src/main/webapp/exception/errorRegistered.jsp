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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.joda.time.YearMonthDay"%>

<html:html xhtml="true">

<head>
<title>
	<bean:message key="dot.title" bundle="GLOBAL_RESOURCES"/><bean:message key="label.support.page" bundle="GLOBAL_RESOURCES"/>
</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style>

body {
	font-family: "Helvetica Neue",Helvetica,Arial,sans-serif;
	font-size: 14px;
	font-weight:300 !important;
	background-color: #F6F4ED;
	color: #4b565c;
}

#container {
	margin: 80px auto 0 auto;
	width: 800px;
	background-color: #f7f7f7;
	padding: 0 0 20px 0;
	-moz-border-radius: 2px;
	-webkit-border-radius: 2px;
	border-radius: 2px;
	-moz-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	-webkit-box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
	box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);
}
</style>

</head>

<body>


<div id="container">
<table style="width: 80%; margin: 0 auto">
<tr>
<td style="width: 20%">
				<img src="${pageContext.request.contextPath}/images/ID_FenixEdu.png" alt="FenixEdu"/>
</td>
<td style="width: 80%; text-align: right">

				<h1><bean:message key="message.error.submitted" bundle="APPLICATION_RESOURCES" /></h1>
				<br/>
				<p><bean:message key="support.mail.submitted" bundle="APPLICATION_RESOURCES" /></p>
</td>

</tr>
</table>
</div>

</body>
</html:html>
