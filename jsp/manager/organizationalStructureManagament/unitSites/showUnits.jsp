<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<html:xhtml/>

<h2><bean:message key="title.unitSite.manage.sites" bundle="MANAGER_RESOURCES"/></h2>

<logic:messagesPresent message="true">
	<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES">
	<p class="error0">
		<bean:write name="messages" />
	</p>
	</html:messages>
</logic:messagesPresent>

<fr:view name="units" layout="unit-sites-tree">
	<fr:layout>
        <fr:property name="treeId" value="managerUnitsSites"/>

        <fr:property name="eachLayout" value="values"/>
        <fr:property name="eachSchema" value="unitSite.tree.unit.name"/>

        <fr:property name="unitWithSiteImage" value="/javaScript/drag-drop-folder-tree/images/folder.gif"/>
        <fr:property name="unitWithoutSiteImage" value="/images/functionalities/folder-disabled.gif"/>

        <fr:property name="createSiteLink">
        	<html:link page="/unitSiteManagement.do?method=createSite&amp;unitID=${idInternal}">
        		<bean:message key="link.site.manage.createSite" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
        <fr:property name="chooseManagersLink">
        	<html:link page="/unitSiteManagement.do?method=chooseManagers&amp;oid=${site.idInternal}">
        		<bean:message key="link.site.manage.chooseManagers" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
    </fr:layout>
</fr:view>
