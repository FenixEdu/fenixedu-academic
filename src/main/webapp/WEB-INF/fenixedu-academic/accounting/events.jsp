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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1><spring:message code="events.title" text="Debts and Payments"/></h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-4 col-sm-12">
                    <div class="overall-description">
                        <dl>
                            <dt><spring:message code="label.name" text="Name"/></dt>
                            <dd><c:out value="${name}"/></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="label.document.id.type" text="ID Document Type"/></dt>
                            <dd><c:out value="${idDocumentType}"/></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="label.document.id" text="ID Document"/></dt>
                            <dd><c:out value="${idDocument}"/></dd>
                        </dl>
                    </div>
                </div>
            </div>
        </header>
        <section>
            <div class="row">
                <div class="col-md-12">
                    <h2><spring:message code="label.current.debts" text="Current debts"/></h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <section class="list-debts">
                        <table class="table">
                            <thead>
                            <tr>
                                <th style="width: 100px;"><spring:message code="accounting.event.details.creation.date"
                                                                          text="Creation Date"/></th>
                                <th style="width: 70%;"><spring:message code="label.description" text="Description"/></th>
                                <th><spring:message code="label.total" text="Total"/></th>
                                <th><spring:message code="accounting.event.details.amount.pay" text="To Pay"/></th>
                                <th><span class="sr-only"><spring:message code="label.actions" text="Actions"/></span></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="event" items="${openEvents}">
                                <spring:url value="{event}/details" var="eventUrl">
                                    <spring:param name="event" value="${event.externalId}"/>
                                </spring:url>
                                <tr>
                                    <td style="width: 100px;">
                                        <time datetime="${event.whenOccured.toString('yyyy-MM-dd')}">${event.whenOccured.toString('dd/MM/yyyy')}</time>
                                    </td>
                                    <td style="width: 70%;"><c:out value="${event.description}"/></td>
                                    <td><c:out value="${event.totalAmount}"/></td>
                                    <td><c:out value="${event.totalAmountToPay}"/></td>
                                    <td style="text-align: right;"><a href="${eventUrl}"><spring:message code="label.details"
                                                                                                         text="Details"/></a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </div>
            </div>
        </section>
        <section>
            <div class="row">
                <div class="col-md-12">
                    <h2><spring:message code="label.history" text="History"/></h2>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <section class="list-debts">
                        <table class="table">
                            <thead>
                            <tr>
                                <th style="width: 100px;"><spring:message code="accounting.event.details.creation.date"
                                                                          text="Creation Date"/></th>
                                <th style="width: 70%;"><spring:message code="label.description" text="Description"/></th>
                                <th><spring:message code="label.total" text="Total"/></th>
                                <th><span class="sr-only"><spring:message code="label.actions" text="Actions"/></span></th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach var="event" items="${otherEvents}">
                                <spring:url value="{event}/details" var="eventUrl">
                                    <spring:param name="event" value="${event.externalId}"/>
                                </spring:url>
                                <tr>
                                    <td style="width: 100px;">
                                        <time datetime="${event.whenOccured.toString('yyyy-MM-dd')}">${event.whenOccured.toString('dd/MM/yyyy')}</time>
                                    </td>
                                    <td style="width: 70%;"><c:out value="${event.description}"/></td>
                                    <td><c:out value="${event.totalAmount}"/></td>
                                    <td style="text-align: right;"><a href="${eventUrl}"><spring:message code="label.details"
                                                                                                         text="Details"/></a></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                    </section>
                </div>
            </div>
        </section>
    </main>
</div>
