<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.viewCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>
<html:form action="/dfaCandidacy.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseCandidacy"/>
	<table>
		<tr>
			<td><bean:message key="label.candidacy.number" bundle="ADMIN_OFFICE_RESOURCES"/>:</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.candidacyNumber" property="candidacyNumber" size="10" maxlength="10"/></td>
		</tr>
	</table>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
</html:form>