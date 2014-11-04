<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<bean:define id="backLink" toScope="request">
	<ul>
		<li>
			<html:link page="/unitSiteManagement.do?method=prepare">
				<bean:message key="label.return" bundle="MANAGER_RESOURCES"/>
			</html:link>
		</li>
	</ul>
</bean:define>

<bean:define id="site" name="site" type="org.fenixedu.academic.domain.Site"/>
<bean:define id="siteActionName" value="/unitSiteManagement.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" value="<%= site.getExternalId().toString() %>" toScope="request"/>

<jsp:include page="/webSiteManager/commons/chooseManagers.jsp"/>
