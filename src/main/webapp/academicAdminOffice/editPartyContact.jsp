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
<%@page import="org.fenixedu.academic.dto.contacts.PhysicalAddressBean"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message key="<%= "label.partyContacts.edit" + partyContactClass %>"
    bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="/partyContacts.do">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"
        value="editPartyContact" />
    <bean:define id="studentID" type="java.lang.String" name="student" property="externalId" />
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID"
        value="<%= studentID.toString() %>" />


	<logic:equal name="partyContactClass" value="PhysicalAddress">
		<bean:define id="partyContact"  name="partyContact" type="org.fenixedu.academic.dto.contacts.PhysicalAddressBean" />

		<fr:edit id="edit-contact" name="partyContact">
	       	<fr:schema type="org.fenixedu.academic.dto.contacts.PhysicalAddressBean" 
	       		bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="type" required="true" />
				<fr:slot name="defaultContact" key="label.partyContacts.defaultContact" />
				
				<fr:slot name="countryOfResidence" layout="menu-select-postback"
					validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass"
						value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
					<fr:property name="format" value="${localizedName.content}" />
					<fr:property name="destination" value="postback-select-country" />
					<fr:property name="sortBy" value="name=asc" />
				</fr:slot>
	       		
				<fr:slot name="address" required="true">
					<fr:property name="size" value="50" />
				</fr:slot>
				<fr:slot name="area">
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<% } %>
				</fr:slot>
				<fr:slot name="areaCode">
					<fr:property name="size" value="10" />
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
						<fr:property name="regexp" value="\d{4}-\d{3}" />
						<fr:property name="message" value="error.areaCode.invalidFormat" />
						<fr:property name="key" value="true" />
						<fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES" />
					</fr:validator>
					<% } %>
				</fr:slot>
				<fr:slot name="areaOfAreaCode">
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<% } %>
				</fr:slot>
				<fr:slot name="parishOfResidence">
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<% } %>
				</fr:slot>
				<fr:slot name="districtSubdivisionOfResidence">
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<% } %>
				</fr:slot>
				<fr:slot name="districtOfResidence">
					<% if(partyContact.getCountryOfResidence() != null && partyContact.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator" />
					<% } %>
				</fr:slot>
	       	</fr:schema>
	        <fr:layout name="tabular-editable">
	            <fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle" />
	            <fr:property name="columnClasses" value=",,tdclear tderror1" />
	        </fr:layout>
	        <fr:destination name="postback-select-country"
	            path="/partyContacts.do?method=postbackSelectCountry&form=edit" />
	        <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=edit"/>
		</fr:edit>
	</logic:equal>
	
	<logic:notEqual name="partyContactClass" value="PhysicalAddress">
	    <fr:edit id="edit-contact" name="partyContact"
	        schema="<%= "contacts." + partyContactClass + ".manage" %>">
	        <fr:layout name="tabular-editable">
	            <fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle" />
	            <fr:property name="columnClasses" value=",,tdclear tderror1" />
	        </fr:layout>
	        <fr:destination name="postback-set-public"
	            path="/partyContacts.do?method=postbackSetPublic&form=edit" />
	        <fr:destination name="postback-set-elements"
	            path="/partyContacts.do?method=postbackSetElements&form=edit" />
	        <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=edit"/>
	    </fr:edit>
	</logic:notEqual>

    <p><html:submit>
        <bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:submit> <html:cancel onclick="this.form.method.value='backToShowInformation';">
        <bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:cancel></p>
</fr:form>