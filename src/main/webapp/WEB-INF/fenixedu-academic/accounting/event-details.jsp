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
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<spring:url var="payUrl" value="../{event}/pay">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<spring:url var="depositUrl" value="../{event}/deposit">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<spring:url var="exemptUrl" value="../{event}/exempt">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<spring:url var="cancelUrl" value="../{event}/cancel">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<spring:url var="backUrl" value="${entrypointUrl}">
    <spring:param name="person" value="${event.person.externalId}"/>
</spring:url>

<div class="container-fluid">
    <header>
        <div class="row">
            <div class="col-md-10">
                <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
                <h1>
                    <c:out value="${event.description}"/>
                    <c:if test="${event.currentEventState == 'CANCELLED'}">
                        <span class="text-danger"> (<spring:message code="accounting.event.canceled" text="Cancelled"/>)</span>
                    </c:if>
                </h1>
            </div>

        </div>
        <div class="row">
            <div class="col-md-4">
                <div class="overall-description">
                    <dl>
                        <dt><spring:message code="label.name" text="Name"/></dt>
                        <dd><c:out value="${event.person.presentationName}"/></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.creation.date" text="Creation Date"/></dt>
                        <dd><time datetime="${event.whenOccured.toString("yyyy-MM-dd")}">${event.whenOccured.toString("dd/MM/yyyy")}</time></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.amount.pay" text="To pay"/></dt>
                        <dd><c:out value="${eventTotalAmountToPay}"/> <span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.debt.amount.pay" text="Debt"/></dt>
                        <dd><c:out value="${eventDebtAmountToPay}"/> <span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.interestOrFines.amount.pay" text="Interest"/></dt>
                        <dd><c:out value="${eventInterestAmountToPay + eventFineAmountToPay}"/> <span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.original.debt.amount" text="Original amount"/></dt>
                        <dd><c:out value="${eventOriginalAmountToPay}"/> <span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                    </dl>
                </div>
            </div>
            <div class="col-md-4">
                <modular:intersect location="event.details.extra.info" position="info"> 
                    <modular:arg key="event" value="${event}"/>
                    <modular:arg key="isPaymentManager" value="${isPaymentManager}"/>
                </modular:intersect>
            </div>
            <div class="col-md-2 col-md-push-1">
                <c:if test="${event.currentEventState != 'CANCELLED'}">
                    <c:if test="${eventTotalAmountToPay > 0 && isEventOwner}">
                        <a class="btn btn-primary btn-block" href="${payUrl}"><spring:message code="accounting.event.action.pay" text="Pay"/></a>
                    </c:if>
                    <c:if test="${isPaymentManager}">
                        <c:if test="${eventTotalAmountToPay > 0}">
                            <a class="btn btn-default btn-block" href="${depositUrl}"><spring:message code="accounting.event.action.deposit" text="Deposit"/></a>
                        </c:if>
                    </c:if>
                    <c:if test="${isAdvancedPaymentManager}">
                        <c:if test="${eventTotalAmountToPay > 0}">
                            <a class="btn btn-default btn-block" href="${exemptUrl}"><spring:message code="accounting.event.action.exempt" text="Exempt"/></a>
                        </c:if>
                        <a class="btn btn-danger btn-block" href="${cancelUrl}"><spring:message code="accounting.event.action.cancel" text="Cancel"/></a>
                    </c:if>
                </c:if>
                <modular:intersect location="event.details.extra.info" position="operations"> 
                    <modular:arg key="event" value="${event}"/>
                    <modular:arg key="isPaymentManager" value="${isPaymentManager}"/>
                </modular:intersect>
            </div>
        </div>
        <div class="row">
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2><spring:message code="accounting.event.installments" text="Installments"/></h2>
            </header>
        </div>
    </div>
    <main>
        <div class="row">
            <div class="col-md-12">
                <div class="cards">
                    <c:forEach var="debt" items="${debts}" varStatus="debtLoop">
                        <c:choose>
                            <c:when test="${!debt.open}">
                                <c:set var="cardClass" value="fully-paid"/>
                            </c:when>
                            <c:when test="${debt.totalOpenPenaltyAmount > 0 && debt.isAfterDueDate(currentDate) && !(debt.isToExemptInterest() || debt.isToExemptFine())}">
                                <c:set var="cardClass" value="passed-limit"/>
                            </c:when>
                            <c:otherwise>
                                <c:set var="cardClass" value=""/>
                            </c:otherwise>
                        </c:choose>
                        <article class="card ${cardClass}">
                            <header>
                                <c:set var="debtNumber" value="#{debtLoop.index + 1}"/>

                                <h1><spring:message code="accounting.event.details.debt.name" arguments="${debtNumber}" text="Installment {0}"/> </h1>
                                <%--<h1><spring:message code="accounting.event.details.debt.name" arguments="${debt.getDate().toString('dd/MM/yyyy')}" text="Installment {0}"/> </h1>--%>
                            </header>
                            <div class="status">
                                <c:choose>
                                    <c:when test="${!debt.open}">
                                        <spring:message code="accounting.event.details.debt.paid" text="Paid"/>
                                    </c:when>
                                    <c:when test="${debt.totalOpenPenaltyAmount > 0 && debt.isAfterDueDate(currentDate) && !(debt.isToExemptInterest() || debt.isToExemptFine())}">
                                        <spring:message code="accounting.event.details.debt.due.with.interest" text="Due with interest"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="accounting.event.details.debt.due" text="Due"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <dl class="total">
                                <dt><spring:message code="accounting.event.details.debt.to.pay" text="To Pay"/></dt>
                                <dd><c:out value="${debt.totalOpenAmount.toPlainString()}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt.fine" text="Debt"/>:</dt>
                                <dd><c:out value="${debt.openAmount.toPlainString()}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt.interestOrFines" text="Interest/Fines"/>:</dt>
                                <dd>
                                    <c:out value="${debt.openInterestAmount.toPlainString() + debt.openFineAmount}"/><span><spring:message code="label.currency.euro" text="Eur"/></span>
                                    <c:if test="${debt.isToExemptInterest() || debt.isToExemptFine()}">
                                        <spring:message code="accounting.event.details.debt.hasExemption" text="(Exemption)"/>
                                    </c:if>
                                </dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt" text="Installment"/>:</dt>
                                <dd><c:out value="${debt.originalAmount.toPlainString()}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></dd>
                            </dl>
                            <dl class="details limit-date">
                                <dt><spring:message code="accounting.event.details.debt.due.date" text="Due Date"/>:</dt>
                                <dd><c:out value="${debt.date.toString('dd/MM/yyyy')}"/></dd>
                            </dl>
                            <footer>
                                <a class="btn btn-default btn-block" href="debt/${debt.date.toString('yyyy-MM-dd')}/details">Ver Detalhes</a>
                            </footer>
                        </article>
                    </c:forEach>
                </div>
            </div>
        </div>
    </main>
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2>Pagamentos e Isenções</h2>
            </header>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table">
                <thead>
                <tr>
                    <th>Data de processamento</th>
                    <th>Data de pagamento</th>
                    <th>Tipo</th>
                    <th>Pago</th>
                    <th>Divida</th>
                    <th>Juros/Multas</th>
                    <th><span class="sr-only">Acções</span></th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty creditEntries}">
                    <tr>
                        <td colspan="6">Não existem pagamentos ou isenções</td>
                    </tr>
                </c:if>
                <c:if test="${not empty creditEntries}">
                <c:forEach var="payment" items="${creditEntries}" varStatus="loop">
                    <spring:url value="../{event}/creditEntry/{payment}/details" var="paymentUrl">
                        <spring:param name="event" value="${eventId}"/>
                        <spring:param name="payment" value="${payment.id}"/>
                    </spring:url>
                    <spring:url value="../{event}/delete/{entry}" var="deleteEntryUrl">
                        <spring:param name="event" value="${eventId}"/>
                        <spring:param name="entry" value="${payment.id}"/>
                    </spring:url>
                    <c:set var="usedAmountInInterestsOrFines" value="#{payment.usedAmountInInterests + payment.usedAmountInFines}"/>
                    <tr>
                        <td><time datetime="${payment.created.toString("yyyy-MM-dd HH:mm:ss")}">${payment.created.toString("dd/MM/yyyy HH:mm:ss")}</time></td>
                        <td><time datetime="${payment.date.toString("yyyy-MM-dd")}">${payment.date.toString("dd/MM/yyyy")}</time></td>
                        <td><c:out value="${payment.typeDescription.content}"/></td>
                        <td><c:out value="${payment.amount}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></td>
                        <td><c:out value="${payment.usedAmountInDebts}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></td>
                        <td><c:out value="${usedAmountInInterestsOrFines}"/><span><spring:message code="label.currency.euro" text="Eur"/></span></td>
                        <td style="text-align: right;">
                            <a href="${paymentUrl}"><spring:message code="label.details" text="Details"/></a>
                            <c:if test="${isAdvancedPaymentManager && loop.first}">
                                <span>, </span><a href="${deleteEntryUrl}" onclick="return confirm('Are you sure?')"><spring:message code="label.cancel" text="Cancel"/></a>
                            </c:if>
                        </td>
                    </tr>
                </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
