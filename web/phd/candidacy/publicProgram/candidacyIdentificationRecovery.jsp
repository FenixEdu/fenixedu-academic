<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<a href="http://www.ist.utl.pt">IST</a> &gt;
	<a href="http://www.ist.utl.pt/en/html/ist-epfl/">IST</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h2><bean:message key="label.phd.public.candidacy.recover" bundle="PHD_RESOURCES" /></h2>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form action="/candidacies/phdProgramCandidacyProcess.do?method=candidacyIdentificationRecovery">

	<p><bean:message key="message.email.recovery.access.process" bundle="CANDIDATE_RESOURCES"/></p>
	
	<fr:edit id="candidacyBean" name="candidacyBean" schema="Public.PhdProgramCandidacyProcessBean.createCandidacyIdentification">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop05" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
			<fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
	
		<fr:destination name="invalid" path="/candidacies/phdProgramCandidacyProcess.do?method=candidacyIdentificationRecoveryInvalid" />
	</fr:edit>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/></html:submit>
</fr:form>
