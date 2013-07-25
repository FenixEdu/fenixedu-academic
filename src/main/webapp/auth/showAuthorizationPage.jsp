<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2>
	Teste
</h2>
<!-- TODO Show information about author -->
<!-- TODO Show information about scopes -->
<!-- TODO Show add cancel -->
<fr:form action="/oauth.do?method=userConfirmation">
	<html:submit>
		<bean:message key="button.submit" />
	</html:submit>
</fr:form>
