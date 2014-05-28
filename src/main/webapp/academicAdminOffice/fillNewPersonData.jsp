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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.student.create" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span></p>
</html:messages>

<fr:form action="/createStudent.do#precedentDegree">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareShowFillOriginInformation"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	
	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.personal.info" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personData" name="personBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.person.PersonBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="name" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="givenNames" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="familyNames" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
				<fr:property name="size" value="50" />
			</fr:slot>
			<fr:slot name="gender" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
			<fr:slot name="socialSecurityNumber" />
			<fr:slot name="professionType" />
			<fr:slot name="professionalCondition" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides"/>
			</fr:slot>
			<fr:slot name="profession" />
			<fr:slot name="maritalStatus">
				<fr:property name="excludedValues" value="UNKNOWN" />
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.identification" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personIdentification" name="personBean" schema="student.documentId-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.filiation" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personFiliation" name="personBean" schema="student.filiation-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.addressInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personAddress" name="personBean" schema="student.address-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>
	
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.contactInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="personContacts" name="personBean" schema="student.contacts-edit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>
	
	<bean:define id="precedentDegreeInformationBean" name="precedentDegreeInformationBean" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean"/>
	<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.previousCompleteDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<a name="precedentDegree"></a>
	<fr:edit name="precedentDegreeInformationBean" id="precedentDegreeInformation" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
			<fr:slot name="schoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
				<fr:property name="eachLayout" value="this-does-not-exist" />
				<fr:property name="destination" value="schoolLevel-postback" />
			</fr:slot>
			<fr:slot name="otherSchoolLevel" />
			<fr:slot name="country" layout="menu-select-postback" key="label.countryOfPrecedenceDegree" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > 
				<fr:property name="format" value="${name}"/>
				<fr:property name="sortBy" value="name=asc" />
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
				<fr:property name="destination" value="schoolLevel-postback" />
			</fr:slot>
			<% if (precedentDegreeInformationBean.isHighSchoolCountryFieldRequired()) { %>
				<fr:slot name="countryWhereFinishedHighSchoolLevel" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
					<fr:property name="format" value="${localizedName}"/>
					<fr:property name="sortBy" value="name=asc" />
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.DistinctCountriesProvider" />
				</fr:slot>
			<% } %>
			<% if(precedentDegreeInformationBean.isUnitFromRaidesListMandatory()) { %>
				<fr:slot name="institutionUnitName" layout="autoCompleteWithPostBack" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchRaidesDegreeUnits"/>
					<fr:property name="args" value="slot=name,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="rawSlotName" value="institutionName"/>
					<fr:property name="destination" value="institutionPostBack"/>
				</fr:slot>
				<fr:slot name="raidesDegreeDesignation" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			    	<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="description"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchRaidesDegreeDesignations"/>
					<fr:property name="args" value="<%="slot=description,size=50,filterSchoolLevelName=" + ((precedentDegreeInformationBean.getSchoolLevel() != null) ? precedentDegreeInformationBean.getSchoolLevel().getName() : "null") + ",filterUnitOID=" + ((precedentDegreeInformationBean.getInstitution() != null) ? precedentDegreeInformationBean.getInstitution().getExternalId() : "null") %>"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.raides.DegreeDesignation"/>
					<fr:property name="minChars" value="3"/>
			    </fr:slot>
			<% } else { %>
				<fr:slot name="institutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>		
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchExternalUnits"/>
					<fr:property name="args" value="slot=name,size=20"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="2"/>
					<fr:property name="rawSlotName" value="institutionName"/>
				</fr:slot>	
			    <fr:slot name="degreeDesignation" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			    	<fr:property name="size" value="50"/>
					<fr:property name="maxLength" value="255"/>
			    </fr:slot>
		    <% } %>
			<fr:slot name="conclusionGrade">
		    	<fr:property name="size" value="5"/>
				<fr:property name="maxLength" value="5"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
		            <fr:property name="regexp" value="\d{2}(.\d{1,2})?"/>
		            <fr:property name="message" value="error.conclusionGrade.invalidFormat"/>
		            <fr:property name="key" value="true"/>
		            <fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES" />
		        </fr:validator>
			</fr:slot>        
		    <fr:slot name="conclusionYear">
		       	<fr:property name="size" value="4"/>
				<fr:property name="maxLength" value="4"/>
		        <fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
		            <fr:property name="regexp" value="(\d{4}|)"/>
		            <fr:property name="message" value="error.conclusionYear.invalidFormat"/>
		            <fr:property name="key" value="true"/>
		        </fr:validator>
		    </fr:slot>  
			<fr:slot name="degreeChangeOrTransferOrErasmusStudent" layout="radio-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" > 
				<fr:property name="destination" value="schoolLevel-postback" />
			</fr:slot>			
		</fr:schema>

		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop05"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
			<fr:destination name="institutionPostBack" path="/createStudent.do?method=prepareEditInstitutionPostback" />
	        <fr:destination name="schoolLevel-postback" path="/createStudent.do?method=fillNewPersonDataPostback" />
	        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />
		</fr:layout>
	</fr:edit>	
	
	<% if(precedentDegreeInformationBean.isDegreeChangeOrTransferOrErasmusStudent()) { %>
		<h3 class="mtop1 mbottom025"><bean:message key="label.person.title.precedenceDegreeInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
		<fr:edit name="precedentDegreeInformationBean" id="precedentDegreeInformationExternal" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean">
			<fr:schema type="net.sourceforge.fenixedu.domain.candidacy.PersonalInformationBean" bundle="ACADEMIC_OFFICE_RESOURCES" >		
				<fr:slot name="precedentInstitutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="unit.name"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchExternalUnitsWithScore"/>
					<fr:property name="args" value="slot=name,size=20"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="rawSlotName" value="precedentInstitutionName"/>
				</fr:slot>
			    <fr:slot name="precedentDegreeDesignationObject" layout="autoComplete">
			    	<fr:property name="showRequired" value="true"/>
			    	<fr:property name="size" value="50"/>
					<fr:property name="labelField" value="description"/>
					<fr:property name="indicatorShown" value="true"/>
					<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchRaidesDegreeDesignations"/>
					<fr:property name="args" value="slot=description,size=50"/>
					<fr:property name="className" value="net.sourceforge.fenixedu.domain.raides.DegreeDesignation"/>
					<fr:property name="minChars" value="3"/>
					<fr:property name="rawSlotName" value="precedentDegreeDesignation"/>
					<fr:validator name="pt.ist.fenixWebFramework.rendererExtensions.validators.RequiredAutoCompleteSelectionValidator">
						<fr:property name="allowsCustom" value="true"/>
						<fr:property name="message" value="renderers.validator.required"/>
					</fr:validator>
			    </fr:slot>
			    <fr:slot name="precedentSchoolLevel" layout="menu-select" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
			    	<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
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
		        <fr:destination name="invalid" path="/createStudent.do?method=invalid" />	        
			</fr:layout>
		</fr:edit>
	<% } %>
	<p>
		<html:submit onclick="this.form.action=removeAnchor(this.form.action);"><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
	</p>
</fr:form>

<script type="text/javascript">
	function removeAnchor(action) {
		var anchorIndex = action.indexOf("#");
		return action.substring(0,anchorIndex);	
	}
</script>