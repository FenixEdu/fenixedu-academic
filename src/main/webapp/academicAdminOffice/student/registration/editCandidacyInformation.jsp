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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message  key="student.editCandidacyInformation" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

	<br/>
	
	<logic:equal name="personalInformationBean" property="valid" value="true">
		<p class="bluetxt">
			<em><bean:message key="label.candidacy.information.is.valid" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
		</p>
	</logic:equal>
	<logic:equal name="personalInformationBean" property="valid" value="false">
		<p class="redtxt">
			<em><bean:message key="label.candidacy.information.not.valid" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
		</p>
	</logic:equal>
	
	<logic:messagesPresent message="true">
		<ul class="nobullet list6">
			<html:messages id="messages" message="true" bundle="APPLICATION_RESOURCES">
				<li><span class="error0"><bean:write name="messages" /></span></li>
			</html:messages>
		</ul>
	</logic:messagesPresent>

	<bean:define id="studentID" name="personalInformationBean" property="student.externalId" />

	<fr:form action="/editCandidacyInformation.do#precedentDegree">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
		<fr:edit id="personalInformationBean" name="personalInformationBean" visible="false" />

		<bean:define id="personalInformationBean" name="personalInformationBean" type="org.fenixedu.academic.domain.candidacy.PersonalInformationBean"/>
		<fr:edit id="personalInformationBean.editPersonalInformation" name="personalInformationBean">
			<fr:schema bundle="APPLICATION_RESOURCES" type="org.fenixedu.academic.domain.candidacy.PersonalInformationBean">
				<fr:slot name="countryOfResidence" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="format" value="${localizedName.content}" />
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
					<fr:property name="destination" value="countryOfResidencePostback" />
				</fr:slot>
				<% if (personalInformationBean.isCountryOfResidenceDefaultCountry()) { %>
					<fr:slot name="districtSubdivisionOfResidence" layout="autoComplete">
						<fr:property name="size" value="50"/>
						<fr:property name="format" value="${name} - (${district.name})"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchDistrictSubdivisions"/>
						<fr:property name="args" value="slot=name,size=50"/>
						<fr:property name="minChars" value="1"/>
					</fr:slot>
				<% } %>
				<fr:slot name="dislocatedFromPermanentResidence" layout="radio" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="classes" value="dinline liinline nobullet"/>
				</fr:slot>
				<fr:slot name="schoolTimeDistrictSubdivisionOfResidence" layout="autoComplete">
					<fr:property name="size" value="50"/>
					<fr:property name="format" value="${name} - (${district.name})"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchDistrictSubdivisions"/>
					<fr:property name="args" value="slot=name,size=50"/>
					<fr:property name="minChars" value="1"/>
				</fr:slot>
				<fr:slot name="grantOwnerType" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.GrantOwnerTypesProvider" />
					<fr:property name="destination" value="grantOwnerTypePostback" />
				</fr:slot>
				<% if (personalInformationBean.isGrantProviderAnotherInstitution()) { %>
					<fr:slot name="grantOwnerProviderUnitName" layout="autoComplete">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="unit.name"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchExternalUnits"/>
						<fr:property name="args" value="slot=name,size=50"/>
						<fr:property name="minChars" value="1"/>
						<fr:property name="rawSlotName" value="grantOwnerProviderName"/>
					</fr:slot>
				<% } %>
				<fr:slot name="highSchoolType" layout="menu-select">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.HighSchoolTypesProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>
				<fr:slot name="maritalStatus" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="excludedValues" value="UNKNOWN" />
				</fr:slot>
				<fr:slot name="professionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
				<fr:slot name="professionalCondition" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides"/>
				</fr:slot>
				<fr:slot name="motherSchoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>
				<fr:slot name="motherProfessionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
				<fr:slot name="motherProfessionalCondition" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
				<fr:slot name="fatherSchoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>
				<fr:slot name="fatherProfessionType" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.ActiveProfessionTypeProvider" />
				</fr:slot>
				<fr:slot name="fatherProfessionalCondition" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"/>
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>

				<fr:destination name="countryOfResidencePostback" path="/editCandidacyInformation.do?method=countryOfResidencePostback" />
				<fr:destination name="grantOwnerTypePostback" path="/editCandidacyInformation.do?method=grantOwnerTypePostback" />
				<fr:destination name="invalid" path="/editCandidacyInformation.do?method=prepareEditInvalid" />
				<fr:destination name="cancel" path="<%= "/student.do?method=visualizeStudent&studentID=" + studentID %>" />
			</fr:layout>
		</fr:edit>
		
		<br/>
		<strong><bean:message  key="label.previous.degree.information" bundle="STUDENT_RESOURCES"/></strong>
		<a name="precedentDegree"> </a>
		<fr:edit id="personalInformationBean.editPrecedentDegreeInformation" name="personalInformationBean">
			<fr:schema type="org.fenixedu.academic.domain.candidacy.PersonalInformationBean" bundle="APPLICATION_RESOURCES">
				<fr:slot name="countryWhereFinishedPreviousCompleteDegree" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="format" value="${localizedName.content}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
					<fr:property name="destination" value="schoolLevelPostback" />
				</fr:slot>
				<fr:slot name="schoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select-postback">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
					<fr:property name="destination" value="schoolLevelPostback" />
				</fr:slot>
				<% if (personalInformationBean.isSchoolLevelOther()) { %>
				<fr:slot name="otherSchoolLevel" />
				<% } %>
				<% if (personalInformationBean.isHightSchoolCountryFieldRequired()) { %>
					<fr:slot name="countryWhereFinishedHighSchoolLevel" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="format" value="${localizedName.content}"/>
						<fr:property name="sortBy" value="name=asc" />
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
					</fr:slot>
				<% } %>
				<% if(personalInformationBean.isUnitFromRaidesListMandatory()) { %>
					<fr:slot name="institutionUnitName" layout="autoCompleteWithPostBack" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="unit.name"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchRaidesDegreeUnits"/>
						<fr:property name="args" value="slot=name,size=50"/>
						<fr:property name="minChars" value="4"/>
						<fr:property name="rawSlotName" value="institutionName"/>
						<fr:property name="destination" value="institutionPostBack"/>
					</fr:slot>
					<fr:slot name="raidesDegreeDesignation" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				    	<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="description"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchRaidesDegreeDesignations"/>
						<fr:property name="args" value="<%="slot=description,size=50,filterSchoolLevelName=" + ((personalInformationBean.getSchoolLevel() != null) ? personalInformationBean.getSchoolLevel().getName() : "null") + ",filterUnitOID=" + ((personalInformationBean.getInstitution() != null) ? personalInformationBean.getInstitution().getExternalId() : "null") %>"/>
						<fr:property name="minChars" value="3"/>
				    </fr:slot>
				<% } else { %>
					<fr:slot name="institutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="unit.name"/>
						<fr:property name="indicatorShown" value="true"/>		
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchExternalUnits"/>
						<fr:property name="args" value="slot=name,size=20"/>
						<fr:property name="minChars" value="2"/>
						<fr:property name="rawSlotName" value="institutionName"/>
					</fr:slot>	
				    <fr:slot name="degreeDesignation" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				    	<fr:property name="size" value="50"/>
						<fr:property name="maxLength" value="255"/>
				    </fr:slot>
			    <% } %>
				<fr:slot name="conclusionGrade">
			    	<fr:property name="size" value="2"/>
					<fr:property name="maxLength" value="2"/>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
			            <fr:property name="regexp" value="\d{2}"/>
			            <fr:property name="message" value="error.conclusionGrade.invalidFormat"/>
			            <fr:property name="key" value="true"/>
			            <fr:property name="bundle" value="CANDIDATE_RESOURCES"/>
			        </fr:validator>
				</fr:slot>    
			    <fr:slot name="conclusionYear">
			       	<fr:property name="size" value="4"/>
					<fr:property name="maxLength" value="4"/>
			        <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
			            <fr:property name="regexp" value="\d{4}"/>
			            <fr:property name="message" value="error.conclusionYear.invalidFormat"/>
			            <fr:property name="key" value="true"/>
			            <fr:property name="bundle" value="CANDIDATE_RESOURCES"/>
			        </fr:validator>
			    </fr:slot>	
			    <fr:slot name="degreeChangeOrTransferOrErasmusStudent" layout="radio-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" >
					<fr:property name="classes" value="dinline liinline nobullet"/>
					<fr:property name="destination" value="changePostback" />
				</fr:slot>			
			</fr:schema>
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thlight thleft mtop15" />
				<fr:property name="columnClasses" value="width300px,,tdclear tderror1"/>
				
				<fr:destination name="institutionPostBack" path="/editCandidacyInformation.do?method=prepareEditInstitutionPostback" />
				<fr:destination name="schoolLevelPostback" path="/editCandidacyInformation.do?method=schoolLevelPostback" />
				<fr:destination name="changePostback" path="/editCandidacyInformation.do?method=changePostback" />
				<fr:destination name="invalid" path="/editCandidacyInformation.do?method=prepareEditInvalid" />
				<fr:destination name="cancel" path="<%= "/student.do?method=visualizeStudent&studentID=" + studentID %>" />
			</fr:layout>
		</fr:edit>	
	
		<% if(personalInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) { %>
			<br/>
			<strong><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></strong>
			<fr:edit name="personalInformationBean" id="personalInformationBeanExternal">
				<fr:schema type="org.fenixedu.academic.domain.candidacy.PersonalInformationBean" bundle="ACADEMIC_OFFICE_RESOURCES" >		
					<fr:slot name="precedentInstitutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
						<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="unit.name"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchExternalUnitsWithScore"/>
						<fr:property name="args" value="slot=name,size=20"/>
						<fr:property name="minChars" value="3"/>
						<fr:property name="rawSlotName" value="precedentInstitutionName"/>
					</fr:slot>
				    <fr:slot name="precedentDegreeDesignationObject" layout="autoComplete">
				    	<fr:property name="showRequired" value="true"/>
				    	<fr:property name="size" value="50"/>
						<fr:property name="labelField" value="description"/>
						<fr:property name="indicatorShown" value="true"/>
						<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchRaidesDegreeDesignations"/>
						<fr:property name="args" value="slot=description,size=50"/>
						<fr:property name="minChars" value="3"/>
						<fr:property name="rawSlotName" value="precedentDegreeDesignation"/>
						<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
							<fr:property name="allowsCustom" value="true"/>
							<fr:property name="message" value="renderers.validator.required"/>
						</fr:validator>
				    </fr:slot>
				    <fr:slot name="precedentSchoolLevel" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				    	<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
				    </fr:slot>
				    <fr:slot name="otherPrecedentSchoolLevel" />
				    <fr:slot name="numberOfPreviousYearEnrolmentsInPrecedentDegree">
				    	<fr:property name="size" value="4"/>
						<fr:property name="maxLength" value="2"/>
					    <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				            <fr:property name="message" value="error.candidacy.numberOfPreviousYearEnrolmentsInPrecedentDegree.mandatory"/>
				            <fr:property name="key" value="true"/>
				            <fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES"/>
				        </fr:validator>				    	
				    </fr:slot>			
					<fr:slot name="mobilityProgramDuration"/>			
				</fr:schema>
				<fr:layout name="tabular" >
					<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
			        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
			        <fr:property name="requiredMarkShown" value="true" />			        
				</fr:layout>
			</fr:edit>
		<% } %>
	
		<html:submit onclick="this.form.action=removeAnchor(this.form.action);" bundle="HTMLALT_RESOURCES" altKey="submit.submit"><bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/></html:submit>
		<html:cancel><bean:message bundle="APPLICATION_RESOURCES" key="label.back"/></html:cancel>	
	
	</fr:form>
	
<script type="text/javascript">
	function removeAnchor(action) {
		var anchorIndex = action.indexOf("#");
		return action.substring(0,anchorIndex);	
	}
</script>	

