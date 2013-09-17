<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.send.reception.email" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />

<html:link action='<%= "/caseHandlingMobilityApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<fr:form action='<%= "/caseHandlingMobilityApplicationProcess.do?method=sendReceptionEmailSetSelectedIndividualProcesses&amp;processId=" + processId.toString() %>'>
	<p>
		<html:submit><bean:message key="button.edit" bundle="APPLICATION_RESOURCES" /></html:submit>
		<html:cancel><bean:message key="button.cancel" bundle="APPLICATION_RESOURCES" /></html:cancel>	
	</p>

	<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
	
	<fr:view name="validIndividualProcesses" schema="SendReceptionEmail.processes.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
			<fr:property name="checkable" value="true"/>
			<fr:property name="checkboxName" value="selectedProcesses"/>
			<fr:property name="checkboxValue" value="externalId"/>
			
		</fr:layout>
			
	</fr:view>
	
</fr:form>