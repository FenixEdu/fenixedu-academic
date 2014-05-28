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
<html:xhtml />

<logic:present role="role(MANAGER)">

	<em><bean:message key="label.manager" bundle="MANAGER_RESOURCES" /></em>
	<h2>
		<bean:message key="label.registration.transitToBolonha" bundle="MANAGER_RESOURCES" />
	</h2>
	
	<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
	</html:messages>
	

	<bean:define id="scpId" name="studentCurricularPlan" property="externalId" />
	<bean:define id="studentId" name="studentCurricularPlan" property="registration.student.externalId" />
	<html:form action="<%= "/bolonhaStudentEnrolment.do?scpId=" + scpId.toString() + "&amp;studentId=" + studentId.toString() %>">
		<html:hidden property="method" value="transitToBolonha" />
		<strong><bean:message bundle="MANAGER_RESOURCES"  key="message.registration.transitToBolonha" /> (<bean:write name="studentCurricularPlan" property="name" /> )?</strong>
		<br/>
		<br/>
		<bean:message bundle="APPLICATION_RESOURCES" key="label.date"/>: <html:text property="date" size="10" /> <bean:message bundle="APPLICATION_RESOURCES" key="label.date.instructions.small" />
		<br/>
		<br/>
		<html:submit><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.yes" /></html:submit>
		<html:cancel onclick="this.form.method.value='showAllStudentCurricularPlans';"><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.no" /></html:cancel>
	</html:form>
	
</logic:present>
