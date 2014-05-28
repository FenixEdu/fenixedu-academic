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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />


<em><bean:message key="label.person.main.title" /></em>
<p><html:link page="/visualizePersonalInfo.do"><bean:message bundle="APPLICATION_RESOURCES" key="label.return"/></html:link></p>
 <h2><bean:message bundle="APPLICATION_RESOURCES" key="label.contact.validation.title"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="infoop2"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>


<logic:notPresent name="isPhysicalAddress">
<logic:present name="valid">
	<logic:equal name="valid" value="true">
		<bean:define id="profileLink">
			<html:link page="/visualizePersonalInfo.do"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.success.profile"/></html:link>
		</bean:define>
		<span class="success0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.success" arg0="<%=profileLink%>"/></span>
	</logic:equal>
	<logic:equal name="valid" value="false">
		<logic:notEqual name="tries" value="3">
			<bean:define id="availableTries">
				<bean:write name="tries"/>
			</bean:define>
			<p><span class="error0"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.invalid" arg0="<%=availableTries%>"/></span></p>
		</logic:notEqual>
	</logic:equal>
</logic:present>
<logic:equal name="valid" value="false">
	<logic:greaterThan name="tries" value="0">
		<form action="<%= request.getContextPath() + "/person/partyContacts.do"%>" method="post">
			<input type="hidden" name="method" value="inputValidationCode"/>
			<input type="hidden" name="partyContactValidation" value="<%= request.getAttribute("partyContactValidation") %>"/>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.token"/><input name="validationCode" type="text"/>
			<input type="submit" value="Validar">
		</form>
		<p class="mbottom2">
		<logic:equal name="canValidateRequests" value="true">
			<bean:define id="tokenRequestURL">
			<html:link page="/partyContacts.do?method=requestValidationToken" paramId="partyContactValidation" paramName="partyContactValidation">
				<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.request.token.submit"/>
			</html:link>
			</bean:define>
			<p>
			<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.contact.validation.request.token" arg0="<%= tokenRequestURL %>"/>
			</p>
		</logic:equal>
		</p>
	</logic:greaterThan>
</logic:equal>
</logic:notPresent>

<logic:present name="isPhysicalAddress">
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
	<div class="mtop2">
	<fr:form action="/partyContacts.do?method=validatePhysicalAddress" encoding="multipart/form-data">
		<fr:edit id="physicalAddressBean" name="physicalAddressBean" schema="contacts.validate.PhysicalAddress">
			<fr:layout name="tabular-editable">
				<fr:property name="columnClasses" value=",,tderror1"/>
			</fr:layout>
			<fr:destination name="invalid" path="/partyContacts.do?method=validatePhysicalAddressInvalid"/>
		</fr:edit>
		<html:submit styleId="submitBtn"><bean:message key="button.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
	</div>
</logic:present>
