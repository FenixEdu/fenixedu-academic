<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p />
<span class="error"><html:errors/></span><br/>
<p />

<!-- Tutor -->

<h2><bean:message key="label.tutor"/>&nbsp;<%=  request.getAttribute("tutorNumber").toString() %></h2>

<html:form action="/tutorManagementLookup">
<!-- Associar aluno -->
<table>
<tr>
	<td colspan="3" class="infoop">
		<bean:message key="message.coordinator.tutor.associateOneStudent.help"/>
		<br />
		<b><bean:message key="label.coordinator.tutor.associatedStudent" /></b>			
	</td>
</tr>
<tr>
	<td><bean:message key="label.number" />:&nbsp;</td>	
	<td><html:text property="studentNumberFirst" /></td>
	<td>
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="label.submit"/>
		</html:submit>
	</td>
</tr>
<tr>
	<td colspan="3" class="infoop">
		<bean:message key="message.coordinator.tutor.associateManyStudent.help"/>
		<br />
		<b><bean:message key="label.coordinator.tutor.associatedManyStudents" /></b>
	</td>
</tr>
<tr>
	<td><bean:message key="label.number" />:&nbsp;</td>	
	<td><html:text property="studentNumberFirst" />-<html:text property="studentNumberSecond" /></td>
	<td>
		<html:submit property="method" styleClass="inputbutton">
			<bean:message key="label.submit"/>
		</html:submit>
	</td>
</tr>
</table>

<!-- Desassociar aluno -->
<logic:present name="studentsOfTutor">
<table>
	<tr>
		<th><bean:message key="label.number" /></th>
		<th><bean:message key="label.masterDegree.administrativeOffice.studentName" /></th>
		<th>&nbsp;</th>
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
		</td>
	</tr>
</table>
</logic:present>
	</html:form>