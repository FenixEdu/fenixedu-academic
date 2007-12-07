<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="announcementActionVariable" value="/department/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/department/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/department/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/department/eventsRSS.do" toScope="request"/>

<bean:define id="siteActionName" value="/researchSite/viewResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="siteID" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="idInternal" toScope="request"/>
<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
<bean:define id="unit" name="site" property="unit" toScope="request"/>
<bean:define id="researchUnit" name="site" property="unit" toScope="request"/>

