<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml/>

<bean:define id="siteID" name="contract" property="unit.site.externalId"/>
<h2><bean:message key="label.editContract" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<fr:edit name="contract" schema="edit.contract">
	<fr:layout>
		<fr:property name="classes" value="tstyle5 thlight thright"/>
		<fr:property name="columnClasses" value=",,tderror1 tdclear"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/manageResearchUnitSite.do?method=managePeople&oid=" + siteID %>"/>
	<fr:destination name="success" path="<%= "/manageResearchUnitSite.do?method=managePeople&oid=" + siteID %>"/>
</fr:edit>