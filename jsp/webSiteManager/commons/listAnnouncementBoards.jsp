<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h2><bean:message key="title.site.announcements.departmentChannels" bundle="WEBSITEMANAGER_RESOURCES"/></h2>

<div class="infoop2 mvert1">
	<bean:message key="title.site.announcements.departmentChannels.instructions" bundle="WEBSITEMANAGER_RESOURCES"/>
</div>

<jsp:include flush="true" page="/messaging/announcements/listAnnouncementBoards.jsp"/>