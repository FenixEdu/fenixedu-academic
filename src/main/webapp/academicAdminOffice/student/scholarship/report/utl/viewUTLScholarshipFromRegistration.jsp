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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<%@page import="org.apache.struts.action.ActionMessages"%><html:xhtml/>

	<h2><bean:message key="title.utl.scholarship.report" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
		

	<p>
		<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="externalId">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>

	<h3 class="mbottom05"><bean:message key="title.utl.scholarship.report.result" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<fr:view name="utlScholarshipBean"  >
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.student.scholarship.report.UTLScholarshipReportBeanFromRegistration">
			<fr:slot name="institutionCode" layout="null-as-label" />
			<fr:slot name="institutionName" layout="null-as-label" />
			<fr:slot name="applicationNumber" layout="null-as-label" />
			<fr:slot name="studentNumber" layout="null-as-label" />
			<fr:slot name="studentName" layout="null-as-label" />
			<fr:slot name="idDocumentType" layout="null-as-label" />
			<fr:slot name="idDocumentNumber" layout="null-as-label" />
			<fr:slot name="degreeCode" layout="null-as-label" />
			<fr:slot name="degreeName" layout="null-as-label" />
			<fr:slot name="degreeTypeName" layout="null-as-label" />
			<fr:slot name="numberOfDegreeChanges" layout="null-as-label" />
			<fr:slot name="hasMadeDegreeChangeInThisExecutionYear" layout="null-as-label" />
			<fr:slot name="currentExecutionYearBeginDate" layout="null-as-label" />
			<fr:slot name="regimen" layout="null-as-label" />
			<fr:slot name="code" layout="null-as-label" />
			<fr:slot name="firstExecutionYearInIST" layout="null-as-label" />
			<fr:slot name="numberOfStudyExecutionYearsInCurrentRegistration" layout="null-as-label" />
			<fr:slot name="numberOfCurricularYearsOnCurrentDegreeCurricularPlan" layout="null-as-label" />
			<fr:slot name="lastYearCurricularYear" layout="null-as-label" />
			<fr:slot name="lastYearEnrolledECTS" layout="null-as-label" />
			<fr:slot name="lastYearApprovedECTS" layout="null-as-label" />
			<fr:slot name="wasApprovedOnMostECTS" layout="null-as-label" />
			<fr:slot name="currentYearCurricularYear" layout="null-as-label" />
			<fr:slot name="currentYearEnrolledECTS" layout="null-as-label" />
			<fr:slot name="degreeConcluded" layout="null-as-label" />
			<fr:slot name="finalResult" layout="null-as-label" />
			<fr:slot name="gratuityAmount" layout="null-as-label" />
			<fr:slot name="numberOfMonthsInExecutionYear" layout="null-as-label" />
			<fr:slot name="firstMonthToPay" layout="null-as-label" />
			<fr:slot name="isCETQualificationOwner" layout="null-as-label" />
			<fr:slot name="isDegreeQualificationOwner" layout="null-as-label" />
			<fr:slot name="isMasterDegreeQualificationOwner" layout="null-as-label" />
			<fr:slot name="isPhdQualificationOwner" layout="null-as-label" />
			<fr:slot name="numberOfEnrolmentsYearsSinceRegistrationStart" layout="null-as-label" />
			<fr:slot name="observations" layout="null-as-label" />
		</fr:schema>
	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			<fr:property name="rowClasses" value=",,tdhl1,,,,,,"/>
		</fr:layout>
	</fr:view>

	<p>
		<html:link page="/student/scholarship/report/utlScholarshipReport.do?method=downloadRegistrationResultsSpreadsheet" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message key="link.utl.scholarship.report.download.spreadsheet" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>
