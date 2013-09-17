<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><strong><bean:message key="message.candidacy.registerCandidacy.success" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<br/>
<h2><strong><bean:message key="label.person.title.personal.info" bundle="CANDIDATE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.show.candidacyWithIstUsername">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.registration.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" property="registration" schema="registration.short" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<html:form action="/dfaCandidacy.do?method=printRegistrationInformation" target="_blank">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidacyNumber" property="candidacyNumber"/>
	<br/>
	<p>
		<html:submit><bean:message key="button.printRegistrationInformation" bundle="ADMIN_OFFICE_RESOURCES" /></html:submit>
	</p>
</html:form>	
