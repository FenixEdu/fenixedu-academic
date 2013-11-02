<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@ page import="java.util.Locale"%>


<html:xhtml/>

<%-- ### Title #### --%>

<div class="breadcumbs">
	<% 
		Locale locale = Language.getLocale();
		if(!locale.getLanguage().equals(Locale.ENGLISH.getLanguage())) {
	%>
		<a href="http://www.ist.utl.pt/en/">IST</a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/en/prospective-students/admissions/PhD/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } else { %>
		<a href="http://www.ist.utl.pt">IST</a> &gt;
		<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX_HAS_CONTEXT_PREFIX %><a href="http://www.ist.utl.pt/pt/candidatos/candidaturas/doutoramentos/"><bean:message bundle="PHD_RESOURCES" key="label.phd.candidacy.institution.breadcumbs.phd.program" /></a> &gt;
	<% } %>
	
	<bean:message key="title.submit.application" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%> 

<%--  ### Return Links / Steps Information (for multistep forms)  ### --%>

<%--  ### Error Messages  ### --%>
<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean" />
<%--  ### End of Error Messages  ### --%>

<%--  ### Operation Area ### --%>

<fr:form action="/applications/phd/phdProgramApplicationProcess.do?method=createApplication">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

	<div class="fs_form">
		
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.program.candidacy" bundle="PHD_RESOURCES"/></legend>
	
		<fr:edit id="candidacyBean.focus.area" name="candidacyBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessBean" bundle="PHD_RESOURCES">
				<fr:slot name="program" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.Action.phd.candidacy.publicProgram.institution.PhdProgramsProviderForPublicCandidacy" />
					<fr:property name="format" value="${name}" />
					<fr:property name="sortBy" value="name" /> 
				</fr:slot>
			</fr:schema>

			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="optionalMarkShown" value="true" />
			</fr:layout>
			<fr:destination name="invalid" path="/applications/phd/phdProgramApplicationProcess.do?method=fillPersonalDataInvalid" />
		</fr:edit>
	</fieldset>
		
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.personal.data" bundle="PHD_RESOURCES"/></legend>

		<fr:edit id="candidacyBean.personBean" name="candidacyBean" property="personBean">
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.person.PersonBean" bundle="PHD_RESOURCES">
				<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
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
			            <fr:property name="regexp" value="(\d{4,15})?"/>
			            <fr:property name="message" value="error.phone.invalidFormat"/>
			            <fr:property name="key" value="true"/>
			            <fr:property name="bundle" value="PHD_RESOURCES" />
			        </fr:validator>
			    </fr:slot>
				<fr:slot name="mobile">
			    	<fr:property name="size" value="20"/>
					<fr:property name="maxLength" value="15"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
			            <fr:property name="regexp" value="(\d{4,15})?"/>
			            <fr:property name="message" value="error.phone.invalidFormat"/>
			            <fr:property name="key" value="true"/>
			            <fr:property name="bundle" value="PHD_RESOURCES" />
			        </fr:validator>
			    </fr:slot>
				<fr:slot name="email" readOnly="true"/>
			</fr:schema>
			
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="optionalMarkShown" value="true" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/applications/phd/phdProgramApplicationProcess.do?method=fillPersonalDataInvalid" />
		</fr:edit>
	</fieldset>
	</div>
	
	<div class="fs_form">
	<fieldset style="display: block;">
		<legend><bean:message key="title.public.phd.institution.id" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>" bundle="PHD_RESOURCES"/></legend>
		<p class="mvert05"><span><bean:message key="message.phd.public.institution.id.note" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym() %>" bundle="PHD_RESOURCES"/></span></p>
		
		<fr:edit id="candidacyBean.institution.id" name="candidacyBean" schema="Public.PhdProgramCandidacyProcessBean.institution.id">
			<fr:layout name="tabular">
					<fr:property name="classes" value="thlight thleft thtop mtop05"/>
			        <fr:property name="columnClasses" value="width175px,,tdclear tderror1"/>
					<fr:property name="optionalMarkShown" value="false" />
			</fr:layout>
		
			<fr:destination name="invalid" path="/applications/phd/phdProgramApplicationProcess.do?method=fillPersonalDataInvalid" />
		</fr:edit>
	</fieldset>
	</div>
	
	<p class="mtop2"><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="PHD_RESOURCES" key="label.create.candidacy"/></html:submit></p>
	
	</fr:form>
