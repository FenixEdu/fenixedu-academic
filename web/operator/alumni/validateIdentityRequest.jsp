<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<!-- validateIdentityRequest.jsp -->

<em><bean:message key="operator.module.title" bundle="MANAGER_RESOURCES"/></em>
<h2><bean:message key="alumni.validate.identity.request" bundle="MANAGER_RESOURCES"/></h2>

<html:link page="/alumni.do?method=prepareIdentityRequestsList">
	<bean:message key="label.back" bundle="MANAGER_RESOURCES"/>
</html:link>

<logic:present role="OPERATOR">

	<fr:view name="requestBody" schema="alumni.identity.request.header" >
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
		</fr:layout>
	</fr:view>

	<table class="tstyle1 thlight thleft thmiddle">
		<tr>
			<th></th>
			<th><b><bean:message key="label.user" bundle="MANAGER_RESOURCES" /></b></th>
			<th><b><bean:message key="label.system" bundle="MANAGER_RESOURCES" /></b></th>
		</tr>
		<tr>
			<th><bean:message key="label.fullName" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="fullName" />
			</td>
			<td>
				<logic:present name="personBody" property="partyName" >
					<fr:view name="personBody" property="partyName" />
				</logic:present>
				<logic:notPresent name="personBody" property="partyName" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.dateOfBirthYearMonthDay" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="dateOfBirthYearMonthDay" />
			</td>
			<td>
				<logic:present name="personBody" property="dateOfBirthYearMonthDay" >
					<fr:view name="personBody" property="dateOfBirthYearMonthDay" />
				</logic:present>
				<logic:notPresent name="personBody" property="dateOfBirthYearMonthDay" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.districtOfBirth" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="districtOfBirth" />
			</td>
			<td>
				<logic:present name="personBody" property="districtOfBirth" >
					<fr:view name="personBody" property="districtOfBirth" />
				</logic:present>
				<logic:notPresent name="personBody" property="districtOfBirth" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.districtSubdivisionOfBirth" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="districtSubdivisionOfBirth" />
			</td>
			<td>
				<logic:present name="personBody" property="districtSubdivisionOfBirth" >
					<fr:view name="personBody" property="districtSubdivisionOfBirth" />
				</logic:present>
				<logic:notPresent name="personBody" property="districtSubdivisionOfBirth" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.parishOfBirth" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="parishOfBirth" />
			</td>
			<td>
				<logic:present name="personBody" property="parishOfBirth" >
					<fr:view name="personBody" property="parishOfBirth" />
				</logic:present>
				<logic:notPresent name="personBody" property="parishOfBirth" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.socialSecurityNumber" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="socialSecurityNumber" />
			</td>
			<td>
				<logic:present name="personBody" property="socialSecurityNumber" >
					<fr:view name="personBody" property="socialSecurityNumber" />
				</logic:present>
				<logic:notPresent name="personBody" property="socialSecurityNumber" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.nameOfFather" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="nameOfFather" />
			</td>
			<td>
				<logic:present name="personBody" property="nameOfFather" >
					<fr:view name="personBody" property="nameOfFather" />
				</logic:present>
				<logic:notPresent name="personBody" property="nameOfFather" >-</logic:notPresent>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.nameOfMother" bundle="MANAGER_RESOURCES" />:</th>
			<td>
				<fr:view name="requestBody" property="nameOfMother" />
			</td>
			<td>
				<logic:present name="personBody" property="nameOfMother" >
					<fr:view name="personBody" property="nameOfMother" />
				</logic:present>
				<logic:notPresent name="personBody" property="nameOfMother" >-</logic:notPresent>
			</td>
		</tr>
	</table>

	<logic:present name="operation">
		<p>
			<bean:message key="identity.validation.info.message" bundle="MANAGER_RESOURCES"/>
		</p>
	
		<fr:form id="reg_form" action="/alumni.do">
			<fr:edit id="requestBody" name="requestBody" visible="false" />
			<input type="hidden" name="method" value="" />
			<html:submit bundle="MANAGER_RESOURCES" altKey="label.authorize" onclick="this.form.method.value='confirmIdentity';">
				<bean:message key="label.authorize" bundle="MANAGER_RESOURCES" />
			</html:submit>
			<html:submit bundle="MANAGER_RESOURCES" altKey="label.invalidate" onclick="this.form.method.value='refuseIdentity';">
				<bean:message key="label.invalidate" bundle="MANAGER_RESOURCES" />
			</html:submit>
		</fr:form>
	</logic:present>
</logic:present>


