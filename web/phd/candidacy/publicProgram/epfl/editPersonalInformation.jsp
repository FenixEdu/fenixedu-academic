<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<fr:form id="editPersonalInformationForm" action="/applications/epfl/phdProgramCandidacyProcess.do">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodForm" name="method" value="editPersonalInformation" />
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>
	
<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.personBean" />
	<%--  ### End of Error Messages  ### --%>
	
	<%--  ### Operation Area ### --%>
	
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></h2>
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
	
		<logic:equal name="canEditPersonalInformation" value="false">
			<h2><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
			<fr:view name="candidacyBean" property="personBean.person" schema="Public.PhdIndividualProgramProcess.view.person.simple">
				<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,,,"/>
				</fr:layout>
			</fr:view>
			<em><bean:message key="message.check.personal.information.in.intranet" bundle="PHD_RESOURCES" /></em>
			<a href="#" onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';document.getElementById('editPersonalInformationForm').submit();">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></a>
		</logic:equal>
	
		<logic:equal name="canEditPersonalInformation" value="true">
				
				<div class="fs_form">
				<fieldset style="display: block;">
					<legend><bean:message key="label.phd.public.candidacy.createCandidacy.fillPersonalInformation.edit" bundle="PHD_RESOURCES"/></legend>
					<p class="mtop05"><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></p>
					<p class="warning0"><span><bean:message key="message.EPFL.Public.PhdProgramCandidacyProcessBean.full.name.format" bundle="PHD_RESOURCES" /></span></p>

					<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean" 
						schema="EPFL.Public.PhdProgramCandidacyProcessBean.editPersonalInformation">
						<fr:layout name="tabular">
								<fr:property name="classes" value="thlight thleft"/>
						        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
								<fr:property name="requiredMarkShown" value="true" />
						</fr:layout>
					
						<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=editPersonalInformationInvalid" />
					</fr:edit>
				</fieldset>
				</div>
				<p class="mtop15">
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
					<html:submit onclick="javascript:document.getElementById('skipValidationId').value='true';javascript:document.getElementById('methodForm').value='backToViewCandidacy';document.getElementById('editPersonalInformationForm').submit();" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:submit>
				</p>
		</logic:equal>
	</logic:present>
</logic:equal>

</fr:form>
