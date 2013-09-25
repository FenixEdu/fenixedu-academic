<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>


<html>
	<head>
		<title>Fenix OAuth API - Erro Autorização</title>
		<link rel="stylesheet/less" type="text/css" href="<%= request.getContextPath() + "/CSS/authstyle.less" %>" />
		<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/less-1.4.1.min.js"%>"></script>
	</head>
	<body>
		<div class="auth-request-wrapper-error">
			<h2 class="auth-app-error"><bean:message key="oauthapps.label.authorization.error"  bundle="APPLICATION_RESOURCES"></bean:message></h2>
		</div>
	</body>
</html>