<%@page import="net.sourceforge.fenixedu.util.FenixConfigurationManager"%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<h2><bean:message key="label.rss"/></h2>
<p><bean:message key="message.rss.1" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></p>

<h2><bean:message key="message.rss.2"/></h2>
<p><bean:message key="message.rss.3"/></p>

<h2><bean:message key="message.rss.4"/></h2>

<p><bean:message key="message.rss.5"/></p>

<br />
<h2><bean:message key="message.rss.7"/></h2>
	
<p><bean:message key="message.rss.copy.feeds"/></p>	

<% final String appContext = FenixConfigurationManager.getConfiguration().appContext(); %>
<% final String context = (appContext != null && appContext.length() > 0) ? "/" + appContext : ""; %>

<bean:define id="linkRSS" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=context%></bean:define>
<table>
	<tr>
		<bean:define id="urlA" type="java.lang.String"><%= linkRSS %>/external/announcementsRSS.do?announcementBoardId=<bean:write name="executionCourse" property="board.externalId"/></bean:define>
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
		<bean:define id="urlS" type="java.lang.String"><%= linkRSS %>/publico/summariesRSS.do?id=<bean:write name="executionCourse" property="externalId"/></bean:define>
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
