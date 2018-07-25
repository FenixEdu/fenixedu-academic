<%--

    Copyright © 2017 Instituto Superior Técnico

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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<spring:url var="backUrl" value="${entrypointUrl}">
    <spring:param name="user" value="${eventUsername}"/>
</spring:url>

${portal.toolkit()}

<div class="row">
    <div class="col-md-12">
        <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
    </div>
</div>

<div class="page-header">
    <h1>
        <spring:message code="accounting.event.summary.title" text="Event Summary"/>
    </h1>
</div>

<c:if test="${not empty error}">
    <section>
        <ul class="nobullet list6">
            <li><span class="error0"><c:out value="${error}"/></span></li>
        </ul>
    </section>
</c:if>

<section>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><spring:message code="accounting.event.details.creation.date" text="Creation date"/></th>
                        <th><spring:message code="label.registered.date" text="Registered date"/></th>
                        <th><spring:message code="accounting.event.summary.type" text="Type"/></th>
                        <th><spring:message code="label.description" text="Description"/></th>
                        <th><spring:message code="label.amount" text="Amount"/></th>
                        <th><spring:message code="label.actions" text="Actions"/></th>
                    </tr>
                </thead>
                <tbody>
                <c:if test="${empty creditEntries}">
                    <tr>
                        <td colspan="6">Não existem eventos para apagar.</td>
                    </tr>
                </c:if>
                <c:if test="${not empty creditEntries}">
                <c:forEach var="entry" items="${creditEntries}" varStatus="loop">
                    <spring:url value="../{event}/delete/{entry}" var="deleteEntryUrl">
                        <spring:param name="event" value="${event.externalId}"/>
                        <spring:param name="entry" value="${entry.id}"/>
                    </spring:url>
                    <tr>
                        <td><c:out value="${entry.created.toString('dd/MM/yyyy HH:mm:ss')}"/></td>
                        <td><c:out value="${entry.date.toString('dd/MM/yyyy')}"/></td>
                        <td><c:out value="${entry.typeDescription.content}"/></td>
                        <td><c:out value="${entry.description}"/></td>
                        <td><c:out value="${entry.amount}"/></td>
                        <td>
                            <c:if test="${loop.last}">
                                <a href="${deleteEntryUrl}" onclick="return confirm('Are you sure?')">Apagar</a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</section>


