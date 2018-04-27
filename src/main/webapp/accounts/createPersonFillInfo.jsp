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
<%@page import="org.fenixedu.academic.domain.Country"%>
<%@ page isELIgnored="true"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<h2><bean:message key="create.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<p>
	<span class="error0"><!-- Error messages go here -->
		<html:messages id="message" message="true" bundle="MANAGER_RESOURCES">
			<bean:write name="message"/>
		</html:messages>
	</span>
	<p>
</logic:messagesPresent>

<fr:form action="/accounts/manageAccounts.do?method=createNewPerson">
	<fr:edit id="personBean" name="personBean" visible="false" />

	<p><b>a) <bean:message key="label.person.identification.info" bundle="MANAGER_RESOURCES"/></b></p>			
	<fr:edit nested="true" name="personBean" id="identification">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="documentIdNumber" required="true" />
			<fr:slot name="idDocumentType" required="true" >	
				<fr:property name="excludedValues" value="CITIZEN_CARD" />
			</fr:slot>
			<fr:slot name="identificationDocumentSeriesNumber" />
			<fr:slot name="documentIdEmissionLocation" />
			<fr:slot name="documentIdEmissionDate" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
			<fr:slot name="documentIdExpirationDate" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<p><b>b) <bean:message key="label.invitedPerson.personal.info" bundle="MANAGER_RESOURCES"/></b></p>			
			<bean:define id="personBean" name="personBean" type="org.fenixedu.academic.dto.person.PersonBean" />
			<% 
				if(personBean.getFiscalCountry() != null) {
				    pageContext.setAttribute("countryCode", personBean.getFiscalCountry().getCode());
				} else {
				    pageContext.setAttribute("countryCode", Country.readDefault().getCode());
				}
			%>
	
	<fr:edit nested="true" name="personBean" id="personal">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="givenNames" required="true" />
			<fr:slot name="familyNames" />
			<fr:slot name="gender" required="true" />
			<fr:slot name="fiscalCountry" key="label.fiscalCountry" bundle="MANAGER_RESOURCES" required="true" layout="menu-select-postback">
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CountryProvider" />
				<fr:property name="format" value="${name} (${code})"/>
				<fr:property name="sortBy" value="name"/>
				<fr:property name="destination" value="fiscalCountryPostback" />
			</fr:slot>			
			<fr:slot name="socialSecurityNumber" required="true" >
				<fr:validator name="org.fenixedu.ulisboa.specifications.ui.renderers.validators.FiscalCodeValidator" >
					<fr:property name="countryCode" value="<%= (String) pageContext.getAttribute("countryCode") %>" />
				</fr:validator>
			</fr:slot>
			<fr:slot name="profession"/>
			<fr:slot name="maritalStatus"/>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
		<fr:destination name="invalid" path='<%="/accounts/manageAccounts.do?method=invalid" %>'/>
		<fr:destination name="fiscalCountryPostback" path='<%="/accounts/manageAccounts.do?method=createNewPersonPostback" %>' />
	</fr:edit>

	<p><b>c) <bean:message key="label.invitedPerson.filiation.info" bundle="MANAGER_RESOURCES"/></b></p>			
	<fr:edit nested="true" name="personBean" id="filiation">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="countryOfBirth" layout="menu-select"> 
				<fr:property name="format" value="\${name}"/>
				<fr:property name="sortBy" value="name=asc" />
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
			</fr:slot>
			<fr:slot name="nationality" layout="menu-select"> 
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.CountryProvider"/> 
				<fr:property name="format" value="\${nationality}"/>
				<fr:property name="sortBy" value="nationality=asc" />
			</fr:slot>
			<fr:slot name="dateOfBirth" validator="pt.ist.fenixWebFramework.renderers.validators.DateValidator"/>
			<fr:slot name="parishOfBirth">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="districtSubdivisionOfBirth">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="districtOfBirth">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="fatherName" key="label.fatherName">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="motherName" key="label.motherName">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<p><b>d) <bean:message key="label.invitedPerson.residence.info" bundle="MANAGER_RESOURCES"/></b></p>			
	<fr:edit nested="true" name="personBean" id="residence">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="countryOfResidence" layout="menu-select" > 
				<fr:property name="format" value="\${name}"/>
				<fr:property name="sortBy" value="name=asc" />
				<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
			</fr:slot>
			<fr:slot name="address" >
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="areaCode"/>
			<fr:slot name="areaOfAreaCode" />
			<fr:slot name="area"/>
			<fr:slot name="parishOfResidence" >
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="districtSubdivisionOfResidence">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="districtOfResidence" >
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<p><b>e) <bean:message key="label.invitedPerson.contacts.info" bundle="MANAGER_RESOURCES"/></b></p>
	<fr:edit nested="true" name="personBean" id="contacts">
		<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="phone">
				<fr:property name="size" value="15"/>
				<fr:property name="maxLength" value="15"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
					<fr:property name="regexp" value="(\d{4,15})?"/>
					<fr:property name="message" value="error.phone.invalidFormat"/>
					<fr:property name="key" value="true"/>
				</fr:validator>
			</fr:slot>
			<fr:slot name="mobile">
				<fr:property name="size" value="15"/>
				<fr:property name="maxLength" value="15"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
					<fr:property name="regexp" value="(\d{4,15})?"/>
					<fr:property name="message" value="error.phone.invalidFormat"/>
					<fr:property name="key" value="true"/>
				</fr:validator>
			</fr:slot>
			<fr:slot name="workPhone">
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
					<fr:property name="regexp" value="(\d{4,15})?" />
					<fr:property name="message" value="error.phone.invalidFormat" />
					<fr:property name="key" value="true" />
				</fr:validator>
			</fr:slot>
			<fr:slot name="email" >
				<fr:property name="size" value="30"/>
				<fr:property name="maxLength" value="100"/>
			</fr:slot>
			<fr:slot name="webAddress">
				<fr:property name="size" value="50"/>
				<fr:property name="maxLength" value="200"/>
				<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.UrlValidator">
					<fr:property name="required" value="false" />
				</fr:validator>
			</fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1"/>
			<fr:property name="columnClasses" value=",,noborder"/>
		</fr:layout>
	</fr:edit>

	<html:submit><bean:message key="button.submit" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>