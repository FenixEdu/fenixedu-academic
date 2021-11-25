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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/academic" prefix="academic" %>
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@page import="org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear"%>
<%@page import="java.util.SortedSet"%>
<%@ page import="java.util.TreeSet" %>

<html:xhtml/>

	<bean:define id="registration" name="registration" scope="request" type="org.fenixedu.academic.domain.student.Registration"/>

	<div style="float: right;">
		<bean:define id="personID" name="registration" property="student.person.username"/>
		<html:img align="middle" src="<%= request.getContextPath() + "/user/photo/" + personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" styleClass="showphoto"/>
	</div>

	<h2><bean:message key="label.visualizeRegistration" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
	

	<p>
		<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.externalId">
			<bean:message key="link.student.backToStudentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
	</p>

	

	
	<p class="mvert2">
		<span class="showpersonid">
		<bean:message key="label.student" bundle="ACADEMIC_OFFICE_RESOURCES"/>: 
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation.short">
				<fr:layout name="flow">
					<fr:property name="labelExcluded" value="true"/>
				</fr:layout>
			</fr:view>
		</span>
	</p>
	
	
	
	<logic:messagesPresent message="true">
		<ul class="list7 mtop2 warning0" style="list-style: none;">
			<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
				<li>
					<span><!-- Error messages go here --><bean:write name="message" /></span>
				</li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<html:messages id="error" bundle="ACADEMIC_OFFICE_RESOURCES">
		<ul class="list7 mtop2">
			<li><span class="error"><bean:write name="error" /></span></li>
		</ul>
	</html:messages>







	<logic:present name="registration" property="ingressionType">
		<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	</logic:present>
	
	<logic:notPresent name="registration" property="ingressionType">
		<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	</logic:notPresent>
	<bean:define id="registration" name="registration" type="org.fenixedu.academic.domain.student.Registration"/>



<table>
	<tr>
		<td>
			<%-- Registration Details --%>
			<jsp:include page="registrationDetailsTable.jsp"/>
		</td>
		
		<td style="vertical-align: top; padding-top: 1em;">
			<academic:allowed operation="MANAGE_REGISTRATIONS" program="<%= registration.getDegree() %>">
			<p class="mtop0 pleft1 asd">
				<span class="dblock pbottom03">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/registration.do?method=prepareViewRegistrationCurriculum" paramId="registrationID" paramName="registration" paramProperty="externalId">
						<bean:message key="link.registration.viewCurriculum" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
				<span class="dblock pbottom03">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/manageRegistrationState.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="externalId">
						<bean:message key="link.student.manageRegistrationState" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
				<span class="dblock pbottom03">
						<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
						<html:link page="/manageIngression.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="externalId">
							<bean:message key="link.student.manageIngressionAndAgreement" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</html:link>
				</span>
				<span class="dblock pbottom03">
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/manageRegistrationStartDates.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="externalId">
						<bean:message key="link.student.manageRegistrationStartDates" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>		
				<logic:equal name="registration" property="degreeType.name" value="BOLONHA_ADVANCED_FORMATION_DIPLOMA">
					<span class="dblock pbottom03">	
						<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
						<html:link page="/manageEnrolmentModel.do?method=prepare" paramId="registrationID" paramName="registration" paramProperty="externalId">
							<bean:message key="link.student.manageEnrolmentModel" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</html:link>
					</span>
				</logic:equal>
				<logic:equal name="registration" property="registrationProtocol.enrolmentByStudentAllowed" value="false">
					<span class="dblock pbottom03">	
						<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
						<html:link page="/manageExternalRegistrationData.do?method=prepare" paramId="registrationId" paramName="registration" paramProperty="externalId">
							<bean:message key="link.student.manageExternalRegistrationData" bundle="ACADEMIC_OFFICE_RESOURCES"/>
						</html:link>
					</span>	
				</logic:equal>
				<academic:allowed operation="MANAGE_CONCLUSION" program="<%= registration.getDegree() %>">
					<logic:equal name="registration" property="qualifiedToRegistrationConclusionProcess" value="true">
						<span class="dblock pbottom03">	
							<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
							<html:link page="/registration.do?method=prepareRegistrationConclusionProcess" paramId="registrationId" paramName="registration" paramProperty="externalId">
								<bean:message key="student.registrationConclusionProcess" bundle="ACADEMIC_OFFICE_RESOURCES"/>
							</html:link>
						</span>	
					</logic:equal>
				</academic:allowed>
				<span class="dblock pbottom03">	
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/registration.do?method=showRegimes" paramId="registrationId" paramName="registration" paramProperty="externalId">
						<bean:message key="student.regimes" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
				<academic:allowed operation="STUDENT_ENROLMENTS" program="<%= registration.getDegree() %>">
				<span class="dblock pbottom03">	
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/registration.do?method=viewAttends" paramId="registrationId" paramName="registration" paramProperty="externalId">
						<bean:message key="student.registrationViewAttends" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
				</academic:allowed>		
				<span class="dblock pbottom03">	
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/student/scholarship/report/utlScholarshipReport.do?method=viewResultsOnRegistration" paramId="registrationId" paramName="registration" paramProperty="externalId">
						<bean:message key="link.utl.scholarship.report.view" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
				<modular:intersect location="registration.process" position="actions">
					<modular:arg key="registrationId" value="<%= registration.getExternalId() %>"></modular:arg>
				</modular:intersect>
			</p>
			</academic:allowed>
		</td>
	</tr>
