<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>
 <logic:present name="siteView" property="commonComponent">
 	<bean:define id="commonComponent" name="siteView" property="commonComponent" />
 	<bean:define id="exeCourse" name="commonComponent" property="executionCourse"/>
<h2><bean:message key="property.executionCourse.curricularHours"/></h2>
<table class="invisible" cellspacing="0" cellpadding="0" width="90%">
       <tr> <td class="listClasses">
          <logic:notEqual name="exeCourse" property="theoreticalHours"  value="0">
			
				<b><bean:message key="property.executionCourse.theoreticalHours"/><b>
				<bean:write name="exeCourse" property="theoreticalHours" />
				<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
			 <td class="listClasses">
			<logic:notEqual name="exeCourse" property="praticalHours"  value="0">
			
				<b><bean:message key="property.executionCourse.practicalHours"/><b>
				<bean:write name="exeCourse" property="praticalHours" />
				<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
         </tr>      
         <tr class="listClasses">
         	 <td class="listClasses">
          <logic:notEqual name="exeCourse" property="theoPratHours"  value="0">
			
				<b><bean:message key="property.executionCourse.theoreticalPracticalHours"/><b>
				<bean:write name="exeCourse" property="theoPratHours" />
			<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
			 <td class="listClasses">
			<logic:notEqual name="exeCourse" property="labHours"  value="0">
			
				<b><bean:message key="property.executionCourse.labHours"/><b>
				<bean:write name="exeCourse" property="labHours" />
			<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
         </tr>                     
            </table>
		<br/>
		<br/>
	</logic:present>	
	<logic:present name="siteView" property="component">
		<bean:define id="component" name="siteView" property="component"/>
		<bean:define id="lessonList" name="component" property="lessons" />
		
	<app:gerarHorario name="lessonList" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/>
	</logic:present>	
	<logic:notPresent name="siteView" property="component" >
		<bean:message key="message.public.notfound.timeTable"/>
	</logic:notPresent>

