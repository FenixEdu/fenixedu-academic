<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml/>

<%-- ### Title #### --%>
<div class="breadcumbs">
	<jsp:include page="/phd/candidacy/publicProgram/institution/commonBreadcumbs.jsp" />

	<bean:message key="title.edit.personal.data" bundle="CANDIDATE_RESOURCES"/>
</div>

<h1><bean:message key="label.phd.institution.public.candidacy" bundle="PHD_RESOURCES" /></h1>
<%-- ### End of Title ### --%>

<%--  ###  Return Links / Steps Information(for multistep forms)  ### --%>

<bean:define id="processId" name="process" property="externalId" />

<fr:form id="editPersonalInformationForm" action="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editPersonalData&processId=" + processId %>">
	<fr:edit id="candidacyBean" name="candidacyBean" visible="false" />

	<logic:equal name="canEditCandidacy" value="true">

		<%--  ### Error Messages  ### --%>
		<jsp:include page="/phd/errorsAndMessages.jsp?viewStateId=candidacyBean.personBean" />
		<%--  ### End of Error Messages  ### --%>
		
		<%--  ### Operation Area ### --%>
		
		
		<h2 style="margin-top: 1em;"><bean:message key="title.public.phd.editPersonalInformation" bundle="PHD_RESOURCES"/></h2>
		
		<logic:notPresent name="candidacyBean">
			<em><bean:message key="label.php.public.candidacy.hash.not.found" bundle="PHD_RESOURCES"/></em>
		</logic:notPresent>
		
		<logic:present name="candidacyBean">
			<bean:define id="hash" name="process" property="candidacyHashCode.value" /> 
		
			<logic:equal name="canEditPersonalInformation" value="false">
				<h2><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
				<fr:view name="candidacyBean" property="personBean.person" schema="Public.PhdIndividualProgramProcess.view.person.simple">
					<fr:layout name="tabular">
						<fr:property name="classes" value="thlight thleft"/>
				        <fr:property name="columnClasses" value="width175px,,,,"/>
					</fr:layout>
				</fr:view>
				<em><bean:message key="message.check.personal.information.in.intranet" bundle="PHD_RESOURCES" /></em>
				<p class="mtop15">
					<html:link action="/applications/phd/phdProgramApplicationProcess.do?method=viewApplication" paramId="hash" paramName="hash">
						« <bean:message bundle="PHD_RESOURCES" key="label.back"/>
					</html:link>
				</p>
			</logic:equal>
		
			<logic:equal name="canEditPersonalInformation" value="true">
			
					<div class="fs_form">
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
									<fr:property name="choiceType" value="net.sourceforge.fenixedu.domain.Country"/>
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
									<fr:property name="classes" value="thlight thleft"/>
							        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
									<fr:property name="requiredMarkShown" value="false" />
									<fr:property name="optionalMarkShown" value="true" />
							</fr:layout>
						
							<fr:destination name="cancel" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=viewApplication&hash=" + hash %>" />
							<fr:destination name="invalid" path="<%= "/applications/phd/phdProgramApplicationProcess.do?method=editPersonalDataInvalid&processId=" + processId %>" />
						</fr:edit>
					</fieldset>
					</div>
					<p class="mtop15">
						<html:submit><bean:message bundle="PHD_RESOURCES" key="label.submit"/></html:submit>
						<html:cancel><bean:message bundle="PHD_RESOURCES" key="label.cancel"/></html:cancel>
					</p>
			</logic:equal>
		</logic:present>
	</logic:equal>

</fr:form>
