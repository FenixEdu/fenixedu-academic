<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<bean:define id="siteID" name="contract" property="unit.site.idInternal"/>
<h2><bean:message key="label.editContract" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<fr:edit name="contract" schema="edit.contract">
	<fr:layout>
			<fr:property name="classes" value="tstyle5"/>
	</fr:layout>
	<fr:destination name="cancel" path="<%= "/manageResearchUnitSite.do?method=managePeople&oid=" + siteID %>"/>
	<fr:destination name="success" path="<%= "/manageResearchUnitSite.do?method=managePeople&oid=" + siteID %>"/>
</fr:edit>