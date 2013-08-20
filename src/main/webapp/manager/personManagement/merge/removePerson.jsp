<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
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
		schema="net.sourceforge.fenixedu.domain.Person.personal.info">
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
		schema="net.sourceforge.fenixedu.domain.Person.user.info">
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
		schema="net.sourceforge.fenixedu.domain.Person.family">
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
