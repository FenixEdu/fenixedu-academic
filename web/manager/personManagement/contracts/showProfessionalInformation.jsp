<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml/>
<logic:present name="person">
	<em><bean:message key="label.person.main.title" /></em>
	<h2><bean:message key="link.title.professionalInformation" bundle="CONTRACTS_RESOURCES"/></h2>
	<div class="infoselected">
		<fr:view name="person" schema="FindPersonFactoryResult" layout="tabular"/>
		<fr:view name="person" schema="net.sourceforge.fenixedu.domain.Person.user.info" layout="tabular"/>
	</div>
	<logic:notEmpty name="person" property="employee">
		<bean:define id="personID" name="person" property="idInternal"/>
		<bean:define id="personExternalId" name="person" property="externalId"/>
		<p class="invisible">
		<html:link page="<%= "/professionalInformation.do?method=showProfessioanlData&personId="+ personExternalId%>"><bean:message key="label.professionalData" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showSituations&personId="+ personExternalId%>"><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showCategories&personId="+ personID%>"><bean:message key="label.categories" bundle="CONTRACTS_RESOURCES"/></html:link>, 
			<html:link page="<%= "/professionalInformation.do?method=showRegimes&personId="+ personID%>"><bean:message key="label.regimes" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showRelations&personId="+ personID%>"><bean:message key="label.relations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showContracts&personId="+ personID%>"><bean:message key="label.contracts" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showFunctionsAccumulations&personId="+ personID%>"><bean:message key="label.functionsAccumulations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showSabbaticals&personId="+ personID%>"><bean:message key="label.sabbaticals" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showServiceExemptions&personId="+ personID%>"><bean:message key="label.serviceExemptions" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showGrantOwnerEquivalences&personId="+ personID%>"><bean:message key="label.grantOwnerEquivalences" bundle="CONTRACTS_RESOURCES"/></html:link>
		</p>
		
		<style>
			.invaliddata { color:red;}
		</style>
		
		<bean:define id="employee" name="person" property="employee"/>

		<logic:present name="professionalData">
			<div class="infoop">
				<strong><bean:message key="label.professionalData" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="professionalData" schema="view.employee.employeeProfessionalData">
				<fr:layout name="matrix">
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
					<fr:property name="slot(professionalContractType)" value="professionalContractType"/>
					<fr:property name="row(professionalContractType)" value="0"/>
					<fr:property name="column(professionalContractType)" value="0"/>
					<fr:property name="columnSpan(professionalContractType)" value="2"/>
					
					<fr:property name="slot(contractSituation)" value="contractSituation"/>
					<fr:property name="row(contractSituation)" value="1"/>
					<fr:property name="column(contractSituation)" value="0"/>
					
					<fr:property name="slot(contractSituationDate)" value="contractSituationDate"/>
					<fr:property name="labelHidden(contractSituationDate)" value="true"/>
					<fr:property name="row(contractSituationDate)" value="1"/>
					<fr:property name="column(contractSituationDate)" value="1"/>
					
					<fr:property name="slot(professionalRelation)" value="professionalRelation"/>
					<fr:property name="row(professionalRelation)" value="2"/>
					<fr:property name="column(professionalRelation)" value="0"/>
					
					<fr:property name="slot(professionalRelationDate)" value="professionalRelationDate"/>
					<fr:property name="labelHidden(professionalRelationDate)" value="true"/>
					<fr:property name="row(professionalRelationDate)" value="2"/>
					<fr:property name="column(professionalRelationDate)" value="1"/>
					
					<fr:property name="slot(professionalCategory)" value="professionalCategory"/>
					<fr:property name="row(professionalCategory)" value="3"/>
					<fr:property name="column(professionalCategory)" value="0"/>
					
					<fr:property name="slot(professionalCategoryDate)" value="professionalCategoryDate"/>
					<fr:property name="labelHidden(professionalCategoryDate)" value="true"/>
					<fr:property name="row(professionalCategoryDate)" value="3"/>
					<fr:property name="column(professionalCategoryDate)" value="1"/>
					
					<fr:property name="slot(professionalRegime)" value="professionalRegime"/>
					<fr:property name="row(professionalRegime)" value="4"/>
					<fr:property name="column(professionalRegime)" value="0"/>
					
					<fr:property name="slot(professionalRegimeDate)" value="professionalRegimeDate"/>
					<fr:property name="labelHidden(professionalRegimeDate)" value="true"/>
					<fr:property name="row(professionalRegimeDate)" value="4"/>
					<fr:property name="column(professionalRegimeDate)" value="1"/>
				</fr:layout>
			</fr:view>
		</logic:present>
		
		<logic:present name="situations">
			<div class="infoop">
				<strong><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="situations" schema="view.employee.employeeContractSituation">
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
			<fr:view name="categories" schema="view.employee.employeeProfessionalCategory">
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
			<fr:view name="regimes" schema="view.employee.employeeProfessionalRegime">
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
			<fr:view name="relations" schema="view.employee.employeeProfessionalRelation">
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
			<fr:view name="contracts" schema="view.employee.employeeProfessionalContract">
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
			<fr:view name="functionsAccumulations" schema="view.employee.employeeFunctionsAccumulation">
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
			<fr:view name="sabbaticals" schema="view.employee.employeeSabbatical">
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
			<fr:view name="serviceExemptions" schema="view.employee.employeeServiceExemption">
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
			<fr:view name="grantOwnerEquivalences" schema="view.employee.employeeGrantOwnerEquivalent">
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
		
	</logic:notEmpty>
</logic:present>