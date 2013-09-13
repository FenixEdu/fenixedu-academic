<%@ page language="java" %>

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<bean:define id="siteActionName" value="/manageDepartmentSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="externalId" toScope="request"/>

<bean:define id="unitId" name="site" property="unit.externalId"/>
<bean:define id="announcementsActionName" value="/manageDepartmentSiteAnnouncements.do" toScope="request"/>
