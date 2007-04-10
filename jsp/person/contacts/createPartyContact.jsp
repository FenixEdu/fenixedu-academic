<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>
<bean:define id="partyContactName" name="partyContactName" />

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="<%= "label.partyContacts.add" +  partyContactName %>" /></h2>

<fr:form action="/partyContacts.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createPartyContact"/>

	<bean:define id="person" name="UserView" property="person" />
	
	<fr:create schema="<%= "contacts." + partyContactName + ".manage-student" %>" type="<%= "net.sourceforge.fenixedu.domain.contacts." + partyContactName  %>" >
		<fr:layout name="tabular-editable" >
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:hidden slot="party" name="person" />
	</fr:create>
	
	<p>
		<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:submit>
		<html:cancel onclick="this.form.method.value='backToShowInformation';"><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:cancel>
	</p>
</fr:form>
