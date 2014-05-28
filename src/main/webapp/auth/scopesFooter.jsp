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
<%@ page import="org.fenixedu.bennu.core.domain.Bennu"%>

<% 
	request.setAttribute("scopes", Bennu.getInstance().getAuthScopes());
%>

<div class="mtop3" style="width:600px;">
<p class="infoop3"><b><bean:message bundle="APPLICATION_RESOURCES" key="oauthapps.title.app.scopes"/></b></p>

<fr:view name="scopes">
	<fr:layout name="tabular-list"> 
		<fr:property name="subSchema" value="oauthapps.view.scope.complete"/>
		<fr:property name="subLayout" value="values"/>
	</fr:layout>
</fr:view>
</div>