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

<table class="tab_simple" cellspacing="0" cellpadding="2">
	<tr>
		<th colspan="9"><bean:message key="label.hoursPerWeek"/></th>
	</tr>
	<tr>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.theoreticalHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.practicalHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.theoreticalPracticalHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.labHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.seminaryHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.problemsHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.fieldWorkHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.trainingPeriodHours"/></td>
		<td class="subheader" width="100"><bean:message key="property.executionCourse.tutorialOrientationHours"/></td>
	</tr>
	<tr>
		<td>
			<logic:notEqual name="executionCourse" property="theoreticalHours" value="0">
				<bean:write name="executionCourse" property="theoreticalHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="theoreticalHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>	
			
		<td>
			<logic:notEqual name="executionCourse" property="praticalHours" value="0">
				<bean:write name="executionCourse" property="praticalHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="praticalHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>

		<td>
			<logic:notEqual name="executionCourse" property="theoPratHours" value="0">
				<bean:write name="executionCourse" property="theoPratHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="theoPratHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>

		<td>
			<logic:notEqual name="executionCourse" property="labHours" value="0">
				<bean:write name="executionCourse" property="labHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="labHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>

		<td>
			<logic:notEqual name="executionCourse" property="seminaryHours" value="0">
				<bean:write name="executionCourse" property="seminaryHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="seminaryHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>
		<td>
			<logic:notEqual name="executionCourse" property="problemsHours" value="0">
				<bean:write name="executionCourse" property="problemsHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="problemsHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>
		<td>
			<logic:notEqual name="executionCourse" property="fieldWorkHours" value="0">
				<bean:write name="executionCourse" property="fieldWorkHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="fieldWorkHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>
		<td>
			<logic:notEqual name="executionCourse" property="trainingPeriodHours" value="0">
				<bean:write name="executionCourse" property="trainingPeriodHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="trainingPeriodHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>
		<td>
			<logic:notEqual name="executionCourse" property="tutorialOrientationHours" value="0">
				<bean:write name="executionCourse" property="tutorialOrientationHours" />
			</logic:notEqual>
			<logic:equal name="executionCourse" property="tutorialOrientationHours" value="0">
				<bean:message key="label.number0.0" />
			</logic:equal>
		</td>

	</tr>
</table>

<app:gerarHorario name="infoLessons" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
