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

<%@ page import="net.sourceforge.fenixedu.dataTransferObject.degreeAdministrativeOffice.gradeSubmission.MarkSheetEnrolmentEvaluationBean" %>
<%@ page import="net.sourceforge.fenixedu.domain.Enrolment" %>

<html:xhtml/>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.curriculum.validation.set.evaluations" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="studentCurricularPlan" name="studentCurricularPlan" type="net.sourceforge.fenixedu.domain.StudentCurricularPlan"/>
<bean:define id="studentCurricularPlanId" name="studentCurricularPlan" property="externalId"/>
<bean:define id="executionSemesterId" name="executionSemester" property="externalId" />

<p>
	<html:link page="<%= "/curriculumValidation.do?method=prepareCurriculumValidation&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>">
		<bean:message key="label.back" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
</p>
<academic:allowed operation="ENROLMENT_WITHOUT_RULES" program="<%= studentCurricularPlan.getRegistration().getDegree() %>">
	<p>
		<html:link page="<%= "/curriculumValidation.do?method=prepareStudentEnrolment&amp;studentCurricularPlanId=" + studentCurricularPlanId  + "&amp;executionSemesterId=" + executionSemesterId %>">
			« <bean:message key="label.curriculum.validation.student.enrolment.without.rules" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</p>
</academic:allowed>




<logic:equal name="studentCurriculumValidationAllowed" value="false">
	<bean:message key="message.curriculum.validation.not.allowed" bundle="ACADEMIC_OFFICE_RESOURCES" />
</logic:equal>

<logic:equal name="studentCurriculumValidationAllowed" value="true"> 
<h3 class="mtop15 mbottom05"><strong><bean:message key="label.student.setEvaluations.chooseExecutionPeriod" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h3>

<fr:form action="<%= "/curriculumValidation.do?method=prepareSetEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId %>">
	<fr:edit id="student.enrolment.bean" name="bolonhaStudentEnrollmentBean" visible="false" />
	
	<fr:edit id="student.enrolment.bean.execution.semester"
			 name="bolonhaStudentEnrollmentBean"
			 schema="bolonha.student.enrolment.choose.executionPeriod">
		<fr:destination name="postBack" path="<%= "/curriculumValidation.do?method=prepareSetEvaluations&amp;studentCurricularPlanId=" + studentCurricularPlanId  %>" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thright thlight mtop025 mbottom05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
</fr:form>

<style>
	th.evaluations {
	padding: 0;
	}
	td.evaluations {
	padding: 0;
	}
	td.evaluations table {
	margin: 0;
	border-collapse: collapse;
	}
	td.evaluations table td {
	padding-left: 0;
	padding-right: 0;
	border-left: none;
	border-right: none;
	}
	td.evaluations table tr th {
	/*display: none;*/
	padding: 0.25em;
	border-left: none;
	border-right: none;
	}
	td.evaluations table tr td {
	padding: 0.5em 0.25em;
	text-align: center;
	}
	table tr td table.tstyle4 tr td.eval_type {
	width: 175px !important;
	}
	td.evaluations table {
	}
	.borderBottomNone td {
	border-bottom: none;
	}
	.borderTopNone td {
	border-top: none;
	}
</style>


<h3 class="mtop15 mbottom05"><bean:message key="label.final.evaluations" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>

<table class="tstyle4 tdcenter thlight mtop05">
	<thead>
		<tr>
			<th style="width: 150px;"><bean:message key="label.set.evaluation.curricular.course.name" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
			<th style="width: 70px;"><bean:message key="label.set.evaluation.enrolment.state" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
			<th style="width: 80px;"><bean:message key="label.set.evaluation.enrolment.weight" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
			<th class="evaluations"></th>
		</tr>
	</thead>
	
	<tbody>
	
		<logic:iterate id="finalEntries" name="finalEntriesList">
		
			<tr>
				<td>
					<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) finalEntries).get(0).getName() %>
				</td>
				<td>
					<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) finalEntries).get(0).getEnrolmentState() %>
				</td>
				<td>
					<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) finalEntries).get(0).getWeight() %>
				</td>
				
				<td class="evaluations separator">
					<fr:view 	name="finalEntries"
								schema="set.evaluation.curriculum.entry.read.only"
								layout="tabular">
						<fr:layout>
							<fr:property name="classes" value="tstyle4 tdcenter thlight mtop05"/>
					        <fr:property name="columnClasses" value="eval_type,,,,,"/>
					        <fr:property name="rowClasses" value="borderTopNone,,,borderBottomNone" />
					        <fr:property name="display-headers" value="false" />				                
						</fr:layout>			 
					</fr:view>					
				</td>
				<td >
					<% Enrolment enrolment =  ((java.util.List<MarkSheetEnrolmentEvaluationBean>) finalEntries).get(0).getEnrolment(); %>
					<html:link page="<%= String.format("/curriculumValidation.do?method=prepareEditEvaluation&amp;enrolmentId=%s&amp;studentCurricularPlanId=%s&amp;executionSemesterId=%s", enrolment.getExternalId(), studentCurricularPlanId, executionSemesterId) %>">
						<bean:message key="label.edit" bundle="ACADEMIC_OFFICE_RESOURCES" />
					</html:link>
				</td>
			</tr>
		
		</logic:iterate>
	</tbody>
