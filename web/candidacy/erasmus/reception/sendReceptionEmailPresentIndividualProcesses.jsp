<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.send.reception.email" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="idInternal" />
<bean:define id="processName" name="processName" />

<html:link action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>



<fr:form action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=sendReceptionEmailPresentIndividualProcesses&amp;processId=" + processId.toString() %>'>
	<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
	
	<fr:edit id="send.reception.email.bean.edit" name="sendReceptionEmailBean">
		<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.SendReceptionEmailBean">
			<fr:slot name="includeOnlyProcessWithNoReceptionEmail" key="label.erasmus.send.reception.email.include.processes.no.reception.email" required="true" layout="radio-postback">
				<fr:property name="destination" value="postBack"/>
			</fr:slot>
		</fr:schema>
		
		<fr:layout name="tabular">
		</fr:layout>
		
		<fr:destination name="postBack" path="<%= "/caseHandlingErasmusCandidacyProcess.do?method=sendReceptionEmailPresentIndividualProcesses&amp;processId=" + processId.toString() %>" />
	</fr:edit>
	
</fr:form>

<p><bean:message key="title.erasmus.send.reception.email.candidates" bundle="ACADEMIC_OFFICE_RESOURCES" /></p>

<p>
	<fr:form action='<%= "/caseHandlingErasmusCandidacyProcess.do?method=sendReceptionEmail&amp;processId=" + processId.toString() %>'>
		<fr:edit id="send.reception.email.bean" name="sendReceptionEmailBean" visible="false" />
		
		<html:link onclick="this.form.submit" href="#">
			<bean:message key="link.add" bundle="ACADEMIC_OFFICE_RESOURCES" />
		</html:link> 
	</fr:form>
</p>

<fr:view name="sendReceptionEmailBean" property="subjectProcesses">
	<fr:schema bundle="ACADEMIC_OFFICE_RESOURCES" type="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.ErasmusIndividualCandidacyProcess">
		<fr:slot name="processCode" key="label.process.id" bundle="CANDIDATE_RESOURCES"/>
		<fr:slot name="personalDetails.name" key="label.name" bundle="CANDIDATE_RESOURCES"/>
		<fr:slot name="personalDetails.documentIdNumber" key="label.identificationNumber" bundle="CANDIDATE_RESOURCES"/>
		<fr:slot name="validatedByGri" key="label.erasmus.validated.by.gri" />
		<fr:slot name="validatedByErasmusCoordinator" key="label.erasmus.validated.by.coordinator" />
		<fr:slot name="lastReceptionEmailSent" bundle="label.erasmus.last.sent.reception.email" layout="null-as-label" />
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:link name="remove" link="<%= "/caseHandlingErasmusCandidacyProcess.do?method=&amp;processId=" + processId.toString() %>" label="link.remove,ACADEMIC_OFFICE_RESOURCES"/>
	</fr:layout>
</fr:view>
