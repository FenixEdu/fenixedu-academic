<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></em>
<h2><bean:message key="title.erasmus.upload.learning.agreement" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="processId" name="process" property="externalId" />
<bean:define id="processName" name="processName" />


<html:link action='<%= "/caseHandling" + processName.toString() + ".do?method=listProcessAllowedActivities&amp;processId=" + processId.toString() %>'>
	<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>	
</html:link>
<br/>

<fr:form action='<%="/caseHandling" + processName + ".do?processId=" + processId.toString() %>' encoding="multipart/form-data" id="candidacyFormId">
	<input type="hidden" name="method" id="methodId" value="executeUploadApprovedLearningAgreement"/>
	<input type="hidden" name="documentFileOid" id="documentFileOidId" />
	
	<fr:edit id="individualCandidacyProcessBean.document.file"
		name="learningAgreementUploadBean" 
		schema="ApprovedLearningAgreement.documentUploadBean">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thleft"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<span class="error0"><fr:messages for="individualCandidacyProcessBean.document" type="global"/></span>
	
	<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>		

<bean:define id="individualCandidacyProcess" name="process"/>
 	
<logic:empty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements">
	<p><em><bean:message key="message.documents.empty" bundle="CANDIDATE_RESOURCES"/>.</em></p>
</logic:empty>

<logic:notEmpty name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements">
<fr:view name="individualCandidacyProcess" property="candidacy.approvedLearningAgreements">
	<fr:schema type="net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyDocumentFile" bundle="CANDIDATE_RESOURCES">
		<fr:slot name="uploadTime" key="label.dateTime.submission" />
		<fr:slot name="filename" key="label.document.file.name" />
		<fr:slot name="candidacyFileActive" key="label.document.file.active" />
		<fr:slot name="this" key="label.erasmus.view.learning.agreement" layout="link"/>
	</fr:schema>
	
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thcenter" />
	</fr:layout>
</fr:view>
</logic:notEmpty>
 </fr:form>
 