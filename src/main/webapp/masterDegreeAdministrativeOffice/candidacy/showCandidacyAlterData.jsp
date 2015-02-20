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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<fr:form action="/dfaCandidacy.do#precedentDegree">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="alterCandidacyData"/>
	<fr:edit id="personBean" name="personBean" visible="false"/>

	<h2><strong><bean:message key="label.person.title.personal.info" /></strong></h2>
	<fr:edit id="personData" name="personBean" schema="candidate.personalData-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.filiation" /></strong></h2>
	<fr:edit id="personFiliation" name="personBean" schema="candidate.filiation-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.addressInfo" /></strong></h2>
	<fr:edit id="personAddress" name="personBean" schema="candidate.address-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="label.person.title.contactInfo" /></strong></h2>
	<fr:edit id="personContacts" name="personBean" schema="candidate.contacts-freeEdit" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<a name="precedentDegree"> </a>
	<logic:present name="precedentDegreeInformation">
		
		<bean:define id="precedentDegreeInformation" name="precedentDegreeInformation" type="org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean"/>
		<% if(precedentDegreeInformation.getPrecedentDegreeInformation().getRegistration() == null) { %>
			<h2><strong><bean:message key="label.person.title.previousCompleteDegree" bundle="ADMIN_OFFICE_RESOURCES" /></strong></h2>		
			<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformation">
				<fr:schema type="org.fenixedu.academic.dto.candidacy.PrecedentDegreeInformationBean" bundle="CANDIDATE_RESOURCES">	
					<fr:slot name="schoolLevel" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" layout="menu-select-postback">
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.candidacy.SchoolLevelTypeForStudentProvider" />
						<fr:property name="destination" value="schoolLevelPostback" />
					</fr:slot>
					<fr:slot name="otherSchoolLevel" />
					<fr:slot name="country" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator"> 
						<fr:property name="format" value="${name}"/>
						<fr:property name="sortBy" value="name=asc" />
						<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
						<fr:property name="destination" value="schoolLevelPostback" />
					</fr:slot>
					<% if(precedentDegreeInformation.getSchoolLevel() != null && precedentDegreeInformation.getSchoolLevel().isHigherEducation()
						&& precedentDegreeInformation.getCountry() != null
						&& precedentDegreeInformation.getCountry().isDefaultCountry()) { %>
						<fr:slot name="institutionUnitName" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
							<fr:property name="size" value="50"/>
							<fr:property name="labelField" value="unit.name"/>
							<fr:property name="indicatorShown" value="true"/>
							<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchRaidesDegreeUnits"/>
							<fr:property name="args" value="slot=name,size=50"/>
							<fr:property name="minChars" value="3"/>
							<fr:property name="rawSlotName" value="institutionName"/>
						</fr:slot>
						<fr:slot name="raidesDegreeDesignation" layout="autoComplete" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					    	<fr:property name="size" value="50"/>
							<fr:property name="labelField" value="description"/>
							<fr:property name="indicatorShown" value="true"/>
							<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchRaidesDegreeDesignations"/>
							<fr:property name="args" value="<%= "slot=description,size=50,filterSchoolLevelName=" + ((precedentDegreeInformation.getSchoolLevel() != null) ? precedentDegreeInformation.getSchoolLevel().getName() : "null") + ",filterUnitOID=" + ((precedentDegreeInformation.getInstitution() != null) ? precedentDegreeInformation.getInstitution().getExternalId() : "null") %>"/>
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
				</fr:schema>
				<fr:layout name="tabular" >				
					<fr:property name="classes" value="tstyle4"/>
			        <fr:property name="columnClasses" value="listClasses,,"/>
			        
			        <fr:destination name="schoolLevelPostback" path="/dfaCandidacy.do?method=schoolLevelPostback" />
				</fr:layout>
			</fr:edit>
		<% } else { %>	
			<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformation" visible="false"/>
		<% } %>
	</logic:present>

	<html:submit onclick="this.form.action=removeAnchor(this.form.action);"><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>	
</fr:form>

<script type="text/javascript">
	function removeAnchor(action) {
		var anchorIndex = action.indexOf("#");
		return action.substring(0,anchorIndex);	
	}
</script>