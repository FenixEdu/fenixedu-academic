<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><bean:write name="message" /></span>
	<br/>
</html:messages>
<h2><strong><bean:message key="label.generate.password" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<br/>
<html:form action="/dfaCandidacy.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showCandidacyGeneratePass"/>
	<table>
		<tr>
			<td>
				<bean:message key="label.candidacy.number" bundle="ADMIN_OFFICE_RESOURCES"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.candidacyNumber" property="candidacyNumber" size="10" maxlength="10"/>
			</td>
		</tr>
	</table>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
</html:form>