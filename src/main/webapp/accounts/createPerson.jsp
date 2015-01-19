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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="create.person.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:notEmpty name="createdPerson">
	<b><bean:message key="label.invitedPerson.created.with.success" bundle="MANAGER_RESOURCES"/>:</b>
	<fr:view name="createdPerson" schema="ShowExistentPersonsDetailsBeforeCreateInvitedPersons">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4"/>
			<fr:property name="rowClasses" value="listClasses"/>
		</fr:layout>
	</fr:view>
</logic:notEmpty>

<logic:empty name="createdPerson">
	<b><bean:message key="label.verify.if.invitedPerson.already.exists" bundle="MANAGER_RESOURCES"/></b>
	<fr:form action="/accounts/manageAccounts.do">
		<fr:edit name="personBean" id="personBean">
			<input type="hidden" name="method"/>
			<fr:schema type="org.fenixedu.academic.dto.person.PersonBean" bundle="MANAGER_RESOURCES">
				<fr:slot name="givenNames" />
				<fr:slot name="familyNames" />
				<fr:slot name="documentIdNumber" />
			</fr:schema>
			<fr:layout name="tabular" >
				<fr:property name="classes" value="tstyle1"/>
				<fr:property name="columnClasses" value=",,noborder"/>
			</fr:layout>
		</fr:edit>
		<html:submit onclick="this.form.method.value='showExistentPersonsWithSameMandatoryDetails';"><bean:message key="label.search" bundle="MANAGER_RESOURCES" /></html:submit>

		<logic:notEmpty name="resultPersons">
			<fr:view name="resultPersons">
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
				<fr:layout name="tabular" />
			</fr:view>
		</logic:notEmpty>
		<logic:present name="resultPersons">
			<html:submit onclick="this.form.method.value='prepareCreatePersonFillInformation';"><bean:message key="link.create.person.because.does.not.exist" bundle="MANAGER_RESOURCES" /></html:submit>
		</logic:present>
	</fr:form>
</logic:empty>