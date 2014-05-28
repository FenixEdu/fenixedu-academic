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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>

<html:xhtml/>

<%! 
	private static String f(String format, Object ... args) {
    	return String.format(format, args);
	}

%>



<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>

<h2><bean:message key="title.curriculum.validation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="studentCurricularPlan" name="studentCurricularPlan" type="net.sourceforge.fenixedu.domain.StudentCurricularPlan"/>
<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>
<bean:define id="registrationId" name="studentCurricularPlan" property="registration.externalId" />


<html:link page="<%= "/student.do?method=visualizeRegistration&amp;registrationId=" + registrationId %>" >
	<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
</html:link>


<ul class="mtop15">
	<academic:allowed operation="ENROLMENT_WITHOUT_RULES" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">
		<li>
			<html:link page="<%= "/curriculumValidation.do?method=prepareStudentEnrolment&amp;studentCurricularPlanId=" + studentCurricularPlanId %>">
				<bean:message key="label.curriculum.validation.student.enrolment.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</li>
	</academic:allowed>
	<academic:allowed operation="MANAGE_MARKSHEETS" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">
		<li>
			<html:link page="<%= "/curriculumValidation.do?method=prepareSetEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
				<bean:message key="label.curriculum.validation.set.evaluations" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</li>
	</academic:allowed>
	<li>
		<html:link page="<%= "/curriculumValidation.do?method=prepareEditEndStageDate&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
			<bean:message key="label.curriculum.validation.set.end.stage.date" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
		
	<li>
		<html:link page="<%= f("/curriculumValidationDocumentRequestsManagement.do?method=listDocuments&amp;studentCurricularPlanId=%s", studentCurricularPlanId) %>">
			<bean:message key="label.curriculum.validation.print.documents" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</li>
	
	<academic:allowed operation="REGISTRATION_CONCLUSION_CURRICULUM_VALIDATION" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">		
		<academic:allowed operation="MANAGE_CONCLUSION" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">
		<li>
			<html:link page="<%= f("/curriculumValidation.do?method=prepareRegistrationConclusionProcess&amp;studentCurricularPlanId=%s", studentCurricularPlanId) %>">
				<bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</li>
		</academic:allowed>
	</academic:allowed>
	
</ul>