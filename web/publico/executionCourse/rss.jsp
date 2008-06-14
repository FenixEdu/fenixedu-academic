<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<h2><bean:message key="label.rss"/></h2>
<p><bean:message key="message.rss.1"/></p>

<h2><bean:message key="message.rss.2"/></h2>
<p><bean:message key="message.rss.3"/></p>

<h2><bean:message key="message.rss.4"/></h2>

<p><bean:message key="message.rss.5"/></p>

<br />
<h2><bean:message key="message.rss.7"/></h2>
	
<p><bean:message key="message.rss.copy.feeds"/></p>	

<% final String appContext = net.sourceforge.fenixedu._development.PropertiesManager.getProperty("app.context"); %>
<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

<bean:define id="linkRSS" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%></bean:define>
<table>
	<tr>
		<bean:define id="urlA" type="java.lang.String"><%= linkRSS %>/external/announcementsRSS.do?announcementBoardId=<bean:write name="executionCourse" property="board.idInternal"/></bean:define>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<strong><bean:message key="label.announcements"/></strong>
		</td>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<a href="<%= urlA %>"><%= urlA %></a>
		</td>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<a href="<%= urlA %>"><img src="<%= request.getContextPath() %>/images/rss_ico.png" alt="<bean:message key="rss_ico" bundle="IMAGE_RESOURCES" />" /></a>
		</td>
	</tr>
	<tr>
		<bean:define id="urlS" type="java.lang.String"><%= linkRSS %>/publico/summariesRSS.do?id=<bean:write name="executionCourse" property="idInternal"/></bean:define>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<strong><bean:message key="label.summaries"/></strong>
		</td>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<a href="<%= urlS %>"><%= urlS %></a>
		</td>
		<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;">
			<a href="<%= urlS %>"><img src="<%= request.getContextPath() %>/images/rss_ico.png" alt="<bean:message key="rss_ico" bundle="IMAGE_RESOURCES" />" /></a>
		</td>
	</tr>
</table>
