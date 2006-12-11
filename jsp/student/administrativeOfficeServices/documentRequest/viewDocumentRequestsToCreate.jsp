<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<em><bean:message key="administrative.office.services"/></em>
<h2><bean:message key="documents.requirement"/></h2>

<logic:messagesPresent message="true">
	<p>
	<span class="error0"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="STUDENT_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	</p>
</logic:messagesPresent>

<logic:notEmpty name="warningsToReport">
	<p class="warning0"><bean:message key="document.request.warnings.title"/></p>
	<ul>
		<logic:iterate id="warningToReport" name="warningsToReport">
			<li><bean:message name="warningToReport"/></li>
		</logic:iterate>
	</ul>
</logic:notEmpty>

<html:form action="/documentRequest.do?method=create">
	<html:hidden property="scpId" />
	<html:hidden property="schoolRegistrationExecutionYearId" />
	<html:hidden property="enrolmentDetailed"  />	
	<html:hidden property="enrolmentExecutionYearId" />
	<html:hidden property="degreeFinalizationAverage" />
	<html:hidden property="degreeFinalizationDetailed"/>
	<html:hidden property="chosenDocumentPurposeType" />
	<html:hidden property="otherPurpose" />
	<html:hidden property="notes" />
	<html:hidden property="urgentRequest" />
	<html:hidden property="registrationId"/>
	

	<logic:iterate id="chosenDocumentRequestType" name="chosenDocumentRequestTypes">
		<html:hidden property="chosenDocumentRequestTypes" value="<%=chosenDocumentRequestType.toString()%>"/>
	</logic:iterate>
	
	<p class="mbottom025"><bean:message key="label.document.request.confirm" bundle="STUDENT_RESOURCES"/></p>
	<fr:edit nested="true" schema="DocumentRequestCreateBean.viewToConfirmCreation" name="documentRequestCreateBeans" id="documentRequestCreateBeans" action="/documentRequest.do?method=create">
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight mtop025" />
		</fr:layout>
		<fr:destination name="cancel" path="/documentRequest.do?method=prepare"/>
	</fr:edit>

	<html:submit styleClass="inputbutton"><bean:message key="button.confirm" bundle="APPLICATION_RESOURCES"/></html:submit>
	<html:cancel styleClass="inputbutton"><bean:message key="return"/></html:cancel>
</html:form>
