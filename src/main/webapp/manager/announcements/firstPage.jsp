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

<h2><bean:message key="manager.announcements.title.label" bundle="MANAGER_RESOURCES"/></h2>

<table>
	<tr>
		<td class="well">
			<bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.welcome"/>		
		</td>
	</tr>
</table>


<br/>
<div class="col-lg-offset-4">
	       <html:link styleClass="btn btn-primary" action="/announcements/manageUnitAnnouncementBoard.do?method=showTree">
	               <bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.manageBoard.link" />
	       </html:link>

	       <html:link styleClass="btn btn-primary" action="/announcements/announcementBoardsManagement?method=stats">
	               <bean:message bundle="MANAGER_RESOURCES" key="manager.announcements.stats.link" />
	       </html:link>    

</div>