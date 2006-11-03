<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<h2><bean:message key="label.schedule" /></h2>

<h3><bean:message key="label.hoursPerWeek"/></h3>

<%--
<ul>
	<li>Teórica: 2.0 horas</li>
	<li>Laboratorial: 3.0 horas</li>
</ul>
--%>

<ul>
	<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">	
		<li><bean:message key="property.executionCourse.theoreticalHours"/>: <bean:write name="executionCourse" property="theoreticalHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="praticalHours" value="0">
		<li><bean:message key="property.executionCourse.practicalHours"/>: <bean:write name="executionCourse" property="praticalHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
		<li><bean:message key="property.executionCourse.theoreticalPracticalHours"/>: <bean:write name="executionCourse" property="theoPratHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="labHours" value="0">	
		<li><bean:message key="property.executionCourse.labHours"/>: <bean:write name="executionCourse" property="labHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="seminaryHours" value="0">
		<li><bean:message key="property.executionCourse.seminaryHours"/>: <bean:write name="executionCourse" property="seminaryHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="problemsHours" value="0">
		<li><bean:message key="property.executionCourse.problemsHours"/>: <bean:write name="executionCourse" property="problemsHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="fieldWorkHours" value="0">
		<li><bean:message key="property.executionCourse.fieldWorkHours"/>: <bean:write name="executionCourse" property="fieldWorkHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="trainingPeriodHours" value="0">
		<li><bean:message key="property.executionCourse.trainingPeriodHours"/>: <bean:write name="executionCourse" property="trainingPeriodHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
	<logic:notEqual name="executionCourse" property="tutorialOrientationHours" value="0">
		<li><bean:message key="property.executionCourse.tutorialOrientationHours"/>: <bean:write name="executionCourse" property="tutorialOrientationHours" /> <bean:message key="label.hours"/></li>
	</logic:notEqual>
</ul>



<%--
<table class="tab_simple" cellspacing="0" cellpadding="2">
	<tr>
		<th colspan="9"><bean:message key="label.hoursPerWeek"/></th>
	</tr>
	<tr>
		<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">	
			<li><bean:message key="property.executionCourse.theoreticalHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="praticalHours" value="0">
			<li><bean:message key="property.executionCourse.practicalHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
			<li><bean:message key="property.executionCourse.theoreticalPracticalHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="labHours" value="0">	
			<li><bean:message key="property.executionCourse.labHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="seminaryHours" value="0">
			<li><bean:message key="property.executionCourse.seminaryHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="problemsHours" value="0">
			<li><bean:message key="property.executionCourse.problemsHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="fieldWorkHours" value="0">
			<li><bean:message key="property.executionCourse.fieldWorkHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="trainingPeriodHours" value="0">
			<li><bean:message key="property.executionCourse.trainingPeriodHours"/></li>
		</logic:notEqual>
		<logic:notEqual name="executionCourse" property="tutorialOrientationHours" value="0">
			<li><bean:message key="property.executionCourse.tutorialOrientationHours"/></li>
		</logic:notEqual>
	</tr>
	<tr>
		<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">
			<td>
				<bean:write name="executionCourse" property="theoreticalHours" />
			</li>
		</logic:notEqual>
			
		<logic:notEqual name="executionCourse" property="praticalHours" value="0">
			<td>
				<bean:write name="executionCourse" property="praticalHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
			<td>
				<bean:write name="executionCourse" property="theoPratHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="labHours" value="0">
			<td>
				<bean:write name="executionCourse" property="labHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="seminaryHours" value="0">
			<td>
				<bean:write name="executionCourse" property="seminaryHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="problemsHours" value="0">
			<td>
				<bean:write name="executionCourse" property="problemsHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="fieldWorkHours" value="0">
			<td>
				<bean:write name="executionCourse" property="fieldWorkHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="trainingPeriodHours" value="0">
			<td>
				<bean:write name="executionCourse" property="trainingPeriodHours" />
			</li>
		</logic:notEqual>

		<logic:notEqual name="executionCourse" property="tutorialOrientationHours" value="0">
			<td>
				<bean:write name="executionCourse" property="tutorialOrientationHours" />
			</li>
		</logic:notEqual>

	</tr>
</table>
--%>


<app:gerarHorario name="infoLessons" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
