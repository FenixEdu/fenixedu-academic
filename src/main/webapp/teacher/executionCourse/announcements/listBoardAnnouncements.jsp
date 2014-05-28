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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<jsp:include flush="true" page="/messaging/context.jsp"/>

<logic:present name="announcements">
	<bean:define id="announcementBoard" name="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>		

	<h2>
		<bean:message key="messaging.announcements.title.label" bundle="MESSAGING_RESOURCES"/> 
		<span class="small">${announcementBoard.name} 
			<a href="${pageContext.request.contextPath}/external/announcementsRSS.do?method=simple&announcementBoardId=${announcementBoard.externalId}">(RSS)</a>
		</span>
	</h2>

	<ul>
			<logic:equal name="announcementBoard" property="currentUserWriter" value="true">
			<li>
				<html:link action="/announcementManagement.do?method=addAnnouncement&announcementBoardId=${announcementBoardId}&executionCourseID=${executionCourseID}">
					<bean:message key="label.createAnnouncement" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link action="/announcementManagement.do?method=prepareAddFile&announcementBoardId=${announcementBoardId}&executionCourseID=${executionCourseID}">
					<bean:message key="label.files.management" bundle="MESSAGING_RESOURCES"/>
				</html:link>
			</li>
			</logic:equal>
	</ul>

	<jsp:include page="/teacher/executionCourse/announcements/listAnnouncements.jsp" flush="true"/>

</logic:present>