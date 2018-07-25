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

<spring:url var="payUrl" value="../{event}/pay">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<spring:url var="backUrl" value="${entrypointUrl}">
    <spring:param name="user" value="${eventUsername}"/>
</spring:url>

<div class="container-fluid">
    <header>
        <div class="row">
            <div class="col-md-10">
                <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
                <h1><c:out value="${eventDescription}"/></h1>
            </div>
            <div class="col-md-2">
                <a class="btn btn-primary btn-block" href="${payUrl}"><spring:message code="accounting.event.action.pay" text="Pay"/></a>
            </div>
        </div>
        <div class="row">
            <div class="col-md-4 col-sm-12">
                <div class="overall-description">
                    <dl>
                        <dt><spring:message code="accounting.event.details.creation.date" text="Creation Date"/></dt>
                        <dd><time datetime="${eventCreationDate.toString("yyyy-MM-dd")}">${eventCreationDate.toString("dd/MM/yyyy")}</time></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.amount.pay" text="To pay"/></dt>
                        <dd><c:out value="${eventTotalAmountToPay}"/><span>€</span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.debt.amount.pay" text="Debt"/></dt>
                        <dd><c:out value="${eventDebtAmountToPay}"/><span>€</span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.interestOrFines.amount.pay" text="Interest"/></dt>
                        <dd><c:out value="${eventInterestAmountToPay + eventFineAmountToPay}"/><span>€</span></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="accounting.event.details.original.debt.amount" text="Original amount"/></dt>
                        <dd><c:out value="${eventOriginalAmountToPay}"/><span>€</span></dd>
                    </dl>
                </div>
            </div>
        </div>
    </header>
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2>Prestações</h2>
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
                            <c:when test="${debt.isAfterDueDate(currentDate)}">
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
                                    <c:when test="${debt.isAfterDueDate(currentDate)}">
                                        <spring:message code="accounting.event.details.debt.due.with.interest" text="Due with interest"/>
                                    </c:when>
                                    <c:otherwise>
                                        <spring:message code="accounting.event.details.debt.due" text="Due"/>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                            <dl class="total">
                                <dt><spring:message code="accounting.event.details.debt.to.pay" text="To Pay"/></dt>
                                <dd><c:out value="${debt.totalOpenAmount.toPlainString()}"/><span>€</span></dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt.fine" text="Debt"/>:</dt>
                                <dd><c:out value="${debt.openAmount.toPlainString()}"/><span>€</span></dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt.interestOrFines" text="Interest"/>:</dt>
                                <dd><c:out value="${debt.openInterestAmount.toPlainString() + debt.openFineAmount}"/><span>€</span></dd>
                            </dl>
                            <dl class="details">
                                <dt><spring:message code="accounting.event.details.debt" text="Installment"/>:</dt>
                                <dd><c:out value="${debt.originalAmount.toPlainString()}"/><span>€</span></dd>
                            </dl>
                            <dl class="details limit-date">
                                <dt><spring:message code="accounting.event.details.debt.due.date" text="Due Date"/>:</dt>
                                <dd><c:out value="${debt.date.toString('dd/MM/yyyy')}"/></dd>
                            </dl>
                            <footer>
                                <a class="btn btn-default btn-block" href="debt/${debt.date.toString('dd-MM-yyyy')}/details">Ver Detalhes</a>
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
                <h2>Pagamentos</h2>
            </header>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <table class="table">
                <thead>
                <tr>
                    <th>Data de processamento</th>
                    <th>Data de criação</th>
                    <th>Tipo</th>
                    <th>Pago</th>
                    <th>Divida</th>
                    <th>Juros/Multas</th>
                    <th><span class="sr-only">Acções</span></th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${empty payments}">
                    <tr>
                        <td colspan="6">Não existem pagamentos</td>
                    </tr>
                </c:if>
                <c:if test="${not empty payments}">
                <c:forEach var="payment" items="${payments}">
                    <spring:url value="../{event}/{payment}/details" var="paymentUrl">
                        <spring:param name="event" value="${eventId}"/>
                        <spring:param name="payment" value="${payment.id}"/>
                    </spring:url>
                    <tr>
                        <td><time datetime="${payment.created.toString("yyyy-MM-dd")}">${payment.created.toString("dd/MM/yyyy")}</time></td>
                        <td><time datetime="${payment.date.toString("yyyy-MM-dd")}">${payment.date.toString("dd/MM/yyyy")}</time></td>
                        <td><c:out value="${payment.typeDescription.content}"/></td>
                        <td><c:out value="${payment.amount}"/><span>€</span></td>
                        <td><c:out value="${payment.usedAmountInDebts}"/><span>€</span></td>
                        <td><c:out value="${payment.usedAmountInInterests}"/><span>€</span></td>
                        <td style="text-align: right;"><a href="${paymentUrl}">Ver Pagamento</a></td>
                    </tr>
                </c:forEach>
                </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>
