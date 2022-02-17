

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

${portal.toolkit()}

<spring:url var="backUrl" value="/event-template-management/{template}">
    <spring:param name="template" value="${template.externalId}"/>
</spring:url>

<script type="text/javascript" src="${pageContext.request.contextPath}/bennu-toolkit/js/libs/moment.js"></script>
<style>
.actions {
    margin-top: 18px;
    margin-bottom: 9px;
}
</style>

<script type="text/javascript">
    let configData = {
        TUITION: {
            dueDateAmountMap: {},
            productCode: "",
            productDescription: "",
            accountId: ""
        },
        INSURANCE: {
            dueDateAmountMap: {},
            productCode: "",
            productDescription: "",
            accountId: ""
        },
        ADMIN_FEES: {
            dueDateAmountMap: {},
            penaltyAmountMap: {},
            productCode: "",
            productDescription: "",
            accountId: ""
        },
    };

    let otherData = {};
    let alternativesData = {};

    function isValidJson(jsonStr) {
        try {
            JSON.parse(jsonStr);
        } catch (e) {
            return false;
        }
        return true;
    }

    function validateInputField(input, required, isValid) {
        var errorMsgContainer = input.parents(".error-msg-container");
        var errorMsg = errorMsgContainer.children(".error-msg");
        var tab = $('#configDataTabs a[href="#' + errorMsgContainer.data("tab") + '"]');
        var tabContents = $("#" + errorMsgContainer.data("tab"));
        
        // clear error message
        errorMsgContainer.removeClass("has-error");
        errorMsg.addClass("hidden");
        if (tabContents.find(".has-error").length == 0) {
            tab.children(".badge").addClass("hidden");
        }
        
        // validate
        if (!required && !input.val()) {
            // not required, input empty
            return true;
        }
        
        if ((required && !input.val()) || !isValid(input.val())) {
            errorMsgContainer.addClass("has-error");
            errorMsg.removeClass("hidden");
            tab.children(".badge").removeClass("hidden");
            return false;
        }

        return true;
    }

    function updateConfigData() {
        var tuitionByEctsInput = $("#tuitionByEcts");
        var tuitionByEctsDaysToPayInput = $("#tuitionByEctsDaysToPay");
        var tuitionByEctsValueInput = $("#tuitionByEctsValue");
        var tuitionProductCodeInput = $("#tuitionProductCode");
        var tuitionProductDescriptionInput = $("#tuitionProductDescription");
        var tuitionAccountInput = $("#tuitionAccount");
        
        var insuranceProductCodeInput = $("#insuranceProductCode");
        var insuranceProductDescriptionInput = $("#insuranceProductDescription");
        var insuranceAccountInput = $("#insuranceAccount");
        
        var adminFeesProductCodeInput = $("#adminFeesProductCode");
        var adminFeesProductDescriptionInput = $("#adminFeesProductDescription");
        var adminFeesAccountInput = $("#adminFeesAccount");

        var maxCreditsInput = $("#maxCredits");
        var semesterInput = $("#semester");

        // check input
        var hasError = false;

        var integerRegex = /^[0-9]+$/;
        var amountRegex = /^[0-9]+(\.[0-9][0-9]?)?$/;

        // byECTS fields are only required if the checkbox is ticked
        // daysToPay must be a valid positive integer
        hasError = !validateInputField(tuitionByEctsDaysToPayInput, tuitionByEctsInput.prop('checked'), (val) => integerRegex.test(val)) || hasError;
        // value must be a valid number with at most two decimal places
        hasError = !validateInputField(tuitionByEctsValueInput, tuitionByEctsInput.prop('checked'), (val) => amountRegex.test(val)) || hasError;

        // tuition ProductCode must be a valid integer
        hasError = !validateInputField(tuitionProductCodeInput, true, (val) => integerRegex.test(val)) || hasError;
        // tuition ProductDescription must exist
        hasError = !validateInputField(tuitionProductDescriptionInput, true, (val) => true) || hasError;
        // tuition Account must be a valid integer
        hasError = !validateInputField(tuitionAccountInput, true, (val) => integerRegex.test(val)) || hasError;

        // insurance ProductCode must be a valid integer
        hasError = !validateInputField(insuranceProductCodeInput, true, (val) => integerRegex.test(val)) || hasError;
        // insurance ProductDescription must exist
        hasError = !validateInputField(insuranceProductDescriptionInput, true, (val) => true) || hasError;
        // insurance Account must be a valid integer
        hasError = !validateInputField(insuranceAccountInput, true, (val) => integerRegex.test(val)) || hasError;

        // adminFees ProductCode must be a valid integer
        hasError = !validateInputField(adminFeesProductCodeInput, true, (val) => integerRegex.test(val)) || hasError;
        // adminFees ProductDescription must exist
        hasError = !validateInputField(adminFeesProductDescriptionInput, true, (val) => true) || hasError;
        // adminFees Account must be a valid integer
        hasError = !validateInputField(adminFeesAccountInput, true, (val) => integerRegex.test(val)) || hasError;

        // maxCredits, if exists, must be a valid number with at most two decimal places
        hasError = !validateInputField(maxCreditsInput, false, (val) => amountRegex.test(val)) || hasError;
        // semester, if exists, must be a valid integer
        hasError = !validateInputField(semesterInput, false, (val) => integerRegex.test(val)) || hasError;
        
        if (hasError) {
            return false;
        }
        
        // populate data with input values
        if (tuitionByEctsInput.prop('checked')) {
            configData.TUITION.byECTS = {
                daysToPay: tuitionByEctsDaysToPayInput.val(),
                value: tuitionByEctsValueInput.val()
            };
        } else if (!!configData.TUITION.byECTS) {
            delete configData.TUITION.byECTS;
        }
        configData.TUITION.productCode = tuitionProductCodeInput.val();
        configData.TUITION.productDescription = tuitionProductDescriptionInput.val();
        configData.TUITION.accountId = tuitionAccountInput.val();
        
        configData.INSURANCE.productCode = insuranceProductCodeInput.val();
        configData.INSURANCE.productDescription = insuranceProductDescriptionInput.val();
        configData.INSURANCE.accountId = insuranceAccountInput.val();
        
        configData.ADMIN_FEES.productCode = adminFeesProductCodeInput.val();
        configData.ADMIN_FEES.productDescription = adminFeesProductDescriptionInput.val();
        configData.ADMIN_FEES.accountId = adminFeesAccountInput.val();

        if (!!maxCreditsInput.val()) {
            configData.maxCredits = JSON.parse(maxCreditsInput.val());
        } else if (!!configData.maxCredits) {
            delete configData.maxCredits;
        }

        if (!!semesterInput.val()) {
            configData.semester = semesterInput.val();
        } else if (!!configData.semester) {
            delete configData.semester;
        }
        
        return true;
    }

    function updateMapTable(table, entries, formatKey, formatValue, deleteEntry) {
        var emptyTableWarn = $(table).find(".map-table-empty");
        var tableEntries = $(table).find(".map-table-entries");
        
        emptyTableWarn.addClass("hidden");
        tableEntries.addClass("hidden");
        tableEntries.empty();

        if ($.isEmptyObject(entries)) {
            emptyTableWarn.removeClass("hidden");
        } else {
            for(e in entries) {
                var entryKeyCell = $("<td class='col-sm-3'>" + formatKey(e) + "</td>");
                var entryValueCell = $("<td class='col-sm-6'>" + formatValue(entries[e]) + "</td>")
                
                var entryActionDelete = $("<button type='button' class='btn btn-danger btn-delete-from-map' data-entry='" + e + "'>Delete</button>");
                entryActionDelete.click(function() {
                    deleteEntry(this);
                    updateMapTable(table, entries, formatKey, formatValue, deleteEntry);
                });
                var entryActionsCell = $("<td class='col-sm-3 text-right'></td>");
                entryActionDelete.appendTo(entryActionsCell);
                
                var entryRow = $("<tr></tr>");
                entryKeyCell.appendTo(entryRow);
                entryValueCell.appendTo(entryRow);
                entryActionsCell.appendTo(entryRow);
                
                tableEntries.append(entryRow);
            }
            tableEntries.removeClass("hidden");
        }
    }

    $(document).ready(function() {

        $(".date-map-group .btn-add-to-map").click(function() {
            var mapGroup = $(this).parents(".date-map-group");
            var section = mapGroup.data("section");
            var mapKey = mapGroup.data("map");
            var mapTable = mapGroup.find(".map-table");
            
            var firstDateInput = mapGroup.find(".first-date");
            var firstDateStr = firstDateInput.val();
            var repeatTimesInput = mapGroup.find(".repeat");
            var repeatTimes = repeatTimesInput.val();
            var amountInput = mapGroup.find(".amount");
            var amount = amountInput.val();

            // validate input values
            var hasError = false;
            var repeatRegex = /^[0-9]+$/;
            var amountRegex = /^[0-9]+(\.[0-9][0-9]?)?$/;
            
            hasError = !validateInputField(firstDateInput, true, (val) => moment(val, "YYYY-MM-DD").isValid()) || hasError;
            hasError = !validateInputField(repeatTimesInput, true, (val) => repeatRegex.test(val)) || hasError;
            hasError = !validateInputField(amountInput, true, (val) => amountRegex.test(val)) || hasError;
            
            if (hasError) {
                return;
            }

            // add entries to map
            var firstDate = moment(firstDateStr, "YYYY-MM-DD");
            for (let i = 0; i < repeatTimes; i++) {
                var dueDateStr = moment(firstDate).add(i, "M").format("DD/MM/YYYY");
                configData[section][mapKey][dueDateStr] = amount;
            }

            updateMapTable(mapTable, configData[section][mapKey], (key) => key, (value) => value + "&euro;",
                function(deleteBtn) {
                    var mapGroup = $(deleteBtn).parents(".date-map-group");
                    var section = mapGroup.data("section");
                    var mapKey = mapGroup.data("map");
                    var entryKey = $(deleteBtn).data("entry");

                    delete configData[section][mapKey][entryKey];
                }
            );
        });

        $("#tuitionByEcts").change(function() {
            var ectsFields = $(".ects-fields")
            if ($(this).prop('checked')) {
                ectsFields.removeClass("hidden");
            } else {
                ectsFields.addClass("hidden");
                ectsFields.find("input").val("");
            }
        });

        $(".map-group .btn-add-to-map").click(function() {
            var mapGroup = $(this).parents(".map-group");
            var mapTable = mapGroup.find(".map-table");
            
            var entryKeyInput = mapGroup.find(".entry-key");
            var entryKey = entryKeyInput.val();
            var entryValueInput = mapGroup.find(".entry-value");
            var entryValue = entryValueInput.val();

            // validate input values
            var hasError = false;
            var reserved = ["TUITION", "INSURANCE", "ADMIN_FEES", "maxCredits", "semester"];

            hasError = !validateInputField(entryKeyInput, true, (val) => reserved.includes(val) || (val in otherData)) || hasError;
            hasError = !validateInputField(entryValueInput, true, isValidJson) || hasError;

            if (hasError) {
                return;
            }

            // add entries to map
            otherData[entryKey] = JSON.parse(entryValue);
            configData[entryKey] = otherData[entryKey];

            updateMapTable(mapTable, otherData, (key) => key, (value) => "<pre>" + JSON.stringify(value) + "</pre>",
                function(deleteBtn) {
                    var entryKey = $(deleteBtn).data("entry");

                    delete otherData[entryKey];
                    delete configData[entryKey];
                }
            );
        });

        $("#submitBtn").click(function() {
            var updateSuccess = updateConfigData();

            if (updateSuccess) {
                var form = $(this).parents("form");
                form.children("#configData").val(JSON.stringify(configData));
                form.children("#alternativesData").val(JSON.stringify(alternativesData));
                form.submit();
            }
        });

        $('#configDataTabs a[href="#raw"]').on('shown.bs.tab', function (e) {
            var rawDataPre = $("#configDataPreview");
            var rawDataError = $("#configDataError");
            
            rawDataPre.addClass("hidden");
            rawDataError.addClass("hidden");
            
            var updateSuccess = updateConfigData();
            if (!updateSuccess) {
                rawDataError.removeClass("hidden");
            } else {
                // update raw data preview
                rawDataPre.text(JSON.stringify(configData, null, 4));
                rawDataPre.removeClass("hidden");
            }
        });

        $('.replicate-group input[type="checkbox"]').change(function() {
            var alternativeId = $(this).parents(".replicate-group").data("alternative");
            if ($(this).prop('checked')) {
                alternativesData[alternativeId] = {};
            } else {
                delete alternativesData[alternativeId];
            }
        });
    });
