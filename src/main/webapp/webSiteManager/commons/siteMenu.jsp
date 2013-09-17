<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<bean:define id="site" name="site" type="net.sourceforge.fenixedu.domain.Site"/>
<bean:define id="siteID" name="site" property="externalId"/>
<bean:define id="actionName" name="siteActionName"/>
<bean:define id="contextParam" name="siteContextParam"/>
<bean:define id="contextParamValue" name="siteContextParamValue"/>
<bean:define id="context" value="<%= contextParam + "=" + contextParamValue %>"/>

<bean:define id="announcementsActionName" name="announcementsActionName"/>

<ul>
    <li>
 	<app:contentLink name="site">
            <bean:message key="link.site.view" bundle="WEBSITEMANAGER_RESOURCES"/>
	</app:contentLink>				
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
        <html:link page="<%= String.format("%s?method=viewBoards&amp;tabularVersion=true&amp;%s&amp;siteId=%s", announcementsActionName, context, siteID) %>">
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
