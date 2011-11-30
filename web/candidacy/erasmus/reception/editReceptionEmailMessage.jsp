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
	<fr:form action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=editReceptionEmailMessage&amp;processId=" + processId.toString() %>' id="send-form">
		<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
		
		<fr:edit id="send.reception.email.bean.edit.message" name="sendReceptionEmailBean">
			<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean">
				<fr:slot name="emailSubject" required="true" key="label.erasmus.send.reception.edit.subject">
					<fr:property name="size" value="120" />
				</fr:slot>
				<fr:slot name="emailBody" layout="longText" required="true" key="label.erasmus.send.reception.edit.body">
					<fr:property name="columns" value="80" />
					<fr:property name="rows" value="18" />
				</fr:slot>
			</fr:schema>
			
			<fr:destination name="invalid" path="<%= "/caseHandlingErasmusCandidacyProcess.do?method=editReceptionEmailMessageInvalid&amp;processId=" + processId.toString() %>" />
			<fr:destination name="cancel" path="<%= "/caseHandlingErasmusCandidacyProcess.do?method=prepareExecuteSendReceptionEmail&amp;processId=" + processId.toString() %>" />
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thright"/>
			</fr:layout>
			
		</fr:edit>
	
		<html:submit><bean:message key="button.edit" bundle="APPLICATION_RESOURCES"/></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>
	
	</fr:form>
</p>
