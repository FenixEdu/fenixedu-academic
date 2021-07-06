<%@ page import="org.fenixedu.academic.domain.Degree" %>
<%@ page import="org.fenixedu.academic.domain.ExecutionSemester" %>
<%@ page import="org.fenixedu.academic.domain.MarkSheet" %>
<%@ page import="org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean" %><%--

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
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<ul>
	<li><html:link action="/createMarkSheet.do?method=prepareCreateMarkSheet"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link></li>
	<li><html:link action="/markSheetManagement.do?method=choosePrinterMarkSheetsWeb"> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.print.web.markSheets"/></html:link></li>
	<!-- TODO AA logic:equal name="LOGGED_USER_ATTRIBUTE" property="person.employee.administrativeOffice.administrativeOfficeType" value="DEGREE"-->
		<li><html:link action="/markSheetSendMail.do?method=prepareSearchSendMail"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.send.mail"/></html:link></li>
	<!-- /logic:equal-->
</ul>

<logic:messagesPresent message="true">
	<ul>
		<html:messages bundle="ACADEMIC_OFFICE_RESOURCES" id="messages" message="true">
			<li><span class="error0"><bean:write name="messages" /></span></li>
		</html:messages>
	</ul>
</logic:messagesPresent>

<h3 class="mbottom025"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.searchMarkSheet"/></h3>

<fr:edit id="search"
		 name="edit"
		 type="org.fenixedu.academic.dto.degreeAdministrativeOffice.gradeSubmission.MarkSheetManagementSearchBean"
		 schema="markSheet.search"
		 action="/markSheetManagement.do?method=searchMarkSheets">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
	</fr:layout>

	<fr:destination name="postBack" path="/markSheetManagement.do?method=prepareSearchMarkSheetPostBack"/>
	<fr:destination name="invalid" path="/markSheetManagement.do?method=prepareSearchMarkSheetInvalid"/>
	<fr:destination name="cancel" path="/markSheetManagement.do?method=prepareSearchMarkSheet" />
</fr:edit>


<logic:present name="searchResult">
<div class="mtop15">
	<p>
		<strong><bean:write name="edit" property="curricularCourseName"/></strong>
		
		<bean:define id="executionPeriodID" name="edit" property="executionPeriod.externalId" />
		<bean:define id="degreeID" name="edit" property="degree.externalId" />
		<bean:define id="degreeCurricularPlanID" name="edit" property="degreeCurricularPlan.externalId" />
		<bean:define id="curricularCourseID" name="edit" property="curricularCourse.externalId" />
	
		<bean:define id="url" name="url"/>
		<logic:equal name="edit" property="curricularCourse.canCreateMarkSheet" value="true">
		 - 
			<html:link action='<%= "/createMarkSheet.do?method=prepareCreateMarkSheetFilled" + url %>'><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.createMarkSheet"/></html:link>
		</logic:equal>
	
		<logic:empty name="searchResult">
			<em><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.noMarkSheetsFound"/></em>
		</logic:empty>
	</p>
	
	<logic:notEmpty name="searchResult">

			<logic:iterate id="entry" name="searchResult" >
				<bean:define id="evaluationSeason" name="entry" property="key"/>
				<bean:define id="markSheetResult" name="entry" property="value"/>

				<div style="margin-left: 2em;">

					<p>
						<strong><bean:write name="evaluationSeason" property="name.content" /></strong>
						<logic:equal name="markSheetResult" property="showStatistics" value="true">
							(<bean:write name="markSheetResult" property="numberOfEnroledStudents"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.of"/> <bean:write name="markSheetResult" property="totalNumberOfStudents"/> <bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.markSheet.evaluatedStudents"/>)
						</logic:equal>
					</p>
				
					<fr:view name="markSheetResult" property="markSheetsSortedByEvaluationDate" schema="markSheet.search.result.list">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle4" />
							<fr:property name="link(view)" value='<%= "/markSheetManagement.do?method=viewMarkSheet" + url %>' />
							<fr:property name="key(view)" value="label.markSheet.view" />
							<fr:property name="param(view)" value="externalId/msID" />
							<fr:property name="bundle(view)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="order(view)" value="1" />
							
							<fr:property name="link(rectify)" value='<%= "/rectifyMarkSheet.do?method=prepareRectifyMarkSheet" + url %>' />
							<fr:property name="key(rectify)" value="label.markSheet.rectify" />
							<fr:property name="param(rectify)" value="externalId/msID" />
							<fr:property name="bundle(rectify)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(rectify)" value="canRectify" />
							<fr:property name="order(rectify)" value="2" />
							
							<fr:property name="link(edit)" value='<%= "/editMarkSheet.do?method=prepareEditMarkSheet" + url %>' />
							<fr:property name="key(edit)" value="label.markSheet.edit" />
							<fr:property name="param(edit)" value="externalId/msID" />
							<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(edit)" value="canEdit" />
							<fr:property name="order(edit)" value="3" />

							<fr:property name="link(delete)" value='<%= "/markSheetManagement.do?method=prepareDeleteMarkSheet" + url %>' />
							<fr:property name="key(delete)" value="label.markSheet.remove" />
							<fr:property name="param(delete)" value="externalId/msID" />
							<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(delete)" value="canEdit" />
							<fr:property name="order(delete)" value="4" />

							<fr:property name="link(confirm)" value='<%= "/markSheetManagement.do?method=prepareConfirmMarkSheet" + url %>' />
							<fr:property name="key(confirm)" value="label.markSheet.confirm" />
							<fr:property name="param(confirm)" value="externalId/msID" />
							<fr:property name="bundle(confirm)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(confirm)" value="canConfirm" />
							<fr:property name="order(confirm)" value="5" />

							<fr:property name="link(removeGrades)" value='<%= "/markSheetManagement.do?method=listMarkSheet" + url %>' />
							<fr:property name="key(removeGrades)" value="label.remove.grades" />
							<fr:property name="param(removeGrades)" value="externalId/msID" />
							<fr:property name="bundle(removeGrades)" value="ACADEMIC_OFFICE_RESOURCES" />
							<fr:property name="visibleIf(removeGrades)" value="canRemoveGrades" />
							<fr:property name="order(removeGrades)" value="6" />																																			
						</fr:layout>
					</fr:view>
				</div>
			</logic:iterate>

	</logic:notEmpty>
