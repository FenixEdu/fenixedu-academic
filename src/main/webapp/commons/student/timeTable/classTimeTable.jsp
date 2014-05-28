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
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="http://struts.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:html xhtml="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/print.css" rel="stylesheet" media="print" type="text/css" />
		<title><bean:message key="private.student.view.timetable" bundle="TITLES_RESOURCES"/></title>
	</head>
	<body>
		<logic:present name="LOGGED_USER_ATTRIBUTE">
			<div class="mbottom2" style="font-size: 0.85em; margin-left: 3em;">
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.name" bundle="APPLICATION_RESOURCES"/></strong>: ${LOGGED_USER_ATTRIBUTE.person.name}</p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.studentNumber" bundle="APPLICATION_RESOURCES"/> </strong>: ${LOGGED_USER_ATTRIBUTE.person.student.number} </p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.istUsername"  bundle="APPLICATION_RESOURCES"/> </strong>: ${LOGGED_USER_ATTRIBUTE.username} </p>
			</div>

		</logic:present>
		
		<div align="center">
			<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE %>" application="${pageContext.request.contextPath}"/>
		</div> 
		<logic:present name="tutor">
			<div style="text-align: left">
				<strong><bean:message key="label.tutor" bundle="APPLICATION_RESOURCES"/></strong><br/>
				<bean:write name="tutor" property="name"/><br/>
				<bean:write name="tutor" property="defaultEmailAddressValue"/>
			</div>
		</logic:present>
	</body>
</html:html>