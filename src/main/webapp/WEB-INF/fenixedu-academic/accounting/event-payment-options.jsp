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
    }

    function recalculateAmount() {
        setTotalAmount(amount + getAmount("input.debt:checked"));
    }

    function getAmount(clazz) {
        var amounts = $(clazz).map(function() {
            return parseFloat($(this).data('amount'));
        }).toArray();

        console.log(JSON.stringify(amounts));

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
        amount = interestAmount;
        setTotalAmount(interestAmount);

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
            <div class="row">
                <div class="col-md-4 col-sm-12">
                    <div class="overall-description">
                        <dl>
                            <dt><spring:message code="accounting.event.details.amount" text="Amount"/></dt>
                            <dd><c:out value="${amount}"/><span>€</span></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="accounting.event.details.creation.date" text="Creation Date"/></dt>
                            <dd><time datetime="${creationDate.toString('yyyy-MM-dd')}">${creationDate.toString('dd/MM/yyyy')}</time></dd>
                        </dl>
                    </div>
                </div>
            </div>
        </header>
        <div class="row">
            <div class="col-xs-12 col-md-4">
                <section class="reference-card">
                    <dl>
                        <dt>Referencia:</dt>
                        <dd>0000 00032 0048 3820 0038</dd>
                    </dl>
                    <dl>
                        <dt>Entidade:</dt>
                        <dd>13041</dd>
                    </dl>
                </section>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <h1><spring:message code="accounting.event.payment.options.debts.and.interests" text="Debts and Interests"/></h1>
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
                        <c:forEach var="interest" items="${interests}" varStatus="interestLoop">
                            <c:set var="interestIndex" value="#{interestLoop.index + 1}"/>
                            <c:set var="amount" value="${interest.openAmount}"/>
                            <tr>
                                <td><input class="interest" data-amount="${amount}" checked disabled type="checkbox"></td>
                                <td><time datetime="${interest.date.toString('yyyy-MM-dd')}">${interest.date.toString('dd/MM/yyyy')}</time></td>
                                <td><spring:message code="accounting.event.pay.interest.description" arguments="${interestIndex}"/></td>
                                <td><c:out value="${amount}"/><span>€</span></td>
                            </tr>
                        </c:forEach>
                        <c:forEach var="debt" items="${debts}" varStatus="debtsLoop">
                            <c:set var="debtIndex" value="#{debtsLoop.index + 1}"/>
                            <c:set var="debtAmount" value="${debt.openAmount.toPlainString()}"/>
                            <tr>
                                <td><input class="debt" data-amount="${debtAmount}" type="checkbox"></td>
                                <td><time datetime="${debt.dueDate.toString('yyyy-MM-dd')}">${debt.dueDate.toString('dd/MM/yyyy')}</time></td>
                                <td><spring:message code="accounting.event.details.debt.name" arguments="${debtIndex}"/></td>
                                <td><c:out value="${debtAmount}"/><span>€</span></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <p><small>O pagamento das prestações é sequencial.</small></p>
                </section>
            </div>
            <div class="col-md-4">
                <section class="resume">
                    <dl class="total">
                        <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                        <dd><span id="totalAmount"></span><span>€</span></dd>
                    </dl>
                    <div class="actions">
                        <a class="btn" href="#">Obter dados de pagamento</a>
                    </div>
                </section>
            </div>
        </div>
    </main>
</div>