<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="siteView">
	<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
	<h1><bean:write name="commonComponent" property="title"/></h1>
	
	<bean:define id="executionCourse" name="commonComponent" property="executionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>	
	<h2 align="center">
		<bean:write name="executionPeriod" property="semester" /><bean:message key="label.ordinal.semester.abbreviation" />
		<bean:write name="executionPeriod" property="infoExecutionYear.year" />
	</h2>
</logic:present>
