<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Instalation.getInstance().getInstituitionURL() %>en/about-IST/global-cooperation/IST-EPFL/">IST-EPFL</a> &gt;
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.epfl.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<p><bean:message key="message.email.required.begin.process" bundle="CANDIDATE_RESOURCES"/></p>

<div class="fs_form">
<fr:form action="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyIdentification">

	<fieldset style="display: block;">
		<legend><bean:message key="message.email.identification" bundle="PHD_RESOURCES"/></legend>

		<fr:edit id="candidacyBean" name="candidacyBean" schema="Public.PhdProgramCandidacyProcessBean.createCandidacyIdentification">
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thright thtop mtop05" />
				<fr:property name="columnClasses" value=",,tdclear tderror1" />
				<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/applications/epfl/phdProgramCandidacyProcess.do?method=createCandidacyIdentificationInvalid" />
		</fr:edit>
	</fieldset>	
	<p class="mtop15"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.continue"/> »</html:submit></p>
</fr:form>
</div>
