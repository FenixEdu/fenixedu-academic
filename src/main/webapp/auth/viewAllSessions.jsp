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

<html:xhtml />

<%@ page
	import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>

<h2>
	<bean:message key="oauthapps.label.manage.applications" bundle="APPLICATION_RESOURCES" />
</h2>

<bean:define id="appOid" name="appOid" scope="request"/>

<html:link page="<%= "/externalApps.do?method=viewAllAuthorizations&appOid=" + appOid %>" >
				<bean:message key="label.back" bundle="APPLICATION_RESOURCES"/>
</html:link>

<logic:notEmpty name="sessions">
		<fr:view name="sessions" schema="oauthapps.view.app.all.sessions">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle4 thcenter thcenter"/>

			</fr:layout>
		</fr:view>
</logic:notEmpty>