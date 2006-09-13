<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><strong><bean:message key="link.candidacy.registerCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.show.candidady">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>

<h2><strong><bean:message key="label.candidacy.title.detail" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<fr:view name="candidacy" schema="candidacy.short" >
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>


<html:form action="/dfaCandidacy.do">
	<html:hidden property="method" value="registerCandidacy" />
	<html:hidden property="candidacyNumber" />
	
	<h2><strong><bean:message key="message.candidacy.registerCandidacy.confirm" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
	
	<html:submit ><bean:message key="button.confirm" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit> 	
	<html:submit onclick="document.forms[0].method.value='cancelRegisterCandidacy'" ><bean:message key="button.cancel" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
	
</html:form>