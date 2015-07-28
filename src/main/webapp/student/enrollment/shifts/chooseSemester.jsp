<%--

    Copyright © 2002 Instituto Superior Técnico

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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<h3>${fr:message('resources.StudentResources', 'label.enrolment.choose.semester')}</h3>
<c:set var="registrationOID" value="${registrationOID}"/>

<fr:form action="/studentShiftEnrollmentManager.do?method=chooseExecutionPeriod&registrationOID=${registrationOID}">
	<fr:edit id="chooseSemester" name="chooseSemester">
		<fr:schema bundle="STUDENT_RESOURCES"
			type="org.fenixedu.academic.ui.struts.action.student.enrollment.bolonha.BolonhaStudentEnrollmentDispatchAction$ChooseEnrolmentSemester">
			<fr:slot name="chosenSemester" layout="menu-select-postback" key="label.semester" required="true">
				<fr:property name="from" value="semestersForClasses" />
				<fr:property name="format" value="\${name} - \${executionYear.name}" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
        <fr:destination name="postBack" path="/studentShiftEnrollmentManager.do?method=chooseExecutionPeriod&registrationOID=${registrationOID}" />
	</fr:edit>      
</fr:form>