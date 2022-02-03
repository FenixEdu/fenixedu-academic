<%@ page import="org.fenixedu.academic.domain.accounting.Event" %>
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

<script type="text/javascript">

    var amount = 0;

    function setTotalAmount(amount) {
        $('#totalAmount').text(Math.round(amount*100)/100);
        $('#totalAmountInputDPG').val(Math.round(amount*100)/100);
    }

    function recalculateAmount() {
        setTotalAmount(amount + getAmount("input.debt:checked"));
        $("#paySubmitDPG").prop('disabled', ($('#totalAmountInputDPG').val() <= 0));
    }

    function getAmount(clazz) {
        var amounts = $(clazz).map(function() {
            return parseFloat($(this).data('amount'));
        }).toArray();

        return amounts.reduce(function(a, b) {
            return a + b;
        }, 0);
    }

    function selectAllUntilToday() {
        var today = new Date();
        var elementDateMap = $("time").map(function (i,e) {
            return {
                element : e,
                when : new Date($(e).attr('datetime'))
            }
        }).filter(function(i, e) {
            return e.when.getTime() < today.getTime();
        }).each(function(i, e) {
            $(e.element).closest('tr').find('input.debt').prop('checked', 'checked');
        });

        $('input.debt').first().prop('checked', 'checked');
    }

    $(document).ready(function() {
        selectAllUntilToday();
        var interestAmount = getAmount('input.interest');
        var fineAmount = getAmount('input.fine');
        amount = interestAmount + fineAmount;

        setTotalAmount(amount);

        $('input.debt').click(function(e) {
            recalculateAmount();
        });

        $('#selectAllDebts').click(function () {
            $('input.debt').prop('checked', this.checked);
            recalculateAmount();
        });

        $('#paySubmitDPG').click(function () {
            $('#paySubmitDPG').attr('disabled', true);
            $("#generatePaymentEntryForm").submit();
        });

        recalculateAmount();
    });

</script>

<spring:url var="backUrl" value="../{event}/details">
    <spring:param name="event" value="${eventId}"/>
