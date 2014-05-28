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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<h2><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked"/></h2>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<div class="mtop2">
	<h3 class="mbottom05"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.bookmarked.boards"/></h3>
	<jsp:include flush="true" page="/messaging/announcements/listBookmarkedAnnouncementBoards.jsp" />
</div>

<logic:notEmpty name="currentExecutionCoursesAnnouncementBoards">
	<div class="mtop15">
		<h3 class="mbottom05"><bean:message bundle="MESSAGING_RESOURCES" key="label.messaging.my.executionCourse.boards"/></h3>
		<jsp:include page="/messaging/announcements/listCurrentExecutionCoursesAnnouncementBoards.jsp" flush="true"/>
	</div>
</logic:notEmpty>
