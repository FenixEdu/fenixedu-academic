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
<%@page import="org.fenixedu.academic.domain.contacts.PhysicalAddress"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />


<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<logic:equal name="partyContactClass" value="<%=PhysicalAddress.class.getSimpleName() %>">
	<bean:define id="changeAddressIRSFormURL">
		<a href="<%= request.getContextPath() %>/templates/Decl_CIRS_ART99.pdf">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.message.ADIST.form"/>
		</a>
	</bean:define>
	<p>
		<span class="infoop2">
			<bean:message key="label.contact.validation.message.ADIST" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= changeAddressIRSFormURL %>" />
		</span>
    </p>
</logic:equal>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>
<bean:define id="personID" name="partyContact" property="party.externalId" />

<fr:edit id="edit-contact"  name="partyContact" action="<%="/partyContacts.do?method=createPartyContact&personID="+personID%>"
    schema="<%= "contacts." + partyContactClass + ".manage" %>">
    <fr:layout name="tabular-editable">
        <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
        <fr:property name="columnClasses" value=",,tdclear tderror1" />
    </fr:layout>
    <fr:destination name="postback-set-public"
        path="/partyContacts.do?method=postbackSetPublic&form=create" />
    <fr:destination name="postback-set-elements"
        path="/partyContacts.do?method=postbackSetElements&form=create" />
    <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create" />
    <fr:destination name="cancel" path="<%="/partyContacts.do?method=backToShowInformation&personID"+personID%>" />
</fr:edit>