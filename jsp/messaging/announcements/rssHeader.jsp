<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%
final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context");
final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : "";
net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard board = (net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard) request.getAttribute("announcementBoard");
net.sourceforge.fenixedu.domain.messaging.Announcement ann = (net.sourceforge.fenixedu.domain.messaging.Announcement) request.getAttribute("announcement");
if (board!=null && board.getReaders()==null)
{
%>
	<bean:define name="announcementBoard" id="announcementBoard" type="net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard"/>
	<bean:define id="linkRSS" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%><%="/external/announcementsRSS.do?announcementBoardId=" + announcementBoard.getIdInternal().toString()%></bean:define>	
	<link rel="alternate" type="application/rss+xml" title="<%= announcementBoard.getName()%>" href="<%= linkRSS.toString()%>" />
<%
}
else if(ann!=null && ann.getAnnouncementBoard().getReaders()==null)
{
%>
	<bean:define name="announcement" id="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>
	<bean:define id="linkRSSAnn" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%><%="/external/announcementsRSS.do?announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal().toString()%></bean:define>	
	<link rel="alternate" type="application/rss+xml" title="<%= announcement.getAnnouncementBoard().getName()%>" href="<%= linkRSSAnn.toString()%>" />
<%
}
%>
