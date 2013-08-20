<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%!

	static String f(String value, Object ... args) {
    	return String.format(value, args);
	}
%>


<html:xhtml/>


<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.view.approved.learning.agreements" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess" />

<p>
	<html:link action='<%= f("/caseHandlingMobilityIndividualApplicationProcess.do?method=listProcessAllowedActivities&amp;processId=%s", processId.toString()) %>'>
		« <bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
	</html:link>
</p>

<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES">
	<p><span class="error0"><bean:write name="message" /></span></p>
</html:messages>


<fr:hasMessages for="individualCandidacyProcessBean.precedentDegreeInformation" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<logic:empty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
	<p class="mbottom05"><em><bean:message key="label.erasmus.approved.learning.agreements.empty" bundle="ACADEMIC_OFFICE_RESOURCES" /></em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" >
	<fr:view name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements" schema="ApprovedLearningAgreementDocumentFile.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
		
			<fr:property name="linkFormat(markAsViewed)" value='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=markApprovedLearningAgreementAsViewed&amp;approvedLearningAgreementId=${externalId}&amp;processId=" + processId %>' />
			<fr:property name="key(markAsViewed)" value="label.erasmus.approved.learning.agreement.mark.as.viewed"/>
			<fr:property name="bundle(markAsViewed)" value="CANDIDATE_RESOURCES"/>
			<fr:property name="confirmationKey(markAsViewed)" value="label.erasmus.approved.learning.agreement.mark.as.viewed.confirm" />
			<fr:property name="confirmationBundle(markAsViewed)" value="CANDIDATE_RESOURCES" />
			<fr:property name="order(markAsViewed)" value="1" />
			<fr:property name="visibleIf(markAsViewed)" value="candidacyFileActive" />

			<fr:property name="linkFormat(markAsSent)" value='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=markApprovedLearningAgreementAsSent&amp;approvedLearningAgreementId=${externalId}&amp;processId=" + processId %>' />
			<fr:property name="key(markAsSent)" value="label.erasmus.approved.learning.agreement.mark.as.sent"/>
			<fr:property name="bundle(markAsSent)" value="CANDIDATE_RESOURCES"/>		
			<fr:property name="confirmationKey(markAsSent)" value="label.erasmus.approved.learning.agreement.mark.as.sent.confirm" />
			<fr:property name="confirmationBundle(markAsSent)" value="CANDIDATE_RESOURCES" />
			<fr:property name="order(markAsSent)" value="2" />
			<fr:property name="visibleIf(markAsSent)" value="candidacyFileActive" />
			
			<fr:property name="linkFormat(sendEmailToAcceptedStudent)" value='<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=sendEmailToAcceptedStudent&amp;approvedLearningAgreementId=${externalId}&amp;processId=" + processId %>' />
			<fr:property name="key(sendEmailToAcceptedStudent)" value="label.erasmus.send.email.to.accepted.student" />
			<fr:property name="bundle(sendEmailToAcceptedStudent)" value="CANDIDATE_RESOURCES" />
			<fr:property name="confirmationKey(sendEmailToAcceptedStudent)" value="message.erasmus.send.email.to.accepted.student.confirm" />
			<fr:property name="confirmationBundle(sendEmailToAcceptedStudent)" value="CANDIDATE_RESOURCES" />
			<fr:property name="order(sendEmailToAcceptedStudent)" value="3" />
			<fr:property name="visibleIf(sendEmailToAcceptedStudent)" value="ableToSendEmailToAcceptStudent" />
			
			<fr:link 	name="revokeLearningAgreementFile" 
						confirmation="message.erasmus.revoke.approved.learning.agreement.file.confirm,ACADEMIC_OFFICE_RESOURCES" 
						link="<%= "/caseHandlingMobilityIndividualApplicationProcess.do?method=revokeApprovedLearningAgreement&amp;approvedLearningAgreementId=${externalId}&amp;processId=" + processId %>" 
						label="label.erasmus.revoke.approved.learning.agreement.file,ACADEMIC_OFFICE_RESOURCES" 
						condition="candidacyFileActive"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>
