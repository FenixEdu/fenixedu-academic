<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<html:xhtml/>

<jsp:include page="/commons/renderers/treeRendererHeader.jsp" />

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
        <fr:property name="eachLayout" value="values"/>
        <fr:property name="eachSchema" value="unitSite.tree.unit.name"/>

        <fr:property name="unitWithSiteImage" value="/javaScript/drag-drop-folder-tree/images/folder.gif"/>
        <fr:property name="unitWithoutSiteImage" value="/images/functionalities/folder-disabled.gif"/>

        <fr:property name="createSiteLink">
        	<html:link page="/unitSiteManagement.do?method=createSite&amp;unitID=${externalId}">
        		<bean:message key="link.site.manage.createSite" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
        <fr:property name="chooseManagersLink">
        	<html:link page="/unitSiteManagement.do?method=chooseManagers&amp;oid=${site.externalId}">
        		<bean:message key="link.site.manage.chooseManagers" bundle="SITE_RESOURCES"/>
        	</html:link> , 
        	<html:link page="/unitSiteManagement.do?method=prepareCreateEntryPoint&amp;oid=${site.externalId}">
        		<bean:message key="link.site.manage.entryPoint" bundle="SITE_RESOURCES"/>
        	</html:link>
		</fr:property>
    </fr:layout>
</fr:view>
