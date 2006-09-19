<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html xhtml="true">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
		<link href="<%= request.getContextPath() %>/CSS/dotist.css" rel="stylesheet" media="screen" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />
		<link href="<%= request.getContextPath() %>/CSS/dotist_print.css" rel="stylesheet" media="print" type="text/css" />
		<title><bean:message key="label.studentSchedule" bundle="APPLICATION_RESOURCES"/></title>
	</head>
	<body>
		<logic:present name="UserView">
		
			<div class="mbottom2" style="font-size: 0.85em; margin-left: 3em;">
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.name" bundle="APPLICATION_RESOURCES"/></strong>: <bean:write name="UserView" property="person.name"/> </p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.studentNumber" bundle="APPLICATION_RESOURCES"/> </strong>: <bean:write name="UserView" property="person.student.number"/> </p>
				<p class="mvert05"><strong style="font-weight: bold;"><bean:message  key="label.istUsername" bundle="APPLICATION_RESOURCES"/> </strong>: <bean:write name="UserView" property="person.istUsername"/> </p>
			</div>	

		</logic:present>
		
		<bean:define id="infoLessons" name="infoLessons"/>
		<div align="center">
			<app:gerarHorario name="infoLessons" type="<%= TimeTableType.CLASS_TIMETABLE %>" application="<%= request.getContextPath() %>"/>
		</div> 
	</body>
</html:html>