</div>
</logic:present>

<%
	final MarkSheetManagementSearchBean markSheetManagementSearchBean = (MarkSheetManagementSearchBean) request.getAttribute("edit");
	final ExecutionSemester executionSemester = markSheetManagementSearchBean.getExecutionPeriod();
	final Degree degree = markSheetManagementSearchBean.getDegree();
	if (executionSemester != null && degree == null) {
%>
<div id="signedMarkSheets" style="display: none; margin-top: 50px;">
<h4>
	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.signed.pendingConfirmation"/>
</h4>
<table class="tstyle4 table">
	<thead>
		<tr>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.degree"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.curricularCourse"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.responsible"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.season"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.evaluationDate"/></th>
			<th><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.mark.sheet.studentCount"/></th>
			<th></th>
		</tr>
	</thead>
	<tbody>
<%
		boolean hasSignedMarkSheets = false;
		for (final MarkSheet markSheet : executionSemester.getMarkSheetsSet()) {
			if (!markSheet.isConfirmed() && markSheet.getSignedMarkSheet() != null) {
				hasSignedMarkSheets = true;
%>
		<tr>
			<td><%= markSheet.getDegreeCurricularPlanName() %></td>
			<td><%= markSheet.getCurricularCourseName() %></td>
			<td><%= markSheet.getResponsibleTeacher().getPerson().getUsername() %></td>
			<td><%= markSheet.getEvaluationSeason().getName().getContent() %></td>
			<td><%= markSheet.getEvaluationDateDateTime().toString("yyyy-MM-dd") %></td>
			<td><%= markSheet.getEnrolmentEvaluationsSet().size() %></td>
			<td>
				<a href="<%= request.getContextPath() + "/academicAdministration/markSheetManagement.do?method=prepareConfirmMarkSheet"
						+ "&ccID=" + markSheet.getCurricularCourse().getExternalId()
						+ "&msID=" + markSheet.getExternalId()
						+ "&dID=" + markSheet.getCurricularCourse().getDegree().getExternalId()
						+ "&epID=" + markSheet.getExecutionPeriod().getExternalId()
						+ "&dcpID=" + markSheet.getCurricularCourse().getDegreeCurricularPlan().getExternalId()
				 		%>">
					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.confirm"/>
				</a>
			</td>
		</tr>
<%
			}
		}
%>
	</tbody>
</table>
</div>
<%
	if (hasSignedMarkSheets) {
%>
<script>
	document.getElementById("signedMarkSheets").style.display = 'block';
</script>
	<%
        }
    %>

<%
	}
%>
