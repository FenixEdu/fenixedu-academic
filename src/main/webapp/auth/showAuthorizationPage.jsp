<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic" prefix="logic"%>


<bean:define id="siteURL" name="application" property="siteUrl"/>
<bean:define id="appOid" name="application" property="externalId"/>
<bean:define id="redirectUrl" name="application" property="redirectUrl"/>
<bean:define id="appName" name="application" property="name" type="java.lang.String"/>
<html>
	<head>
		<title>Fenix OAuth API - Autorização</title>
		<link rel="stylesheet/less" type="text/css" href="<%= request.getContextPath() + "/CSS/authstyle.less" %>" />
		<script type="text/javascript" src="<%= request.getContextPath() + "/javaScript/less-1.4.1.min.js"%>"></script>
	</head>
	<body>
	<div id="logoist">
		<h1><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %></a></h1>
	</div>
		<div class="auth-request-wrapper">
			<div class="auth-app-details">
				<h2><bean:message key="oauthapps.label.authorization.question"  arg0="<%= appName %>" bundle="APPLICATION_RESOURCES"></bean:message></h2>
				<p><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.scopes"/></p>
				<ul>
					<logic:iterate id="scope" name="application" property="scopes">
						<li><bean:write name="scope" property="presentationName" filter="false"/> </li>
					</logic:iterate>
				</ul>
				<div class="button-group">
					<fr:form action="/oauth.do?method=userConfirmation">
						<input name="client_id" value="<%= appOid %>" type="hidden">
						<input name="redirect_uri" value="<%= redirectUrl %>" type="hidden">
						<button class="btn btn-authorize"><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.yes"/></button>
					</fr:form>
					<fr:form action="/oauth.do?method=userCancelation">
						<input name="redirect_uri" value="<%= redirectUrl %>" type="hidden">
						<button class="btn btn-default"><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.label.authorization.no"/></button>
					</fr:form>
				</div>
			</div>
			<div class="auth-app-info">
				<img class="app-thumbnail" src="<%= request.getContextPath() + "/person/externalApps.do?method=appLogo&amp;contentContextPath_PATH=/pessoal/pessoal&amp;appOid=" + appOid %>" />
				
				<h2><bean:write name="application" property="name"/></h2>
				
				<h4><bean:write name="application" property="authorNameForUserDialog"/></h4>

				<code><a href="<%= siteURL %>"><%= siteURL %></a></code>
				<p>
					<bean:write name="application" property="description"/> 
				</p>
			</div>
		</div>
	</body>
</html>
