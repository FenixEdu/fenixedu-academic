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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/taglib/enum" prefix="e"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>
<logic:present name="person">

	<h2><bean:message key="link.title.professionalInformation" bundle="CONTRACTS_RESOURCES"/></h2>
	<div class="infoselected">
		<fr:view name="person" schema="FindPersonFactoryResult" layout="tabular"/>
		<table>
			<tr>
				<th scope="row"><bean:message key="label.person.username" bundle="APPLICATION_RESOURCES"/></th>
				<logic:notEmpty name="person" property="user">
					<td>&nbsp; <bean:write name="person" property="user.username"/></td>
				</logic:notEmpty>
				<logic:notEmpty name="person" property="student">
					<td>&nbsp;- <bean:write name="person" property="student.number"/></td>
				</logic:notEmpty>
				<logic:notEmpty name="person" property="employee">
					<td>&nbsp;- <bean:write name="person" property="employee.employeeNumber"/></td>
				</logic:notEmpty>
			</tr>
		</table>
	</div>
	<logic:notEmpty name="person" property="employee">
		<bean:define id="personExternalId" name="person" property="externalId"/>
		<br />
		<p>
		<html:link page="<%= "/professionalInformation.do?method=showProfessioanlData&personId="+ personExternalId%>"><bean:message key="label.professionalData" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showSituations&personId="+ personExternalId%>"><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showCategories&personId="+ personExternalId%>"><bean:message key="label.categories" bundle="CONTRACTS_RESOURCES"/></html:link>, 
			<html:link page="<%= "/professionalInformation.do?method=showRegimes&personId="+ personExternalId%>"><bean:message key="label.regimes" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showRelations&personId="+ personExternalId%>"><bean:message key="label.relations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showContracts&personId="+ personExternalId%>"><bean:message key="label.contracts" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showFunctionsAccumulations&personId="+ personExternalId%>"><bean:message key="label.functionsAccumulations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showSabbaticals&personId="+ personExternalId%>"><bean:message key="label.sabbaticals" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showServiceExemptions&personId="+ personExternalId%>"><bean:message key="label.serviceExemptions" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showGrantOwnerEquivalences&personId="+ personExternalId%>"><bean:message key="label.grantOwnerEquivalences" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showAbsences&personId="+ personExternalId%>"><bean:message key="label.absences" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showEmployeeWorkingUnits&personId="+ personExternalId%>"><bean:message key="label.employeeWorkingUnits" bundle="CONTRACTS_RESOURCES"/></html:link>
		</p>
		
		<style>
			.invaliddata { color:red;}
		</style>
		
		<bean:define id="employee" name="person" property="employee"/>

		<logic:present name="professionalData">
			<div class="infoop">
				<strong><bean:message key="label.professionalData" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<logic:iterate id="giafProfessionalData" name="professionalData" property="giafProfessionalDatas">
				<fr:view name="giafProfessionalData" schema="view.person.personProfessionalData">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle1 thlight mtop025" />
						<fr:property name="slot(professionalContractType)" value="professionalContractType"/>
						<fr:property name="row(professionalContractType)" value="0"/>
						<fr:property name="column(professionalContractType)" value="0"/>
						<fr:property name="columnSpan(professionalContractType)" value="3"/>
						
						<fr:property name="slot(giafPersonIdentification)" value="giafPersonIdentification"/>
						<fr:property name="row(giafPersonIdentification)" value="1"/>
						<fr:property name="column(giafPersonIdentification)" value="0"/>
						<fr:property name="columnSpan(giafPersonIdentification)" value="3"/>
						
						<fr:property name="slot(institutionEntryDate)" value="institutionEntryDate"/>
						<fr:property name="row(institutionEntryDate)" value="2"/>
						<fr:property name="column(institutionEntryDate)" value="0"/>
						<fr:property name="columnSpan(institutionEntryDate)" value="3"/>
						
						<fr:property name="slot(campus)" value="campus"/>
						<fr:property name="row(campus)" value="3"/>
						<fr:property name="column(campus)" value="0"/>
						<fr:property name="columnSpan(campus)" value="3"/>
						
						<fr:property name="slot(contractSituation)" value="contractSituation"/>
						<fr:property name="row(contractSituation)" value="4"/>
						<fr:property name="column(contractSituation)" value="0"/>
						
						<fr:property name="slot(contractSituationDate)" value="contractSituationDate"/>
						<fr:property name="labelHidden(contractSituationDate)" value="true"/>
						<fr:property name="row(contractSituationDate)" value="4"/>
						<fr:property name="column(contractSituationDate)" value="1"/>
						
						<fr:property name="slot(terminationSituationDate)" value="terminationSituationDate"/>
						<fr:property name="labelHidden(terminationSituationDate)" value="true"/>
						<fr:property name="row(terminationSituationDate)" value="4"/>
						<fr:property name="column(terminationSituationDate)" value="2"/>
						
						<fr:property name="slot(professionalRelation)" value="professionalRelation"/>
						<fr:property name="row(professionalRelation)" value="5"/>
						<fr:property name="column(professionalRelation)" value="0"/>
						
						<fr:property name="slot(professionalRelationDate)" value="professionalRelationDate"/>
						<fr:property name="labelHidden(professionalRelationDate)" value="true"/>
						<fr:property name="row(professionalRelationDate)" value="5"/>
						<fr:property name="column(professionalRelationDate)" value="1"/>
						<fr:property name="columnSpan(professionalRelationDate)" value="2"/>
						
						<fr:property name="slot(professionalCategory)" value="professionalCategory"/>
						<fr:property name="row(professionalCategory)" value="6"/>
						<fr:property name="column(professionalCategory)" value="0"/>
						
						<fr:property name="slot(professionalCategoryDate)" value="professionalCategoryDate"/>
						<fr:property name="labelHidden(professionalCategoryDate)" value="true"/>
						<fr:property name="row(professionalCategoryDate)" value="6"/>
						<fr:property name="column(professionalCategoryDate)" value="1"/>
						<fr:property name="columnSpan(professionalCategoryDate)" value="2"/>
						
						<fr:property name="slot(professionalRegime)" value="professionalRegime"/>
						<fr:property name="row(professionalRegime)" value="7"/>
						<fr:property name="column(professionalRegime)" value="0"/>
						
						<fr:property name="slot(professionalRegimeDate)" value="professionalRegimeDate"/>
						<fr:property name="labelHidden(professionalRegimeDate)" value="true"/>
						<fr:property name="row(professionalRegimeDate)" value="7"/>
						<fr:property name="column(professionalRegimeDate)" value="1"/>
						<fr:property name="columnSpan(professionalRegimeDate)" value="2"/>
					</fr:layout>
				</fr:view>
			</logic:iterate>
		</logic:present>
		<logic:present name="situations">
			<div class="infoop">
				<strong><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<bean:define id="schema" value="view.person.personContractSituation"/>
			<logic:present role="role(MANAGER)">
				<bean:define id="schema" value="view.person.personContractSituation.MANAGER"/>
			</logic:present>
			<fr:view name="situations" schema="<%=schema.toString()%>">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
					<fr:property name="conditionalColumnClass(3)" value="invaliddata"/>
					<fr:property name="useCssIfNot(3)" value="valid"/>
					<fr:property name="column(3)" value="3"/>
					<fr:property name="conditionalColumnClass(4)" value="invaliddata"/>
					<fr:property name="useCssIfNot(4)" value="valid"/>
					<fr:property name="column(4)" value="4"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="categories">
			<div class="infoop">
				<strong><bean:message key="label.categories" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="categories" schema="view.person.personProfessionalCategory">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
					<fr:property name="conditionalColumnClass(3)" value="invaliddata"/>
					<fr:property name="useCssIfNot(3)" value="valid"/>
					<fr:property name="column(3)" value="3"/>
					<fr:property name="conditionalColumnClass(4)" value="invaliddata"/>
					<fr:property name="useCssIfNot(4)" value="valid"/>
					<fr:property name="column(4)" value="4"/>
					<fr:property name="conditionalColumnClass(5)" value="invaliddata"/>
					<fr:property name="useCssIfNot(5)" value="valid"/>
					<fr:property name="column(5)" value="5"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="regimes">
			<div class="infoop">
				<strong><bean:message key="label.regimes" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="regimes" schema="view.person.personProfessionalRegime">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="relations">
			<div class="infoop">
				<strong><bean:message key="label.relations" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="relations" schema="view.person.personProfessionalRelation">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
					<fr:property name="conditionalColumnClass(3)" value="invaliddata"/>
					<fr:property name="useCssIfNot(3)" value="valid"/>
					<fr:property name="column(3)" value="3"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="contracts">
			<div class="infoop">
				<strong><bean:message key="label.contracts" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="contracts" schema="view.person.personProfessionalContract">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="functionsAccumulations">
			<div class="infoop">
				<strong><bean:message key="label.functionsAccumulations" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="functionsAccumulations" schema="view.person.personFunctionsAccumulation">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
					<fr:property name="conditionalColumnClass(3)" value="invaliddata"/>
					<fr:property name="useCssIfNot(3)" value="valid"/>
					<fr:property name="column(3)" value="3"/>
					<fr:property name="conditionalColumnClass(4)" value="invaliddata"/>
					<fr:property name="useCssIfNot(4)" value="valid"/>
					<fr:property name="column(4)" value="4"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="sabbaticals">
			<div class="infoop">
				<strong><bean:message key="label.sabbaticals" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="sabbaticals" schema="view.person.personSabbatical">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="serviceExemptions">
			<div class="infoop">
				<strong><bean:message key="label.serviceExemptions" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="serviceExemptions" schema="view.person.personServiceExemption">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="grantOwnerEquivalences">
			<div class="infoop">
				<strong><bean:message key="label.grantOwnerEquivalences" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="grantOwnerEquivalences" schema="view.person.personGrantOwnerEquivalent">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
						<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
					<fr:property name="conditionalColumnClass(3)" value="invaliddata"/>
					<fr:property name="useCssIfNot(3)" value="valid"/>
					<fr:property name="column(3)" value="3"/>
					<fr:property name="conditionalColumnClass(4)" value="invaliddata"/>
					<fr:property name="useCssIfNot(4)" value="valid"/>
					<fr:property name="column(4)" value="4"/>
					<fr:property name="conditionalColumnClass(5)" value="invaliddata"/>
					<fr:property name="useCssIfNot(5)" value="valid"/>
					<fr:property name="column(5)" value="5"/>
					<fr:property name="conditionalColumnClass(6)" value="invaliddata"/>
					<fr:property name="useCssIfNot(6)" value="valid"/>
					<fr:property name="column(6)" value="6"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="absences">
			<div class="infoop">
				<strong><bean:message key="label.absences" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="absences" schema="view.person.personAbsence">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="conditionalColumnClass(0)" value="invaliddata"/>
					<fr:property name="useCssIfNot(0)" value="valid"/>
					<fr:property name="column(0)" value="0"/>
					<fr:property name="conditionalColumnClass(1)" value="invaliddata"/>
					<fr:property name="useCssIfNot(1)" value="valid"/>
					<fr:property name="column(1)" value="1"/>
					<fr:property name="conditionalColumnClass(2)" value="invaliddata"/>
					<fr:property name="useCssIfNot(2)" value="valid"/>
					<fr:property name="column(2)" value="2"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="workingUnits">
			<div class="infoop">
				<strong><bean:message key="label.employeeWorkingUnits" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="workingUnits">
				<fr:schema type="org.fenixedu.academic.domain.organizationalStructure.EmployeeContract" bundle="CONTRACTS_RESOURCES">
					<fr:slot name="beginDate"/>
					<fr:slot name="endDate"/>
					<fr:slot name="parentParty.presentationName" key="label.unit">
					</fr:slot>
					<fr:slot name="teacherContract"/>
				</fr:schema>
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
				</fr:layout>
			</fr:view>
		</logic:present>
	</logic:notEmpty>
</logic:present>