<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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
	<html:hidden property="candidacyNumber"/>
	<br/>
	<p>
		<html:submit><bean:message key="button.printRegistrationInformation" bundle="ADMIN_OFFICE_RESOURCES" /></html:submit>
	</p>
</html:form>	
