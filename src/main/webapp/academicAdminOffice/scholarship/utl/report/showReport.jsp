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

<h2><bean:message key="title.academicAdminOffice.scholarship.utl.report" bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>	

<bean:define id="correctStudentLines" name="correctStudentLines" />
<bean:define id="erroneousStudentLines" name="erroneousStudentLines" />

<logic:notEmpty name="erroneousStudentLines">
	<p><strong><bean:message key="title.academicAdminOffice.scholarship.utl.report.failed.student.lines" bundle="ACADEMIC_OFFICE_RESOURCES" />
	
	<fr:view name="erroneousStudentLines">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.StudentLine" 
			bundle="ACADEMIC_OFFICE_RESOURCES" >
			
			<fr:slot name="institutionCode" />
			<fr:slot name="institutionName" />
			<fr:slot name="studentName" />
			<fr:slot name="candidacyNumber" />
			<fr:slot name="studentNumberForPrint" />
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>
</logic:notEmpty>


<p><strong><bean:message key="title.academicAdminOffice.scholarship.utl.report.student.lines" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<logic:empty name="correctStudentLines">
	<p>
		<em><bean:message key="label.academicAdminOffice.scholarship.utl.report.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
	</p>
</logic:empty>

<logic:notEmpty name="correctStudentLines">
	
	<p>
		<fr:form action="/reportStudentsUTLCandidates.do?method=exportReport" >
			<fr:edit id="report" name="report" visible="false" />
			<html:submit><bean:message key="label.export" bundle="APPLICATION_RESOURCES" /></html:submit>
		</fr:form>
	</p>
	<p>
		<fr:form action="/reportStudentsUTLCandidates.do?method=exportErrors" >
			<fr:edit id="report" name="report" visible="false" />
			<html:submit><bean:message key="label.errors" bundle="APPLICATION_RESOURCES" /></html:submit>
		</fr:form>
	</p>


	<fr:view name="correctStudentLines">
		<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.administrativeOffice.scholarship.utl.report.StudentLine" 
			bundle="ACADEMIC_OFFICE_RESOURCES" >
			
			<fr:slot name="institutionCode" />
			<fr:slot name="institutionName" />
			<fr:slot name="candidacyNumber" />
			<fr:slot name="studentNumberForPrint" />
			<fr:slot name="studentName" />
			<fr:slot name="documentTypeName" />
			<fr:slot name="documentNumber" />
			<fr:slot name="degreeCode" />
			<fr:slot name="degreeName" />
			<fr:slot name="degreeTypeName" />
			<fr:slot name="countNumberOfDegreeChanges" />
			<fr:slot name="hasMadeDegreeChange" />
			<fr:slot name="firstEnrolmentOnCurrentExecutionYear" />
			<fr:slot name="regime" />
			<fr:slot name="regimeCode" />
			<fr:slot name="firstRegistrationExecutionYear" />
			<fr:slot name="countNumberOfEnrolmentsYearsSinceRegistrationStart" />
			<fr:slot name="numberOfDegreeCurricularYears" />
			<fr:slot name="curricularYearOneYearAgo" />
			<fr:slot name="numberOfEnrolledEctsOneYearAgo" />
			<fr:slot name="numberOfApprovedEctsOneYearAgo" />
			<fr:slot name="studentHadPerformanceLastYear" />
			<fr:slot name="curricularYearInCurrentYear" />
			<fr:slot name="numberOfEnrolledECTS" />
			<fr:slot name="degreeConclusionValue" />
			<fr:slot name="finalResultValue" />
			<fr:slot name="gratuityAmount" />
			<fr:slot name="numberOfMonthsExecutionYear" />
			<fr:slot name="firstMonthOfPayment" />
			<fr:slot name="ownerOfCETQualification" />
			<fr:slot name="degreeQualificationOwner" />
			<fr:slot name="masterQualificationOwner" />
			<fr:slot name="phdQualificationOwner" />
			<fr:slot name="ownerOfCollegeQualification" />
			<fr:slot name="observations" />
			<fr:slot name="lastEnrolledExecutionYear" />
			<fr:slot name="nif" />
		</fr:schema>

		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>

</logic:notEmpty>
