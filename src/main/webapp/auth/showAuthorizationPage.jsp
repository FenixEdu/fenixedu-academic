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
<input name="client_id" value="<%= request.getParameter("client_id") %>" type="hidden">
<input name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>" type="hidden">
	<html:submit>
		<bean:message key="button.submit" />
	</html:submit>
</fr:form>
