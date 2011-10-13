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
<jsp:include page="/phd/candidacy/publicProgram/epfl/createCandidacyStepsBreadcrumb.jsp?step=1"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.personBean" />
<%--  ### End of Error Messages  ### --%>

<%-- <p><em><bean:message key="message.max.file.size" bundle="PHD_RESOURCES"/></em></p> --%>

<%--  ### Operation Area ### --%>

<logic:notPresent name="candidacyBean">
	<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
</logic:notPresent>

<logic:present name="candidacyBean">

	<fr:form action="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepTwo">
		<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></legend>
			<p class="mvert05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
			<p class="warning0"><span><bean:message key="message.EPFL.Public.PhdProgramCandidacyProcessBean.full.name.format" bundle="PHD_RESOURCES" /></span></p>
			
			<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean" 
				schema="EPFL.Public.PhdProgramCandidacyProcessBean.editPersonalInformation">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepOneInvalid" />
			</fr:edit>
		</fieldset>
		</div>
		
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="title.public.phd.institution.id" bundle="PHD_RESOURCES"/></legend>
			<p class="mvert05"><span><bean:message key="message.phd.public.institution.id.note" bundle="PHD_RESOURCES"/></span></p>
			
			<fr:edit id="candidacyBean.institution.id" name="candidacyBean" schema="Public.PhdProgramCandidacyProcessBean.institution.id">
				<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft thtop mtop05"/>
				        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
						<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
			
				<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyStepOneInvalid" />
			</fr:edit>
		</fieldset>
		</div>
		
		<p>Note: all the information required for the application process will be shared by both <a href="http://www.ist.utl.pt/">IST</a> and <a href="http://www.epfl.ch/">EPFL</a>.</p>
		
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit></p>
	</fr:form>
</logic:present>
