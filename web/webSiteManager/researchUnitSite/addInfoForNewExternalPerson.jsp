<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.inviteExternalPerson" bundle="WEBSITEMANAGER_RESOURCES"/></h2>
<bean:define id="siteID" name="site" property="idInternal"/>

<fr:view name="bean" schema="research.contract.information">
	<fr:layout>
		<fr:property name="classes" value="tstyle1 thlight thright"/>
	</fr:layout>
</fr:view>


<bean:define id="schema" value="research.contract.extraInformation.for.externalPerson"/>

<logic:present name="bean" property="documentIDNumber">
	<bean:define id="schema" value="research.contract.extraInformation.for.internalPerson"/>
</logic:present>

<fr:edit id="extraInfo" name="bean" schema="<%= schema %>" action="<%= "/manageResearchUnitSite.do?method=addNewPerson&oid=" + siteID %>" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		<fr:destination name="invalid" path="<%= "/manageResearchUnitSite.do?method=prepareAddNewPerson&oid=" + siteID %>"/>
	</fr:layout>
</fr:edit>

