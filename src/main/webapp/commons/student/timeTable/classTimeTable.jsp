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
		<logic:present name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>">
		
			<div class="mbottom2" style="font-size: 0.85em; margin-left: 3em;">
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.name" bundle="APPLICATION_RESOURCES"/></strong>: <bean:write name="person" property="name"/> </p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.studentNumber" bundle="APPLICATION_RESOURCES"/> </strong>: <bean:write name="person" property="student.number"/> </p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.istUsername"  bundle="APPLICATION_RESOURCES"/> </strong>: <bean:write name="person" property="istUsername"/> </p>
			</div>	

		</logic:present>
		
		<bean:define id="infoLessons" name="infoLessons"/>
		<div align="center">
			<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE %>" application="<%= request.getContextPath() %>"/>
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