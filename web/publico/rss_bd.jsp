<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<logic:present name="siteView"> 
	<br/>
	<h2><bean:message key="label.rss"/></h2>
	<p><bean:message key="message.rss.1"/></p>

	
	<h2><bean:message key="message.rss.2"/></h2>
	<p><bean:message key="message.rss.3"/></p>

	<h2><bean:message key="message.rss.4"/></h2>

	<p><bean:message key="message.rss.5"/></p>
	<p><bean:message key="message.rss.6"/></p>

	<br />
	<h2><bean:message key="message.rss.7"/></h2>
	
	<p>Copie os URL's para o leitor RSS.</p>	PAPADA SHORT !!!
	
		<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
		<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

		<bean:define id="linkRSS" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%></bean:define>
	<table>
		<tr>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<strong><bean:message key="label.announcements"/></strong>
			</td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + pageContext.findAttribute("announcementBoardId")%>"><%= linkRSS %><%="/publico/announcementsRSS.do?announcementBoardId=" + pageContext.findAttribute("announcementBoardId")%></a>
			</td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + pageContext.findAttribute("announcementBoardId")%>"><img src="<%= request.getContextPath() %>/images/rss_ico.png" alt="<bean:message key="rss_ico" bundle="IMAGE_RESOURCES" />" /></a>
			</td>
		</tr>
		<tr>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<strong><bean:message key="label.summaries"/></strong>
			</td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<a href="<%= linkRSS %><%="/publico/summariesRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%>"><%= linkRSS %><%="/publico/summariesRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%></a>
			</td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
				<a href="<%= linkRSS %><%="/publico/summariesRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%>"><img src="<%= request.getContextPath() %>/images/rss_ico.gif" alt="<bean:message key="rss_ico" bundle="IMAGE_RESOURCES" />" /></a>
			</td>
		</tr>
	</table>
	<table>
		<tr>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;"><strong><bean:message key="label.announcements"/></strong></td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;"><a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getIdInternal()%>"><%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getIdInternal()%></a></td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;"><a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getIdInternal()%>"><img src="<%= request.getContextPath() %>/images/rss_ico.gif"></td>
		</tr>
	</table>
</logic:present>
				
            	
        
		
	


