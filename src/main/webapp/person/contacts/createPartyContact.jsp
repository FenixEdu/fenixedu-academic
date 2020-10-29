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
<%@page import="org.fenixedu.academic.ui.struts.action.externalServices.PhoneValidationUtils"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="org.fenixedu.academic.dto.contacts.*"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />

<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>" /></h2>

<%
PartyContactBean partyContact = (PartyContactBean) request.getAttribute("partyContact");
request.setAttribute("isPhone", (partyContact instanceof PhoneBean || partyContact instanceof MobilePhoneBean) && PhoneValidationUtils.getInstance().shouldRun());
request.setAttribute("isEmail", partyContact instanceof EmailAddressBean);
request.setAttribute("isPhysicalAddress", partyContact instanceof PhysicalAddressBean);
request.setAttribute("hideValidationWarning", !partyContact.isToBeValidated());
%>


<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>


<logic:notEqual name="hideValidationWarning" value="true">
<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td>
				<!--   <div style="padding: 0 2em;">-->
                    <div class="infoop2">
                        <logic:equal name="isPhone" value="true">
                        	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.info.Phone"/>
						</logic:equal>
						<logic:equal name="isPhoneNoValidation" value="true">
                        	<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.info.Phone"/>
						</logic:equal>
						<logic:equal name="isEmail" value="true">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.info.EmailAddress"/>
						</logic:equal>
						<logic:equal name="isPhysicalAddress" value="true">
							<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.info.PhysicalAddress"/>  
						</logic:equal>
                    </div>
                </td>
            </tr>
        </tbody>
</table>
</logic:notEqual>

<logic:equal name="isPhone" value="true">
	<bean:define id="confirm">
		<bean:message  bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.confirm.Phone" />
	</bean:define>
	<script type="text/javascript">
	 $(document).ready(function() {
		 $('#edit-contact').submit(function() {
					return confirm('<%= confirm %>');
			})
		 });
	</script>
</logic:equal>

<logic:equal name="partyContactClass" value="PhysicalAddress">
		<bean:define id="physicalAddressBean"  name="partyContact" type="org.fenixedu.academic.dto.contacts.PhysicalAddressBean" />

		<fr:edit id="edit-contact" name="partyContact" action="/partyContacts.do?method=createPartyContact">
	       	<fr:schema type="org.fenixedu.academic.dto.contacts.PhysicalAddressBean" bundle="ACADEMIC_OFFICE_RESOURCES">
				<fr:slot name="type" required="true" />
				<fr:slot name="defaultContact" key="label.partyContacts.defaultContact" />
				
				<fr:slot name="countryOfResidence" layout="menu-select-postback" validator="pt.ist.fenixWebFramework.renderers.validators.RequiredValidator">
					<fr:property name="providerClass" value="org.fenixedu.academic.ui.renderers.providers.DistinctCountriesProvider" />
					<fr:property name="format" value="${localizedName.content}" />
					<fr:property name="destination" value="postback-select-country" />
					<fr:property name="sortBy" value="name=asc" />
				</fr:slot>
	       		
				<fr:slot name="address" required="true">
					<fr:property name="size" value="50" />
				</fr:slot>
				
				<fr:slot name="area">
				</fr:slot>
				
				<fr:slot name="areaCode">
					<fr:property name="size" value="10" />
					<% if(physicalAddressBean.getCountryOfResidence() != null && physicalAddressBean.getCountryOfResidence().isDefaultCountry()) { %>
					<fr:validator name="pt.ist.fenixWebFramework.renderers.validators.RegexpValidator">
						<fr:property name="regexp" value="\d{4}-\d{3}" />
						<fr:property name="message" value="error.areaCode.invalidFormat" />
						<fr:property name="key" value="true" />
						<fr:property name="bundle" value="ACADEMIC_OFFICE_RESOURCES" />
					</fr:validator>
					<% } %>
				</fr:slot>

				<% if(physicalAddressBean.getCountryOfResidence() != null && physicalAddressBean.getCountryOfResidence().isDefaultCountry()) { %>
				<fr:slot name="parishOfResidence" required="true" />
				<% } %>

				<% if(physicalAddressBean.getCountryOfResidence() == null || physicalAddressBean.getCountryOfResidence().isDefaultCountry()) { %>
				<fr:slot name="districtSubdivisionOfResidence" required="true" />
				<% } %>

				<% if(physicalAddressBean.getCountryOfResidence() != null && !physicalAddressBean.getCountryOfResidence().isDefaultCountry()) { %>
				<fr:slot name="districtSubdivisionOfResidence" required="true" key="label.districtSubdivisionOfResidence.city" bundle="ACADEMIC_OFFICE_RESOURCES" />
				<% } %>

				<% if(physicalAddressBean.getCountryOfResidence() != null && physicalAddressBean.getCountryOfResidence().isDefaultCountry()) { %>
				<fr:slot name="districtOfResidence" required="true" />
				<% } %>
				
	       	</fr:schema>
	        <fr:layout name="tabular-editable">
	            <fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle" />
	            <fr:property name="columnClasses" value=",,tdclear tderror1" />
	        </fr:layout>
	        <fr:destination name="postback-select-country" path="/partyContacts.do?method=postbackSelectCountry&form=create" />
	        <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create"/>
		    <fr:destination name="cancel" path="/partyContacts.do?method=backToShowInformation" />
		</fr:edit>
			
</logic:equal>

<logic:notEqual name="partyContactClass" value="PhysicalAddress">
<fr:edit id="edit-contact" name="partyContact" action="/partyContacts.do?method=createPartyContact"
    schema="<%= "contacts." + partyContactClass + ".manage-student" %>">
    <fr:layout name="tabular-editable">
        <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
        <fr:property name="columnClasses" value=",,tdclear tderror1" />
    </fr:layout>
    <fr:destination name="postback-set-public" path="/partyContacts.do?method=postbackSetPublic&form=create" />
    <fr:destination name="postback-set-elements" path="/partyContacts.do?method=postbackSetElements&form=create" />
    <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create" />
    <fr:destination name="cancel" path="/partyContacts.do?method=backToShowInformation" />
</fr:edit>
</logic:notEqual>
