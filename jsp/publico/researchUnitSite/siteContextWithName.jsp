<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="siteActionName" value="/researchSite/viewResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="siteID" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>
<bean:define id="site" name="site" toScope="request"/>
<bean:define id="announcementActionVariable" value="/researchSite/manageResearchUnitAnnouncements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/researchSite/manageResearchUnitAnnouncements.do" toScope="request"/>

<h1 class="mbottom03 cnone"><fr:view name="site" property="unit.nameWithAcronym"/></h1>