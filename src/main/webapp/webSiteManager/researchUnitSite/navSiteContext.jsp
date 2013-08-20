<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<bean:define id="siteActionName" value="/manageResearchUnitSite.do" toScope="request"/>
<bean:define id="siteContextParam" value="oid" toScope="request"/>
<bean:define id="siteContextParamValue" name="site" property="externalId" toScope="request"/>

<bean:define id="siteId" name="site" property="externalId"/>
<bean:define id="announcementsActionName" value="/manageResearchUnitAnnouncements.do" toScope="request"/>
