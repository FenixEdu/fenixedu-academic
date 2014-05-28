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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.apache.struts.action.ActionMessages" %>
<%@ page import="java.util.Locale"%>

<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcess" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.mobility.MobilityIndividualApplicationProcessBean" %>
<%@ page import="net.sourceforge.fenixedu.domain.candidacyProcess.erasmus.StorkAttributeType" %>


<%!static String f(String value, Object ... args) {
    	return String.format(value, args);
	}%>

<html:xhtml/>

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%=request.getContextPath() + "/publico" + mappingPath + ".do"%></bean:define>
<bean:define id="applicationInformationLinkDefault" name="application.information.link.default"/>
<bean:define id="applicationInformationLinkEnglish" name="application.information.link.english"/>

<script src="<%=request.getContextPath() + "/javaScript/jquery/jquery.js"%>" type="text/javascript" ></script>

<bean:define id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" type="MobilityIndividualApplicationProcessBean"/>


<logic:notEmpty name="individualCandidacyProcessBean" property="personBean.gender">
	<script language="javascript">
		$(document).ready(function() {
			var inputReadOnly = $('input[name$="gender"] + input[readonly="readonly"]');
			
			if(inputReadOnly != null) {
				inputReadOnly.val(<%= "'"  + individualCandidacyProcessBean.getPersonBean().getGender().toLocalizedString() + "'" %>);
			}
		})
	</script>
</logic:notEmpty>

<div class="breadcumbs">
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en">NMCI</a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="http://gri.ist.utl.pt/en/ist/">Study at <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href='<%= f("%s/candidacies/erasmus", request.getContextPath()) %>'><bean:message key="title.application.name.mobility" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="erasmus.title.application.submission" bundle="CANDIDATE_RESOURCES" />
</div>

<h1><bean:write name="application.name"/></h1>
<bean:define id="mobilityProgram" name="individualCandidacyProcessBean" property="mobilityStudentDataBean.selectedMobilityProgram.registrationAgreement.description"/>
<h1><strong><bean:write name="mobilityProgram"/></strong></h1>

<bean:define id="individualCandidacyProcess" name="individualCandidacyProcessBean" property="individualCandidacyProcess" type="MobilityIndividualApplicationProcess"/>
<bean:define id="individualCandidacyProcessOID" name="individualCandidacyProcess" property="OID"/>

<p><a href='<%= fullPath + "?method=backToViewCandidacy&individualCandidacyProcess=" + individualCandidacyProcessOID %>'>« <bean:message key="label.back" bundle="CANDIDATE_RESOURCES"/></a></p>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="true">
	<p><bean:message key="message.application.employee.edition.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateEmployee" value="false">
<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="true">
	<p><bean:message key="message.application.person.with.roles.forbidden" bundle="CANDIDATE_RESOURCES"/></p>
</logic:equal>

<logic:equal name="individualCandidacyProcessBean" property="individualCandidacyProcess.isCandidateWithRoles" value="false">
<%--
<p><span><bean:message key="message.all.fields.are.required" bundle="CANDIDATE_RESOURCES"/></span></p>
--%>
<html:messages id="message" message="true" bundle="APPLICATION_RESOURCES" property="<%= ActionMessages.GLOBAL_MESSAGE %>">
	<span class="error0"> <bean:write name="message" /> </span>
	<br />
</html:messages>
<fr:hasMessages for="CandidacyProcess.personalDataBean" type="conversion">
	<ul class="nobullet list6">
		<fr:messages>
			<li><span class="error0"><fr:message/></span></li>
		</fr:messages>
	</ul>
</fr:hasMessages>

<html:messages id="message" message="true" bundle="CANDIDATE_RESOURCES" property="individualCandidacyMessages">
	<p><span class="error0"><bean:write name="message"/></span></p>
</html:messages>

