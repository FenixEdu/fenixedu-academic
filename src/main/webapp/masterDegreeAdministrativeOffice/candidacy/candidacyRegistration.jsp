<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h2><strong><bean:message key="link.candidacy.registerCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.show.candidady">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.candidacy.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<html:form action="/dfaCandidacy.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="registerCandidacy" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.candidacyNumber" property="candidacyNumber" />
	
	<fr:edit name="registerCandidacyBean" schema="candidacy.short.forRegistration" >
		<fr:layout name="tabular" >
			<fr:property name="classes" value="tstyle4"/>
	        <fr:property name="columnClasses" value="listClasses,,"/>
		</fr:layout>
	</fr:edit>
	
	<h2><strong><bean:message key="message.candidacy.registerCandidacy.confirm" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" ><bean:message key="button.confirm" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit> 	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="document.forms[0].method.value='cancelRegisterCandidacy'" ><bean:message key="button.cancel" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
	
</html:form>