</spring:url>

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1>
                        <jsp:include page="heading-event.jsp"/>
                    </h1>
                </div>
            </div>
        </header>
        <c:set var="person" scope="request" value="${event.person}"/>
        <jsp:include page="heading-person.jsp"/>
        <c:if test="${not empty error}">
            <div class="row">
                <br/>
                <div class="col-md-8 col-sm-12">
                    <div class="alert alert-danger">
                        <span><c:out value="${error}"/></span>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="row">
            <h2>
                <spring:message code="label.payment.methods" text="Payment Methods"/>
            </h2>
        </div>

        <c:if test="${not empty availableAdvancements}">
            <div class="row">
                <div class="col-md-12">
                    <div class="overall-description">
                        <spring:url value="owner-accounting-events" var="eventContextPrefix" scope="request"/>
                        <jsp:include page="event-depositAdvancment.jsp"/>
                    </div>
                </div>
            </div>
        </c:if>

        <div class="row">
            <div class="col-md-12">
                <h2><spring:message code="accounting.event.payment.options.debts.and.interests" text="Debts and Interests"/></h2>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8 col-sm-12">
                <section class="list-debts">
                    <table class="table">
                        <thead>
                        <tr>
                            <th>
                                <label for="selectAllDebts" class="sr-only"><spring:message code="accounting.event.payment.selectAllDebts" text="Select all debts"/></label>
                                <input type="checkbox" id="selectAllDebts">
                            </th>
                            <th><spring:message code="accounting.event.details.due.date"/></th>
                            <th><spring:message code="accounting.event.details.description" text="Description"/></th>
                            <th><spring:message code="accounting.event.details.value" text="Value"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach var="interest" items="${interests}" varStatus="interestLoop" >
                            <c:if test="${interest.isOpen()}">
                                <c:set var="interestIndex" value="#{interestLoop.index + 1}"/>
                                <c:set var="amount" value="${interest.openAmount}"/>
                                <tr>
                                    <td><input class="interest" data-amount="${amount}" checked disabled type="checkbox"></td>
                                    <td><time datetime="${interest.date.toString('yyyy-MM-dd')}">${interest.date.toString('dd/MM/yyyy')}</time></td>
                                    <td><spring:message code="accounting.event.pay.interest.description" arguments="${interestIndex}"/></td>
                                    <td><c:out value="${amount}"/><span>€</span></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="fine" items="${fines}" varStatus="fineLoop">
                            <c:if test="${fine.isOpen()}">
                                <c:set var="fineIndex" value="#{fineLoop.index + 1}"/>
                                <c:set var="amount" value="${fine.openAmount}"/>
                                <tr>
                                    <td><input class="fine" data-amount="${amount}" checked disabled type="checkbox"></td>
                                    <td><time datetime="${fine.date.toString('yyyy-MM-dd')}">${fine.date.toString('dd/MM/yyyy')}</time></td>
                                    <td><spring:message code="accounting.event.pay.fine.description" arguments="${fineIndex}"/></td>
                                    <td><c:out value="${amount}"/><span>€</span></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        <c:forEach var="debt" items="${debts}">
                            <c:if test="${debt.isOpen()}">
                                <c:set var="debtAmount" value="${debt.openAmount.toPlainString()}"/>
                                <tr>
                                    <td><input class="debt" data-amount="${debtAmount}" type="checkbox"></td>
                                    <td><time datetime="${debt.dueDate.toString('yyyy-MM-dd')}">${debt.dueDate.toString('dd/MM/yyyy')}</time></td>
                                    <td><c:out value="${debt.description}"/></td>
                                    <td><c:out value="${debtAmount}"/><span>€</span></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    <p><small><spring:message code="accounting.event.debts.payment.explanation" text="The payment of installments is sequential."/></small></p>
                </section>
            </div>
            <% final Event event = (Event) request.getAttribute("event"); %>
            <div class="col-md-4">
                <section class="resume">
                    <spring:url var="generatePaymentEntry" value="../{event}/payDPG">
                        <spring:param name="event" value="${eventId}"/>
                    </spring:url>
                    <spring:url var="allocateIBAN" value="../{event}/allocateIBAN">
                        <spring:param name="event" value="${eventId}"/>
                    </spring:url>
                    <spring:url var="bankTransfer" value="../{event}/bankTransfer">
                        <spring:param name="event" value="${eventId}"/>
                    </spring:url>
                    <dl class="sum">
                        <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                        <dd><span id="totalAmount"></span><span>€</span></dd>
                    </dl>
                    <% if (event.allowSEPATransfer()) { %>
                    <p>
                        <form:form id="allocateIBANForm" class="form-horizontal" method="POST" action="${allocateIBAN}">
                            ${csrf.field()}
                            <div class="actions">
                                <button id="allocateIBANSubmit" class="btn btn-block btn-primary" type="submit"><spring:message code="accounting.event.allocate.iban" text="Pay Via SEPA Transfer"/></button>
                            </div>
                        </form:form>
                    </p>
                    <% } %>
                    <% if (event.allowBankTransfer()) { %>
                    <p>
                        <form:form id="bankTransferForm" class="form-horizontal" method="POST" action="${bankTransfer}">
                            ${csrf.field()}
                            <div class="actions">
                                <button id="bankTransferSubmit" class="btn btn-block btn-primary" type="submit"><spring:message code="label.payment.method.bankTransfer" text="Pay Via Non SEPA Transfer"/></button>
                            </div>
                        </form:form>
                    </p>
                    <% } %>
                    <p>
                        <form:form id="generatePaymentEntryForm" class="form-horizontal" method="POST" action="${generatePaymentEntry}">
                            ${csrf.field()}
                            <input id="totalAmountInputDPG" name="totalAmount" hidden/>
                            <div class="actions">
                                <button id="paySubmitDPG" class="btn btn-block btn-primary" type="submit"><spring:message code="accounting.event.get.payment.data" text="Proceed with payment"/></button>
                            </div>
                        </form:form>
                    </p>
                </section>
            </div>
        </div>
    </main>
</div>