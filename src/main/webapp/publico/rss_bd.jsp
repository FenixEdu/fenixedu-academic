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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<logic:present name="siteView"> 
	<br/>
	<h2><bean:message key="label.rss"/></h2>
	<p><bean:message key="message.rss.1" arg0="<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>"/></p>

	
	<h2><bean:message key="message.rss.2"/></h2>
	<p><bean:message key="message.rss.3"/></p>

	<h2><bean:message key="message.rss.4"/></h2>

	<p><bean:message key="message.rss.5"/></p>
	<p><bean:message key="message.rss.6"/></p>

	<br />
	<h2><bean:message key="message.rss.7"/></h2>
	
	<p>Copie os URL's para o leitor RSS.</p>

		<bean:define id="linkRSS" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%></bean:define>
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
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;"><a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getExternalId()%>"><%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getExternalId()%></a></td>
			<td style="border-bottom: 1px solid #eee; padding: 0.5em 1em;"><a href="<%= linkRSS %><%="/external/announcementsRSS.do?announcementBoardId=" + ((net.sourceforge.fenixedu.domain.messaging.AnnouncementBoard)pageContext.findAttribute("announcementBoard")).getExternalId()%>"><img src="<%= request.getContextPath() %>/images/rss_ico.gif"></td>
		</tr>
	</table>
</logic:present>
				
            	
        
		
	


