<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.send.reception.email" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />

<p>
	<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
		<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<p>
	<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=sendReceptionEmail&amp;processId=" + processId.toString() %>' id="send-form">
	
		<fr:view name="sendReceptionEmailBean">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean">
				<fr:slot name="emailSubject" key="label.erasmus.send.reception.edit.subject"/>
				<fr:slot name="emailBody" key="label.erasmus.send.reception.edit.body" />
			</fr:schema>

			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright"/>
			</fr:layout>
		</fr:view>
		
		<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" >
			<fr:destination name="cancel" path="<%= "/caseHandlingMobilityApplicationProcess.do?method=prepareExecuteSendReceptionEmail&amp;processId=" + processId.toString() %>" />		
		</fr:edit>
		
		<html:submit><bean:message key="button.erasmus.send.reception.email.send" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	</fr:form>
</p>


<fr:view name="sendReceptionEmailBean" property="subjectProcesses" schema="SendReceptionEmail.processes.view">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
	</fr:layout>
</fr:view>
