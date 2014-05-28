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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<!-- validateIdentityRequest.jsp -->

<h2><bean:message key="alumni.validate.identity.request" bundle="MANAGER_RESOURCES"/></h2>

<html:link page="/alumni.do?method=prepareIdentityRequestsList">
	<bean:message key="label.back" bundle="MANAGER_RESOURCES"/>
</html:link>

<logic:present role="role(OPERATOR)">

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
				<logic:notPresent name="personBody" property="socialSecurityNumber">
					<logic:notPresent name="operation">
					-
					</logic:notPresent>
					<logic:present name="operation">
						<bean:define id="requestId" name="requestBody" property="externalId" />
						<bean:define id="personId" name="personBody" property="externalId" />
						<html:link page="<%= "/alumni.do?method=updateSocialSecurityNumber&requestId=" + requestId + "&personId=" + personId %>">
							<bean:message key="alumni.update.socialSecurityNumber" bundle="MANAGER_RESOURCES"/>
						</html:link>
					</logic:present>
				</logic:notPresent>
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

		<div class="reg_form">	
			<fr:form action="/alumni.do">
				<fr:edit id="requestBody" name="requestBody" visible="false" />
				
				<fr:edit id="requestComment" name="requestBody" schema="alumni.identity.request.comment" >
					<fr:layout name="tabular" >
						<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
						<fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
						<fr:property name="optionalMarkShown" value="true" />
					</fr:layout>
				</fr:edit>

				<p>
					<bean:message key="identity.validation.info.message" bundle="MANAGER_RESOURCES"/>
				</p>
				
				<input type="hidden" name="method" value="" />
				<html:submit bundle="MANAGER_RESOURCES" altKey="label.authorize" onclick="this.form.method.value='confirmIdentity';">
					<bean:message key="label.authorize" bundle="MANAGER_RESOURCES" />
				</html:submit>
				<html:submit bundle="MANAGER_RESOURCES" altKey="label.invalidate" onclick="this.form.method.value='refuseIdentity';">
					<bean:message key="label.invalidate" bundle="MANAGER_RESOURCES" />
				</html:submit>
			</fr:form>
		</div>
	</logic:present>
	<logic:notPresent name="operation">
		<fr:view name="requestBody" schema="alumni.identity.request.comment" >
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1 thlight thleft thmiddle"/>
			</fr:layout>
		</fr:view>
	</logic:notPresent>
	
</logic:present>


