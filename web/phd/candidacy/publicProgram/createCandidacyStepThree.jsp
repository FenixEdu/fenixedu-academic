<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>
<jsp:include page="/phd/candidacy/publicProgram/createCandidacyStepsBreadcrumb.jsp?step=3"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<br/>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.curriculumVitae" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/candidacies/phdProgramCandidacyProcess.do" encoding="multipart/form-data" >

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

	<input type="hidden" id="methodId" name="method" value="createCandidacy"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	
	<h2 class="mtop1"><bean:message key="label.phd.public.candidacy.createCandidacy.updloadDocuments" bundle="PHD_RESOURCES"/></h2>
	
	<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>

	<br/>
	<fr:edit id="candidacyBean.curriculumVitae" name="candidacyBean" property="curriculumVitae" 
			 schema="Public.PhdCandidacyDocumentUploadBean.curriculum.vitae">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>
	
	<fr:edit id="candidacyBean.identificationDocument" name="candidacyBean" property="identificationDocument" 
			 schema="Public.PhdCandidacyDocumentUploadBean.identificationDocument">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>

	<fr:edit id="candidacyBean.motivationLetter" name="candidacyBean" property="motivationLetter" 
			 schema="Public.PhdCandidacyDocumentUploadBean.motivationLetter">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>

	<fr:edit id="candidacyBean.socialSecurityDocument" name="candidacyBean" property="socialSecurityDocument" 
			 schema="Public.PhdCandidacyDocumentUploadBean.socialSecurityDocument">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>

	<fr:edit id="candidacyBean.researchPlan" name="candidacyBean" property="researchPlan" 
			 schema="Public.PhdCandidacyDocumentUploadBean.researchPlan">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>

	<fr:edit id="candidacyBean.reportOrWorkDocument" name="candidacyBean" property="reportOrWorkDocument" 
			 schema="Public.PhdCandidacyDocumentUploadBean.reportOrWorkDocument">
		<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
	</fr:edit>

	<br />
	<strong class="mtop1"><bean:message key="label.phd.public.documents.habilitationCertificateDocuments" bundle="PHD_RESOURCES"/></strong>

	<br />
	<logic:notEmpty name="candidacyBean" property="habilitationCertificateDocuments">
		<logic:iterate id="document" name="candidacyBean" property="habilitationCertificateDocuments" indexId="index">
			<strong><%= index.intValue() + 1 %>.</strong>
			<fr:edit id="<%= "candidacyBean.habilitationCertificateDocument" + index %>" name="document" schema="Public.PhdCandidacyDocumentUploadBean.edit">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
		        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
			</fr:edit>
			<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"removeIndexId\").value=" + index + "; document.getElementById(\"methodId\").value=\"removeHabilitationCertificateDocument\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" ><bean:message key="label.remove" bundle="PHD_RESOURCES"/></a></p>
		</logic:iterate>
	</logic:notEmpty>
	<p><a onclick='<%= "document.getElementById(\"skipValidationId\").value=\"true\"; document.getElementById(\"methodId\").value=\"addHabilitationCertificateDocument\"; document.getElementById(\"candidacyForm\").submit();" %>' href="#" ><bean:message key="label.add" bundle="PHD_RESOURCES"/></a></p>

	<logic:notEmpty name="candidacyBean" property="phdGuidingLetters">
		<br />
		<strong class="mtop1"><bean:message key="label.phd.public.documents.phdGuidingLetters" bundle="PHD_RESOURCES"/></strong>
		<br/>
		<logic:iterate id="guiding" name="candidacyBean" property="phdGuidingLetters" indexId="index">
			<strong><%= index.intValue() + 1 %>.</strong>
			<fr:edit id="<%= "candidacyBean.phdGuidingLetter" + index %>" name="guiding" schema="Public.PhdCandidacyDocumentUploadBean.edit">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
		        	<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=createCandidacyStepThreeInvalid" />
			</fr:edit>
		</logic:iterate>
	</logic:notEmpty>

	<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.phd.public.submit.candidacy"/></html:submit>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='returnCreateCandidacyStepTwo'; document.getElementById('candidacyForm').submit();" %>"><bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>

	<p class="mtop15"><span><bean:message key="message.fields.required" bundle="CANDIDATE_RESOURCES"/></span></p>
	<p><em><bean:message key="message.max.file.size" bundle="CANDIDATE_RESOURCES"/></em></p>	

</fr:form>
