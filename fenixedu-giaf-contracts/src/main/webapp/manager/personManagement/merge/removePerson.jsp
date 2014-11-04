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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml/>

<h2><bean:message bundle="MANAGER_RESOURCES" key="title.personManagement.merge" /></h2>

<p class="breadcumbs">
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 1</strong>: <bean:message key="label.personManagement.merge.choose.persons" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 2</strong>: <bean:message key="label.personManagement.merge.transfer.personal.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 3</strong>: <bean:message key="label.personManagement.merge.transfer.events.and.accounts" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 4</strong>: <bean:message key="label.personManagement.merge.transfer.student.data" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 5</strong>: <bean:message key="label.personManagement.merge.transfer.registrations" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 6</strong>: <bean:message key="label.personManagement.merge.delete.student" bundle="MANAGER_RESOURCES" /> </span> &gt;
	<span class="actual"><strong><bean:message key="label.step" bundle="MANAGER_RESOURCES" /> 7</strong>: <bean:message key="label.personManagement.merge.delete.person" bundle="MANAGER_RESOURCES" /> </span>
</p>

<br />

<logic:notEmpty name="studentRemoved">
	<p><bean:message key="message.personManagement.merge.student.removed" bundle="MANAGER_RESOURCES" /></p>
</logic:notEmpty>


	<!-- Dados Pessoais -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">3</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.personal.info" /></strong></td>
		</tr>
	</table>

	<fr:view name="person"
		schema="org.fenixedu.academic.domain.Person.personal.info">
		<fr:layout name="tabular">
			<fr:property name="classes"
				value="tstyle2 thleft thlight mtop15 thwhite" />
		</fr:layout>
	</fr:view>

	<!-- Informa��o de Utilizador -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">4</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.login.info" /></strong></td>
		</tr>
	</table>
	<fr:view name="person"
		schema="org.fenixedu.academic.domain.Person.user.info">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
		</fr:layout>
	</fr:view>

	<!-- Filia��o -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">5</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.filiation" /></strong></td>
		</tr>
	</table>


	<fr:view name="person"
		schema="org.fenixedu.academic.domain.Person.family">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
		</fr:layout>
	</fr:view>


	<!-- Resid�ncia -->
	<table class="mtop15" width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop" width="25"><span class="emphasis-box">6</span></td>
			<td class="infoop"><strong><bean:message
				key="label.person.title.addressInfo" /></strong></td>
		</tr>
	</table>

	<logic:iterate id="address" name="person" property="physicalAddresses">

		<bean:define id="addressID" name="address" property="externalId" />
		
		<fr:view name="address"
			schema="contacts.PhysicalAddress.view-for-student">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle2 thleft thlight thwhite" />
			</fr:layout>
		</fr:view>
	</logic:iterate>

<fr:form action="/mergePersons.do?method=removeFromUploadUnits">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<html:submit><bean:message key="label.merge.persons.remove.from.unitsWithPermissionToUpload" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
	
<fr:form action="/mergePersons.do?method=removeFromPersistentGroups">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<html:submit><bean:message key="label.merge.persons.remove.from.persistentGroups" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
	
<fr:form action="/mergePersons.do?method=deletePerson">

	<fr:edit id="mergePersonsBean" name="mergePersonsBean" visible="false"/>
	
	<html:submit><bean:message key="label.continue" bundle="MANAGER_RESOURCES" /></html:submit>
</fr:form>
