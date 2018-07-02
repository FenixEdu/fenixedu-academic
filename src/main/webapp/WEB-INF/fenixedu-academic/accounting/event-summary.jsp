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
<%@ page import="org.fenixedu.academic.domain.degreeStructure.CurricularStage" %>

<div class="page-header">
    <h1>
        <spring:message code="accounting.event.summary.title" text="Event Summary"/>
    </h1>
</div>

<section>
    <div class="row">
        <div class="col-xs-12">
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th><spring:message code="accounting.event.summary.created" text="Creation Date"/></th>
                        <th><spring:message code="accounting.event.summary.date" text="Real Date"/></th>
                        <th><spring:message code="accounting.event.summary.type" text="Tipo"/></th>
                        <th><spring:message code="accounting.event.summary.description" text="Description"/></th>
                        <th><spring:message code="accounting.event.summary.amount" text="Amount"/></th>
                        <th><spring:message code="accounting.event.summary.actions" text="Actions"/></th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="entry" items="${debtCalculator.getCreditEntries()}">
                    <tr>
                        <td><c:out value="${entry.created.toString('dd/MM/yyyy HH:mm:ss')}"/></td>
                        <td><c:out value="${entry.date.toString('dd/MM/yyyy')}"/></td>
                        <td><c:out value="${entry.typeDescription.content}"/></td>
                        <td><c:out value="${entry.description}"/></td>
                        <td><c:out value="${entry.amount}"/></td>
                        <td></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</section>

<%--<c:if test="${not empty error}">--%>
    <%--<section>--%>
        <%--<ul class="nobullet list6">--%>
            <%--<li><span class="error0"><c:out value="${error}"/></span></li>--%>
        <%--</ul>--%>
    <%--</section>--%>
<%--</c:if>--%>
