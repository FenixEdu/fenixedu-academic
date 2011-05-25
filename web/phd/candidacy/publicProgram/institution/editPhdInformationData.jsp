<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.ContentInjectionRewriter"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.edit.candidacy.information" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>

<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />
<fr:form id="editPhdInformationForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editPhdInformationData&processId=" + processId %>">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<logic:equal name="canEditCandidacy" value="true">

	<%--  ### Error Messages  ### --%>
	<jsp:include page="/phd/errorsAndMessages.jsp" />
	<%--  ### End of Error Messages  ### --%>
	
	<h2 style="margin-top: 1em;"><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES" /></h2>
	<bean:define id="hash" name="process" property="candidacyHashCode.value" />
	
	<logic:notPresent name="candidacyBean">
		<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
		<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
			« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
		</html:link>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation.edit" bundle="PHD_RESOURCES" /></legend>
			<p class="mtop15"><span><bean:message key="message.mandatory.fields" bundle="PHD_RESOURCES"/></span></p>
			
			<fr:edit id="individualProcessBean" name="individualProcessBean" schema="Public.PhdIndividualProgramProcessBean.editDetails">
				<fr:schema type="net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcessBean" bundle="PHD_RESOURCES">
					<fr:slot name="phdProgram" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution.PhdProgramsProviderForPublicCandidacy" />
						<fr:property name="format" value="${name}" />
						<fr:property name="sortBy" value="name" /> 
					</fr:slot>
					<fr:slot name="thesisTitle" key="label.net.sourceforge.fenixedu.domain.phd.PhdIndividualProgramProcess.thesis.title.proposed">
						<fr:property name="size" value="80"/>
					</fr:slot>
				</fr:schema>
					
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="true" />
				</fr:layout>
				
				<fr:destination name="cancel" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=viewApplication&hash=" + hash %>" />
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editPhdInformationDataInvalid&processId=" + processId %>" />
				<fr:destination name="focusAreaPostBack" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPhdInformationDataFocusAreaPostback&processId=" + processId %>" />
			</fr:edit>
			</fieldset>
			</div>
			<p>
				<html:submit><bean:message bundle="PHD_RESOURCES" key="label.edit"/></html:submit>
				<html:cancel><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
			</p>
	</logic:present>
</logic:equal>

</fr:form>
