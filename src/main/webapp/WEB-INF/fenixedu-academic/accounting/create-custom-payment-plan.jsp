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

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

${portal.toolkit()}

<spring:url var="baseUrl" value="/accounting-management/"/>

<spring:url var="backUrl" value="../../{event}/details">
    <spring:param name="event" value="${event.externalId}"/>
</spring:url>

<script type="text/javascript" src="${pageContext.request.contextPath}/bennu-toolkit/js/libs/moment.js"></script>

<script type="text/javascript">

    $(document).ready(function() {
        let exemptionDateInput = $("#exemptionDate");
        let dueDateInput = $("#dueDate");
        let installmentInput = $("#installment");
        let numberOfInstallmentInput = $("#numberOfInstallment");
        let addInstallmentButton = $("#add");
        let table = $("#table");
        let tbody = $("#table > tbody");
        let submitForm = $("#submitForm");
        let errorDueDate = $("#errorDueDate");
        let errorInstallment = $("#errorInstallment");
        let errorNumberOfInstallment = $("#errorNumberOfInstallment");
        let errorDomain = $("#errorDomain");
        let currentAmountSpan = $("#currentAmount");
        let totalAmountSpan = $("#totalAmount");

        let currentAmount = "0.00";
        let totalAmount = "${totalAmount}";

        let data = {
            "exemptionDate" : "${exemptionDate}",
            "map" : {}
        };

        currentAmountSpan.text(formatNumberToCurrency(currentAmount));
        totalAmountSpan.text(formatNumberToCurrency(totalAmount));
        submitForm.prop("disabled", !areCurrenciesInStringEqual(currentAmount, totalAmount));

        function isDuplicateDate(date) {
            return !!data["map"][date];
        }

        function formatNumberToCommaForm(n) {
            return n.replace(/\D/g, "").replace(/\B(?=(\d{3})+(?!\d))/g, ".");
        }

        function formatNumberToCurrency(n) {
            let decimalPoint = n.indexOf(".");
            return "€" + formatNumberToCommaForm(n.substring(0, decimalPoint)) + "," + n.substring(decimalPoint + 1);
        }

        function formatCurrencyToNumber(n) {
            return n.substring(1).replace(/\./g, "").replace(/,/g, ".");
        }

        function formatCurrency(input, blur) {
            let inputValue = input.val();
            if (inputValue === "") { return; }

            let originalLength = inputValue.length;
            let caretPosition = input.prop("selectionStart");

            if (inputValue.indexOf(",") >= 0) {
                let decimalPosition = inputValue.indexOf(",");

                let leftSide = inputValue.substring(0, decimalPosition).replace(/\D/g, "");
                leftSide = (leftSide === "" ? "0" : leftSide);

                leftSide = parseInt(leftSide).toString();
                let rightSide = inputValue.substring(decimalPosition);

                leftSide = formatNumberToCommaForm(leftSide);
                rightSide = formatNumberToCommaForm(rightSide);

                if (blur === "blur") {
                    rightSide += "00";
                }
                rightSide = rightSide.substring(0, 2);

                inputValue = "€" + leftSide + "," + rightSide;
            }
            else {
                inputValue = formatNumberToCommaForm(parseInt(inputValue.replace(/\D/g, "")).toString());
                inputValue = "€" + inputValue;

                if (blur === "blur") {
                    inputValue += ",00";
                }
            }

            input.val(inputValue);

            let updatedLength = inputValue.length;
            caretPosition = updatedLength - originalLength + caretPosition;
            input[0].setSelectionRange(caretPosition, caretPosition);
        }

        function sumCurrencyAsString(a, b) {
            let decimalPositionA = a.indexOf(".");
            let decimalPositionB = b.indexOf(".");

            let leftSideA = a.substring(0, decimalPositionA);
            let leftSideB = b.substring(0, decimalPositionB);

            let rightSideA = a.substring(decimalPositionA + 1);
            let rightSideB = b.substring(decimalPositionB + 1);

            let leftSideSum = (parseInt(leftSideA) + parseInt(leftSideB)).toString();
            let rightSideSum = (parseInt(rightSideA) + parseInt(rightSideB)).toString();

            if(rightSideSum.length > 2) {
                leftSideSum = (parseInt(leftSideSum) + parseInt(rightSideSum.substring(0, rightSideSum.length - 2))).toString();
                rightSideSum = rightSideSum.substring(rightSideSum.length - 2);
            }

            rightSideSum = (parseInt(rightSideSum) < 10 ? "0" + parseInt(rightSideSum).toString() : parseInt(rightSideSum).toString());

            return leftSideSum + "." + rightSideSum;
        }

        function areCurrenciesInStringEqual(a, b) {
            let decimalPositionA = a.indexOf(".");
            let decimalPositionB = b.indexOf(".");

            let leftSideA = a.substring(0, decimalPositionA);
            let leftSideB = b.substring(0, decimalPositionB);

            let rightSideA = a.substring(decimalPositionA + 1);
            let rightSideB = b.substring(decimalPositionB + 1);

            return parseInt(leftSideA) === parseInt(leftSideB) && parseInt(rightSideA) === parseInt(rightSideB);
        }

        function addToData(dueDate, installment) {
            data["map"][dueDate] = formatCurrencyToNumber(installment);
        }

        function updateCurrentAmount() {
            currentAmount = "0.00";

            Object.keys(data["map"])
                .forEach(function(v) {
                    currentAmount = sumCurrencyAsString(currentAmount, data["map"][v]);
                });

            currentAmountSpan.text(formatNumberToCurrency(currentAmount));
            submitForm.prop("disabled", !areCurrenciesInStringEqual(currentAmount, totalAmount));
        }

        function addToTable(dueDate, installment) {
            let tds = "<tr>";

            let formattedInstallment = formatNumberToCurrency(installment);

            tds += "<td>" + dueDate + "</td>";
            tds += "<td>" + formattedInstallment + "</td>";
            tds += "<td><a class=\"delete-installment\" href=\"\"><spring:message code="accounting.event.custom.payment.plan.creation.delete.installment" text="Delete"/></a></td>";
            tds += "</tr>";

            tbody.length > 0 ? tbody.append(tds) : table.append(tds);
        }

        function addNewInstallment(dueDate, installment, numberOfInstallment) {
            errorDueDate.text("");
            errorInstallment.text("");

            let error = false;

            if(dueDate === "") {
                errorDueDate.text("<spring:message code="accounting.event.custom.payment.plan.creation.due.date.required" text="Due date required"/>");
                error = true;
            }
            if(installment === "") {
                errorInstallment.text("<spring:message code="accounting.event.custom.payment.plan.creation.installment.required" text="Installment required"/>");
                error = true;
            }
            if(numberOfInstallment === "") {
                errorNumberOfInstallment.text("<spring:message code="accounting.event.custom.payment.plan.creation.numberOfInstallment.required" text="Number of Installments required"/>");
                error = true;
            }
            if(dueDate !== "" && isDuplicateDate(dueDate)) {
                errorDueDate.text("<spring:message code="accounting.event.custom.payment.plan.creation.duplicate.due.date" text="Duplicate due date"/>");
                error = true;
            }
            if(installment !== "" && formatCurrencyToNumber(installment) === "0.00") {
                errorInstallment.text("<spring:message code="accounting.event.custom.payment.plan.creation.installment.cannot.be.zero" text="Installment cannot be zero"/>");
                error = true;
            }

            if(error) return;

            var n;
            for (n = 0; n < numberOfInstallment; n++) {
                addToData(moment(dueDate, 'YYYY-MM-DD', true).add(n, 'month').format("YYYY-MM-DD"), installment);
            }
            tbody.empty();

            updateCurrentAmount();

            if (currentAmount < totalAmount) {
                var diffValue = totalAmount - currentAmount;
                addToData(moment(dueDate, 'YYYY-MM-DD', true).add(n, 'month').format("YYYY-MM-DD"), formatNumberToCurrency("" + diffValue));
            }

            updateCurrentAmount();

            Object.keys(data["map"])
                .sort()
                .forEach(function(v) {
                    addToTable(v, data["map"][v]);
                });

            installmentInput.val("");
            errorDueDate.text("");
            errorInstallment.text("");
        }

        function deleteInstallment(row, date) {
            delete data["map"][date];
            row.remove();
            updateCurrentAmount();
        }

        exemptionDateInput.on("change", function() {
            data["exemptionDate"] = $(this).val();
        });

        dueDateInput.on("change", function() {
            isDuplicateDate($(this).val()) ? errorDueDate.text("<spring:message code="accounting.event.custom.payment.plan.creation.duplicate.due.date" text="Duplicate due date"/>") : errorDueDate.text("");
        });

        $("input[data-type=\"currency\"]").on({
            keyup: function() {
                formatCurrency($(this));
            },
            blur: function() {
                formatCurrency($(this), "blur");
            }
        });

        addInstallmentButton.click(function(e) {
            e.preventDefault();
            e.stopPropagation();
            addNewInstallment(dueDateInput.val(), installmentInput.val(), numberOfInstallmentInput.val());
        });

        $(document).on("click", ".delete-installment", function(e) {
            e.preventDefault();
            e.stopPropagation();

            let target = $(e.target);
            let row = target.closest("tr");
            let date = row.find("td:first").text();

            deleteInstallment(row, date);
        });

        submitForm.click(function(e) {
            e.preventDefault();
            e.stopPropagation();

            let url = "${baseUrl}" + "${event.externalId}" + "/customPaymentPlan/create";
            $.ajax({
                url : url,
                type : "POST",
                contentType : "application/json",
                headers : { "${csrf.headerName}" : "${csrf.token}" },
                data : JSON.stringify(data),
                success : function() {
                    window.location.replace("${backUrl}");
                },
                error : function(result) {
                    errorDomain.text(result.responseText);
                }
            });
        });
    });

