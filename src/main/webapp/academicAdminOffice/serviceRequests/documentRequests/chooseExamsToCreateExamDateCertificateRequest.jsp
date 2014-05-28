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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<h3 class="mtop15"><bean:message key="label.certificateRequests.ExamDateCertificateRequest.exams" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>


<fr:form action="/documentRequestsManagement.do?method=chooseExamsToCreateExamDateCertificateRequest">

	<fr:edit visible="false" name="documentRequestCreateBean" id="documentRequestCreateBean" />
		
	<logic:empty name="examSelectionBean" property="entries">
		<bean:message  key="label.certificateRequests.ExamDateCertificateRequest.noExamsForCurricularCourses" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</logic:empty>
	
	<logic:notEmpty name="examSelectionBean" property="entries">
		<fr:view
			name="examSelectionBean"
			property="entries"
			schema="ExamDateCertificateExamSelectionEntryBean.view">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
				<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
				<fr:property name="checkable" value="true" />
				<fr:property name="checkboxName" value="selectedExams" />
				<fr:property name="checkboxValue" value="exam.externalId" />
				<fr:property name="sortBy" value="enrolment.curricularCourse.name=asc,exam.season.season=asc"/>
			</fr:layout>
		</fr:view>
		
		<br/>
		<br/>
		<logic:notEmpty name="enrolmentsWithoutExam">
			<strong><bean:message  key="label.certificateRequests.ExamDateCertificateRequest.curricularCoursesWithoutMarkedExams" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</strong>
			<br/>	
			<fr:view
				name="enrolmentsWithoutExam"
				schema="Enrolment.view-curricular-course-name-only">
				<fr:layout name="values">
					<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
					<fr:property name="columnClasses" value=",inobullet ulmvert0,inobullet ulmvert0,," />
					<fr:property name="sortBy" value="degreeModule.name" />
					<fr:property name="htmlSeparator" value="<br/>" />
					
				</fr:layout>
			</fr:view>
		</logic:notEmpty>	
	</logic:notEmpty>
	
	
	<p class="mtop15">
		<html:submit><bean:message key="button.continue" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</p>
	
</fr:form>
