<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<%@page import="org.apache.struts.action.ActionMessages"%><html:xhtml />

<h2><bean:message bundle="MANAGER_RESOURCES"
	key="title.ectsComparabilityTablesManagement" /></h2>

<logic:messagesPresent message="true">
	<p><span class="error0"> <html:messages id="message"
		message="true" bundle="MANAGER_RESOURCES">
		<bean:write name="message" />
	</html:messages> </span>
	<p>
</logic:messagesPresent>

<h3><bean:message bundle="MANAGER_RESOURCES"
	key="label.ectsComparabilityTables.target" /></h3>

<fr:edit id="tableInput" name="tableInput"
	schema="ECTSComparabilityTableInput"
	action="/manageECTSComparabilityTables.do?method=submitInput">
	<fr:destination name="cancel"
		path="/manageECTSComparabilityTables.do?method=prepare" />
	<fr:destination name="invalid"
		path="/manageECTSComparabilityTables.do?method=prepare" />
	<fr:layout name="tabular">
		<fr:property name="classes"
			value="tstyle5 thlight thright thmiddle mtop05" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
</fr:edit>

<logic:present name="statistics">
	<fr:view name="statistics" schema="ECTSComparabilityTableStatistics">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
			<fr:property name="columnClasses" value=",,noborder" />
		</fr:layout>
	</fr:view>
</logic:present>