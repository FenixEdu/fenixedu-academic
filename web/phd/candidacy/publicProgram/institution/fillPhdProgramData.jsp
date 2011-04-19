<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ChecksumRewriter"%>


<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<a href="http://www.ist.utl.pt/en/">IST</a> &gt;
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/admissions/PhD/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } else { %>
		<a href="http://www.ist.utl.pt">IST</a> &gt;
		<%= ChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/candidaturas/doutoramentos/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } %>
	
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%> 
<jsp:include page="/phd/candidacy/publicProgram/institution/createCandidacyStepsBreadcrumb.jsp?step=2"></jsp:include>

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form id="candidacyForm" action="/applications/phd/phdProgramApplicationProcess.do" >

	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />
	<input type="hidden" id="methodId" name="method" value="createApplication"/>
	<input type="hidden" id="removeIndexId" name="removeIndex" value=""/>
	<input type="hidden" id="skipValidationId" name="skipValidation" value="false"/>

	<div class="fs_form">
		<p class="mvert05"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
	</div>

	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.focus.area" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean.focus.area" name="candidacyBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean" bundle="PHD_RESOURCES">
				<fr:slot name="focusArea" layout="menu-select-postback">	
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution.PhdProgramFocusAreasProvider" />
					<fr:property name="format" value="${name}" />
			        <fr:property name="sortBy" value="name"/>
			        <fr:property name="destination" value="focusAreaPostBack" />
				</fr:slot>
				<fr:slot name="program" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution.PhdProgramsProviderForPublicCandidacy" />
					<fr:property name="format" value="${name}" />
					<fr:property name="sortBy" value="name" /> 
				</fr:slot>
			</fr:schema>

			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/phd/phdProgramApplicationProcess.do?method=fillPhdProgramDataInvalid" />
			<fr:destination name="focusAreaPostBack" path="/applications/phd/phdProgramApplicationProcess.do?method=fillPhdProgramDataPostback" />
		</fr:edit>
	</fieldset>
	</div>
	
	<p class="mtop15">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" onclick="<%= "document.getElementById('skipValidationId').value='true'; document.getElementById('methodId').value='returnToPersonalData'; document.getElementById('candidacyForm').submit();" %>">« <bean:message bundle="PHD_RESOURCES" key="label.back"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.submit"/> »</html:submit>
	</p>

</fr:form>
