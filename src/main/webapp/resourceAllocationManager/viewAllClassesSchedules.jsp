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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.TagLib.sop.v3.TimeTableType" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoLesson" %>
<%@ page import="net.sourceforge.fenixedu.domain.SchoolClass" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<link href="${pageContext.request.contextPath}/CSS/dotist_timetables.css" rel="stylesheet" type="text/css" />

<c:forEach var="degree" items="${degrees}">
	<c:forEach var="schoolClass" items="${degree.schoolClassesSet}">
		<c:if test="${schoolClass.academicInterval == academicInterval}">
			<div class="single">
			<div class="alert alert-warning">
				${degree.presentationName} - ${academicInterval.pathName}
			</div>
			<div>
				<h4><bean:message key="title.class.timetable" />${schoolClass.nome}</h4>
				<% request.setAttribute("lessons", InfoLesson.newInfosForSchoolClass((SchoolClass) pageContext.findAttribute("schoolClass"))); %>
				<app:gerarHorario name="lessons" definedWidth="false" type="<%= TimeTableType.CLASS_TIMETABLE_WITHOUT_LINKS %>"/>
			</div>
			<br /><br />
			<c:set var="foundAny" value="${true}" />
			</div>
		</c:if>
	</c:forEach>
</c:forEach>

<style type="text/css" media="print">
.single {
	page-break-after: always;
	page-break-inside: avoid;
}
</style>

<c:if test="${!foundAny}">
	<span class="error"><bean:message key="message.classes.notExisting"/></span>
</c:if>