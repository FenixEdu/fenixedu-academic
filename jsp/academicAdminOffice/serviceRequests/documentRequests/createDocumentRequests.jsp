<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" %> 
<html:xhtml/>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<hr/>


<h2><strong><bean:message key="label.studentDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>

<table >
	<tr>
		<td>
			<fr:view name="registration" property="student" schema="student.show.personAndStudentInformation">
				<fr:layout name="tabular">
					<fr:property name="classes" value="tstyle4"/>
			      	<fr:property name="columnClasses" value="listClasses,,"/>
				</fr:layout>
			</fr:view>
		</td>
		<td>
			<bean:define id="personID" name="registration" property="student.person.idInternal"/>
			<html:img align="middle" height="100" width="100" src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES"/>
		</td>
	</tr>
</table>
<br/>

<logic:present name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationDetail" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:present>
<logic:notPresent name="registration" property="ingressionEnum">
<h2><strong><bean:message key="label.registrationDetails" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="registration" schema="student.registrationsWithStartData" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4"/>
      	<fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
</logic:notPresent>





	<html:form action="/documentRequestsManagement">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewDocumentRequestsToCreate" />
		<bean:define id="registrationId" name="registration" property="idInternal"/>
		<html:hidden property="registrationId" value="<%= pageContext.findAttribute("registrationId").toString()%>" />

<logic:messagesPresent message="true">
	<p class="mtop05 mbottom1">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	</p>
</logic:messagesPresent>

<p>
		
		
		<logic:present name="executionYears">

		
		<p class="mtop2">
		<bean:message key="label.documentRequestsManagement.searchDocumentRequests.documentRequestType" bundle="ACADEMIC_OFFICE_RESOURCES"/>:</p>
		<p class="mbottom05">
		<bean:message key="message.document.to.request" bundle="ACADEMIC_OFFICE_RESOURCES"/>
		</p>
 		
			<table class="tstyle2 mtop05">
				<e:labelValues id="documentRequestTypes" enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentRequestType" bundle="ENUMERATION_RESOURCES"/>
				<logic:iterate id="documentRequestType" name="documentRequestTypes">
					<tr>
						<td>
						     <bean:define id="documentRequestTypeid" name="documentRequestType" property="value"/>
							<html:radio property="chosenDocumentRequestType" value="<%= ""+documentRequestTypeid %>" />
							<%--	<bean:write name="documentRequestType" property="value"/>
							</html:radio> --%>
							<bean:write name="documentRequestType" property="label"/>
						</td>
						
						<logic:equal name="documentRequestType" property="value" value="SCHOOL_REGISTRATION_CERTIFICATE">
						<td>
							<bean:message key="label.executionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
							<html:select property="schoolRegistrationExecutionYearId" value="currentExecutionYearId" >
								<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
							</html:select>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="SCHOOL_REGISTRATION_CERTIFICATE">
						<td></td>
						</logic:equal>
							
						<logic:equal name="documentRequestType" property="value" value="ENROLMENT_CERTIFICATE">
						<td>
							<bean:message key="label.executionYear" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
							<html:select property="enrolmentExecutionYearId" value="currentExecutionYearId" >
								<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
							</html:select>
							<span class="pleft2">
								<bean:message key="label.documentRequestsManagement.searchDocumentRequests.detailed" bundle="ACADEMIC_OFFICE_RESOURCES"/>?
								<html:radio property="enrolmentDetailed" value="true"/><bean:message key="label.yes"/>
								<html:radio property="enrolmentDetailed" value="false"/><bean:message key="label.no"/>
							</span>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="ENROLMENT_CERTIFICATE">
						<td></td>
						</logic:equal>
						
						<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
						<td>
							<bean:message key="label.documentRequestsManagement.searchDocumentRequests.average" bundle="ACADEMIC_OFFICE_RESOURCES"/>?
							<html:radio property="degreeFinalizationAverage" value="true"/><bean:message key="label.yes"/>
							<html:radio property="degreeFinalizationAverage" value="false"/><bean:message key="label.no"/>
							<span class="pleft2">
								<bean:message key="label.documentRequestsManagement.searchDocumentRequests.detailed" bundle="ACADEMIC_OFFICE_RESOURCES"/>?
								<html:radio property="degreeFinalizationDetailed" value="true"/><bean:message key="label.yes"/>
								<html:radio property="degreeFinalizationDetailed" value="false"/><bean:message key="label.no"/>
							</span>
						</td>
						</logic:equal>
	
						<logic:equal name="documentRequestType" property="value" value="DEGREE_FINALIZATION_CERTIFICATE">
						<td></td>					
						</logic:equal>	
					
					</tr>
				</logic:iterate>
			</table>
			
			<p class="mtop2">
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.otherPurpose" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
				<e:labelValues 
					id="documentPurposeTypes" 
					enumeration="net.sourceforge.fenixedu.domain.serviceRequests.documentRequests.DocumentPurposeType" 
					bundle="ENUMERATION_RESOURCES"/>
				<logic:iterate id="documentPurposeType" name="documentPurposeTypes">
					<p>
						<html:radio property="chosenDocumentPurposeType" idName="documentPurposeType" value="value"/><bean:write name="documentPurposeType" property="label"/>
						<logic:equal name="documentPurposeType" property="value" value="OTHER">
							<html:text property="otherPurpose" size="40"/>
						</logic:equal>
					</p>
				</logic:iterate>
			</p>
			
			<p class="mtop2">
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.notes" bundle="ACADEMIC_OFFICE_RESOURCES"/>:
				<p>
					<html:textarea property="notes" cols="70" rows="2"/>
				</p>
			</p>
			
			<p style="margin-top: 2em;">
				<bean:message key="label.documentRequestsManagement.searchDocumentRequests.urgent" bundle="ACADEMIC_OFFICE_RESOURCES"/><br/>
				<div class="warning0"><bean:message key="message.urgency.charge.explanation" bundle="ACADEMIC_OFFICE_RESOURCES"/></div>
				
				<p>
					<html:radio property="isUrgent" value="true"/><bean:message key="label.yes"/>
				</p>
				<p>
					<html:radio property="isUrgent" value="false"/><bean:message key="label.no"/>
				</p>
			</p>
			
			<p class="mtop2"><html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit></p>

		</p>

		</logic:present>
</p>		
	</html:form>
	

