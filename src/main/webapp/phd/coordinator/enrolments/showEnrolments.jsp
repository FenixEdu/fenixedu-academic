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
<%@page import="net.sourceforge.fenixedu.domain.phd.ManageEnrolmentsBean"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.Action.phd.coordinator.providers.CurricularCourseDegreeExecutionSemesterProvider"%>

<logic:present role="role(COORDINATOR)">

<h2><bean:message key="label.phd.manage.enrolments" bundle="PHD_RESOURCES" /></h2>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<%-- add method to bean to retrieve phd program --%>
<bean:define id="phdProgramOid" name="manageEnrolmentsBean" property="curricularCourse.degreeCurricularPlan.degree.phdProgram.externalId" />

<html:link action="<%= "/phdEnrolmentsManagement.do?method=showPhdProgram&phdProgramOid=" + phdProgramOid.toString() %>">
	<bean:message bundle="PHD_RESOURCES" key="label.back"/>
</html:link>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<br/>
<fr:form action="/phdEnrolmentsManagement.do?method=manageEnrolments">
	<fr:edit id="manageEnrolmentsBean" name="manageEnrolmentsBean">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= ManageEnrolmentsBean.class.getName() %>">
			<fr:slot name="semester" layout="menu-select-postback">
				<fr:property name="providerClass" value="<%= CurricularCourseDegreeExecutionSemesterProvider.class.getName()  %>"/>
				<fr:property name="format" value="${qualifiedName}" />
			</fr:slot>
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		
		<fr:destination name="postback" path="/phdEnrolmentsManagement.do?method=manageEnrolments" />
	</fr:edit>
</fr:form>

<h3><bean:write name="manageEnrolmentsBean" property="curricularCourse.degreeCurricularPlan.name" />: <bean:write name="manageEnrolmentsBean" property="curricularCourseName" /></h3>

<logic:notEmpty name="manageEnrolmentsBean" property="remainingEnrolments">

	<bean:define id="executionSemesterOid" name="manageEnrolmentsBean" property="semester.externalId" />
	<bean:define id="degreeModuleOid" name="manageEnrolmentsBean" property="curricularCourse.externalId" />
	
	<html:link action="<%= String.format("/phdEnrolmentsManagement.do?method=exportEnrolmentsToExcel&degreeModuleOid=%s&executionSemesterOid=%s", degreeModuleOid, executionSemesterOid) %>">
		<img src="<%= request.getContextPath() %>/images/excel.gif" /> <bean:message key="label.phd.export.enrolments" bundle="PHD_RESOURCES" />
	</html:link>

	<fr:view name="manageEnrolmentsBean" property="remainingEnrolments">
	
		<fr:schema bundle="PHD_RESOURCES" type="<%= Enrolment.class.getName() %>">
			<fr:slot name="registration.number" />
			<fr:slot name="person.name" />
			<fr:slot name="person.institutionalEmailAddressValue" />
<!--			<fr:slot name="finalGrade" />-->
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thlight mtop10" />
			<fr:property name="sortBy" value="person.name=asc" />
			
			<fr:link name="view" label="label.view,PHD_RESOURCES" target="blank" link="/phdIndividualProgramProcess.do?method=viewProcess&processId=${registration.phdIndividualProgramProcess.externalId}"/>
		</fr:layout>
	
	</fr:view>
</logic:notEmpty>

<logic:empty name="manageEnrolmentsBean" property="remainingEnrolments">
	<em><bean:message key="label.phd.no.enrolments.found" bundle="PHD_RESOURCES" /></em>
</logic:empty>

</logic:present>
