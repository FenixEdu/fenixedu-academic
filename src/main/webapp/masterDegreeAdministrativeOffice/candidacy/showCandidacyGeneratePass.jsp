<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<h2><strong><bean:message key="label.show.candidady.to.genPass" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>

<fr:view name="candidacy" schema="candidacy.show.candidady">
	<fr:layout name="tabular" >
		<fr:property name="classes" value="tstyle4"/>
        <fr:property name="columnClasses" value="listClasses,,"/>
	</fr:layout>
</fr:view>
<logic:equal name="payed" value="true">
	<br/>
	<br/>
	<bean:define id="candidacy_number" name="candidacy" property="number"/>
	<html:link target="_blank" action="<%= "/dfaCandidacy.do?method=generatePass&candidacyNumber=" + candidacy_number.toString() %>">
		<bean:message key="link.candidacy.generate.password" bundle="ADMIN_OFFICE_RESOURCES"/>
	</html:link>
</logic:equal>