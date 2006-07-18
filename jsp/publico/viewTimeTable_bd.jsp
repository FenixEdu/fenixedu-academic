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

<logic:present name="siteView" property="commonComponent">
 	<bean:define id="commonComponent" name="siteView" property="commonComponent" />
 	<bean:define id="exeCourse" name="commonComponent" property="executionCourse"/>
	<h2><bean:message key="label.schedule" /></h2>

	
	<table class="tab_simple" cellspacing="0" cellpadding="2">
		<tr>
			<th colspan="4"><bean:message key="label.hoursPerWeek"/></th>
		</tr>
		<tr>
			<td class="subheader" width="100px"><bean:message key="property.executionCourse.theoreticalHours"/></td>
			<td class="subheader" width="100px"><bean:message key="property.executionCourse.practicalHours"/></td>
			<td class="subheader" width="100px"><bean:message key="property.executionCourse.theoreticalPracticalHours"/></td>
			<td class="subheader" width="100px"><bean:message key="property.executionCourse.labHours"/></td>
		</tr>
		<tr>
			<td>
				<logic:notEqual name="exeCourse" property="theoreticalHours" value="0">
					<bean:write name="exeCourse" property="theoreticalHours" />
				</logic:notEqual>
				<logic:equal name="exeCourse" property="theoreticalHours" value="0">
					<bean:message key="label.number0.0" />
				</logic:equal>
			</td>	
			
			<td>
				<logic:notEqual name="exeCourse" property="praticalHours" value="0">
					<bean:write name="exeCourse" property="praticalHours" />
				</logic:notEqual>
				<logic:equal name="exeCourse" property="praticalHours" value="0">
					<bean:message key="label.number0.0" />
				</logic:equal>
			</td>

			<td>
				<logic:notEqual name="exeCourse" property="theoPratHours" value="0">
					<bean:write name="exeCourse" property="theoPratHours" />
				</logic:notEqual>
				<logic:equal name="exeCourse" property="theoPratHours" value="0">
					<bean:message key="label.number0.0" />
				</logic:equal>
			</td>
			
			<td>
				<logic:notEqual name="exeCourse" property="labHours" value="0">
					<bean:write name="exeCourse" property="labHours" />
				</logic:notEqual>
				<logic:equal name="exeCourse" property="labHours" value="0">
					<bean:message key="label.number0.0" />
				</logic:equal>
			</td>
		</tr>
	</table>
</logic:present>	

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component"/>
	<bean:define id="lessonList" name="component" property="lessons" />		
	<app:gerarHorario name="lessonList" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
</logic:present>	

<logic:notPresent name="siteView" property="component" >
	<bean:message key="message.public.notfound.timeTable"/>
</logic:notPresent>

