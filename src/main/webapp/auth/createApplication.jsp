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
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.domain.person.RoleType"%>
<html:xhtml />

<logic:present role="role(DEVELOPER)">
<h2>
	<bean:message key="oauthapps.label.create.application" bundle="APPLICATION_RESOURCES" />
</h2>

<fr:create type="net.sourceforge.fenixedu.domain.ExternalApplication" id="create" schema="oauthapps.create.app">
	<fr:hidden slot="author" name="currentUser"/>
	<fr:destination name="success" path="/externalApps.do?method=createApplication"/>
	<fr:destination name="cancel" path="/externalApps.do?method=createApplication" />
</fr:create>
</logic:present>
<logic:notPresent  role="role(DEVELOPER)">
	<p>Nesta interface pode registar aplicações criadas por si que utilizem as API's disponibilizadas pelo sistema FenixEdu. No site de desenvolvimento do FenixEdu pode encontrar informação sobre como utilizar a API em:</p>
</logic:notPresent>