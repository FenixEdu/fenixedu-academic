<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.dataTransferObject.contacts.*"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>" /></h2>

<%
PartyContactBean partyContact = (PartyContactBean) request.getAttribute("partyContact");
request.setAttribute("isPhone", partyContact instanceof PhoneBean || partyContact instanceof MobilePhoneBean);
request.setAttribute("isEmail", partyContact instanceof EmailAddressBean);
request.setAttribute("isPhysicalAddress", partyContact instanceof PhysicalAddressBean);
%>



<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
    <p><span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<table class="mvert1 tdtop">
		<tbody>
			<tr>
				<td>
				<!--   <div style="padding: 0 2em;">-->
                    <div class="infoop2">
                        <logic:equal name="isPhone" value="true">
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

<fr:edit id="edit-contact" name="partyContact" action="/partyContacts.do?method=createPartyContact"
    schema="<%= "contacts." + partyContactClass + ".manage-student" %>">
    <fr:layout name="tabular-editable">
        <fr:property name="classes" value="tstyle5 thlight thright thmiddle" />
        <fr:property name="columnClasses" value=",,tdclear tderror1" />
    </fr:layout>
    <fr:destination name="postback-set-public"
        path="/partyContacts.do?method=postbackSetPublic&form=create" />
    <fr:destination name="postback-set-elements"
        path="/partyContacts.do?method=postbackSetElements&form=create" />
    <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create" />
    <fr:destination name="cancel" path="/partyContacts.do?method=backToShowInformation" />
</fr:edit>