<fr:form action='<%= mappingPath + ".do?method=editCandidacyProcess" %>' >
	<fr:edit id="individualCandidacyProcessBean" name="individualCandidacyProcessBean" visible="false" />
	
	<h2 style="margin-top: 1em;"><bean:message key="title.personal.data" bundle="CANDIDATE_RESOURCES"/></h2>
	
	<fr:edit id="candidacyProcess.personalDataBean"
		name="individualCandidacyProcessBean"
		property="personBean">
		
			<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.person.PersonBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="name" key="label.name" >
					<fr:property name="readOnly" value="<%= String.valueOf(individualCandidacyProcess.getPersonalFieldsFromStork().getTypes().contains(StorkAttributeType.STORK_NAME)) %>" />
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
						<fr:property name="type" value="character"/>
						<fr:property name="length" value="100"/>
					</fr:validator>
					<fr:property name="size" value="50"/>
					<fr:property name="maxlength" value="100"/>		
				</fr:slot>
						
				<fr:slot name="gender" key="label.gender" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
					<fr:property name="readOnly" value="<%= String.valueOf(individualCandidacyProcess.getPersonalFieldsFromStork().getTypes().contains(StorkAttributeType.STORK_GENDER)) %>" />
				</fr:slot>

				<fr:slot name="dateOfBirth" key="label.dateOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="readOnly" value="<%= String.valueOf(individualCandidacyProcess.getPersonalFieldsFromStork().getTypes().contains(StorkAttributeType.STORK_BIRTHDATE)) %>" />
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.AdvancedDateValidator">
						<fr:property name="validationPeriod" value="PAST"/>
						<fr:property name="bundle" value="CANDIDATE_RESOURCES"/>
						<fr:property name="message" value="error.birth.date.not.less.actual.date"/>
					</fr:validator>	
					<fr:property name="size" value="15"/>
					<fr:property name="maxLength" value="10"/>
				</fr:slot>	
	
				<fr:slot name="documentIdNumber" key="label.identificationNumber" >
					<fr:property name="readOnly" value="<%= String.valueOf(individualCandidacyProcess.getPersonalFieldsFromStork().getTypes().contains(StorkAttributeType.STORK_EIDENTIFIER)) %>" />
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
						<fr:property name="type" value="character"/>
						<fr:property name="length" value="100"/>
					</fr:validator>
					<fr:property name="size" value="50"/>
					<fr:property name="maxlength" value="100"/>			
				</fr:slot>
	
				<fr:slot name="nationality" layout="menu-select" key="label.nationality" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="readOnly" value="<%= String.valueOf(individualCandidacyProcess.getPersonalFieldsFromStork().getTypes().contains(StorkAttributeType.STORK_NATIONALITY)) %>" />
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.CountryProvider"/>
					<fr:property name="format" value="${countryNationality}" />
					<fr:property name="sortBy" value="countryNationality"/>
				</fr:slot>
	
				<fr:slot name="address" key="label.address">
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
						<fr:property name="type" value="character"/>
						<fr:property name="length" value="100"/>
					</fr:validator>
					<fr:property name="size" value="50"/>
					<fr:property name="maxlength" value="100"/>
				</fr:slot>
	
				<fr:slot name="areaCode" key="label.areaCode">
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
						<fr:property name="type" value="character"/>
						<fr:property name="length" value="10"/>
					</fr:validator>
					<fr:property name="size" value="15"/>
				</fr:slot>
	
				<fr:slot name="area" key="label.area" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:validator name="net.sourceforge.fenixedu.presentationTier.renderers.validators.TextLengthValidator">
						<fr:property name="type" value="character"/>
						<fr:property name="length" value="40"/>
					</fr:validator>
					<fr:property name="size" value="40"/>
				</fr:slot>
	
				<fr:slot name="countryOfResidence" key="label.countryOfResidence" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="format" value="${localizedName}"/>
					<fr:property name="sortBy" value="localizedName=asc" />
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
			
				<fr:slot name="phone" key="label.phone">
			    	<fr:property name="size" value="15"/>
					<fr:property name="maxLength" value="15"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
			            <fr:property name="regexp" value="(\+?\d{4,15})?"/>
			            <fr:property name="message" value="error.phone.invalidFormat"/>
			            <fr:property name="key" value="true"/>
			            <fr:property name="bundle" value="APPLICATION_RESOURCES" />
			        </fr:validator>
			    </fr:slot>
			</fr:schema>
		
		
			<fr:layout name="tabular">
				<fr:property name="classes" value="thlight thleft"/>
		        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
			</fr:layout>
			
		<fr:destination name="invalid" path='<%= mappingPath + ".do?method=editCandidacyProcessInvalid" %>' />
	</fr:edit>
	
	<p class="mtop15">	
		<html:submit><bean:message key="button.submit" bundle="APPLICATION_RESOURCES" /></html:submit>
	</p>
</fr:form>
</logic:equal>
</logic:equal>
