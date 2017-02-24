<%--

    Copyright © 2002 Instituto Superior TÃ©cnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>


<h1>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
		key="title.manageStudentCurricularPlans.create" />
</h1>

<p>
	<html:link action="/manageStudentCurricularPlans.do?method=list"
		paramId="registrationId" paramName="studentCurricularPlanBean"
		paramProperty="registration.externalId">
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"
			key="label.back" />
	</html:link>
</p>

<logic:messagesPresent message="true" property="success">
	<ul class="nobullet list6">
		<html:messages id="messages" message="true" property="success"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<li><span class="success0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>
<logic:messagesPresent>
	<ul class="nobullet list6">
		<html:messages id="messages" bundle="ACADEMIC_OFFICE_RESOURCES">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<bean:define id="registrationId" name="studentCurricularPlanBean" property="registration.externalId" />

<fr:edit id="studentCurricularPlanBean" name="studentCurricularPlanBean" action="/manageStudentCurricularPlans.do?method=create">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 mtop2 thright" />
		<fr:property name="columnClasses" value=",,tderror1 tdclear" />
		<fr:property name="requiredMarkShown" value="true" />
	</fr:layout>
	<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= request.getAttribute("studentCurricularPlanBean").getClass().getName() %>">
		<fr:slot name="degreeCurricularPlan" required="true" layout="menu-select" key="label.manageStudentCurricularPlans.degreeCurricularPlan">
			<fr:property name="from" value="degreeCurricularPlanOptions" />
			<fr:property name="format" value="${name}" />
			<fr:property name="sortBy" value="name" />
		</fr:slot>
		<fr:slot name="executionInterval" layout="menu-select" required="true" key="label.manageStudentCurricularPlans.executionInterval">
			<fr:property name="from" value="executionIntervalOptions" />
			<fr:property name="format" value="${qualifiedName}" />
			<fr:property name="sortBy" value="beginDate=desc" />
		</fr:slot>	
	</fr:schema>
	<fr:destination name="invalid" path="/manageStudentCurricularPlans.do?method=prepareCreateInvalid"/>
	<fr:destination name="cancel" path='<%= "/manageStudentCurricularPlans.do?method=list&registrationId=" + registrationId.toString() %>'/>
</fr:edit>

