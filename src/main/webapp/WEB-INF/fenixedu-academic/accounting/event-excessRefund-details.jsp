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

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<spring:url var="payUrl" value="../../../{event}/pay">
    <spring:param name="event" value="${eventId}"/>
</spring:url>
<spring:url var="backUrl" value="../../../{event}/details">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<c:set var="totalAmount" value="#{debt.totalOpenAmount}"/>

<div class="container-fluid">
    <header>
        <h1>
            <jsp:include page="heading-event.jsp"/>
            <c:if test="${event.currentEventState == 'CANCELLED'}">
                <span class="text-danger"> (Cancelada)</span>
            </c:if>
        </h1>
    </header>
    <c:set var="person" scope="request" value="${event.person}"/>
    <jsp:include page="heading-person.jsp"/>
        <div class="row">
            <div class="col-md-10">
                <h2><c:out value="${excessRefund.description}"/></h2>
            </div>
        </div>
    <div class="row">
        <div class="col-md-4 col-sm-12">
            <div class="overall-description">
                <dl>
                    <dt><spring:message code="accounting.event.details.excessRefund.amount" text="Original amount"/></dt>
                    <dd><c:out value="${excessRefund.amount}"/><span>€</span></dd>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.creation.date" text="Creation Date"/></dt>
                    <dd><time datetime="${excessRefund.created.toString("yyyy-MM-dd")}">${excessRefund.created.toString("dd/MM/yyyy")}</time></dd>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.excessRefund.date" text="Advance Date"/></dt>
                    <dd><time datetime="${excessRefund.date.toString("yyyy-MM-dd")}">${excessRefund.date.toString("dd/MM/yyyy")}</time></dd>
                </dl>
                <c:if test="${not empty targetPaymentId}">
                    <dl>
                        <dt><spring:message code="accounting.event.details.excessRefund.payment" text="Advance Date"/></dt>
                        <dd>
                            <spring:url value="../../../{event}/transaction/{targetPaymentId}/details" var="targetPaymentIdUrl">
                                <spring:param name="event" value="${targetPaymentId.event.externalId}"/>
                                <spring:param name="targetPaymentId" value="${targetPaymentId.externalId}"/>
                            </spring:url>
                            <a href="${targetPaymentIdUrl}"><c:out value="${targetPaymentId.event.description}"/></a>
                        </dd>
                    </dl>
                </c:if>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2>Pagamentos</h2>
            </header>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <section class="fee-payments">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Data de processamento</th>
                        <th>Data de adiantamento</th>
                        <th>Tipo</th>
                        <th>Pago</th>
                        <th>Divida</th>
                        <th>Juros/Multas</th>
                        <th>Adiantamento</th>
                        <th><span class="sr-only">Acções</span></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty payments}">
                        <tr>
                            <td colspan="5">Não existem pagamentos</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty payments}">
                    <c:forEach var="accountingEntrySummary" items="${payments}">
                        <c:set var="amountUsedInInterestOrFine" value="#{accountingEntrySummary.amountUsedInInterest + accountingEntrySummary.amountUsedInFine}"/>
                        <tr>
                            <td><time datetime="${accountingEntrySummary.created.toString('yyyy-MM-dd HH:mm:ss')}"><c:out value="${accountingEntrySummary.created.toString('dd/MM/yyyy HH:mm:ss')}"/></time></td>
                            <td><time datetime="${accountingEntrySummary.date.toString('yyyy-MM-dd')}"><c:out value="${accountingEntrySummary.date.toString('dd/MM/yyyy')}"/> </time></td>
                            <td><c:out value="${accountingEntrySummary.typeDescription.content}"/></td>
                            <td><c:out value="${accountingEntrySummary.amount.toPlainString()}"/><span>€</span></td>
                            <td><c:out value="${accountingEntrySummary.amountUsedInDebt.toPlainString()}"/><span>€</span></td>
                            <td><c:out value="${amountUsedInInterestOrFine}"/><span>€</span></td>
                            <td><c:out value="${accountingEntrySummary.amountUsedInAdvance}"/><span>€</span></td>
                            <spring:url value="../../../{event}/transaction/{id}/details" var="paymentUrl" scope="request">
                                <spring:param name="event" value="${eventId}"/>
                                <spring:param name="id" value="${accountingEntrySummary.id}"/>
                            </spring:url>
                            <td><a href="${paymentUrl}"><spring:message code="label.details"/></a></td>
                        </tr>
                    </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </section>
        </div>
    </div>
</div>
