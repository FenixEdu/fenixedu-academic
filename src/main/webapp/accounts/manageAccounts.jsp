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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<h2 class="page-header"><bean:message key="link.accountmanagement.manageaccounts" bundle="MANAGER_RESOURCES" /></h2>

<fr:form action="/accounts/manageAccounts.do?method=search">
	<fr:edit id="searchParameters" name="searchParameters">
		<fr:schema type="org.fenixedu.academic.ui.struts.action.accounts.SearchParametersBean" bundle="MANAGER_RESOURCES">
			<fr:slot name="username" />
			<fr:slot name="name" />
			<fr:slot name="documentIdNumber" />
			<fr:slot name="email" />
			<fr:slot name="socialSecurityNumber" />
		</fr:schema>
	</fr:edit>
	<div class="col-sm-offset-2 col-sm-10">
		<html:submit><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>
		<html:link styleClass="btn btn-default" action="/accounts/manageAccounts.do?method=prepareCreatePerson">
			<span class="glyphicon glyphicon-plus-sign"></span> <bean:message key="create.person.title" bundle="MANAGER_RESOURCES" />
		</html:link>
	</div>
</fr:form>

<logic:present name="matches">
	<h3>Resultados</h3>
	<fr:view name="matches">
		<fr:schema type="org.fenixedu.academic.domain.Person" bundle="MANAGER_RESOURCES">
			<fr:slot name="username" layout="link" key="label.name">
				<fr:property name="linkFormat"
					value="/accounts/manageAccounts.do?method=viewPerson&amp;personId=\${externalId}" />
				<fr:property name="useParent" value="true" />
				<fr:property name="format" value="\${profile.fullName} (\${username})" />
			</fr:slot>
			<fr:slot name="documentIdNumber" layout="format">
				<fr:property name="format" value="\${documentIdNumber} (\${idDocumentType.localizedName})" />
				<fr:property name="useParent" value="true" />
			</fr:slot>
			<fr:slot name="profile.email" key="label.email" />
			<fr:slot name="dateOfBirth" />
		</fr:schema>
	</fr:view>
</logic:present>