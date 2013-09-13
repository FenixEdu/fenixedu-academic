<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<h3><bean:message key="title.site.announcements.departmentChannels" bundle="WEBSITEMANAGER_RESOURCES"/></h3>

<div class="infoop2 mvert1">
	<bean:message key="title.site.announcements.departmentChannels.instructions" bundle="WEBSITEMANAGER_RESOURCES"/>
</div>

<jsp:include flush="true" page="/messaging/announcements/listAnnouncementBoards.jsp"/>