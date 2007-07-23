<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="siteActionName" value="/manageResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>

<bean:define id="siteId" name="site" property="idInternal"/>
<bean:define id="publicSiteUrl" value="<%= "/researchSite/viewResearchUnitSite.do?method=frontPage&amp;siteID=" + siteId %>" toScope="request"/>
<bean:define id="announcementsActionName" value="/manageResearchUnitAnnouncements.do" toScope="request" toScope="request"/>
