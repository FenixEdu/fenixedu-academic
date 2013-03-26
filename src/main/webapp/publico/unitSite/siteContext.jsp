<%@ page language="java" %>
<%@page import="net.sourceforge.fenixedu.domain.functionalities.FunctionalityContext"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="site" name="<%= FunctionalityContext.CONTEXT_KEY %>" property="selectedContainer" toScope="request"/>

<bean:define id="announcementActionVariable" value="/units/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/units/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/units/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/units/eventsRSS.do" toScope="request"/>

<bean:define id="unit" name="site" property="unit" toScope="request"/>
<bean:define id="siteActionName" value="/units/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="idInternal" toScope="request"/>
