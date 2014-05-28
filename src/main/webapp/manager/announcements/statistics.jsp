<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>


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
