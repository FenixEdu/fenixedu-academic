<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="documentRequests" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>
<hr/>
<br/>

<logic:messagesPresent message="true">
	<span class="error"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
</logic:messagesPresent>

<logic:notEmpty name="warningsToReport">
	<p class="warning0"><bean:message key="document.request.warnings.title"/></p>
	<ul>
		<logic:iterate id="warningToReport" name="warningsToReport">
			<li><bean:message name="warningToReport"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<html:form action="/documentRequestsManagement.do?method=create">
<p style="margin-top: 4em;">
	<html:link page="/student.do?method=visualizeRegistration" paramId="registrationID" paramName="registration" paramProperty="idInternal">
		<bean:message key="link.student.back" bundle="ACADEMIC_OFFICE_RESOURCES"/>
	</html:link>
</p>
	<html:hidden property="registrationId" />
	<html:hidden property="schoolRegistrationExecutionYearId" />
	<html:hidden property="enrolmentDetailed"  />	
	<html:hidden property="enrolmentExecutionYearId" />
	<html:hidden property="degreeFinalizationAverage" />
	<html:hidden property="degreeFinalizationDetailed"/>
	<html:hidden property="chosenDocumentPurposeType" />
	<html:hidden property="otherPurpose" />
	<html:hidden property="notes" />
	<html:hidden property="isUrgent" />
	<html:hidden property="chosenDocumentRequestType"/>


	<fr:edit nested="true" schema="DocumentRequestCreateBean.viewToConfirmCreation" name="documentRequestCreateBeans" id="documentRequestCreateBeans" action="/documentRequest.do?method=create">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright" />
		</fr:layout>
		<fr:destination name="cancel" path="/documentRequest.do?method=prepare"/>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="button.submit"/></html:submit>
	
</html:form>
