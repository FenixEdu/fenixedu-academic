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
<%@page import="org.fenixedu.academic.domain.accounting.PaymentMethod"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>


<spring:url var="backUrl" value="../../../{event}/details">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<div class="container-fluid">
    <header>
        <h1>
            <jsp:include page="heading-event.jsp"/>
            <c:if test="${event.currentEventState == 'CANCELLED'}">
                <span class="text-danger"> <spring:message code="accounting.event.details.currentEventState.cancelled" text="(Cancelled)"/></span>
            </c:if>
        </h1>
    </header>
    <c:set var="person" scope="request" value="${event.person}"/>
    <jsp:include page="heading-person.jsp"/>
        <div class="row">
            <div class="col-md-12">
                <h2>
                    <spring:message code="accounting.refund.details.title" text="Relatório de Reembolso"/>
                </h2>
            </div>
        </div>
    <div class="row">
        <div class="col-md-5">
            <section class="payment-metadata">
                <dl>
                    <dt><spring:message code="accounting.event.details.creation.date" text="Creation Date"/>:</dt>
                    <time datetime="${refund.whenOccured.toString('yyyy-MM-dd HH:mm:ss')}">${refund.whenOccured.toString('dd/MM/yyyy HH:mm:ss')}</time>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.state" text="Estado"/></dt>
                    <dd>
                        <time datetime="${refund.state.qualifiedName}">${refund.state.qualifiedName}</time>
                    </dd>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.state.date" text="Data do Estado"/></dt>
                    <dd>
                        <time datetime="${refund.stateDate.toString('yyyy-MM-dd')}">${refund.stateDate.toString('dd/MM/yyyy')}</time>
                    </dd>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.value" text="Value"/>:</dt>
                    <dd>
                        <c:out value="${refund.amount.toPlainString()}"/><span>€</span>
                    </dd>
                </dl>
                <dl>
                    <dt><spring:message code="label.org.fenixedu.academic.dto.accounting.bankAccountNumber" text="Bank Account Number"/>:</dt>
                    <dd>
                        <c:out value="${refund.bankAccountNumber}"/><span>€</span>
                    </dd>
                </dl>
            </section>
        </div>
        <div class="col-md-2 col-md-offset-2">
            <section class="payment-totals">
                <dl class="total">
                    <dt><spring:message code="accounting.payment.details.refund.value" text="Valor Total"/></dt>
                    <dd><c:out value="${amount}"/><span>€</span></dd>
                </dl>
                <c:forEach var="payment" items="${payments}">
                    <c:set var="totalPaidAmount" value="#{payment.amountUsedInDebt + payment.amountUsedInInterest + payment.amountUsedInFine}"/>
                    <dl>
                        <dt><c:out value="${payment.debtEntry.description}"/> </dt>
                        <dd><c:out value="${totalPaidAmount.toPlainString()}"/><span>€</span></dd>
                    </dl>
                </c:forEach>
            </section>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2><spring:message code="accounting.event.details.debts" text="Debts"/></h2>
            </header>
        </div>
    </div>
    <section class="payment-values">
        <table class="table">
            <thead>
            <tr>
                <th><spring:message code="accounting.event.details.description" text="Description"/></th>
                <th><spring:message code="accounting.event.details.date" text="Date"/></th>
                <th><spring:message code="accounting.event.details.total" text="Total"/></th>
                <th><spring:message code="accounting.event.details.transactions.debt" text="Debt"/></th>
                <th><spring:message code="accounting.event.details.transactions.interestOrFine" text="Interest/Fine"/></th>
                <th><span class="sr-only"><spring:message code="accounting.event.details.transactions.actions" text="Actions"/></span></th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty payments}">
                <tr>
                    <td colspan="6"><spring:message code="accounting.event.details.noInstallments" text="There are no installments."/></td>
                </tr>
                </c:if>
            <c:if test="${not empty payments}">
                <c:forEach var="payment" items="${payments}">
                    <c:set var="amountUsedInInterestOrFine" value="#{payment.amountUsedInInterest + payment.amountUsedInFine}"/>
                    <c:set var="totalPaidAmount" value="#{payment.amountUsedInDebt + payment.amountUsedInInterest + payment.amountUsedInFine}"/>
                    <tr>
                        <td><c:out value="${payment.debtEntry.description}"/></td>
                        <td>
                            <time datetime="${payment.debtEntry.date.toString('yyyy-MM-dd')}">${payment.debtEntry.date.toString('dd/MM/yyyy')}</time>
                        </td>
                        <td><c:out value="${totalPaidAmount.toPlainString()}"/><span>€</span></td>
                        <td><c:out value="${payment.amountUsedInDebt.toPlainString()}"/><span>€</span></td>
                        <td><c:out value="${amountUsedInInterestOrFine}"/><span>€</span></td>

                        <spring:url value="../../../{event}/transaction/{id}/details" var="debtUrl">
                            <spring:param name="event" value="${event.externalId}"/>
                            <spring:param name="id" value="${payment.debtEntry.id}"/>
                        </spring:url>

                        <td style="text-align: right;"><a href="${debtUrl}"><spring:message code="label.details"/></a></td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </section>
    </main>
</div>