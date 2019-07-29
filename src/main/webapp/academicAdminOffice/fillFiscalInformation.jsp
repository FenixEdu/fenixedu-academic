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

<%@page import="org.fenixedu.academic.FenixEduAcademicConfiguration"%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="link.student.editFiscalData" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="personBean" name="personBean" type="org.fenixedu.academic.dto.person.PersonBean" />

<fr:view name="personBean">
	<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="ACADEMIC_OFFICE_RESOURCES">
		<fr:slot name="givenNames" >
			<fr:property name="size" value="50" />
		</fr:slot>
		<fr:slot name="familyNames" >
			<fr:property name="size" value="50" />
		</fr:slot>
		<fr:slot name="gender" />
	</fr:schema>
	
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle1 thright thlight mtop0"/>
		<fr:property name="columnClasses" value="width14em,"/>
	</fr:layout>
</fr:view>

<logic:messagesPresent message="true">
    <ul class="nobullet list6">
        <html:messages id="messages" message="true">
            <li><span class="error0"><bean:write name="messages" /></span></li>
        </html:messages>
    </ul>
</logic:messagesPresent>

<p><em class="infoop2">
	<bean:message 
		key="message.warning.fillFiscalInformation.select.address.equal.fiscalNumber.country" 
		bundle="ACADEMIC_OFFICE_RESOURCES" 
		arg0="<%= FenixEduAcademicConfiguration.getConfiguration().getDefaultSocialSecurityNumber() %>" />
</em></p>

<fr:form action="/createStudent.do">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="validateFiscalInformation"/>

	<fr:edit id="executionDegree" name="executionDegreeBean" visible="false" />
	<fr:edit id="person" name="personBean" visible="false" />	
	<fr:edit id="chooseIngression" name="ingressionInformationBean" visible="false" />	
	<fr:edit id="precedentDegreeInformation" name="precedentDegreeInformationBean" visible="false" />

    <fr:edit id="editPersonBean" name="personBean">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="socialSecurityNumber" required="true" >
				<logic:notEmpty name="fiscalCountryCode">
				<bean:define id="fiscalCountryCode" name="fiscalCountryCode" type="java.lang.String" />
				<fr:validator name="org.fenixedu.ulisboa.specifications.ui.renderers.validators.FiscalCodeValidator">
					<fr:property name="countryCode" value="<%= fiscalCountryCode  %>" />
				</fr:validator>
				</logic:notEmpty>
			</fr:slot>
			
			<fr:slot name="usePhysicalAddress" bundle="ACADEMIC_OFFICE_RESOURCES" layout="radio-postback" 
				key="label.createStudent.fillFiscalInformation.usePhysicalAddress">
				<fr:property name="classes" value="liinline nobullet" />
				<fr:property name="destination" value="postback" />
			</fr:slot>
			
			<% if(personBean.getPerson() == null && personBean.isUsePhysicalAddress()) { %>
        	<fr:slot name="uiResidenceAddressForFiscalPresentationValue" readOnly="true" key="label.address" />
			<% } %>

			<% if(personBean.getPerson() != null  && personBean.isUsePhysicalAddress()) { %>
        	<fr:slot name="fiscalAddressInCreateRegistrationBean" layout="menu-select-postback" required="true" key="label.fiscalAddress" >
                <fr:property name="from" value="sortedValidAddressesBeansForFiscalDataInCreateRegistration" />
				<fr:property name="format" value="${uiFiscalPresentationValue} (${countryOfResidence.code})" />
				<fr:property name="destination" value="postback" />	
        	</fr:slot>
			<% } %>

			<% if(!personBean.isUsePhysicalAddress()) { %>

			<fr:slot name="fiscalAddressCountryOfResidence" layout="menu-select-postback" 
				validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.countryOfResidence" >
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
				<fr:property name="format" value="${name}"/>
				<fr:property name="sortBy" value="name"/>		
				<fr:property name="destination" value="postback" />
			</fr:slot>
			<fr:slot name="fiscalAddressAddress" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" key="label.address">
				<fr:property name="size" value="50"/>
			</fr:slot>

			<fr:slot name="fiscalAddressAreaCode" key="label.areaCode">
				<fr:property name="size" value="10"/>

				<% if(personBean.getFiscalAddressCountryOfResidence() != null && personBean.getFiscalAddressCountryOfResidence().isDefaultCountry()) { %>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
		        	<fr:property name="regexp" value="(\d{4}-\d{3})?"/>
		        	<fr:property name="message" value="error.areaCode.invalidFormat"/>
		           	<fr:property name="key" value="true"/>
		           	<fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES" />
		        </fr:validator>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
				<% } %>
			</fr:slot>

			<% if(personBean.getFiscalAddressCountryOfResidence() != null && personBean.getFiscalAddressCountryOfResidence().isDefaultCountry()) { %>
			<fr:slot name="fiscalAddressParishOfResidence" key="label.parishOfResidence" />
			<% } %>

			<% if(personBean.getFiscalAddressCountryOfResidence() == null || personBean.getFiscalAddressCountryOfResidence().isDefaultCountry()) { %>
		   	<fr:slot name="fiscalAddressDistrictSubdivisionOfResidenceObject" layout="autoComplete" key="label.districtSubdivisionOfResidenceObject.required" required="true">
				<fr:property name="size" value="50" />
				<fr:property name="format" value="${name} (${district.name})" />
				<fr:property name="indicatorShown" value="true" />
				<fr:property name="provider" value="org.fenixedu.academic.service.services.commons.searchers.SearchDistrictSubdivisions"/>
				<fr:property name="args" value="slot=name,size=20" />
				<fr:property name="minChars" value="2" />
			</fr:slot>	
			<% } %>
			
			<% if(personBean.getFiscalAddressCountryOfResidence() != null && !personBean.getFiscalAddressCountryOfResidence().isDefaultCountry()) { %>
			<fr:slot name="fiscalAddressDistrictSubdivisionOfResidence" required="true" key="label.districtSubdivisionOfResidence.city" bundle="ACADEMIC_OFFICE_RESOURCES" />
			<% } %>

			<% } %>
			
        </fr:schema>
	    <fr:layout name="tabular" >
            <fr:property name="classes" value="tstyle1 thlight thright mtop025"/>
            <fr:property name="columnClasses" value="width14em,,tdclear tderror1"/>
	    </fr:layout>
	    
        <fr:destination name="postback" path="/createStudent.do?method=fillFiscalInformationPostback" />
        <fr:destination name="invalid" path="/createStudent.do?method=fillFiscalInformationInvalid" />
    </fr:edit>
	
    <p><html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit></p>    
	
</fr:form>
