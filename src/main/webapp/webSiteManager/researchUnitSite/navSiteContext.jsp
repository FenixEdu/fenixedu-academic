<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="siteActionName" value="/manageResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="externalId" toScope="request"/>

<bean:define id="siteId" name="site" property="externalId"/>
<bean:define id="announcementsActionName" value="/manageResearchUnitAnnouncements.do" toScope="request"/>
