<%@page import="net.sourceforge.fenixedu.domain.contacts.PhysicalAddress"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />
<bean:define id="contactType" name="partyContact" property="type.name" />


<h2><bean:message key="<%= "label.partyContacts.edit" +  partyContactClass %>" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

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
<fr:edit id="edit-contact" name="partyContact" action="<%="/partyContacts.do?method=editPartyContact&personID=" + personID %>"
    schema="<%= "contacts." + (contactType.equals("INSTITUTIONAL") ? "Institutional." : "") + partyContactClass + ".manage" %>">
    <fr:layout name="tabular-editable">
        <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
        <fr:property name="columnClasses" value=",,tdclear tderror1" />
    </fr:layout>
    <fr:destination name="postback-set-public"
        path="/partyContacts.do?method=postbackSetPublic&form=edit" />
    <fr:destination name="postback-set-elements"
        path="/partyContacts.do?method=postbackSetElements&form=edit" />
    <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=edit"/>
    <fr:destination name="cancel" path="<%="/partyContacts.do?method=backToShowInformation&personID"+personID%>" />
</fr:edit>
