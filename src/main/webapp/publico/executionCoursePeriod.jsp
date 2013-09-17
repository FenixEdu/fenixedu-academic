<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<logic:present name="siteView">
	<bean:define id="commonComponent" name="siteView" property="commonComponent"/>
	<bean:define id="executionCourse" name="commonComponent" property="executionCourse"/>
	<bean:define id="executionPeriod" name="executionCourse" property="infoExecutionPeriod"/>	
	<h2 class="greytxt">
		<bean:write name="executionPeriod" property="semester" /><bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:write name="executionPeriod" property="infoExecutionYear.year" />
	</h2>
</logic:present>
