<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exam" name="component" property="infoExam"/>
<bean:define id="executionCourseCode"  name="siteView" property="commonComponent.executionCourse.idInternal"/>
<bean:define id="examCode"  name="exam" property="idInternal"/>
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.studentsEnrolled.information" /></td>
		</tr>
	</table>

<br/>
<table cellspacing="1" border="0">
	<tr>
		<td class="listClasses-header" ><bean:message key="label.season"/></td>
		<td class="listClasses-header" ><bean:message key="label.day"/></td>
		<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
		<td class="listClasses-header"><bean:message key="label.number.students.enrolled"/></td>
		<logic:notEqual name="component" property="size" value="0">
			<td class="listClasses-header"><bean:message key="label.student.room.distribution"/></td>		
		</logic:notEqual>
	</tr>
	<tr>
		<td class="listClasses"><bean:write name="exam" property="season"/></td>
		<td class="listClasses"><bean:write name="exam" property="date"/></td>
		<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
		<td class="listClasses"><bean:write name="component" property="size"/></td>
		<logic:notEqual name="component" property="size" value="0">		
			<td class="listClasses">
				<html:link page='<%= "/distributeStudentsByRoom.do?method=prepare&objectCode=" + executionCourseCode + "&examCode=" + examCode %>'>
					<bean:message key="link.student.room.distribution"/>
				</html:link>
			</td>		
		</logic:notEqual>		
	</tr>
</table>
<br/>
<bean:size id="sizeOfExamStudentRooms" name="component" property="infoExamStudentRoomList" />

<logic:notEmpty name="component" property="infoStudents" >
<h2><bean:message key="label.students.enrolled.exam"/></h2>
<table>
<tr>
	<td class="listClasses-header"><bean:message key="label.number"/></td>
	<td class="listClasses-header"><bean:message key="label.name"/></td>
	<td class="listClasses-header"><bean:message key="label.room"/></td>	
</tr>
<logic:equal name="sizeOfExamStudentRooms" value="0">
	<logic:iterate id="student" name="component" property="infoStudents">
		<tr>
			<td class="listClasses"><bean:write name="student" property="number"/></td>
			<td class="listClasses"><bean:write name="student" property="infoPerson.nome"/></td>
			<td class="listClasses">&nbsp;</td>			
		</tr>
	</logic:iterate>
</logic:equal>
<logic:notEqual name="sizeOfExamStudentRooms" value="0">
	<logic:iterate id="infoExamStudentRoom" name="component" property="infoExamStudentRoomList">
		<tr>
			<td class="listClasses"><bean:write name="infoExamStudentRoom" property="infoStudent.number"/></td>
			<td class="listClasses"><bean:write name="infoExamStudentRoom" property="infoStudent.infoPerson.nome"/></td>
			<td class="listClasses">
				<logic:present name="infoExamStudentRoom" property="infoRoom">
					<bean:write name="infoExamStudentRoom" property="infoRoom.nome"/>			
				</logic:present>
				<logic:notPresent name="infoExamStudentRoom" property="infoRoom">
					&nbsp;
				</logic:notPresent>
			</td>
		</tr>
	</logic:iterate>
</logic:notEqual> 

</table>
</logic:notEmpty>



