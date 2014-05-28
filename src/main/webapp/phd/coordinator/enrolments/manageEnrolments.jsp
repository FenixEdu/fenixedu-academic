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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.Enrolment"%>
<%@page import="net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean" %>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers.PhdManageEnrolmentsExecutionSemestersProvider"%>

<logic:present role="role(COORDINATOR)">

<h2><bean:message key="label.phd.manage.enrolments" bundle="PHD_RESOURCES" /></h2>


<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>


<html:link action="/phdIndividualProgramProcess.do?method=viewProcess" paramId="processId" paramName="process" paramProperty="externalId">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<br/><br/>
<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />

<fr:form action="<%= "/phdIndividualProgramProcess.do?method=manageEnrolments&processId=" + processId %>">

	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean">
		<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
			<fr:slot name="semester" layout="menu-select-postback">
				<fr:property name="providerClass" value="<%= PhdManageEnrolmentsExecutionSemestersProvider.class.getName()  %>"/>
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="<%= "/phdIndividualProgramProcess.do?method=manageEnrolments&processId=" + processId %>" />
	</fr:edit>
</fr:form>


<strong><bean:message key="label.phd.enrolments.performed.by.student.to.approve" bundle="PHD_RESOURCES" /></strong>

<logic:empty name="manageEnrolmentsBean" property="enrolmentsPerformedByStudent">
	<em><bean:message key="label.phd.no.enrolments.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<logic:notEmpty name="manageEnrolmentsBean" property="enrolmentsPerformedByStudent">
	
	<table class="tstyle2 thlight mtop10">
		<tr>
			<th><bean:message key="label.net.sourceforge.fenixedu.domain.Enrolment.name" bundle="PHD_RESOURCES" /></th>
			<th><bean:message key="label.net.sourceforge.fenixedu.domain.Enrolment.ectsCredits" bundle="PHD_RESOURCES" /></th>
			<th><bean:message key="label.net.sourceforge.fenixedu.domain.Enrolment.executionPeriod.qualifiedName" bundle="PHD_RESOURCES" /></th>
			<th><bean:message key="label.net.sourceforge.fenixedu.domain.Enrolment.enrolmentCondition" bundle="PHD_RESOURCES" /></th>
		</tr>
		<logic:iterate id="enrolment" name="manageEnrolmentsBean" property="enrolmentsPerformedByStudent">
			<tr>
				<td><bean:write name="enrolment" property="presentationName.content" /></td>
				<td><bean:write name="enrolment" property="ectsCredits" /></td>
				<td><bean:write name="enrolment" property="executionPeriod.qualifiedName" /></td>
				<td>
					<bean:define id="enrolmentCondition" name="enrolment" property="enrolmentCondition" />
					<logic:equal name="enrolmentCondition" value="VALIDATED">
						<div style="color: #146e14;" ><bean:write name="enrolment" property="enrolmentCondition.description" /></div>
					</logic:equal>
					<logic:equal name="enrolmentCondition" value="TEMPORARY">
						<div style="color: #804500;"><bean:write name="enrolment" property="enrolmentCondition.description" /></div>
					</logic:equal>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<p>
	<bean:define id="executionSemesterId" name="manageEnrolmentsBean" property="semester.externalId" />
	<html:link action="<%= String.format("/phdIndividualProgramProcess.do?method=prepareValidateEnrolments&processId=%s&executionSemesterId=%s", processId, executionSemesterId) %>">
		<bean:message bundle="PHD_RESOURCES" key="label.phd.validate.enrolments"/>
	</html:link>
	</p>
</logic:notEmpty>

<br/>
<strong><bean:message key="label.phd.remaining.enrolments" bundle="PHD_RESOURCES" /></strong>

<logic:empty name="manageEnrolmentsBean" property="remainingEnrolments">
	<em><bean:message key="label.phd.no.enrolments.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

<fr:view name="manageEnrolmentsBean" property="remainingEnrolments">
	<fr:schema bundle="PHD_RESOURCES" type="<%= Enrolment.class.getName() %>">
		<fr:slot name="presentationName" />
		<fr:slot name="ectsCredits" />
		<fr:slot name="executionPeriod.qualifiedName" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight mtop10" />
		<fr:property name="sortBy" value="name=asc" />
	</fr:layout>
</fr:view>


</logic:present>
