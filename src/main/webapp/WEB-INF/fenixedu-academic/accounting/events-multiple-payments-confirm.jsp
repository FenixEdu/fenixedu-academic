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

    window.onbeforeunload = function() {
        // this message is not shown anymore when using the most recent browsers.
        return "Are you sure you want to leave ?";
    };

    function reset() {
        window.onbeforeunload = undefined;
    }
    
    function setTotalAmount(amount) {
        $('#totalAmount').text(Math.round(amount*100)/100);
        $('#totalAmount').val(Math.round(amount*100)/100);
    }

    function recalculateAmount() {
        setTotalAmount(getAmount('input.penalty') + getAmount("input.debt:checked"));
        $("#submitForm").prop('disabled', ($('#totalAmount').val() <= 0));
    }

    function getAmount(clazz) {
        var amounts = $(clazz).map(function() {
            return parseFloat($(this).data('amount'));
        }).toArray();

        return amounts.reduce(function(a, b) {
            return a + b;
        }, 0);
    }

    $(document).ready(function() {
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

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1><spring:message code="label.payments.confirm" text="Payment confirmation"/></h1>
                </div>
            </div>
        </header>
        <div class="row">
            <div class="col-md-12">
                <jsp:include page="heading-person.jsp"/>
            </div>
        </div>
        <div class="row">
            <div class="col-md-12">
                <c:if test="${success}">
                    <br/>
                    <p class="bg-success"><spring:message code="label.payments.registered.with.success"/></p>
                </c:if>
            </div>
        </div>
        <div class="row">
            <div class="col-md-8">
                <div class="overall-description">
                    <dl>
                        <dt>Data de pagamento</dt>
                        <dd><c:out value="${paymentsManagementDTO.paymentDate.toString('dd/MM/yyyy HH:mm:ss')}"/></dd>
                    </dl>
                    <dl>
                        <dt>Método de Pagamento</dt>
                        <dd><c:out value="${paymentsManagementDTO.paymentMethod.localizedName}"/></dd>
                    </dl>
                    <dl>
                        <dt>Referência de Pagamento</dt>
                        <dd><c:out value="${paymentsManagementDTO.paymentReference}"/></dd>
                    </dl>
                    <dl style="font-size: 24px;">
                        <dt>Valor Total</dt>
                        <dd><strong><c:out value="${paymentsManagementDTO.selectedTotalAmountToPay} €"/></strong></dd>
                    </dl>
                </div>
            </div>
            <div class="col-md-4"></div>
        </div>
        <c:if test="${not empty multiplePayments}">
        <form role="form" class="form-horizontal" action="register" method="post">
            ${csrf.field()}
            <input type="hidden" name="identifier" value="${multiplePayments}"/>
        </c:if>
            <div class="row">
                <div class="col-md-12">
                    <section class="list-debts">
                        <table class="table">
                            <thead>
                            <tr>
                                <th>Descrição</th>
                                <th>Valor a pagar</th>
                            </tr>
                            </thead>
                            <c:forEach items="${eventEntryDTOMap}" var="eventKey">
                                <tbody>
                                <tr>
                                    <th colspan="3">
                                        <spring:url var="eventUrl" value="/accounting-management/{event}/details">
                                            <spring:param name="event" value="${eventKey.key.externalId}"/>
                                        </spring:url>
                                        <a href="${eventUrl}"><c:out value="${eventKey.key.description}"/></a>
                                    </th>
                                </tr>
                                <c:forEach items="${eventKey.value}" var="entryDTO" varStatus="status">
                                    <tr>
                                        <td><c:out value="${entryDTO.description}"/></td>
                                        <td><c:out value="${entryDTO.amountToPay}"/><span>€</span></td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </c:forEach>
                        </table>
                    </section>
                </div>
            </div>
            <spring:url var="backUrl" value="../../{person}">
                <spring:param name="person" value="${person.externalId}"/>
            </spring:url>

            <c:if test="${not empty multiplePayments}">
                <button class="btn btn-primary" type="submit">Confirmar pagamento</button>
                <a href="${backUrl}" onclick="reset()" class="btn btn-default">Cancelar</a>
            </c:if>

            <c:if test="${empty multiplePayments}">
                <a href="${backUrl}" class="btn btn-default"><spring:message code="label.ok"/></a>
            </c:if>
        <c:if test="${not empty multiplePayments}">
        </form>
        </c:if>
    </main>
</div>