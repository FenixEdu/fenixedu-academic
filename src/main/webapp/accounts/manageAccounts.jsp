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
			<fr:slot name="nationality.localizedName.content" key="label.nationality" />
		</fr:schema>
	</fr:view>
</logic:present>