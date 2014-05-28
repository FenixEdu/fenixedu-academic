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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<jsp:include flush="true" page="/messaging/context.jsp"/>
<logic:present name="announcementBoard">
	<bean:define id="contextPrefix" name="contextPrefix" />
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoardId" name="announcementBoard" property="externalId"/>
	<h2>
		<bean:write name="announcementBoard" property="name"/>
	</h2>
	
	<ul>
		<li>
			<html:link action="<%= contextPrefix + "method=listAnnouncements&announcementBoardId="+announcementBoardId + "&"+ extraParameters%>">
				<bean:message key="messaging.messaging.list" bundle="WEBSITEMANAGER_RESOURCES"/> 
			</html:link>
		</li>
		<li>
			<html:link action="<%= contextPrefix + "method=addAnnouncement&announcementBoardId="+announcementBoardId + "&" + extraParameters%>">
				<bean:message key="messaging.add.label" bundle="WEBSITEMANAGER_RESOURCES"/> 
			</html:link>
		</li>
		<li>
			<html:link action="<%= contextPrefix + "method=prepareAddFile&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
				<bean:message key="label.files.management" bundle="MESSAGING_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link action="<%= contextPrefix + "method=showStickyAnnoucements&amp;announcementBoardId="+announcementBoardId+"&amp;"+extraParameters%>">
				<bean:message key="label.sticky.management" bundle="MESSAGING_RESOURCES"/>
			</html:link>
		</li>

	</ul>
</logic:present>