</script>

<div class="container-fluid">
    <main>
    
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1>Event Templates</h1>
                </div>
            </div>
        </header>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>Create Event Template Configuration</h2>
                </div>
                
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}">Back</a>
                </div>
            </div>
        </section>

        
        <section>
            <div class="alert alert-info" role="alert">
                You are creating a configuration to the following event template:
                <ul>
                    <li> ${template.code} </li>
                    <li> ${template.title.content} </li>
                    <li> ${template.description.content} </li>
                </ul>
            </div>
        </section>

        <form role="form" class="form-horizontal" action="" method="post">
            ${csrf.field()}
            <input type="hidden" name="configData" id="configData">
            <input type="hidden" name="alternativesData" id="alternativesData">

            <section>
                <div class="form-group">
                    <label for="applyFrom" class="control-label col-sm-2">From</label>
                    <div class="col-sm-10">
                        <input id="applyFrom" name="applyFrom" bennu-date required>
                    </div>
                </div>
                <div class="form-group">
                    <label for="applyUntil" class="control-label col-sm-2">Until</label>
                    <div class="col-sm-10">
                        <input id="applyUntil" name="applyUntil" bennu-date required>
                    </div>
                </div>
            </section>
            
            <div>
                <ul class="nav nav-pills" id="configDataTabs" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#tuition" aria-controls="tuition" role="tab" data-toggle="tab">
                            Tuition
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#insurance" aria-controls="insurance" role="tab" data-toggle="tab">
                            Insurance
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#adminfees" aria-controls="adminfees" role="tab" data-toggle="tab">
                            Administration Fees
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#additional" aria-controls="additional" role="tab" data-toggle="tab">
                            Additional Data
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#raw" aria-controls="raw" role="tab" data-toggle="tab">Raw Data Preview</a>
                    </li>
                    <c:if test="${!children.isEmpty()}">
                        <li role="presentation">
                            <a href="#alternatives" aria-controls="alternatives" role="tab" data-toggle="tab">
                                Replicate to Template Alternatives
                                <span class="badge glyphicon glyphicon-remove hidden"></span>
                            </a>
                        </li>
                    </c:if>
                </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="tuition">
                        <div class="row">
                            <div class="col-sm-12">
                                <h3>Tuition</h3>
                            </div>
                        </div>


                        <div class="date-map-group"  data-section="TUITION" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label class="control-label col-md-2">Due Date Amount Map</label>
                            </div>
                            
                            <div class="form-group">
                                <label for="dueDate" class="control-label col-md-2 col-md-offset-2">First Due Date</label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <input class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden">Must be a valid date.</span>
                                </div>

                                <label for="tuitionNumberOfInstallments" class="control-label col-md-2">Number of Installments</label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <input class="form-control repeat" type="number" min="1" max="12" value="1">
                                    <span class="help-block error-msg hidden">Must be a valid integer.</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionInstallment" class="control-label col-md-2 col-md-offset-2">Amount</label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <div class="input-group">
                                        <input class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                                </div>

                                <div class="col-md-4 col-md-offset-1">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map">Add Installments</button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty">(Empty)</p>
                                    <div class="table-responsive">
                                        <table class="table table-stripped">
                                            <tbody class="map-table-entries hidden">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div>
                            <div class="form-group">
                                <div class="col-md-offset-2 col-md-2">
                                    <div class="checkbox">
                                        <label class="control-label">
                                            <input type="checkbox" id="tuitionByEcts">
                                            By ECTS?
                                        </label>
                                    </div>
                                </div>

                                <div class="ects-fields hidden">
                                    <label for="tuitionByEctsDaysToPay" class="control-label col-md-2">Days To Pay</label>
                                    <div class="col-md-2 error-msg-container" data-tab="tuition">
                                        <input id="tuitionByEctsDaysToPay" class="form-control" type="number" min="1" step="1">
                                        <span class="help-block error-msg hidden">Must be a valid integer.</span>
                                    </div>
                                    
                                    <label for="tuitionByEctsValue" class="control-label col-md-2">Value</label>
                                    <div class="col-md-2 error-msg-container" data-tab="tuition">
                                        <div class="input-group">
                                            <input id="tuitionByEctsValue" class="form-control" type="number" step="0.01" min="0">
                                            <span class="input-group-addon">&euro;</span>
                                        </div>
                                        <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="tuitionProductCode" class="control-label col-md-2">Product Code</label>
                            <div class="col-md-2 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionProductCode" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>

                            <label for="tuitionProductDescription" class="control-label col-md-2">Product Description</label>
                            <div class="col-md-6 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionProductDescription" class="form-control">
                                <span class="help-block error-msg hidden">This field is required.</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="tuitionAccount" class="control-label col-md-2">Account</label>
                            <div class="col-md-10 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionAccount" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="insurance">
                        <div class="row">
                            <div class="col-md-12">
                                <h3>Insurance</h3>
                            </div>
                        </div>

                        <div class="date-map-group"  data-section="INSURANCE" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label class="control-label col-md-2">Due Date Amount Map</label>
                            </div>
                            
                            <div class="form-group">
                                <label for="dueDate" class="control-label col-md-2 col-md-offset-2">First Due Date</label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <input class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden">Must be a valid date.</span>
                                </div>

                                <label for="tuitionNumberOfInstallments" class="control-label col-md-2">Number of Installments</label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <input class="form-control repeat" type="number" min="1" max="12" value="1">
                                    <span class="help-block error-msg hidden">Must be a valid integer.</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionInstallment" class="control-label col-md-2 col-md-offset-2">Amount</label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <div class="input-group">
                                        <input class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                                </div>

                                <div class="col-md-4 col-md-offset-1">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map">Add Installments</button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty">(Empty)</p>
                                    <div class="table-responsive">
                                        <table class="table table-stripped">
                                            <tbody class="map-table-entries hidden">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="insuranceProductCode" class="control-label col-md-2">Product Code</label>
                            <div class="col-md-2 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceProductCode" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>

                            <label for="insuranceProductDescription" class="control-label col-md-2">Product Description</label>
                            <div class="col-md-6 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceProductDescription" class="form-control">
                                <span class="help-block error-msg hidden">This field is required.</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="insuranceAccount" class="control-label col-md-2">Account</label>
                            <div class="col-md-10 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceAccount" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="adminfees">
                        <div class="row">
                            <div class="col-md-12">
                                <h3>Administration Fees</h3>
                            </div>
                        </div>

                        <div class="date-map-group"  data-section="ADMIN_FEES" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label class="control-label col-md-2">Due Date Amount Map</label>
                            </div>
                            
                            <div class="form-group">
                                <label for="dueDate" class="control-label col-md-2 col-md-offset-2">First Due Date</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <input class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden">Must be a valid date.</span>
                                </div>

                                <label for="tuitionNumberOfInstallments" class="control-label col-md-2">Number of Installments</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <input class="form-control repeat" type="number" min="1" max="12" value="1">
                                    <span class="help-block error-msg hidden">Must be a valid integer.</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionInstallment" class="control-label col-md-2 col-md-offset-2">Amount</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <div class="input-group">
                                        <input class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                                </div>

                                <div class="col-md-4 col-md-offset-1">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map">Add Installments</button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty">(Empty)</p>
                                    <div class="table-responsive">
                                        <table class="table table-stripped">
                                            <tbody class="map-table-entries hidden">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="date-map-group"  data-section="ADMIN_FEES" data-map="penaltyAmountMap">
                            <div class="form-group">
                                <label class="control-label col-md-2">Penalty Amount Map</label>
                            </div>
                            
                            <div class="form-group">
                                <label for="dueDate" class="control-label col-md-2 col-md-offset-2">First Due Date</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <input class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden">Must be a valid date.</span>
                                </div>

                                <label for="tuitionNumberOfInstallments" class="control-label col-md-2">Number of Installments</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <input class="form-control repeat" type="number" min="1" max="12" value="1">
                                    <span class="help-block error-msg hidden">Must be a valid integer.</span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionInstallment" class="control-label col-md-2 col-md-offset-2">Amount</label>
                                <div class="col-md-3 error-msg-container" data-tab="adminfees">
                                    <div class="input-group">
                                        <input class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                                </div>

                                <div class="col-md-4 col-md-offset-1">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map">Add Installments</button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty">(Empty)</p>
                                    <div class="table-responsive">
                                        <table class="table table-stripped">
                                            <tbody class="map-table-entries hidden">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="adminFeesProductCode" class="control-label col-md-2">Product Code</label>
                            <div class="col-md-2 error-msg-container" data-tab="adminfees">
                                <input type="text" id="adminFeesProductCode" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>

                            <label for="adminFeesProductDescription" class="control-label col-md-2">Product Description</label>
                            <div class="col-md-6 error-msg-container" data-tab="adminfees">
                                <input type="text" id="adminFeesProductDescription" class="form-control">
                                <span class="help-block error-msg hidden">This field is required.</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="adminFeesAccount" class="control-label col-md-2">Account</label>
                            <div class="col-md-10 error-msg-container" data-tab="adminfees">
                                <input type="text" id="adminFeesAccount" class="form-control">
                                <span class="help-block error-msg hidden">Must be a valid integer.</span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="additional">
                        <div class="row">
                            <div class="col-md-12">
                                <h3>Additional Data</h3>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="maxCredits" class="control-label col-md-2">Max Credits</label>
                            <div class="col-md-10 error-msg-container" data-tab="additional">
                                <input id="maxCredits" class="form-control" type="number" step="1">
                                <span class="help-block error-msg hidden">Must be a valid number with at most two decimal places.</span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="semester" class="control-label col-md-2">Semester</label>
                            <div class="col-md-10 error-msg-container" data-tab="additional">
                                <select class="form-control" id="semester" name="config">
                                    <option value="">(None)</option>
                                    <c:forEach items="${semesters}" var="semester">
                                        <option value="${semester.externalId}">${semester.qualifiedName}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block error-msg hidden">Must be a valid semester.</span>
                            </div>
                        </div>

                        <div class="map-group">
                            <div class="form-group">
                                <label for="semester" class="control-label col-md-2">Other Fields</label>
                            </div>
                            <div class="form-group">
                                <label for="otherKey" class="control-label col-md-1 col-md-offset-2">Key</label>
                                <div class="col-md-3 error-msg-container" data-tab="additional">
                                    <input type="text" class="form-control entry-key">
                                    <span class="help-block error-msg hidden">Must be a valid JSON key not already in use.</span>
                                </div>

                                <label for="otherValue" class="control-label col-md-1">Value</label>
                                <div class="col-md-3 error-msg-container" data-tab="additional">
                                    <input type="text" class="form-control entry-value">
                                    <span class="help-block error-msg hidden">Must be a valid JSON.</span>
                                </div>

                                <div class="col-md-2">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map">Add</button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty">(Empty)</p>
                                    <div class="table-responsive">
                                        <table class="table table-stripped">
                                            <tbody class="map-table-entries hidden">
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="raw">
                        <div class="row">
                            <div class="col-md-12">
                                <h3>Raw Data Preview</h3>
                            </div>
                        </div>
                        <pre id="configDataPreview"></pre>
                        <div id="configDataError" class="alert alert-danger hidden" role="alert">Unable to generate configuration JSON due to errors in the given data. Please fix them and try again.</div>
                    </div>
                    
                    <c:if test="${!children.isEmpty()}">
                        <div role="tabpanel" class="tab-pane" id="alternatives">
                            <div class="row">
                                <div class="col-md-12">
                                    <h3>Replicate to Template Alternatives</h3>
                                </div>
                            </div>
                            <c:forEach items="${children}" var="child">
                                <div class="replicate-group" data-alternative="${child.externalId}">
                                    <div class="form-group">
                                        <div class="col-md-offset-2 col-md-10">
                                            <div class="checkbox">
                                                <label class="control-label">
                                                    <input type="checkbox">
                                                    Replicate to ${child.title.content} (${child.code})
                                                </label>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="row">
                <div class="col-sm-offset-8 col-sm-4">
                    <button id="submitBtn" class="btn btn-block btn-primary" type="button">Create</button>
                </div>
            </div>

        </form>

    </main>
</div>
