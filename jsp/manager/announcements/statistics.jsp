<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<h2>
	<bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/>
</h2>

<h3>
	<bean:message key="manager.announcements.stats.label" bundle="MANAGER_RESOURCES"/>
</h3>

<h4>
	<bean:message key="manager.announcements.stats.boardStats.label" bundle="MANAGER_RESOURCES"/>
</h4>
<table width="100%">
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.existingBoards.label" bundle="MANAGER_RESOURCES"/>		
		</td>
		<td width="65%">
			<bean:write name="boardsCount"/>
		</td>
	</tr>
</table>
<h4>
	<bean:message key="manager.announcements.stats.announcementsStats.label" bundle="MANAGER_RESOURCES"/>
</h4>
<table width="100%">
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.visibleAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="announcementsCount"/>
		</td>
	</tr>
</table>
<h4>
	<bean:message key="manager.announcements.stats.visibleAnnouncementStats.label" bundle="MANAGER_RESOURCES"/>
</h4>
<table width="100%">
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.visibleAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="visibleAnnouncementsCount"/>
		</td>
	</tr>
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.visibleNotExpiredAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="visibleNotExpiredAnnouncementsCount"/>
		</td>
	</tr>
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.visibleExpiredAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="visibleExpiredAnnouncementsCount"/>
		</td>
	</tr>
</table>
<h4>
	<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.stats.invvisibleAnnouncementStats.label"/>
</h4>
<table width="100%">
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.invisibleAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="invisibleAnnouncementsCount"/>
		</td>
	</tr>
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.invisibleNotExpirednnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="invisibleNotExpiredAnnouncementsCount"/>
		</td>
	</tr>
	<tr>
		<td width="35%">
			<bean:message key="manager.announcements.stats.invisibleExpiredAnnouncements.label" bundle="MANAGER_RESOURCES"/>
		</td>
		<td width="65%">
			<bean:write name="invisibleExpiredAnnouncementsCount"/>
		</td>
	</tr>						
</table>        
