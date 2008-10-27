<%@ page language="java"%>
<%@page contentType="text/html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />
<em><bean:message key="label.facultyAdmOffice.portal.name" /></em>
<h2><bean:message key="link.export.grants" /></h2>

<logic:present name="grantSearch">
	<fr:form action="/exportGrants.do?method=searchGrants">
		<h3><bean:message key="label.contract" /></h3>
		
		<bean:define id="datesTypeChoiceSchema" value="search.grants.contract.date"/>
		<logic:notEmpty name="grantSearch" property="datesTypeChoice">
			<logic:equal name="grantSearch" property="datesTypeChoice" value="DATE_INTERVAL">
				<bean:define id="datesTypeChoiceSchema" value="search.grants.contract.dateInterval"/>
			</logic:equal>
		</logic:notEmpty>
		
		<fr:edit id="grantSearch.datesTypeChoice" name="grantSearch" schema="<%=datesTypeChoiceSchema.toString()%>">
			<fr:destination name="datesTypeChoicePostBack" path="/exportGrants.do?method=choicesPostBack" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thmiddle thright mvert0" />
				<fr:property name="columnClasses" value="width12em,inobullet width20em,tdclear tderror1" />
			</fr:layout>
		</fr:edit>
		<logic:notEmpty name="grantSearch" property="activityChoice">
			<logic:equal name="grantSearch" property="activityChoice" value="ACTIVITY">
				<fr:edit id="grantSearch.activityChoice" name="grantSearch" schema="search.grants.contract.activityChoise.activity">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thmiddle thright mvert0" />
						<fr:property name="columnClasses" value="width12em,inobullet width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>
			<logic:equal name="grantSearch" property="activityChoice" value="TERM">
				<fr:edit id="grantSearch.activityChoice" name="grantSearch" schema="search.grants.contract.activityChoise.term">
					<fr:layout>
						<fr:property name="classes" value="tstyle5 thlight thmiddle thright mvert0" />
						<fr:property name="columnClasses" value="width12em,inobullet width20em,tdclear tderror1" />
					</fr:layout>
				</fr:edit>
			</logic:equal>
		</logic:notEmpty>
		
		<h3><bean:message key="label.subsidy" /></h3>
		<bean:define id="subsidySchema" value="search.grants.subsidy.typeChoice"/>
		<logic:notEmpty name="grantSearch" property="subsidyCostCenterOrProjectChoice">
			<logic:notEqual name="grantSearch" property="subsidyCostCenterOrProjectChoice" value="ANY">
				<bean:define id="subsidySchema" value="search.grants.subsidy"/>
			</logic:notEqual>
		</logic:notEmpty>
		
		<fr:edit id="grantSearch.subsidy" name="grantSearch" schema="<%=subsidySchema.toString()%>">
			<fr:destination name="subsidyTypeChoicePostBack" path="/exportGrants.do?method=choicesPostBack" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		
		<h3><bean:message key="label.insurance" /></h3>
		<bean:define id="insuranceSchema" value="search.grants.insurance.typeChoice"/>
		<logic:notEmpty name="grantSearch" property="insuranceCostCenterOrProjectChoice">
			<logic:notEqual name="grantSearch" property="insuranceCostCenterOrProjectChoice" value="ANY">
				<bean:define id="insuranceSchema" value="search.grants.insurance"/>
			</logic:notEqual>
		</logic:notEmpty>
		
		<fr:edit id="grantSearch.insurance" name="grantSearch" schema="<%=insuranceSchema.toString()%>">
			<fr:destination name="insuranceTypeChoicePostBack" path="/exportGrants.do?method=choicesPostBack" />
			<fr:layout>
				<fr:property name="classes" value="tstyle5 thlight thright"/>
				<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
			</fr:layout>
		</fr:edit>
		
		<p><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="invisible">
			<bean:message key="button.submit" />
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.export" property="export">
			<bean:message key="link.export"/>
		</html:submit></p>
	</fr:form>
	<bean:define id="searchList" name="grantSearch" property="search" />
	<logic:notEmpty name="searchList">
		<bean:size id="searchResultNumber" name="searchList"/>
		<bean:message key="message.searchResultNumber" arg0="<%= searchResultNumber.toString()%>"/>
		<fr:view name="searchList" schema="show.grantContractRegime">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
			</fr:layout>
		</fr:view>
	</logic:notEmpty>
</logic:present>

