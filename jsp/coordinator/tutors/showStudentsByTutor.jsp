<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h2><bean:message key="label.coordinator.tutor.managementTutor"/></h2>

<!-- AVISOS E ERROS -->
<span class="error">
<logic:notPresent name="studentsOfTutor">
	<bean:message key="error.tutor.noStudent" />
</logic:notPresent>
<logic:present name="studentsOfTutor">
<logic:empty name="studentsOfTutor">
	<bean:message key="error.tutor.noStudent" />
</logic:empty>
</logic:present>
<br />
<html:errors/>
<br />
<logic:present name="studentsWithErros">
	<bean:message key="error.tutor.studentsIncorrects" /><br />
	<bean:size id="studentsSize" name="studentsWithErros"/>
	<logic:iterate id="studentNumber" name="studentsWithErros" indexId="index">
		<bean:write name="studentNumber" />
		<logic:notEqual name="index" value="<%= String.valueOf(studentsSize.intValue() - 1) %>">
			,&nbsp; 
		</logic:notEqual>
	</logic:iterate>
</logic:present>
</span>

<html:form action="/tutorManagementLookup" focus="studentNumber">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=  request.getAttribute("executionDegreeId").toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tutorNumber" property="tutorNumber" value="<%=  request.getAttribute("tutorNumber").toString() %>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" />
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
	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumber" property="studentNumber" size="5"/></td>
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" onclick="document.tutorForm.page=2;">
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
	<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumberFirst" property="studentNumberFirst" size="5" />-<html:text bundle="HTMLALT_RESOURCES" altKey="text.studentNumberSecond" property="studentNumberSecond" size="5" /></td>
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" onclick="document.tutorForm.page=2;">
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
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.deletedTutorsIds" property="deletedTutorsIds">
				<bean:write name="tutorId"/>
			</html:multibox>
		</td>
	</tr>
</logic:iterate>

<tr>
	<td colspan='3'>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" onclick="document.tutorForm.page=0;">
			<bean:message key="button.coordinator.tutor.remove"/>
		</html:submit>
			
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.method" property="method" styleClass="inputbutton" onclick="document.tutorForm.page=0;">
			<bean:message key="button.cancel"/>
		</html:submit>
	</td>
</tr>
</logic:notEmpty>
</logic:present>
</table>
</html:form>