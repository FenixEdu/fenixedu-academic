<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<span class="error"><html:errors/></span>	

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="exam" name="component" property="infoExam"/>

<br/>
<table cellspacing="1" border="0">
	<tr>
		<td class="listClasses-header" ><bean:message key="label.season"/></td>
		<td class="listClasses-header" ><bean:message key="label.day"/></td>
		<td class="listClasses-header" ><bean:message key="label.beginning"/></td>	
		<td class="listClasses-header"><bean:message key="label.number.students.enrolled"/></td>
		<td class="listClasses-header"><bean:message key="label.student.room.distribution"/></td>		
	</tr>
	<tr>
		<td class="listClasses"><bean:write name="exam" property="season"/></td>
		<td class="listClasses"><bean:write name="exam" property="date"/></td>
		<td class="listClasses"><bean:write name="exam" property="beginningHour"/></td>
		<td class="listClasses"><bean:write name="component" property="size"/></td>
		<td class="listClasses"><html:link page="/"><bean:message key="link.student.room.distribution"/></html:link></td>		
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
			<td class="listClasses"><bean:write name="infoExamStudentRoom" property="infoRoom.nome"/></td>			
		</tr>
	</logic:iterate>
</logic:notEqual> 

</table>
</logic:notEmpty>



