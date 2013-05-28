<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.send.reception.email" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<br/>

<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=sendReceptionEmailPresentIndividualProcesses&amp;processId=" + processId.toString() %>'>
	<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
	
	<fr:edit id="send.reception.email.bean.edit" name="sendReceptionEmailBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean">
			<fr:slot name="includeOnlyProcessWithNoReceptionEmail" key="label.erasmus.send.reception.email.include.processes.no.reception.email" required="true" layout="radio-postback">
				<fr:property name="destination" value="postBack"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>		
		</fr:layout>
		
		<fr:destination name="postBack" path="<%= "/caseHandlingMobilityApplicationProcess.do?method=sendReceptionEmailSetFilter&amp;processId=" + processId.toString() %>" />
	</fr:edit>
	
</fr:form>

<p><bean:message key="title.erasmus.send.reception.email.candidates" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<p>
	<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=sendReceptionEmailEditIndividualCandidacies&amp;processId=" + processId.toString() %>' id="edit-form">
		<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
		
		<html:link onclick="document.getElementById('edit-form').submit()" href="#">
			<bean:message key="link.edit.recipients.list" bundle="APPLICATION_RESOURCES" />
		</html:link>
	</fr:form> 
</p>
<p>
	<%--
	<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=prepareEditReceptionEmailMessage&amp;processId=" + processId.toString() %>' id="edit-message-form">
		<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
		
		<html:link onclick="document.getElementById('edit-message-form').submit()" href="#">
			<bean:message key="link.erasmus.edit.reception.email.message" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link>
	</fr:form>
	--%>
	
	
	<html:link href='<%= request.getContextPath() + "/internationalRelatOffice/caseHandlingMobilityApplicationProcess.do?method=manageEmailTemplates&amp;template=Reception&amp;processId=" + processId.toString() %>'>
		<bean:message key="link.erasmus.edit.reception.email.message" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:link>
	
</p>
<p>
	<logic:equal name="process" property="receptionEmailMessageDefined" value="true">
		<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=viewEmailToSend&amp;processId=" + processId.toString() %>' id="send-form">
			<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
		
			<html:link onclick="document.getElementById('send-form').submit()" href="#">
				<bean:message key="label.erasmus.send.reception.email.action" bundle="ACADEMIC_OFFICE_RESOURCES" />
			</html:link>
		</fr:form>
	</logic:equal>
</p>
<p>
	<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<fr:view name="sendReceptionEmailBean" property="subjectProcesses" schema="SendReceptionEmail.processes.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	</fr:layout>
</fr:view>
