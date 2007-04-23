<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="siteID" name="site" property="idInternal"/>

<ul>
	<li></li>
	<li>
		<html:link target="blank" module="/publico" page="<%= "/researchSite/viewResearchUnitSite.do?method=frontPage&amp;siteID=" + siteID%>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.view"/>
		</html:link>
	</li>
	<li class="navheader"><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.manage"/></li>
	<li>
		<html:link page="<%= "/manageResearchUnitSite.do?method=sections&amp;oid=" + siteID %>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.sectionsManagement"/>
		</html:link>
	</li>
	 <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=manageConfiguration&amp;oid=" + siteID %>">
            <bean:message key="link.site.configuration" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=chooseLogo&amp;oid=" + siteID %>">
            <bean:message key="link.site.logo" bundle="WEBSITEMANAGER_RESOURCES"/>
        </html:link>
    </li>
    <li>
        <html:link page="<%= "/manageResearchUnitSite.do?method=manageBanners&amp;oid=" + siteID %>">
            <bean:message key="link.site.banners" bundle="WEBSITEMANAGER_RESOURCES"/>
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
		<html:link page="<%= "/manageResearchUnitAnnouncements.do?method=editAnnouncementBoards&amp;oid=" + siteID %>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.announcements"/>
		</html:link>
	</li>
</ul>