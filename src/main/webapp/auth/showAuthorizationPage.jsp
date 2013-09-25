<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean"prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>


<bean:define id="siteURL" name="application" property="siteUrl"/>
<bean:define id="appOid" name="application" property="externalId"/>
<bean:define id="appName" name="application" property="name" type="java.lang.String"/>
<html>
	<head>
		<title>Fenix OAuth API - Autorização</title>
		<link rel="stylesheet/less" type="text/css" href="<%= request.getContextPath() + "/CSS/authstyle.less" %>" />
		<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/less-1.4.1.min.js"%>"></script>
	</head>
	<body>
	<div id="logoist">
		<h1><a href="http://www.ist.utl.pt">Instituto Superior Técnico</a></h1>
	</div>
		<div class="auth-request-wrapper">
			<div class="auth-app-details">
				<h2><bean:message key="oauthapps.label.authorization.question"  arg0="<%= appName %>" bundle="APPLICATION_RESOURCES"></bean:message></h2>
				<p><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.scopes"/></p>
				<ul>
					<logic:iterate id="scope" name="application" property="scopes">
						<li><bean:write name="scope" property="name"/> </li>
					</logic:iterate>
				</ul>
				<div class="button-group">
					<fr:form action="/oauth.do?method=userConfirmation">
						<input name="client_id" value="<%= request.getParameter("client_id") %>" type="hidden">
						<input name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>" type="hidden">
						<button class="btn btn-authorize"><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.yes"/></button>
					</fr:form>
					<fr:form action="/oauth.do?method=userCancelation">
						<input name="redirect_uri" value="<%= request.getParameter("redirect_uri") %>" type="hidden">
						<button class="btn btn-default"><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.no"/></button>
					</fr:form>
				</div>
			</div>
			<div class="auth-app-info">
				<img class="app-thumbnail" src="<%= request.getContextPath() + "/person/externalApps.do?method=appLogo&amp;contentContextPath_PATH=/pessoal/pessoal&amp;appOid=" + appOid %>" />
				<h2><bean:write name="application" property="name"/></h2>
				<code><a href="<%= siteURL %>"><%= siteURL %></a></code>
				<p>
					<bean:write name="application" property="description"/> 
				</p>
			</div>
		</div>
	</body>
</html>