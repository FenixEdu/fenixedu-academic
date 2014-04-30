<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<bean:define id="announcementActionVariable" value="/units/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/units/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/units/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/units/eventsRSS.do" toScope="request"/>

<bean:define id="unit" name="actual$site" property="unit" toScope="request"/>
<bean:define id="siteActionName" value="/units/viewSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="unitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="externalId" toScope="request"/>
