<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/joda.tld" prefix="joda"%>

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
