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