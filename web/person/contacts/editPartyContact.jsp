<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="partyContactClass" name="partyContact" property="class.simpleName" />

<em><bean:message key="label.person.main.title" /></em>
<h2><bean:message key="<%= "label.partyContacts.edit" +  partyContactClass %>" /></h2>

<fr:form action="/partyContacts.do">	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editPartyContact"/>

	<fr:edit name="partyContact" schema="<%= "contacts." + partyContactClass + ".manage-student" %>">
		<fr:layout name="tabular-editable" >
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle"/>
	        <fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="button.submit" /></html:submit>
		<html:cancel onclick="this.form.method.value='backToShowInformation';"><bean:message key="button.cancel" /></html:cancel>
	</p>
</fr:form>