</table>

	<logic:notEmpty name="registration" property="phdIndividualProgramProcess">
		<academic:allowed operation="MANAGE_PHD_PROCESSES" program="<%= registration.getPhdIndividualProgramProcess().getPhdProgram() %>">
		
		<%-- Phd Individual Program Process --%>
		<bean:define id="phdProcess" name="registration" property="phdIndividualProgramProcess" />
		<h3 class="mbottom05 mtop25 separator2"><bean:message key="PhdIndividualProgramProcess" bundle="PHD_RESOURCES"/></h3>
		<table>
			<tr>
				<td>
					<fr:view schema="AcademicAdminOffice.PhdIndividualProgramProcess.view" name="phdProcess">
						<fr:layout name="tabular">
							<fr:property name="classes" value="tstyle2 thlight mtop15 thleft" />
						</fr:layout>
					</fr:view>
				</td>
			</tr>
		</table>
		
		<p>
			<html:link target="_blank" page="/phdIndividualProgramProcess.do?method=viewProcess"  paramId="processId" paramName="phdProcess" paramProperty="externalId">
				<bean:message key="link.org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess.view" bundle="PHD_RESOURCES" />
			</html:link>
		</p>
		</academic:allowed>
	</logic:notEmpty>

	<%-- Registration Data by Execution Year --%>
	<h3 class="mbottom05 mtop25 separator2"><bean:message key="title.registrationDataByExecutionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:empty name="registration" property="registrationDataByExecutionYear">
		<bean:message key="label.registrationDataByExecutionYear.noResults" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="registration" property="registrationDataByExecutionYear">
		<%
			final SortedSet<RegistrationDataByExecutionYear> byExecutionYears = new TreeSet();
			byExecutionYears.addAll(registration.getRegistrationDataByExecutionYearSet());
		%>
			<table class="tstyle2 thright thlight thcenter" style="width: 100%;">
				<thead>
					<tr>
						<th class="acenter"><bean:message key="label.executionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th class="acenter"><bean:message key="label.enrolmentDate" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th class="acenter"><bean:message key="label.enrolment.limit" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th class="acenter"><bean:message key="label.allowed.semesters.for.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th class="acenter"><bean:message key="label.payment.plan" bundle="ACADEMIC_OFFICE_RESOURCES"/></th>
						<th class="acenter"></th>
					</tr>
				</thead>
				<tbody>
					<%
						for (final RegistrationDataByExecutionYear byExecutionYear : byExecutionYears) {
					%>
							<tr>
								<td class="acenter"><%= byExecutionYear.getExecutionYear().getQualifiedName() %></td>
								<td class="acenter"><%= byExecutionYear.getEnrolmentDate() == null ? "-" : byExecutionYear.getEnrolmentDate().toString("yyyy-MM-dd") %></td>
								<td class="acenter"><%= byExecutionYear.getMaxCreditsPerYear() == null ? "100%" : (byExecutionYear.getMaxCreditsPerYear() + " ECTS")%></td>
								<td class="acenter">
									<% if (byExecutionYear.getAllowedSemesterForEnrolments() == null) { %>
										<bean:message key="label.both.semesters" bundle="ACADEMIC_OFFICE_RESOURCES"/>
									<% } else { %>
										<%= byExecutionYear.getAllowedSemesterForEnrolments().getQualifiedName() %>
									<% } %>
								</td>
								<td class="acenter"><%= byExecutionYear.getEventTemplate() == null ? "-" : byExecutionYear.getEventTemplate().getTitle().getContent() %></td>
								<td class="acenter">
									<a href="<%= "/academicAdministration/manageRegistrationDataByExecutionYear.do?registrationDataByExecutionYearId="
										+ byExecutionYear.getExternalId()
										+ "&method=prepareEdit" %>">
										<bean:message key="label.edit" bundle="ACADEMIC_OFFICE_RESOURCES"/>
									</a>
								</td>
							</tr>
					<% } %>
				</tbody>
			</table>
	</logic:notEmpty>
	
	
	<%-- Curricular Plans --%>
	
	<academic:allowed operation="MANAGE_REGISTRATIONS" program="<%= registration.getDegree() %>">
	<h3 class="mbottom05 mtop25 separator2"><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	
	<fr:view name="registration" property="sortedStudentCurricularPlans" schema="student.studentCurricularPlans" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
			<fr:property name="groupLinks" value="false"/>
			
			<fr:property name="linkFormat(enrol)" value="/studentEnrolments.do?method=prepare&amp;scpID=${externalId}" />
			<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
			<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(enrol)" value="true"/>      
			<fr:property name="visibleIf(enrol)" value="allowedToManageEnrolments" />
			<fr:property name="order(enrol)" value="1"/>     					
			
			<fr:property name="linkFormat(dismissal)" value="/studentDismissals.do?method=manage&amp;scpID=${externalId}" />
			<fr:property name="key(dismissal)" value="link.student.dismissal.management"/>
			<fr:property name="bundle(dismissal)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(dismissal)" value="true"/>      	
			<fr:property name="order(dismissal)" value="2"/>
			<fr:property name="visibleIf(dismissal)" value="allowedToManageEquivalencies"/>
			
			<fr:property name="linkFormat(createAccountingEvents)" value="/accountingEventsManagement.do?method=prepare&amp;scpID=${externalId}" />
			<fr:property name="key(createAccountingEvents)" value="label.accountingEvents.management.createEvents"/>
			<fr:property name="bundle(createAccountingEvents)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(createAccountingEvents)" value="true"/>      	
			<fr:property name="order(createAccountingEvents)" value="3"/>
			<fr:property name="visibleIf(createAccountingEvents)" value="allowedToManageAccountingEvents"/>
			
			<fr:property name="linkFormat(edit)" value="/manageStudentCurricularPlans.do?method=prepareEdit&amp;studentCurricularPlanId=${externalId}" />
			<fr:property name="key(edit)" value="label.edit"/>
			<fr:property name="bundle(edit)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(edit)" value="true"/>      	
			<fr:property name="visibleIf(edit)" value="allowedToManageEnrolments" />
			<fr:property name="order(edit)" value="4"/>
			
			<fr:property name="linkFormat(delete)" value="/manageStudentCurricularPlans.do?method=delete&amp;studentCurricularPlanId=${externalId}" />
			<fr:property name="key(delete)" value="label.delete"/>
			<fr:property name="bundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="confirmationKey(delete)" value="message.manageStudentCurricularPlans.delete.confirmation"/>
			<fr:property name="confirmationBundle(delete)" value="ACADEMIC_OFFICE_RESOURCES"/>
			<fr:property name="contextRelative(delete)" value="true"/>      	
			<fr:property name="visibleIf(delete)" value="allowedToDelete" />
			<fr:property name="order(delete)" value="5"/>
			
		</fr:layout>
	</fr:view>
	
	<p class="mtop0">
		
		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="externalId">
				<bean:message key="link.registration.viewStudentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>

		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/studentExternalEnrolments.do?method=manageExternalEnrolments" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:message key="label.student.manageExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>

		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/manageStudentCurricularPlans.do?method=prepareCreate" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:message key="link.manageStudentCurricularPlans.create" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>
		
	</p>
	
	</academic:allowed>
	
	<academic:notAllowed operation="MANAGE_REGISTRATIONS" program="<%= registration.getDegree() %>">
		<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM">
			<h3 class="mbottom05 mtop25 separator2"><bean:message key="label.studentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
			
			<fr:view name="registration" property="sortedStudentCurricularPlans" schema="student.studentCurricularPlans" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
					<fr:property name="groupLinks" value="false"/>
					
					<fr:property name="linkFormat(enrol)" value="/studentEnrolments.do?method=prepare&amp;scpID=${externalId}" />
					<fr:property name="key(enrol)" value="link.student.enrolInCourses"/>
					<fr:property name="bundle(enrol)" value="ACADEMIC_OFFICE_RESOURCES"/>
					<fr:property name="contextRelative(enrol)" value="true"/>      
					<fr:property name="visibleIf(enrol)" value="allowedToManageEnrolments" />
					<fr:property name="order(enrol)" value="1"/>
					
					<fr:property name="linkFormat(createAccountingEvents)" value="/accountingEventsManagement.do?method=prepare&amp;scpID=${externalId}" />
					<fr:property name="key(createAccountingEvents)" value="label.accountingEvents.management.createEvents"/>
					<fr:property name="bundle(createAccountingEvents)" value="ACADEMIC_OFFICE_RESOURCES"/>
					<fr:property name="contextRelative(createAccountingEvents)" value="true"/>      	
					<fr:property name="order(createAccountingEvents)" value="3"/>
					<fr:property name="visibleIf(createAccountingEvents)" value="allowedToManageAccountingEvents"/>
				</fr:layout>
			</fr:view>
			<p class="mtop0">
				<span>
					<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
					<html:link page="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="externalId">
						<bean:message key="link.registration.viewStudentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/>
					</html:link>
				</span>
			</p>
		</academic:allowed>
	</academic:notAllowed>
	
	<%-- Academic Services --%>
	
	<academic:allowed operation="SERVICE_REQUESTS">
	<h3 class="mtop25 mbottom05 separator2"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<bean:define id="registration" name="registration" scope="request" type="org.fenixedu.academic.domain.student.Registration"/>
	<p>
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.documentRequestsManagement.createDocumentRequest"/>:
		<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequestQuick" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="declarations"/>
		</html:link>	
		|
		<html:link action="/documentRequestsManagement.do?method=prepareCreateDocumentRequest" paramId="registrationId" paramName="registration" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="certificates"/>
		</html:link>

		|
		<html:link action="/academicServiceRequestsManagement.do?method=chooseServiceRequestType" paramId="registrationID" paramName="registration" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.serviceRequests"/>
		</html:link>

	</p>
	
	<p class="mtop1">
		<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
		<html:link action="/academicServiceRequestsManagement.do?method=viewRegistrationAcademicServiceRequestsHistoric" paramId="registrationID" paramName="registration" paramProperty="externalId">
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="requests.historic"/>
		</html:link>	
	</p>

    <logic:iterate id="requestSelector"
        collection="<%= java.util.Arrays.asList("newAcademicServiceRequests", "processingAcademicServiceRequests", "toDeliverAcademicServiceRequests") %>">
        <p class="mtop2">
    		<b><bean:message key="<%= requestSelector + ".title" %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></b>
            <bean:define id="requests" name="registration" property="<%= requestSelector.toString() %>"/>
            <logic:notEmpty name="requests">
                <fr:view name="requests" schema="AcademicServiceRequest.view">
                    <fr:layout name="tabular">
                        <fr:property name="classes" value="tstyle4 thlight mtop0" />
                        <fr:property name="columnClasses" value="smalltxt acenter nowrap,smalltxt acenter nowrap,acenter,,acenter,tdhl1 nowrap,,,acenter nowrap,nowrap" />

    					<fr:property name="linkFormat(view)" value="/academicServiceRequestsManagement.do?method=viewAcademicServiceRequest&amp;academicServiceRequestId=${externalId}&amp;backAction=student&amp;backMethod=visualizeRegistration"/>
    					<fr:property name="key(view)" value="view"/>

                        <fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&amp;academicServiceRequestId=${externalId}&amp;registrationID=${registration.externalId}"/>
                        <fr:property name="key(reject)" value="reject"/>
                        <fr:property name="visibleIf(reject)" value="rejectedSituationAccepted" />
                        <fr:property name="visibleIfNot(reject)" value="piggyBackedOnRegistry" />

                        <fr:property name="linkFormat(cancel)" value="/academicServiceRequestsManagement.do?method=prepareCancelAcademicServiceRequest&amp;academicServiceRequestId=${externalId}&amp;registrationID=${registration.externalId}"/>
                        <fr:property name="key(cancel)" value="cancel"/>
                        <fr:property name="visibleIf(cancel)" value="loggedPersonCanCancel"/>
                        <fr:property name="visibleIfNot(cancel)" value="piggyBackedOnRegistry"/>

                        <fr:property name="linkFormat(payments)" value="/../accounting-management/${registration.person.externalId}"/>
                        <fr:property name="key(payments)" value="payments"/>
                        <fr:property name="visibleIfNot(payments)" value="isPayed"/>
						<fr:property name="visibleIf(payments)" value="paymentsAccessible"/>

                        <fr:property name="linkFormat(processing)" value="/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(processing)" value="processing"/>
                        <fr:property name="visibleIf(processing)" value="processingSituationAccepted"/>
                        <fr:property name="visibleIfNot(processing)" value="piggyBackedOnRegistry"/>

                        <fr:property name="linkFormat(send)" value="/academicServiceRequestsManagement.do?method=prepareSendAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(send)" value="label.send"/>
                        <fr:property name="visibleIf(send)" value="sendToExternalEntitySituationAccepted"/>
                        <fr:property name="visibleIfNot(send)" value="managedWithRectorateSubmissionBatch"/>

                        <fr:property name="linkFormat(receiveFrom)" value="/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(receiveFrom)" value="label.receiveFrom"/>
                        <fr:property name="visibleIf(receiveFrom)" value="receivedSituationAccepted"/>

                        <fr:property name="linkFormat(print)" value="/documentRequestsManagement.do?method=downloadDocument&amp;documentRequestId=${externalId}&amp;"/>
                        <fr:property name="key(print)" value="print"/>
                        <fr:property name="visibleIf(print)" value="downloadPossible"/>


                        <fr:property name="linkFormat(reprint)" value="/documentRequestsManagement.do?method=printDocument&amp;documentRequestId=${externalId}&amp;"/>
                        <fr:property name="key(reprint)" value="reprint"/>
                        <fr:property name="visibleIf(reprint)" value="rePrintPossible"/>
        
                        <fr:property name="linkFormat(deliver)" value="/academicServiceRequestsManagement.do?method=deliveredAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(deliver)" value="deliver"/>
                        <fr:property name="visibleIf(deliver)" value="deliveredSituationAccepted"/>
                        
    
                        <fr:property name="linkFormat(code)" value="/academicServiceRequestsManagement.do?method=generateRegistryCode&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(code)" value="label.generateRegistryCode"/>
                        <fr:property name="visibleIf(code)" value="canGenerateRegistryCode"/>
    
                        <fr:property name="linkFormat(conclude)" value="/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(conclude)" value="conclude"/>
                        <fr:property name="visibleIf(conclude)" value="concludedSituationAccepted"/>

    					<fr:property name="order(view)" value="1" />
                        <fr:property name="order(reject)" value="2" />
                        <fr:property name="order(cancel)" value="3" />
                        <fr:property name="order(payments)" value="4" />
                        <fr:property name="order(processing)" value="5" />
                        <fr:property name="order(send)" value="6" />
                        <fr:property name="order(receiveFrom)" value="7" />
                        <fr:property name="order(print)" value="8" />
                        <fr:property name="order(deliver)" value="9" />
                        <fr:property name="order(code)" value="10" />
                        <fr:property name="order(conclude)" value="11" />

                        <fr:property name="sortBy" value="requestDate=desc, activeSituation.situationDate=desc, urgentRequest=desc, description=asc"/>
                    </fr:layout>
                </fr:view>
            </logic:notEmpty>
    		<logic:empty name="requests">
    			<p>
    				<em>
    					<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="<%= requestSelector + ".empty" %>"/>
    				</em>
    			</p>
    		</logic:empty>
        </p>
    </logic:iterate>
    </academic:allowed>

	<%-- Precedence Info --%>
	
	<logic:present name="registration" property="studentCandidacy">
		<h3 class="mtop2 mbottom05 separator2"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
		<fr:view name="registration" property="studentCandidacy.precedentDegreeInformation" schema="student.precedentDegreeInformation" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thright thlight mtop05"/>
			</fr:layout>
		</fr:view>
	</logic:present>
<bean:define id="deliveryWarning">
<bean:message bundle="ACADEMIC_OFFICE_RESOURCES"  key="academic.service.request.delivery.confirmation"/>
</bean:define>
<script type="text/javascript">
	$(function(){
		$('a[href*="deliveredAcademicServiceRequest"]').each(function(index) {
    		$(this).click(function() {	
    	  		return confirm("<%= deliveryWarning %>");
    	  	});
    	});
  	});
</script>
	<%--
	<ul class="mtop2">
		<li>
		<html:link page="/student.do?method=visualizeStudent" paramId="studentID" paramName="registration" paramProperty="student.externalId">
			<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</html:link>
		</li>
	</ul>
	--%>