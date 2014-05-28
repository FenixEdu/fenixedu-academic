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
<%@ page language="java"%>

<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@page import="net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateLog"%>

<logic:present role="role(MANAGER)">
	<h2><bean:message key="student.registration.states.log" bundle="APPLICATION_RESOURCES" /></h2>

	<html:link action="/bolonhaStudentEnrolment.do?method=showAllStudentCurricularPlans" paramId="studentId" paramName="registration" paramProperty="student.externalId">
		<bean:message key="label.back"  bundle="APPLICATION_RESOURCES"/>
	</html:link>

	<br/>
	<br/>

	<fr:view name="registration" property="registrationStateLogs">
		
		<fr:schema bundle="APPLICATION_RESOURCES" type="<%= RegistrationStateLog.class.getName() %>">
			<fr:slot name="stateDate" />
			<fr:slot name="stateType" />
			<fr:slot name="action.name" />
			<fr:slot name="who" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4" />
			<fr:property name="sortBy" value="stateDate=desc" />
		</fr:layout>

	</fr:view>

</logic:present>
