<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<bean:define id="siteID" name="site" property="idInternal"/>

<ul>
	<li class="navheader"><bean:message bundle="WEBSITEMANAGER_RESOURCES" key="title.site.manage"/></li>
	<li>
		<html:link page="<%= "/manageResearchUnitSite.do?method=sections&amp;oid=" + siteID %>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="label.section"/>
		</html:link>
	</li>
	<li>
		<html:link target="blank" module="/publico" page="<%= "/researchSite/viewResearchUnitSite.do?method=frontPage&amp;siteID=" + siteID%>">
			<bean:message bundle="WEBSITEMANAGER_RESOURCES" key="link.site.view"/>
		</html:link>
	</li>
</ul>