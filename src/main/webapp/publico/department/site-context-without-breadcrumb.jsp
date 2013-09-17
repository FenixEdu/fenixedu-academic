<%@ page language="java" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="announcementActionVariable" value="/department/announcements.do" toScope="request"/>
<bean:define id="eventActionVariable" value="/department/events.do" toScope="request"/>
<bean:define id="announcementRSSActionVariable" value="/department/announcementsRSS.do" toScope="request"/>
<bean:define id="eventRSSActionVariable" value="/department/eventsRSS.do" toScope="request"/>
            
<bean:define id="siteActionName" value="/department/departmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="selectedDepartmentUnitID" toScope="request"/>
<bean:define id="siteContextParamValue" name="unit" property="externalId" toScope="request"/>
<bean:define id="department" name="site" property="unit.department" toScope="request"/>

