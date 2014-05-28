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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<html:xhtml/>

<br />
<h2><bean:message key="label.delete" bundle="ACADEMIC_OFFICE_RESOURCES"/> <bean:message name="unit" property="partyType.type.name" bundle="ENUMERATION_RESOURCES" /> <bean:write name="unit" property="name" /></h2>

<bean:define id="unitId">oid=<bean:write name="unit" property="externalId" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<html:form action="<%= "/externalUnits.do?" + unitId.toString() %>">
	<html:hidden property="method" value="deleteExternalUnit" />
	<strong><bean:message key="label.externalUnits.confirmDeleteUnit" bundle="ACADEMIC_OFFICE_RESOURCES"/>?</strong>
	<br/>
	<br/>
	<html:submit><bean:message key="label.yes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='viewUnit';"><bean:message key="label.no" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>
</html:form>	
