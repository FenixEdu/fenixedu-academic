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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<%@page import="net.sourceforge.fenixedu.util.EnrolmentEvaluationState"%>

<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet"/></h2>

<p class="breadcumbs"><span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.one"/></span> &gt; <span class="actual"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.two"/></span> &gt; <span><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyOldMarkSheet.step.three"/></span></p>

<fr:view name="rectifyBean" schema="oldMarkSheet.curricular.course.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value=",,"/>
	</fr:layout>
</fr:view>


<fr:form action="/rectifyOldMarkSheet.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" name="markSheetManagementForm" property="method" value="rectifyMarkSheetStepOneByStudentNumber" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.epID" name="markSheetManagementForm" property="epID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dID" name="markSheetManagementForm" property="dID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.dcpID" name="markSheetManagementForm" property="dcpID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ccID" name="markSheetManagementForm" property="ccID"  />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.msID" name="markSheetManagementForm" property="msID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.tn" name="markSheetManagementForm" property="tn" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.ed" name="markSheetManagementForm" property="ed"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mss" name="markSheetManagementForm" property="mss" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.mst" name="markSheetManagementForm" property="mst" />
	
	<p>
		<strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.rectifyMarkSheet.chooseStudent"/></strong>
	</p>
	
	<table class="tstyle4 thlight mtop05">
		<tr>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.number"/>
			</th>
		
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.name"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.evaluationDate"/>
			</th>
			<th>
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.student.grade"/>
			</th>		
		</tr>
		
		<bean:define id="url" name="rectifyBean" property="url" />
		
		<logic:iterate id="evaluation" name="enrolmentEvaluations" type="net.sourceforge.fenixedu.domain.EnrolmentEvaluation" >
			<bean:define id="evaluationID" name="evaluation" property="externalId"/>
			<bean:define id="studentNumber" name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>		
			<tr>
				<td>
					<% if(evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ) || evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)){ %>
						<html:link action='<%= "/rectifyOldMarkSheet.do?method=rectifyMarkSheetStepOneByEvaluation&evaluationID=" +  evaluationID + url %>'>
							<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>
						</html:link>					
					<% } else { %>
						<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.number"/>					
					<% }  %>						
				</td>
				<td>
					<% if(evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ) || evaluation.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)){ %>
						<html:link action='<%= "/rectifyOldMarkSheet.do?method=rectifyMarkSheetStepOneByEvaluation&evaluationID=" +  evaluationID  + url %>'>
							<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.person.name"/>		
						</html:link>										
					<% } else { %>
						<bean:write name="evaluation" property="enrolment.studentCurricularPlan.student.person.name"/>		
					<% }  %>						
				</td>
				<td>
					<logic:present name="evaluation" property="examDate">
						<dt:format pattern="dd/MM/yyyy">
							<bean:write name="evaluation" property="examDate.time"/>
						</dt:format>
					</logic:present>
				</td>
				<td>
					<bean:write name="evaluation" property="gradeValue"/>
				</td>
			</tr>
		</logic:iterate>
	</table>
	
	<p>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton" onclick="this.form.method.value='prepareSearchMarkSheetFilled';this.form.submit();"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.back"/></html:cancel>
	</p>
</fr:form>
