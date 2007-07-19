<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="siteID" name="site" property="idInternal"/>

<ul>
	<li>
		<html:link target="blank" module="/publico" page="<%= "/researchSite/viewResearchUnitSite.do?method=frontPage&amp;siteID=" + siteID%>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.view"/>
		</html:link>
	</li>
	
	<li class="navheader"><bean:message key="link.site.researchUnit" bundle="WEBSITEMANAGER_RESOURCES"/></li>
    <li>
	    <html:link page="<%= "/manageResearchUnitSite.do?method=managePeople&amp;oid=" + siteID %>">
            <bean:message key="link.site.contract" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>

	
	<li class="navheader"><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.configuration"/></li>
	<li>
		<html:link page="<%= "/manageResearchUnitSite.do?method=prepareAddManager&amp;oid=" + siteID%>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.addManager"/>
		</html:link>
	</li>
	 <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=manageConfiguration&amp;oid=" + siteID %>">
            <bean:message key="link.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
  
    
	<li class="navheader"><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.manageContents"/></li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=chooseLogo&amp;oid=" + siteID %>">
            <bean:message key="link.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
	<li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=introduction&amp;oid=" + siteID %>">
            <bean:message key="link.site.introduction" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=manageBanners&amp;oid=" + siteID %>">
            <bean:message key="link.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=sideBanner&amp;oid=" + siteID %>">
            <bean:message key="link.site.sideBanner" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=topNavigation&amp;oid=" + siteID %>">
            <bean:message key="link.site.topNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=footerNavigation&amp;oid=" + siteID %>">
            <bean:message key="link.site.footerNavigation" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
	<li>
		<html:link page="<%= "/manageResearchUnitSite.do?method=sections&amp;oid=" + siteID %>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.sectionsManagement"/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/manageResearchUnitAnnouncements.do?method=editAnnouncementBoards&amp;tabularVersion=true&amp;oid=" + siteID %>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.announcements"/>
		</html:link>
	</li>
	
	<li class="navheader">
		<bean:message key="title.site.functions" bundle="WEBSITEMANAGER_RESOURCES"/>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=manageFunctions&amp;oid=" + siteID %>">
            <bean:message key="link.site.manage.functions" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
</ul>