<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="oid" name="site" property="idInternal"/>

<ul>
    <li class="navheader">
        <bean:message key="title.site.manage" bundle="WEBSITEMANAGER_RESOURCES"/>
    </li>
    <li>
        <bean:define id="unitId" name="site" property="unit.idInternal"/>
        <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>" module="/publico" target="_blank">
            <bean:message key="link.site.view" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=introduction&amp;oid=" + oid %>">
            <bean:message key="link.site.introduction" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=manageConfiguration&amp;oid=" + oid %>">
            <bean:message key="link.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=chooseLogo&amp;oid=" + oid %>">
            <bean:message key="link.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=manageBanners&amp;oid=" + oid %>">
            <bean:message key="link.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=sideBanner&amp;oid=" + oid %>">
            <bean:message key="link.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=topNavigation&amp;oid=" + oid %>">
            <bean:message key="link.site.topNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=footerNavigation&amp;oid=" + oid %>">
            <bean:message key="link.site.footerNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=sections&amp;oid=" + oid %>">
            <bean:message key="link.site.sectionsManagement" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageDepartmentSiteAnnouncements.do?method=viewBoards&amp;tabularVersion=true&amp;oid=" + oid %>">
            <bean:message key="link.site.announcements" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
</ul>
