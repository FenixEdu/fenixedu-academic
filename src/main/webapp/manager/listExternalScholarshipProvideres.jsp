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
<%@page import="pt.ist.fenixframework.DomainObject"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>


<logic:present role="role(MANAGER)">

<h2>Listar Entidades</h2>
<logic:notEmpty name="externalScholarshipProviders">
<table class="tstyle1">
<tr>
    <th >
      Nome
    </th>
    <th >
      NIF
    </th>
    <th>
      Acções
    </th>
  </tr>
<logic:iterate id="provider" name="externalScholarshipProviders">
	<tr>
	<td>
		<bean:write name="provider" property="name"/>
	</td>
	<td>
		<bean:define id="partySocialSecurityNumber" name="provider" property="partySocialSecurityNumber" />
		<bean:write name="partySocialSecurityNumber" property="socialSecurityNumber"/>
	</td>
	<td>
		<logic:empty name="provider" property="phdGratuityExternalScholarshipExemption">
			<html:link action="<%= "externalScholarshipProvider.do?method=remove&provider=" + ((DomainObject)provider).getExternalId() %>">Remover Entidade Externa</html:link>
		</logic:empty>
	</td>
	</tr>
</logic:iterate>
</table>
</logic:notEmpty>
<html:link action="externalScholarshipProvider.do?method=add">Adicionar Entidade</html:link>
</logic:present>

