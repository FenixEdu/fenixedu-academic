<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p />
<span class="error"><html:errors/></span><br/>
<p />

<!-- Tutor -->

<h2><bean:message key="label.coordinator.tutor.managementTutor"/></h2>

<html:form action="/tutorManagementLookup">
<html:hidden property="executionDegreeId" value="<%=  request.getAttribute("executionDegreeId").toString() %>"/>
<html:hidden property="tutorNumber" value="<%=  request.getAttribute("tutorNumber").toString() %>"/>
<html:hidden property="page" value="2" />
<!-- Associar aluno -->
<table border="0">
<tr>
	<td colspan="3">
		<b><bean:message key="label.coordinator.tutor.associatedStudent" /></b>			
	</td>
</tr>
<tr>
	<td colspan="3" class="infoop">
		<bean:message key="message.coordinator.tutor.associateOneStudent.help"/>
	</td>
</tr>
<tr>
	<td><bean:message key="label.number" />:&nbsp;</td>	
	<td><html:text property="studentNumber" size="5"/></td>
	<td>
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="button.coordinator.tutor.associateOneStudent"/>
		</html:submit>
	</td>
</tr>
<tr>
	<td colspan="3">
		<br /><br />
	</td>
</tr>
<tr>
	<td colspan="3">
		<b><bean:message key="label.coordinator.tutor.associatedManyStudents" /></b>
	</td>
</tr>
<tr>
	<td colspan="3" class="infoop">
		<bean:message key="message.coordinator.tutor.associateManyStudent.help"/>
	</td>
</tr>
<tr>
	<td><bean:message key="label.numbersRange" />:&nbsp;</td>	
	<td><html:text property="studentNumberFirst" size="5" />-<html:text property="studentNumberSecond" size="5" /></td>
	<td>
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="button.coordinator.tutor.associateManyStudent"/>
		</html:submit>
	</td>
</tr>


<!-- Desassociar aluno -->
<logic:present name="studentsOfTutor">
<logic:notEmpty name="studentsOfTutor">
<bean:size id="studentsSize" name="studentsOfTutor" />
<tr>
	<td colspan="3">
		<br /><br />
	</td>
</tr>
<tr>
	<td colspan="3">
		<b><bean:message key="error.tutor.numberStudents" arg0="<%= studentsSize.toString() %>"/></b>
	</td>
</tr>	
<tr>
	<td colspan="3" class="infoop">
		<bean:message key="message.coordinator.tutor.unassociateStudent.help"/>
	</td>
</tr>	
<tr>
	<th><bean:message key="label.number" /></th>
	<th><bean:message key="label.masterDegree.administrativeOffice.studentName" /></th>
	<th><bean:message key="label.coordinator.tutor.unassociatedStudent" /></th>
</tr>

<logic:iterate id="infoTutor" name="studentsOfTutor">
	<bean:define id="tutorId" name="infoTutor" property="idInternal" />
	<tr>
		<td><bean:write name="infoTutor" property="infoStudent.number"/></td>
		<td><bean:write name="infoTutor" property="infoStudent.infoPerson.nome"/></td>
		<td>
			<html:multibox property="deletedTutorsIds">
				<bean:write name="tutorId"/>
			</html:multibox>
		</td>
	</tr>
</logic:iterate>
<tr>
	<td colspan='3'>
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="button.coordinator.tutor.remove"/>
		</html:submit>
			
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="button.cancel"/>
		</html:submit>
	</td>
</tr>
</table>
</logic:notEmpty>
</logic:present>
</html:form>