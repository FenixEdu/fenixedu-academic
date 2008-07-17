<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />
<bean:define id="partyContactClass" scope="request" name="partyContactClass" />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactClass %>"
    bundle="ACADEMIC_OFFICE_RESOURCES" /></h2>

<fr:form action="/partyContacts.do">
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"
        value="createPartyContact" />

    <bean:define id="studentID" type="java.lang.Integer" name="student" property="idInternal" />
    <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="studentID"
        value="<%= studentID.toString() %>" />

    <bean:define id="person" name="student" property="person" />

    <logic:notPresent name="partyContact">
        <fr:create schema="<%= "contacts." + partyContactClass + ".manage" %>"
            type="<%= "net.sourceforge.fenixedu.domain.contacts." + partyContactClass  %>">
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle5 thlight thright mtop025" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
            <fr:hidden slot="party" name="person" />
        </fr:create>
    </logic:notPresent>

    <logic:present name="partyContact">
        <fr:edit id="edit-contact" name="partyContact"
            schema="<%= "contacts." + partyContactClass + ".manage" %>">
            <fr:layout name="tabular-editable">
                <fr:property name="classes" value="tstyle5 thlight thright mtop025" />
                <fr:property name="columnClasses" value=",,tdclear tderror1" />
            </fr:layout>
            <fr:destination name="postback-set-public"
                path="/partyContacts.do?method=postbackSetPublic&form=create" />
            <fr:destination name="postback-set-elements"
                path="/partyContacts.do?method=postbackSetElements&form=create" />
            <fr:destination name="invalid" path="/partyContacts.do?method=invalid&form=create"/>
        </fr:edit>
    </logic:present>

    <p><html:submit>
        <bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:submit> <html:cancel onclick="this.form.method.value='backToShowInformation';">
        <bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" />
    </html:cancel></p>
</fr:form>
