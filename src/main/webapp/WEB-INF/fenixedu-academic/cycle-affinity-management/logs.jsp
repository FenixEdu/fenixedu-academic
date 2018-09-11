<%--

    Copyright © 2018 Instituto Superior Técnico

    This file is part of FenixEdu Academic.

    FenixEdu Academic is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Academic is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="baseUrl" value="/cycle-affinity-management"/>

<div class="page-header">
    <h1>
        <spring:message code="title.manage.cycles.affinity"/>
        <small><spring:message code="label.history" /></small>
    </h1>
</div>

<div class="btn-group">
    <a class="btn btn-default" href="${baseUrl}"><spring:message code="label.back"/></a>
</div>

<table class="table results">
    <thead>
    <th><spring:message code="label.log.datetime" /></th>
    <th><spring:message code="label.log.person" /></th>
    <th><spring:message code="label.log.description" /></th>
    </thead>
    <tbody>
    <c:forEach var="affinityLog" items="${affinityLogs}">
        <tr>
            <td>
                <c:out value="${affinityLog.whenDateTime}"/>
            </td>
            <td>
                <c:out value="${affinityLog.person.profile.displayName}"/>
            </td>
            <td>
                <c:out value="${affinityLog.description}"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>