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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<logic:present role="role(COORDINATOR)">

	<jsp:include page="/coordinator/context.jsp" />

	<h2><bean:message key="label.transition.bolonha.registrationsInTransition"
		bundle="STUDENT_RESOURCES" /></h2>

	<logic:empty name="registrations">
		<span class="error0"> <bean:message bundle="STUDENT_RESOURCES"
			key="label.transition.bolonha.registrationsInTransition.noRegistrations" />
		</span>
	</logic:empty>

	<logic:notEmpty name="registrations">
		<fr:view name="registrations"
			schema="student.registrationsToList">
			<fr:layout name="tabular">
				<fr:property name="sortBy" value="startDate=desc"/>			
				<fr:property name="classes" value="tstyle4 thlight mtop025 boldlink1" />
				<fr:property name="columnClasses" value=",tdhl1,," />
				<fr:property name="linkFormat(view)" value="/bolonhaTransitionManagement.do?method=showStudentCurricularPlan&amp;registrationId=${externalId}&amp;studentId=${student.externalId}" />
				<fr:property name="key(view)" value="label.view"/>
				<fr:property name="bundle(view)" value="APPLICATION_RESOURCES"/>
				<fr:property name="contextRelative(view)" value="true"/>
			</fr:layout>
		</fr:view>
	</logic:notEmpty>

</logic:present>
