<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%><html:xhtml/>

<logic:present role="STUDENT">

<%
	ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	request.setAttribute("executionSemester", executionSemester);
%>

<em>Portal do Estudante</em>

<logic:present name="executionSemester" property="enrolmentInstructions">
	<bean:write name="executionSemester" property="enrolmentInstructions.instructions" filter="false"/>
</logic:present>

<bean:define id="registrationOid" name="registration" property="externalId" />

<html:form action="<%= "/bolonhaStudentEnrollment.do?method=prepare&registrationOid=" + registrationOid.toString() %>">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</html:form>


</logic:present>
