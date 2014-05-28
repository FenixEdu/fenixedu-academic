<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.ExecutionSemester"%><html:xhtml/>

<logic:present role="role(STUDENT)">

<%
	ExecutionSemester executionSemester = ExecutionSemester.readActualExecutionSemester();
	request.setAttribute("executionSemester", executionSemester);
%>

<logic:present name="executionSemester" property="enrolmentInstructions">
	<bean:write name="executionSemester" property="enrolmentInstructions.instructions" filter="false"/>
</logic:present>

<bean:define id="registrationOid" name="registration" property="externalId" />

<fr:form action="/bolonhaStudentEnrollment.do?method=prepare&registrationOid=${registrationOid}">
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message  key="label.continue" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</fr:form>


</logic:present>
