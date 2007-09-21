<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="announcementsActionName" name="announcementsActionName"/>
<bean:define id="publicSiteUrl" name="publicSiteUrl"/>

<ul>
    <li>
        <html:link page="<%= "" + publicSiteUrl %>" module="/publico" target="_blank">
            <bean:message key="link.site.view" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li class="navheader">
        <bean:message key="title.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=chooseManagers&amp;%s", actionName, context) %>">
            <bean:message key="link.site.chooseManagers" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=manageConfiguration&amp;%s", actionName, context) %>">
            <bean:message key="link.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=analytics&amp;%s", actionName, context) %>">
            <bean:message key="link.site.analytics" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    
    
	<li class="navheader"><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.manageContents"/></li>
    <li>
        <html:link page="<%= String.format("%s?method=chooseLogo&amp;%s", actionName, context) %>">
            <bean:message key="link.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>

    <li>
        <html:link page="<%= String.format("%s?method=introduction&amp;%s", actionName, context) %>">
            <bean:message key="link.site.introduction" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=manageBanners&amp;%s", actionName, context) %>">
            <bean:message key="link.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=sideBanner&amp;%s", actionName, context) %>">
            <bean:message key="link.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=topNavigation&amp;%s", actionName, context) %>">
            <bean:message key="link.site.topNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=footerNavigation&amp;%s", actionName, context) %>">
            <bean:message key="link.site.footerNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=sections&amp;%s", actionName, context) %>">
            <bean:message key="link.site.sectionsManagement" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=viewBoards&amp;tabularVersion=true&amp;%s", announcementsActionName, context) %>">
            <bean:message key="link.site.announcements" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    
    <li class="navheader">
		<bean:message key="title.site.functions" bundle="WEBSITEMANAGER_RESOURCES"/>
    </li>
    <li>
        <html:link page="<%= String.format("%s?method=manageFunctions&amp;%s", actionName, context) %>">
            <bean:message key="link.site.manage.functions" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
</ul>
