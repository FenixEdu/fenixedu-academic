<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<bean:define id="announcementActionVariable" value="/scientificCouncil/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/scientificCouncil/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/scientificCouncil/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/scientificCouncil/eventsRSS.do" toScope="request"/>

<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>
<bean:define id="unit" name="site" property="unit" toScope="request"/>
<bean:define id="siteActionName" value="/scientificCouncil/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>





