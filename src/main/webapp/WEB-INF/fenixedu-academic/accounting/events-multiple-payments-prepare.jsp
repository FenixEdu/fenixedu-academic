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
<%@ include file="update-payment-reference.jsp" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<script type="text/javascript" src="${pageContext.request.contextPath}/bennu-toolkit/js/libs/moment.js"></script>

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

        return amounts.reduce((a, b) => a + b, 0);
    }

    function disableAllPenaltyInputs(penaltySelector) {
        $(penaltySelector).prop('checked', true);
        $(penaltySelector).prop('readonly', true);
    }

    function enableOnlyFirstDebtInputs(debtSelector) {
        // Reset debt entries selections
        $(debtSelector).prop('checked', false);
        $(debtSelector).prop('disabled', true);

        document.querySelectorAll("tbody").forEach(function(tbody) {
            const firstDebtInput = $(tbody).find(debtSelector).toArray()[0];
            $(firstDebtInput).prop("disabled", false);
        });
    }

    function unckeckDebtInput(parentRow, selector) {
        $(parentRow).find(selector).prop('checked', false);
        $(parentRow).find(selector).prop('disabled', true);
    }

    function refreshSelectAllState(parentBody, debtSelector) {
        let numEntriesSelected = parentBody.find(debtSelector + ":checked").length;
        parentBody.find("input.selectAllEntries").prop("checked", numEntriesSelected > 0);
    }

    $(document).ready(function() {
        const debtSelector = "input.debt";
        const penaltySelector = "input.penalty";

        disableAllPenaltyInputs(penaltySelector);
        enableOnlyFirstDebtInputs(debtSelector);
        recalculateAmount();
        !$("#paymentMethod").val() ? $("#paymentReference").val("") : updatePaymentReference(moment().format("YYYY-MM-DDTHH:mm:ss.SSSZ"));

        // Prevent clicks on selected penalty entries
        $(penaltySelector).click(event => event.preventDefault());

        // Recalculate total amount and refresh available entries for selection
        $(debtSelector).bind("change", function(){
            if($(this).is(":checked")) {
                $(this).parents("tr").next().find(debtSelector).prop("disabled", false);
            }
            else {
                $(this).parents("tr").nextAll().toArray().forEach(row => unckeckDebtInput(row, debtSelector))
            }

            refreshSelectAllState($(this).parents("tbody"), debtSelector);
            recalculateAmount();
        });

        $("input.selectAllEntries").bind("change", function(){
            $(this).parents("tbody").find(debtSelector).prop("checked", $(this).is(":checked")).change();
        });

        $("#paymentMethod").change(function () {
            !$("#paymentMethod").val() ? $("#paymentReference").val("") : updatePaymentReference(moment().format("YYYY-MM-DDTHH:mm:ss.SSSZ"));
        });

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
                    <h1><spring:message code="accounting.event.debts.payment.title" text="Debts Payment"/></h1>
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
                                <option value="" selected="selected" disabled="disabled" hidden="hidden"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentMethod.placeholder" /></option>
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option value="${paymentMethod.externalId}"><c:out value="${paymentMethod.localizedName}"/></option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentReference" text="Payment Reference"/></label>
                    <div class="col-sm-4">
                        <input id="paymentReference" name="paymentReference" type="text" required/>
                    </div>
                </div>
            <div class="row">
                <div class="col-md-8 col-sm-12">
                    <section class="list-debts">
                        <table class="table">
                            <thead>
                            <tr>
                                <th></th>
                                <th><spring:message code="accounting.event.details.description" text="Description"/></th>
                                <th><spring:message code="accounting.event.details.debt.amount.to.pay" text="Amount to pay"/></th>
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
                                            <input type="checkbox" style="margin-left: 5px" class="selectAllEntries">
                                        </th>
                                    </tr>
                                    <c:forEach items="${eventKey.value}" var="entryDTO" varStatus="status">
                                        <tr>
                                            <c:set var="amount" value="${entryDTO.amountToPay}"/>
                                            <td>
                                                <c:choose>
                                                <c:when test="${entryDTO.isForPenalty()}">
                                                    <input type="checkbox" class="penalty" value="${entryDTO.toString()}" name="entries" data-amount="${amount}">
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
                            </c:forEach>
                        </table>
                        <p><small><spring:message code="accounting.event.debts.payment.explanation" text="The payment of installments is sequential."/></small></p>
                    </section>
                </div>
                <div class="col-md-3">
                    <section class="resume">
                        <dl class="sum">
                            <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                            <dd><span id="totalAmount"></span><span>€</span></dd>
                        </dl>
                        <div class="actions">
                            <button id="submitForm" class="btn btn-block btn-primary" type="submit">
                                <spring:message code="accounting.event.debts.payment.submit" text="Submit Payment"/>
                            </button>
                        </div>
                    </section>
                </div>
        </div>
        </form>
    </div>
    </main>
</div>