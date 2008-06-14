<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

    <logic:present name="announcementBoard">
    	<logic:notPresent name="announcementBoard" property="readers">
			<bean:define name="announcementBoard" id="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard" toScope="request"/>
			<bean:define id="linkRSS" type="java.lang.String" toScope="request">
				<%= request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getIdInternal().toString() %>
			</bean:define>	
			<link rel="alternate" type="application/rss+xml" title="<%= announcementBoard.getName()%>" href="<%= linkRSS.toString()%>" />
		</logic:notPresent>
	</logic:present>

	<logic:present name="announcement">
		<logic:notPresent name="announcement" property="announcementBoard.readers">
			<bean:define name="announcement" id="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>
			<bean:define id="linkRSSAnn" type="java.lang.String">
				<%=request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/external/announcementsRSS.do?announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal().toString()%>
			</bean:define>	
			<link rel="alternate" type="application/rss+xml" title="<%= announcement.getAnnouncementBoard().getName()%>" href="<%= linkRSSAnn.toString()%>" />
		</logic:notPresent>
	</logic:present>
