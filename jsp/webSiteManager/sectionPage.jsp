<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<jsp:include flush="true" page="/messaging/context.jsp"/>
<logic:present name="announcementBoard">
	<bean:define id="contextPrefix" name="contextPrefix" />
	<bean:define id="extraParameters" name="extraParameters" />
	<bean:define id="announcementBoardId" name="announcementBoard" property="idInternal"/>
	<h2>
		<bean:message key="management.label" bundle="WEBSITEMANAGER_RESOURCES"/> 
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
	</ul>
</logic:present>
