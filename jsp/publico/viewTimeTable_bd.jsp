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

<logic:notPresent name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">
<table align="center"  cellpadding='0' cellspacing='0'>
			<tr align="center">
				<td>
					<font color='red'> <bean:message key="message.public.notfound.executionCourse"/> </font>
				</td>
			</tr>
		</table>
</logic:notPresent>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" scope="session">

<h2><bean:message key="property.executionCourse.curricularHours"/></h2>
	
<table cellspacing="0" cellpadding="0" width="90%">
       <tr class="timeTable_line">
          <logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoreticalHours" value="0">
			 <td class="horariosHoras_first">
				<b><bean:message key="property.executionCourse.theoreticalHours"/><b>
				<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoreticalHours"/>
				<bean:message key="property.hours"/>
			</td>
			</logic:notEqual>
			<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="praticalHours" value="0">
			 <td class="horariosHoras_first">
				<b><bean:message key="property.executionCourse.practicalHours"/><b>
				<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="praticalHours"/>
				<bean:message key="property.hours"/>
			</td>
			</logic:notEqual>
         </tr>      
         <tr class="timeTable_line">
          <logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoPratHours" value="0">
			 <td class="horariosHoras_first">
				<b><bean:message key="property.executionCourse.theoreticalPracticalHours"/><b>
				<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="theoPratHours"/>
			<bean:message key="property.hours"/>
			</td>
			</logic:notEqual>
			<logic:notEqual name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="labHours" value="0">
			 <td class="horariosHoras_first">
				<b><bean:message key="property.executionCourse.labHours"/><b>
				<bean:write name="<%= SessionConstants.EXECUTION_COURSE_KEY %>" property="labHours"/>
			<bean:message key="property.hours"/>
			</td>
			</logic:notEqual>
         </tr>                     
            </table>

		<br/>
		<br/>



	<app:gerarHorario name="<%= SessionConstants.LESSON_LIST_ATT %>" type="<%= TimeTableType.EXECUTION_COURSE_TIMETABLE %>"/> 

	
</logic:present>	
		
