<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<%
final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context");
final String context = (appContext != null && appContext.length() > 0) ? ("/" + appContext) : "";
net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard board = (net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard) request.getAttribute("announcementBoard");
net.sourceforge.fenixedu.domain.messaging.Announcement ann = (net.sourceforge.fenixedu.domain.messaging.Announcement) request.getAttribute("announcement");
StringBuffer buffer = new StringBuffer();
if (board!=null && board.getReaders()==null)
{
	for (net.sourceforge.fenixedu.domain.messaging.Announcement currentAnnouncement: board.getAnnouncements())
	{
		if (currentAnnouncement.getKeywords() != null)
		{
			buffer.append(currentAnnouncement.getKeywords().getContent()).append(",");
		}
	}
%>
	<meta name="keywords" content="<%=buffer.toString()%>"/>
<%
}
else if(ann!=null && ann.getAnnouncementBoard().getReaders()==null)
{
	if (ann.getKeywords() != null)
	{
		buffer.append(ann.getKeywords().getContent());
	}
%>

	<meta name="keywords" content="<%=buffer.toString()%>"/>
	<bean:define name="announcement" id="announcement" type="net.sourceforge.fenixedu.domain.messaging.Announcement"/>
	<bean:define id="linkRSSAnn" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%><%="/external/announcementsRSS.do?announcementBoardId=" + announcement.getAnnouncementBoard().getIdInternal().toString()%></bean:define>	
<%
}
%>
