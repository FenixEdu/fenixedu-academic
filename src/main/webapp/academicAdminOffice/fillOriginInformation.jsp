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

<bean:define id="precedentDegreeInformationBean" name="precedentDegreeInformationBean" type="net.sourceforge.fenixedu.dataTransferObject.candidacy.PrecedentDegreeInformationBean"/>

<fr:form action="/createStudent.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareShowCreateStudentConfirmation"/>
	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformationBean" visible="false" />
	
	<h3 class="mtop15 mbottom025"><bean:message key="label.person.title.originInfo" bundle="ACADEMIC_OFFICE_RESOURCES" /></h3>
	<fr:edit id="originInformation" name="originInformationBean">
		<fr:schema type="net.sourceforge.fenixedu.dataTransferObject.candidacy.OriginInformationBean" bundle="ACADEMIC_OFFICE_RESOURCES" >
			<fr:slot name="dislocatedFromPermanentResidence"  />
		   	<fr:slot name="schoolTimeDistrictSubdivisionOfResidence" layout="autoComplete">
				<fr:property name="size" value="50"/>
				<fr:property name="labelField" value="name"/>
				<fr:property name="format" value="${name} - (${district.name})"/>
				<fr:property name="indicatorShown" value="true"/>		
				<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchDistrictSubdivisions"/>
				<fr:property name="args" value="slot=name,size=20"/>
				<fr:property name="className" value="net.sourceforge.fenixedu.domain.DistrictSubdivision"/>
				<fr:property name="minChars" value="2"/>
			</fr:slot>	    
		    <fr:slot name="grantOwnerType"/>
		   	<fr:slot name="grantOwnerProviderUnitName" layout="autoComplete">
				<fr:property name="size" value="50"/>
				<fr:property name="labelField" value="unit.name"/>
				<fr:property name="indicatorShown" value="true"/>
				<fr:property name="provider" value="net.sourceforge.fenixedu.applicationTier.Servico.commons.searchers.SearchExternalUnits"/>
				<fr:property name="args" value="slot=name,size=50"/>
				<fr:property name="className" value="net.sourceforge.fenixedu.domain.organizationalStructure.UnitName"/>
				<fr:property name="minChars" value="1"/>
				<fr:property name="rawSlotName" value="grantOwnerProviderName"/>
			</fr:slot>
			<% if(precedentDegreeInformationBean.getSchoolLevel().isHighSchoolOrEquivalent()) { %>
				<fr:slot name="highSchoolType" layout="menu-select">
					<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.HighSchoolTypesProvider" />
					<fr:property name="eachLayout" value="this-does-not-exist" />
				</fr:slot>
			<% } %>
			<fr:slot name="motherSchoolLevel" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
				<fr:property name="eachLayout" value="this-does-not-exist" />
			</fr:slot>
			<fr:slot name="motherProfessionType" />
			<fr:slot name="motherProfessionalCondition" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides"/>
			</fr:slot>
		
			<fr:slot name="fatherSchoolLevel" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.candidacy.SchoolLevelTypeForStudentHouseholdProvider" />
				<fr:property name="eachLayout" value="this-does-not-exist" />
			</fr:slot>
			<fr:slot name="fatherProfessionType"/>
			<fr:slot name="fatherProfessionalCondition" layout="menu-select">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ProfessionalSituationConditionTypeProviderForRaides"/>
			</fr:slot>	    
		</fr:schema>
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	        <fr:property name="requiredMarkShown" value="true" />
		</fr:layout>
		<fr:destination name="invalid" path="/createStudent.do?method=prepareShowFillOriginInformation"/>
	</fr:edit>
	
	<p>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>	
	</p>
</fr:form>