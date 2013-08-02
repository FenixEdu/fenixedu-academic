<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2>Confirmar Autorização - Teste</h2>


<!--
http://localhost:8080/fenix/external/oauth.do?method=getUserPermission&contentContextPath_PATH=/pessoal/pessoal&_request_checksum_=ebf8ba2ebb6645de4caa9241dd6f359912fd8a58
-->


<logic:present name="app">
	<fr:view name="app" layout="tabular" schema="my.schema.confirm.app" />
</logic:present>


<fr:form action="/oauth.do?method=userConfirmation">
<input name="client_id" value="<%= request.getParameter("client_id") %>" type="hidden">
<input name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>" type="hidden">
	<html:submit>
		<bean:message key="button.submit" />
	</html:submit>
</fr:form>


<fr:form action="/oauth.do?method=userCancelation">
	<html:submit>
		<bean:message key="button.cancel" />
	</html:submit>
	</p>
</fr:form>
