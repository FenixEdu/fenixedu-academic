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
		<p class="invisible">
			<html:link page="<%= "/professionalInformation.do?method=showSituations&personId="+ personID%>"><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showCategories&personId="+ personID%>"><bean:message key="label.categories" bundle="CONTRACTS_RESOURCES"/></html:link>, 
			<html:link page="<%= "/professionalInformation.do?method=showRegimes&personId="+ personID%>"><bean:message key="label.regimes" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showRelations&personId="+ personID%>"><bean:message key="label.relations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showContracts&personId="+ personID%>"><bean:message key="label.contracts" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showFunctionsAccumulations&personId="+ personID%>"><bean:message key="label.functionsAccumulations" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showSabbaticals&personId="+ personID%>"><bean:message key="label.sabbaticals" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showServiceExemptions&personId="+ personID%>"><bean:message key="label.serviceExemptions" bundle="CONTRACTS_RESOURCES"/></html:link>,
			<html:link page="<%= "/professionalInformation.do?method=showGrantOwnerEquivalences&personId="+ personID%>"><bean:message key="label.grantOwnerEquivalences" bundle="CONTRACTS_RESOURCES"/></html:link>
		</p>
		
		<bean:define id="employee" name="person" property="employee"/>

		<logic:present name="situations">
			<div class="infoop">
				<strong><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></strong>
			</div><br/>
			<fr:view name="situations" schema="view.employee.employeeContractSituation">
				<fr:layout name="tabular">
					<fr:property name="sortBy" value="beginDate, endDate"/>
					<fr:property name="classes" value="tstyle1 thlight mtop025" />
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
				</fr:layout>
			</fr:view>
		</logic:present>
		
	</logic:notEmpty>
</logic:present>