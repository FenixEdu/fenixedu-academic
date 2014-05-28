<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>en/education/fct-phd-programmes/">FCT Doctoral Programmes</a> &gt;
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

					<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean">
						<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.person.PersonBean" bundle="PHD_RESOURCES">
							<fr:slot name="givenNames" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.EPFL.Public.PhdProgramCandidacyProcessBean.firstName">
								<fr:property name="size" value="60"/>
								<fr:property name="maxLength" value="255"/>
							</fr:slot>
							<fr:slot name="familyNames" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.EPFL.Public.PhdProgramCandidacyProcessBean.surname">
								<fr:property name="size" value="60"/>
								<fr:property name="maxLength" value="255"/>
							</fr:slot>
							<fr:slot name="gender" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
							<fr:slot name="idDocumentType" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="includedValues" value="IDENTITY_CARD,PASSPORT" />
							</fr:slot>
							<fr:slot name="documentIdNumber" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="20"/>
							</fr:slot>
							<fr:slot name="documentIdEmissionLocation">
								<fr:property name="size" value="20"/>
								<fr:property name="maxLength" value="50"/>
							</fr:slot>
							<fr:slot name="socialSecurityNumber">
						    	<fr:property name="size" value="20"/>
								<fr:property name="maxLength" value="15"/>
						    </fr:slot>
						    <fr:slot name="dateOfBirth">
								<fr:property name="size" value="10"/>
								<fr:property name="maxLength" value="10"/>
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.DateValidator" />
							</fr:slot>
							<fr:slot name="districtSubdivisionOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="40"/>
							</fr:slot>
							<fr:slot name="nationality" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.choiceType.replacement.single.CountryProvider"/>
								<fr:property name="format" value="${countryNationality}" />
								<fr:property name="sortBy" value="countryNationality"/>
							</fr:slot>
							<fr:slot name="address" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="60"/>
							</fr:slot>
							<fr:slot name="area" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="40"/>
							</fr:slot>
							<fr:slot name="areaCode" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
								<fr:property name="size" value="20"/>
							</fr:slot>
						    <fr:slot name="countryOfResidence" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
								<fr:property name="format" value="${localizedName}"/>
								<fr:property name="sortBy" value="localizedName=asc" />
								<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
							</fr:slot>
							<fr:slot name="phone">
						    	<fr:property name="size" value="20"/>
								<fr:property name="maxLength" value="15"/>
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
						            <fr:property name="regexp" value="(\+?\d{4,15})?"/>
						            <fr:property name="message" value="error.phone.invalidFormat"/>
						            <fr:property name="key" value="true"/>
						            <fr:property name="bundle" value="PHD_RESOURCES" />
						        </fr:validator>
						    </fr:slot>
							<fr:slot name="mobile">
						    	<fr:property name="size" value="20"/>
								<fr:property name="maxLength" value="15"/>
								<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
						            <fr:property name="regexp" value="(\+?\d{4,15})?"/>
						            <fr:property name="message" value="error.phone.invalidFormat"/>
						            <fr:property name="key" value="true"/>
						            <fr:property name="bundle" value="PHD_RESOURCES" />
						        </fr:validator>
						    </fr:slot>    
							<fr:slot name="email" readOnly="true"/>
						</fr:schema>
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
