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

    function disableAllPenaltyInputs() {
        $('input.penalty').prop('checked', true);
    }

    $(document).ready(function() {
        disableAllPenaltyInputs();

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

<spring:url var="backUrl" value="../../{person}">
    <spring:param name="person" value="${person.externalId}"/>
</spring:url>

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <p><a href="${backUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
                    <h1>Pagamento de Dívidas</h1>
                </div>
            </div>
            <div class="row">
                <div class="col-md-5 col-sm-12">
                    <div class="overall-description">
                        <dl>
                            <dt><spring:message code="label.name" text="Name"/></dt>
                            <dd><c:out value="${person.presentationName}"/></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="label.document.id.type" text="ID Document Type"/></dt>
                            <dd><c:out value="${person.idDocumentType.localizedName}"/></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="label.document.id" text="ID Document"/></dt>
                            <dd><c:out value="${person.documentIdNumber}"/></dd>
                        </dl>
                    </div>
                </div>
            </div>
        </header>

        <div class="row">
            <div class="col-md-12">
                <h2><spring:message code="accounting.event.payment.options.debts.and.interests" text="Debts and Interests"/></h2>
            </div>
        </div>

        <div class="row">
            <form role="form" class="form-horizontal" action="confirm" method="post">
                ${csrf.field()}
                <input type="hidden" name="identifier" value="${multiplePayments}"/>
                <div class="form-group">
                        <label for="paymentMethod" class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentMethod" text="Payment Method"/></label>
                    <div class="col-sm-4">
                        <select name="paymentMethod" id="paymentMethod" required>
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option value="${paymentMethod.externalId}"><c:out value="${paymentMethod.localizedName}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentReference" text="Payment Reference"/></label>
                    <div class="col-sm-4">
                        <input name="paymentReference" type="text" required/>
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
                                <th>Descrição</th>
                                <th>Valor a pagar</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:forEach items="${paymentsManagementDTO.entryDTOs}" var="entryDTO" varStatus="status">
                                <tr>
                                    <c:set var="amount" value="${entryDTO.amountToPay}"/>
                                    <td>
                                        <c:choose>
                                            <c:when test="${entryDTO.isForPenalty()}">
                                                <input type="checkbox" class="penalty" value="${entryDTO.toString()}" name="entries" data-amount="${amount}" disabled>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="checkbox" class="debt" value="${entryDTO.toString()}" name="entries" data-amount="${amount}">
                                            </c:otherwise>
                                    </c:choose>
                                    <td><c:out value="${entryDTO.description}"/></td>
                                    <td><c:out value="${entryDTO.amountToPay}"/><span>€</span></td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <p><small>O pagamento das prestações é sequencial.</small></p>
                    </section>
                </div>
                <div class="col-md-3">
                    <section class="resume">
                        <dl class="sum">
                            <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                            <dd><span id="totalAmount"></span><span>€</span></dd>
                        </dl>
                        <div class="actions">
                            <button id="submitForm" class="btn btn-block btn-primary" type="submit">Realizar Pagamento</button>
                        </div>
                    </section>
                </div>
        </div>
        </form>
    </div>
    </main>
</div>