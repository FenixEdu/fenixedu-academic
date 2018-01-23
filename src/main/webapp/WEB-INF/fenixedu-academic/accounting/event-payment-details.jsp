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


<spring:url var="backUrl" value="../../{event}/details">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<div class="container-fluid">
    <header>
        <div class="row">
            <div class="col-md-10">
                <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <h1><spring:message code="accounting.payment.details.title" text="Payment report"/></h1>
            </div>
        </div>
    </header>
    <div class="row">
        <div class="col-md-5">
            <section class="payment-metadata">
                <dl>
                    <dt>Pago por:</dt>
                    <dd><c:out value="${transactionDetail.event.person.presentationName}"></c:out></dd>
                </dl>
                <dl>
                    <dt>Método Pagamento:</dt>
                    <dd><c:out value="${transactionDetail.transactionDetail.paymentMode}"/></dd>
                </dl>
                <c:if test="${transactionDetail.transactionDetail.paymentMode == 'ATM'}">
                    <dl>
                        <dt>Transacção SIBS:</dt>
                        <dd><c:out value="${transactionDetail.transactionDetail.sibsTransactionId}"/></dd>
                    </dl>
                    <dl>
                        <dt>Referência:</dt>
                        <dd><c:out value="${transactionDetail.transactionDetail.sibsCode}"/></dd>
                    </dl>
                </c:if>
                <c:if test="${transactionDetail.transactionDetail.paymentMode == 'CASH'}">
                    <dl>
                        <dt>Responsável:</dt>
                        <dd><c:out value="${transactionDetail.responsibleUser.person.presentationName}"/></dd>
                    </dl>
                    <dl>
                        <dt>Motivo:</dt>
                        <dd><c:out value="${transactionDetail.transactionDetail.comments}"/></dd>
                    </dl>
                </c:if>
                <dl>
                    <dt>Data de pagamento:</dt>
                    <dd>
                        <time datetime="${registeredDate.toString('yyyy-MM-dd')}">${registeredDate.toString('dd/MM/yyyy')}</time>
                    </dd>
                </dl>
                <dl>
                    <dt>Data de processamento:</dt>
                    <dd>
                        <time datetime="${processedDate.toString('yyyy-MM-dd')}">${processedDate.toString('dd/MM/yyyy')}</time>
                    </dd>
                </dl>
            </section>
        </div>
        <div class="col-md-5 col-md-offset-2">
            <section class="payment-totals">
                <dl class="total">
                    <dt><spring:message code="accounting.payment.details.paid.value" text="Paid Amount"/></dt>
                    <dd><c:out value="${amount}"/><span>€</span></dd>
                </dl>
                <c:forEach var="payment" items="${payments}" varStatus="paymentLoop">
                    <c:set var="debtEntryIndex" value="#{paymentLoop.index + 1}"/>
                    <c:set var="totalPaidAmount" value="#{payment.amountUsedInDebt + payment.amountUsedInInterest}"/>
                    <dl>
                        <dt><spring:message code="accounting.event.details.debt.name" arguments="${debtEntryIndex}"/></dt>
                        <dd><c:out value="${totalPaidAmount.toPlainString()}"/><span>€</span></dd>
                    </dl>
                </c:forEach>
            </section>
        </div>
    </div>
    <div class="row">
        <div class="col-md-12">
            <header>
                <h2>Prestações</h2>
            </header>
        </div>
    </div>
    <section class="payment-values">
        <table class="table">
            <thead>
            <tr>
                <th>Prestação</th>
                <th>Data limite</th>
                <th>Total Pago</th>
                <th>Dívida</th>
                <th>Juros</th>
                <th><span class="sr-only">Acções</span></th>
            </tr>
            </thead>
            <tbody>
            <c:if test="${empty payments}">
                <tr>
                    <td colspan="6">Não existem prestações associadas.</td>
                </tr>
                </c:if>
            <c:if test="${not empty payments}">
                <c:forEach var="payment" items="${payments}" varStatus="paymentLoop">
                    <c:set var="debtEntryIndex" value="#{paymentLoop.index + 1}"/>
                    <c:set var="totalPaidAmount" value="#{payment.amountUsedInDebt + payment.amountUsedInInterest}"/>
                    <tr>
                        <td><spring:message code="accounting.event.details.debt.name" arguments="${debtEntryIndex}"/></td>
                        <td>
                            <time datetime="${payment.debtEntry.date.toString('yyyy-MM-dd')}">${payment.debtEntry.date.toString('dd/MM/yyyy')}</time>
                        </td>
                        <td><c:out value="${totalPaidAmount.toPlainString()}"/><span>€</span></td>
                        <td><c:out value="${payment.amountUsedInDebt.toPlainString()}"/><span>€</span></td>
                        <td><c:out value="${payment.amountUsedInInterest.toPlainString()}"/><span>€</span></td>

                        <spring:url value="../../{event}/debt/{debtDueDate}/details" var="debtUrl">
                            <spring:param name="event" value="${event.externalId}"/>
                            <spring:param name="debtDueDate" value="${payment.debtEntry.date.toString('dd-MM-yyyy')}"/>
                        </spring:url>

                        <td style="text-align: right;"><a href="${debtUrl}"><spring:message code="accounting.event.details.link"/></a></td>
                    </tr>
                </c:forEach>
            </c:if>
            </tbody>
        </table>
    </section>
    </main>
</div>