</script>

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
                <h2><spring:message code="accounting.event.custom.payment.plan.creation.title" text="Custom Payment Plan Creation"/></h2>
            </div>
        </div>

        <div class="row">
            <div class="col-sm-12">
                <span id="errorDomain" class="error"></span>
            </div>
        </div>

        <div class="row">
            <form role="form" class="form-horizontal" action="" method="post">
                ${csrf.field()}
                <div class="form-group">
                    <div class="row">
                        <label for="exemptionDate" class="control-label col-sm-1"><spring:message code="accounting.event.custom.payment.plan.exemption.date" text="Exemption Date"/></label>
                        <div class="col-sm-3">
                            <input id="exemptionDate" name="exemptionDate" value="${exemptionDate}" bennu-datetime required>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="row">
                        <label for="dueDate" class="control-label col-sm-1"><spring:message code="accounting.event.custom.payment.plan.due.date" text="Due Date"/></label>
                        <div class="col-sm-3">
                            <input id="dueDate" name="dueDate" value="" bennu-date/>
                            <span id="errorDueDate" class="error"></span>
                        </div>
                        <label for="installment" class="control-label col-sm-1"><spring:message code="accounting.event.custom.payment.plan.installment" text="Installment"/></label>
                        <div class="col-sm-2">
                            <input type="text" id="installment" name="installment" pattern="^€\d{1,3}(\.\d{3})*(,\d+)?$" value="" data-type="currency" placeholder="<spring:message code="accounting.event.custom.payment.plan.creation.placeholder" text="e.g."/> €1.234,56">
                            <span id="errorInstallment" class="error"></span>
                        </div>
                        <label for="numberOfInstallment" class="control-label col-sm-1"><spring:message code="accounting.event.custom.payment.plan.numberOfInstallment" text="Number of Installment"/></label>
                        <div class="col-sm-2">
                            <input type="text" id="numberOfInstallment" name="numberOfInstallment" pattern="^\d+$" value="" data-type="integer" placeholder="<spring:message code="accounting.event.custom.payment.plan.creation.placeholder" text="e.g."/> 12">
                            <span id="errorNumberOfInstallment" class="error"></span>
                        </div>
                        <div class="col-sm-offset-6">
                            <a id="add" class="btn btn-primary" type="button" href=""><spring:message code="accounting.event.custom.payment.plan.creation.add.installment" text="Add Installment"/></a>
                        </div>
                    </div>
                </div>
                <h3><spring:message code="accounting.event.custom.payment.plan.creation.installments" text="Installments"/></h3>
                <div class="row">
                    <div class="col-md-7 col-sm-10">
                        <section>
                            <table id="table" class="table">
                                <thead>
                                <tr>
                                    <th><spring:message code="accounting.event.custom.payment.plan.due.date" text="Due Date"/></th>
                                    <th><spring:message code="accounting.event.custom.payment.plan.installment" text="Installment"/></th>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </section>
                    </div>
                    <div class="col-md-5">
                        <section>
                            <dl class="sum">
                                <dt><spring:message code="accounting.event.details.total" text="Total"/></dt>
                                <dd><span id="currentAmount"></span><span> <spring:message code="accounting.event.custom.payment.plan.creation.current.amount.of.total" text="of"/> </span><span id="totalAmount"></span></dd>
                            </dl>
                            <div class="actions">
                                <button id="submitForm" class="btn btn-block btn-primary" type="submit">
                                    <spring:message code="accounting.event.custom.payment.plan.creation.submit" text="Create"/>
                                </button>
                                <a class="btn btn-block btn-default" href="${backUrl}"><spring:message code="accounting.event.custom.payment.plan.creation.back" text="Back"/></a>
                            </div>
                        </section>
                    </div>
                </div>
            </form>
        </div>

    </main>
</div>
