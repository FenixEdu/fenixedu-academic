<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<h2><bean:message key="label.managePeople.title" bundle="WEBSITEMANAGER_RESOURCES"/></h2>
<bean:define id="siteID" name="site" property="idInternal"/>

<fr:view name="bean" schema="research.contract.information"/>

<fr:edit id="extraInfo" name="bean" schema="research.contract.extraInformation.for.externalPerson" action="<%= "/manageResearchUnitSite.do?method=addNewPerson&oid=" + siteID %>" >
	<fr:layout>
		<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
</fr:edit>