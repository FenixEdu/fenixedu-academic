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

<%@page import="org.fenixedu.academic.domain.ExecutionYear"%>
<%@page import="org.fenixedu.academic.domain.student.RegistrationDataByExecutionYear"%>

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
			<logic:present name="registration" property="ingressionType">
			<fr:view name="registration" schema="student.registrationDetail" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight"/>
					<fr:property name="rowClasses" value=",,,,,,,,"/>
				</fr:layout>
			</fr:view>
			</logic:present>
			<logic:notPresent name="registration" property="ingressionType">
			<fr:view name="registration" schema="student.registrationsWithStartData" >
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4 thright thlight mtop0"/>
					<fr:property name="rowClasses" value=",,,,,,,"/>
				</fr:layout>
			</fr:view>
			</logic:notPresent>
		
		</td>
		
		<td style="vertical-align: top; padding-top: 1em;">
			<academic:allowed operation="MANAGE_REGISTRATIONS" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS" program="<%= registration.getDegree() %>">
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
				<academic:allowed operation="MANAGE_CONCLUSION" permission="ACADEMIC_OFFICE_CONCLUSION" program="<%= registration.getDegree() %>">
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
			</p>
			</academic:allowed>
		</td>
	</tr>
</table>
	
	<%-- Registration Data by Execution Year --%>
	<h3 class="mbottom05 mtop25 separator2"><bean:message key="title.registrationDataByExecutionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<logic:empty name="registration" property="registrationDataByExecutionYear">
		<bean:message key="label.registrationDataByExecutionYear.noResults" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</logic:empty>
	<logic:notEmpty name="registration" property="registrationDataByExecutionYear">
		<fr:view name="registration" property="registrationDataByExecutionYear">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="<%= RegistrationDataByExecutionYear.class.getName() %>">
				<fr:slot name="executionYear.qualifiedName" key="label.executionYear" />
				<fr:slot name="enrolmentDate" key="label.enrolmentDate" />
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thright thlight thcenter"/>
				<fr:property name="columnClasses" value="acenter,acenter,acenter" />
				<fr:property name="sortBy" value="executionYear=desc" />
				<fr:link name="edit" label="label.edit,ACADEMIC_OFFICE_RESOURCES" 
							 link="/manageRegistrationDataByExecutionYear.do?method=prepareEdit&registrationDataByExecutionYearId=${externalId}" order="1" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
	
	
	<%-- Curricular Plans --%>
	
	<academic:allowed operation="MANAGE_REGISTRATIONS" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS" program="<%= registration.getDegree() %>">
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

		<span>
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/studentExternalEnrolments.do?method=manageExternalEnrolments" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:message key="label.student.manageExternalEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>

		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/viewStudentCurriculum.do?method=prepare" paramId="registrationOID" paramName="registration" paramProperty="externalId">
				<bean:message key="link.registration.viewStudentCurricularPlans" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			</html:link>
		</span>
		<span class="pleft1">
			<img src="<%= request.getContextPath() %>/images/dotist_post.gif" alt="<bean:message key="dotist_post" bundle="IMAGE_RESOURCES" />" />
			<html:link page="/manageStudentCurricularPlans.do?method=prepareCreate" paramId="registrationId" paramName="registration" paramProperty="externalId">
				<bean:message key="link.manageStudentCurricularPlans.create" bundle="ACADEMIC_OFFICE_RESOURCES"/>
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
	
	<academic:notAllowed operation="MANAGE_REGISTRATIONS" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS" program="<%= registration.getDegree() %>">
		<academic:allowed operation="VIEW_FULL_STUDENT_CURRICULUM" permission="ACADEMIC_OFFICE_REGISTRATION_ACCESS">
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
	
	<academic:allowed operation="SERVICE_REQUESTS" permission="ACADEMIC_REQUISITIONS">
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
    					<fr:property name="bundle(view)" value="APPLICATION_RESOURCES" />

                        <fr:property name="linkFormat(reject)" value="/academicServiceRequestsManagement.do?method=prepareRejectAcademicServiceRequest&amp;academicServiceRequestId=${externalId}&amp;registrationID=${registration.externalId}"/>
                        <fr:property name="key(reject)" value="reject"/>
                        <fr:property name="bundle(reject)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(reject)" value="rejectedSituationAccepted" />
                        <fr:property name="visibleIfNot(reject)" value="piggyBackedOnRegistry" />

                        <fr:property name="linkFormat(cancel)" value="/academicServiceRequestsManagement.do?method=prepareCancelAcademicServiceRequest&amp;academicServiceRequestId=${externalId}&amp;registrationID=${registration.externalId}"/>
                        <fr:property name="key(cancel)" value="cancel"/>
                        <fr:property name="bundle(cancel)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(cancel)" value="loggedPersonCanCancel"/>
                        <fr:property name="visibleIfNot(cancel)" value="piggyBackedOnRegistry"/>

                        <fr:property name="linkFormat(payments)" value="${paymentURL}"/>
                        <fr:property name="module(payments)" value=""/>                        
                        <fr:property name="key(payments)" value="payments"/>
                        <fr:property name="bundle(payments)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIfNot(payments)" value="isPayed"/>
						<fr:property name="visibleIf(payments)" value="paymentsAccessible"/>

                        <fr:property name="linkFormat(processing)" value="/academicServiceRequestsManagement.do?method=processNewAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(processing)" value="processing"/>
                        <fr:property name="bundle(processing)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(processing)" value="processingSituationAccepted"/>
                        <fr:property name="visibleIfNot(processing)" value="piggyBackedOnRegistry"/>

                        <fr:property name="linkFormat(send)" value="/academicServiceRequestsManagement.do?method=prepareSendAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(send)" value="label.send"/>
                        <fr:property name="bundle(send)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(send)" value="sendToExternalEntitySituationAccepted"/>
                        <fr:property name="visibleIfNot(send)" value="managedWithRectorateSubmissionBatch"/>

                        <fr:property name="linkFormat(receiveFrom)" value="/academicServiceRequestsManagement.do?method=prepareReceiveAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(receiveFrom)" value="label.receiveFrom"/>
                        <fr:property name="bundle(receiveFrom)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(receiveFrom)" value="receivedSituationAccepted"/>

                        <fr:property name="linkFormat(print)" value="/documentRequestsManagement.do?method=downloadDocument&amp;documentRequestId=${externalId}&amp;"/>
                        <fr:property name="key(print)" value="print"/>
                        <fr:property name="bundle(print)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(print)" value="downloadPossible"/>


                        <fr:property name="linkFormat(reprint)" value="/documentRequestsManagement.do?method=printDocument&amp;documentRequestId=${externalId}&amp;"/>
                        <fr:property name="key(reprint)" value="reprint"/>
                        <fr:property name="bundle(reprint)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(reprint)" value="rePrintPossible"/>
        
                        <fr:property name="linkFormat(deliver)" value="/academicServiceRequestsManagement.do?method=deliveredAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(deliver)" value="deliver"/>
                        <fr:property name="bundle(deliver)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(deliver)" value="deliveredSituationAccepted"/>
                        
    
                        <fr:property name="linkFormat(code)" value="/academicServiceRequestsManagement.do?method=generateRegistryCode&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(code)" value="label.generateRegistryCode"/>
                        <fr:property name="bundle(code)" value="APPLICATION_RESOURCES" />
                        <fr:property name="visibleIf(code)" value="canGenerateRegistryCode"/>
    
                        <fr:property name="linkFormat(conclude)" value="/academicServiceRequestsManagement.do?method=prepareConcludeAcademicServiceRequest&amp;academicServiceRequestId=${externalId}"/>
                        <fr:property name="key(conclude)" value="conclude"/>
                        <fr:property name="bundle(conclude)" value="APPLICATION_RESOURCES" />
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
		<fr:view name="registration" property="studentCandidacy.completedDegreeInformation" schema="student.precedentDegreeInformation" >
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