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
        $('#totalAmountInput').val(Math.round(amount*100)/100);
    }

    function recalculateAmount() {
        setTotalAmount(amount + getAmount("input.debt:checked"));
        $("#submitForm").prop('disabled', ($('#totalAmountInput').val() <= 0));
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
                    <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
                    <h1>${description}</h1>
                </div>
            </div>
            <c:if test="${not empty error}">
            <div class="row">
                <div class="col-md-8 col-sm-12">
                    <div class="alert alert-danger">
                        <span><c:out value="${error}"/></span>
                    </div>
                </div>
            </div>
            </c:if>
            <c:if test="${not empty paymentCodeEntries}">
            <div class="row">
                <div class="col-xs-12 col-md-4">
                    <section class="reference-card">
                        <c:set var="mostRecentEntry" value="${paymentCodeEntries[0]}"/>
                        <h2>Última referência gerada</h2>
                        <dl>
                            <dt>Entidade</dt>
                            <dd><c:out value="${mostRecentEntry.paymentCode.entityCode}"/></dd>
                        </dl>
                        <dl>
                            <dt>Referencia</dt>
                            <dd><c:out value="${mostRecentEntry.paymentCode.formattedCode}"/></dd>
                        </dl>
                        <dl>
                            <dt>Valor</dt>
                            <dd><c:out value="${mostRecentEntry.amount}"/><span>€</span></dd>
                        </dl>
                        <dl>
                            <dt>Estado</dt>
                            <dd>${fr:message('resources.EnumerationResources',mostRecentEntry.paymentCode.state.qualifiedName)}</dd>
                        </dl>
                        <dl>
                            <dt>Data de Criação</dt>
                            <dd><time datetime="${mostRecentEntry.created.toString("yyyy-MM-dd HH:mm:ss")}">${mostRecentEntry.created.toString("dd/MM/yyyy HH:mm:ss")}</time></dd>
                        </dl>
                    </section>
                </div>
            </div>
            </c:if>
        </header>

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
                                <label for="selectAllDebts" class="sr-only">Seleccionar todas as dividas</label>
                                <input type="checkbox" id="selectAllDebts">
                            </th>
                            <th><spring:message code="accounting.event.details.due.date"/></th>
                            <th>Título</th>
                            <th>Valor</th>
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
                        <c:forEach var="debt" items="${debts}" varStatus="debtsLoop">
                            <c:if test="${debt.isOpen()}">
                                <c:set var="debtIndex" value="#{debtsLoop.index + 1}"/>
                                <c:set var="debtAmount" value="${debt.openAmount.toPlainString()}"/>
                                <tr>
                                    <td><input class="debt" data-amount="${debtAmount}" type="checkbox"></td>
                                    <td><time datetime="${debt.dueDate.toString('yyyy-MM-dd')}">${debt.dueDate.toString('dd/MM/yyyy')}</time></td>
                                    <td><spring:message code="accounting.event.details.debt.name" arguments="${debtIndex}"/></td>
                                    <td><c:out value="${debtAmount}"/><span>€</span></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                        </tbody>
                    </table>
                    <p><small>O pagamento das prestações é sequencial.</small></p>
                </section>
            </div>
            <div class="col-md-4">
                <section class="resume">
                    <spring:url var="generatePaymentEntry" value="../{event}/pay">
                        <spring:param name="event" value="${eventId}"/>
                    </spring:url>
                    <form:form class="form-horizontal" method="POST" action="${generatePaymentEntry}">
                        ${csrf.field()}
                        <dl class="sum">
                            <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                            <dd><span id="totalAmount"></span><span>€</span></dd>
                            <input id="totalAmountInput" name="totalAmount" hidden/>
                        </dl>
                        <div class="actions">
                            <button id="submitForm" class="btn btn-block btn-primary" type="submit">Obter dados de pagamento</button>
                        </div>
                    </form:form>
                </section>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <header>
                    <h2>Dados de Pagamento Gerados</h2>
                </header>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <table class="table">
                    <thead>
                    <tr>
                        <th>Data de Criação</th>
                        <th>Entidade</th>
                        <th>Referência</th>
                        <th>Valor</th>
                        <th>Estado</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${empty paymentCodeEntries}">
                        <tr>
                            <td colspan="6">Não existem dados de pagamento gerados</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty paymentCodeEntries}">
                        <c:forEach var="entry" items="${paymentCodeEntries}">
                            <tr data-id="${entry.externalId}">
                                <td><time datetime="${entry.created.toString("yyyy-MM-dd HH:mm:ss")}">${entry.created.toString("dd/MM/yyyy HH:mm:ss")}</time></td>
                                <td><c:out value="${entry.paymentCode.entityCode}"/></td>
                                <td><c:out value="${entry.paymentCode.formattedCode}"/></td>
                                <td><c:out value="${entry.amount}"/><span>€</span></td>
                                <td>${fr:message('resources.EnumerationResources',entry.paymentCode.state.qualifiedName)}</td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </main>
</div>