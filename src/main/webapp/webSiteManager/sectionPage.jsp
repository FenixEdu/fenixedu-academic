<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

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
