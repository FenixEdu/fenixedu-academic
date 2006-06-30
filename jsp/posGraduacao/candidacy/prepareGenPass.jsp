<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><strong><bean:message key="label.generate.password"/></strong></h2>
<br/>
<html:form target="_blank" action="/dfaCandidacy.do">
	<html:hidden property="method" value="generatePass"/>
	<table>
		<tr>
			<td>
				<bean:message key="" bundle="ADMIN_OFFICE_RESOURCES"/>
			</td>
			<td>
				<html:text property="candidacyNumber"/>
			</td>
		</tr>
	</table>
	<html:submit/>
</html:form>