</table>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="set.evaluation.action.messages">
	<p><span class="success0"><bean:write name="message"/></span></p>
</html:messages>

<fr:hasMessages type="validation">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES" property="grade-messages">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>


<logic:notEmpty name="entriesList">

<h3 class="mtop15 mbottom05"><bean:message key="label.courses.not.evaluated" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>


<fr:form action="<%= String.format("/curriculumValidation.do?method=setEvaluations&amp;studentCurricularPlanId=%s&amp;executionSemesterId=%s", studentCurricularPlanId, executionSemesterId) %>">
	<table class="tstyle4 tdcenter thlight mtop05">
		<thead>
			<tr>
				<th style="width: 150px;"><bean:message key="label.set.evaluation.curricular.course.name" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th style="width: 70px;"><bean:message key="label.set.evaluation.enrolment.state" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th style="width: 80px;"><bean:message key="label.set.evaluation.enrolment.weight" bundle="ACADEMIC_OFFICE_RESOURCES" /></th>
				<th></th>
			</tr>
		</thead>
		
		<tbody>
			<% int i = 0; %>
			<logic:iterate id="entries" name="entriesList">
				<%
					request.setAttribute("markSheetBeanForWeight", ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0));
				%>
			
				<bean:define id="markSheetBeanForWeight" name="markSheetBeanForWeight" />
				
				<tr>
					<td>
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getName() %>
						(<bean:message key="label.grade.scale" bundle="ACADEMIC_OFFICE_RESOURCES" />
						<%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolment().getGradeScale().getDescription() %>)
					</td>
					<td>
						<bean:define id="enrolmentId"><%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolment().getExternalId() %></bean:define>
						<p><%= ((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).getEnrolmentState() %></p>
						
						<% if(((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).isPossibleToUnEnrolEnrolment()) { %>
							<p>
							<html:link page="<%= String.format("/curriculumValidation.do?method=unEnrol&amp;studentCurricularPlanId=%s&amp;executionSemesterId=%s&amp;enrolmentId=%s", studentCurricularPlanId, executionSemesterId, enrolmentId) %>">
								<bean:message key="label.curriculum.validation.unenrol" bundle="ACADEMIC_OFFICE_RESOURCES" />
							</html:link>
							</p>
						<% } else  if(((java.util.List<MarkSheetEnrolmentEvaluationBean>) entries).get(0).isEnrolmentBeMarkedAsEnroled()) { %>
							<p>
							<html:link page="<%= String.format("/curriculumValidation.do?method=markAsTemporaryEnrolled&amp;studentCurricularPlanId=%s&amp;executionSemesterId=%s&amp;enrolmentId=%s", studentCurricularPlanId, executionSemesterId, enrolmentId) %>">
								<bean:message key="label.curriculum.validation.mark.enrollment.as.temporary" bundle="ACADEMIC_OFFICE_RESOURCES" />
							</html:link>
							</p>
						<% } %>
					</td>
					<td>
						<fr:edit id="<%= "set.evaluation.form.weight" + i %>"
								name="markSheetBeanForWeight"
								slot="weight">
							<fr:property name="flow">
								<fr:property name="size" value="4" />
							</fr:property>
						</fr:edit>
					</td>
					
					<td class="evaluations separator">
						<fr:edit 	id="<%= "set.evaluations.form." + i %>"
									name="entries"
									schema="set.evaluation.curriculum.entry"
									layout="tabular-editable">
							<fr:layout>
								<fr:property name="classes" value="tstyle4 tdcenter thlight"/>
						        <fr:property name="columnClasses" value="eval_type,,,,,,"/>
						        <fr:property name="rowClasses" value="borderTopNone,,,borderBottomNone" />
						        <fr:property name="display-headers" value="false" />				                
							</fr:layout>			 
						</fr:edit>
					</td>
				</tr>
				
				<% i++; %>
			</logic:iterate>
			
		</tbody>
	</table>

	<fr:edit id="set.evaluations.form" name="entriesList" />
	<fr:edit id="student.enrolment.bean" name="bolonhaStudentEnrollmentBean" visible="false"/>
	
	<html:submit><bean:message key="label.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
</fr:form>

</logic:notEmpty>


<logic:empty name="entriesList">
	<em><bean:message key="message.curriculum.evaluations.empty" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
</logic:empty>
</logic:equal>
