<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="ServidorApresentacao.TagLib.sop.v3.TimeTableType" %>
<%@ page import="DataBeans.InfoShiftWithAssociatedInfoClassesAndInfoLessons"%>
<%@ page import="DataBeans.InfoLesson"%>
<%@ page import="java.util.Calendar" %>

<h2><bean:message key="property.executionCourse.curricularHours"/></h2>
<table cellspacing="0" cellpadding="0" width="90%">
       <tr> <td class="listClasses">
          <logic:notEqual name="exeCourse.theo"  value="0">
			
				<b><bean:message key="property.executionCourse.theoreticalHours"/><b>
				<bean:write name="exeCourse.theo" />
				<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
			 <td class="listClasses">
			<logic:notEqual name="exeCourse.prat"  value="0">
			
				<b><bean:message key="property.executionCourse.practicalHours"/><b>
				<bean:write name="exeCourse.prat" />
				<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
         </tr>      
         <tr class="listClasses">
         	 <td class="listClasses">
          <logic:notEqual name="exeCourse.theoPrat"  value="0">
			
				<b><bean:message key="property.executionCourse.theoreticalPracticalHours"/><b>
				<bean:write name="exeCourse.theoPrat" />
			<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
			 <td class="listClasses">
			<logic:notEqual name="exeCourse.lab"  value="0">
			
				<b><bean:message key="property.executionCourse.labHours"/><b>
				<bean:write name="exeCourse.lab" />
			<bean:message key="property.hours"/>
			
			</logic:notEqual></td>
         </tr>                     
            </table>
		<br/>
		<br/>
	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/> 



