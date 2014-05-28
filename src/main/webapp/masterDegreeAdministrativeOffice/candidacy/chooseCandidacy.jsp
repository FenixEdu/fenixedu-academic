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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.dfaCandidacy.viewCandidacy" bundle="ADMIN_OFFICE_RESOURCES"/></strong></h2>
<html:messages id="message" message="true" bundle="ADMIN_OFFICE_RESOURCES">
	<span class="error"><!-- Error messages go here --><bean:write name="message" /></span>
	<br/>
</html:messages>
<br/>
<html:form action="/dfaCandidacy.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseCandidacy"/>
	<table>
		<tr>
			<td><bean:message key="label.candidacy.number" bundle="ADMIN_OFFICE_RESOURCES"/>:</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.candidacyNumber" property="candidacyNumber" size="10" maxlength="10"/></td>
		</tr>
	</table>
	<br/>
	<html:submit><bean:message key="button.submit" bundle="ADMIN_OFFICE_RESOURCES"/></html:submit>
</html:form>