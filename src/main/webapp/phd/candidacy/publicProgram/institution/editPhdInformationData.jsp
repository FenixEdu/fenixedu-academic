<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>

<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

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
	
	<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.editCandidacyInformation" bundle="PHD_RESOURCES" /></h2>
	<bean:define id="hash" name="process" property="candidacyHashCode.value" />
	
	<logic:notPresent name="candidacyBean">
		<p><em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em></p>
		
		<p>
			<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash" >
				« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
			</html:link>
		</p>
	</logic:notPresent>
	
	<logic:present name="candidacyBean">
		<div class="fs_form">
		<fieldset style="display: block;">
			<legend><bean:message key="label.phd.public.candidacy.createCandidacy.fillCandidacyInformation" bundle="PHD_RESOURCES" /></legend>

			<fr:edit id="individualProcessBean" name="individualProcessBean" schema="Public.PhdIndividualProgramProcessBean.editDetails">
				<fr:schema type="org.fenixedu.academic.domain.phd.PhdIndividualProgramProcessBean" bundle="PHD_RESOURCES">
					<fr:slot name="phdProgram" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.struts.action.phd.candidacy.publicProgram.institution.PhdProgramsProviderForPublicCandidacy" />
						<fr:property name="format" value="${name.content}" />
						<fr:property name="sortBy" value="name" /> 
					</fr:slot>
					<fr:slot name="thesisTitle" key="label.org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess.thesis.title.proposed">
						<fr:property name="size" value="80"/>
					</fr:slot>
				</fr:schema>
					
				<fr:layout name="tabular-editable">
					<fr:property name="classes" value="thlight thleft"/>
					<fr:property name="columnClasses" value=",,tdclear tderror1"/>
					<fr:property name="requiredMarkShown" value="false" />
					<fr:property name="optionalMarkShown" value="true" />
				</fr:layout>
				
				<fr:destination name="cancel" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=viewApplication&hash=" + hash %>" />
				<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editPhdInformationDataInvalid&processId=" + processId %>" />
				<fr:destination name="focusAreaPostBack" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=prepareEditPhdInformationDataFocusAreaPostback&processId=" + processId %>" />
			</fr:edit>
			</fieldset>
			</div>
			<p>
				<html:submit><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
				<html:cancel><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
			</p>
	</logic:present>
</logic:equal>

</fr:form>
