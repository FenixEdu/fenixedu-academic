<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.public.candidacy.recover" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form action="/applications/epfl/phdProgramCandidacyProcess.do?method=candidacyIdentificationRecovery">

	<p><bean:message key="message.email.recovery.access.process" bundle="CANDIDATE_RESOURCES"/></p>
	
	<div class="fs_form">
		<fieldset style="display: block;">
		<legend><bean:message key="message.email.identification" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean" name="candidacyBean" schema="Public.PhdProgramCandidacyProcessBean.createCandidacyIdentification">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thright thtop mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=candidacyIdentificationRecoveryInvalid" />
		</fr:edit>
	</fieldset>
	</div>
	
	<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/> »</html:submit></p>
</fr:form>
