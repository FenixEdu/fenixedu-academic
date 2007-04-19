<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<h2><bean:message key="label.createResearchSite.title" bundle="MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
	<p class="error0">
		<bean:write name="messages" />
	</p>
	</html:messages>
</logic:messagesPresent>

<p>
<fr:view name="resultUnits" schema="researchUnit">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1"/>
		<fr:property name="key(createSite)" value="label.createSite"/>
		<fr:property name="bundle(createSite)" value="MANAGER_RESOURCES" />
		<fr:property name="link(createSite)" value="/researchUnitSiteManagement.do?method=createSite"/>
		<fr:property name="param(createSite)" value="idInternal/unitID"/>
		<fr:property name="visibleIfNot(createSite)" value="siteAvailable"/>
		<fr:property name="key(editManagers)" value="label.editManagers"/>
		<fr:property name="bundle(editManagers)" value="MANAGER_RESOURCES" />
		<fr:property name="link(editManagers)" value="/researchUnitSiteManagement.do?method=editManagers"/>
		<fr:property name="param(editManagers)" value="idInternal/unitID"/>
		<fr:property name="visibleIf(editManagers)" value="siteAvailable"/>
		<fr:property name="sortBy" value="name"/>
	</fr:layout>
	
</fr:view>
</p>