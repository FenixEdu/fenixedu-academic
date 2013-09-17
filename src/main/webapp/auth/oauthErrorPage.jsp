<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<html>
	<head>
		<title>Fenix OAuth API - Erro Autorização</title>
		<link rel="stylesheet/less" type="text/css" href=" <%= request.getContextPath() + "/CSS/authstyle.less" %>" />
		<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/less-1.4.1.min.js"%>"></script>
	</head>
	<body>
		<div class="auth-request-wrapper-error">
			<h2 class="auth-app-error"><bean:message key="oauthapps.label.authorization.error"  bundle="APPLICATION_RESOURCES"></bean:message></h2>
		</div>
	</body>
</html>