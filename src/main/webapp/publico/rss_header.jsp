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

<logic:present name="siteView">
	<logic:present name="siteView" property="commonComponent.executionCourse.nome">
		<bean:define id="executionCourseName" name="siteView" property="commonComponent.executionCourse.nome" />

		<bean:define id="linkRSSa" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%="/publico/announcementsRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%></bean:define>
		<link rel="alternate" type="application/rss+xml" title="<%= executionCourseName %>" href="<%= linkRSSa %>"/>

		<bean:define id="linkRSSs" type="java.lang.String"><%=request.getScheme()%>://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%><%="/publico/summariesRSS.do?id=" + pageContext.findAttribute("executionCourseCode")%></bean:define>
		<link rel="alternate" type="application/rss+xml" title="<%= executionCourseName %>" href="<%= linkRSSs %>"/>

	</logic:present>
</logic:present>