

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

<c:choose>
    <c:when test="${prevConfigApplyUntil != null}">
        <script type="text/javascript">
            const applyFromHasLowerBound = true;
            const prevConfigApplyUntil = moment("${prevConfigApplyUntil.toString()}");
        </script>
    </c:when>
    <c:otherwise>
        <script type="text/javascript">
            const applyFromHasLowerBound = false;
            const prevConfigApplyUntil = moment();
        </script>
    </c:otherwise>
</c:choose>

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
    let alternativesDataHolder = {};
    let patches = {};

    function isValidJson(jsonStr) {
        try {
            JSON.parse(jsonStr);
        } catch (e) {
            return false;
        }
        return true;
    }

    function validateInputField(input, required, isValid, alternative) {
        let errorMsgContainer = input.parents(".error-msg-container");
        let errorMsg = errorMsgContainer.children(".error-msg");
        let tabHolder = !alternative ? "#configDataTabs" : "#" + alternative + "dataTabs"
        let tab = $(tabHolder + ' a[href="#' + errorMsgContainer.data("tab") + '"]');
        let tabContents = $("#" + errorMsgContainer.data("tab"));
        // the following variables are used only if the error occurs inside of an alternative's modal 
        let alternativesTabContents = $('#alternatives');
        let alternativesTab = $('#configDataTabs a[href="#alternatives"]');
        
        // clear error message and tab's error indication badge
        errorMsgContainer.removeClass("has-error");
        errorMsg.addClass("hidden");
        if (tabContents.find(".has-error").length == 0) {
            tab.children(".badge").addClass("hidden");
        }
        if (!!alternative) {
            // do the same in the alternatives tab on the main screen
            $("#" + alternative + "error").addClass("hidden");
            if (alternativesTabContents.find(".has-error").length == 0) {
                alternativesTab.children(".badge").addClass("hidden");
            }
        }
        
        // validate
        if ((!input.val() && !required)
            || (!!input.val() && isValid(input.val()))) {
            // input empty and field not required
            // OR input not empty and valid
            return true;
        }
        
        // either is required but input empty, or non-empty input is not valid
        // this input field is not valid, so we must show all error indicators (messages, badges)
        errorMsgContainer.addClass("has-error");
        errorMsg.removeClass("hidden");
        tab.children(".badge").removeClass("hidden");
        if (!!alternative) {
            // do the same in the alternatives tab on the main screen
            $("#" + alternative + "error").removeClass("hidden");
            alternativesTab.children(".badge").removeClass("hidden");
        }

        return false;
    }

    function updateConfigData() {
        let applyFromInput = $("#applyFrom");
        let applyUntilInput = $("#applyUntil");

        let tuitionByEctsInput = $("#tuitionByEcts");
        let tuitionByEctsDaysToPayInput = $("#tuitionByEctsDaysToPay");
        let tuitionByEctsValueInput = $("#tuitionByEctsValue");
        let tuitionProductCodeInput = $("#tuitionProductCode");
        let tuitionProductDescriptionInput = $("#tuitionProductDescription");
        let tuitionAccountInput = $("#tuitionAccount");
        
        let insuranceProductCodeInput = $("#insuranceProductCode");
        let insuranceProductDescriptionInput = $("#insuranceProductDescription");
        let insuranceAccountInput = $("#insuranceAccount");
        
        let adminFeesProductCodeInput = $("#adminFeesProductCode");
        let adminFeesProductDescriptionInput = $("#adminFeesProductDescription");
        let adminFeesAccountInput = $("#adminFeesAccount");

        let maxCreditsInput = $("#maxCredits");
        let semesterInput = $("#semester");

        // check input
        let hasError = false;

        let digitsRegex = /^[0-9]+$/;
        let posIntRegex = /^[1-9][0-9]*$/;
        let amountRegex = /^[0-9]+(\.[0-9][0-9]?)?$/;

        // applyFrom must be a valid date that comes after prevConfigApplyUntil (if applyFromHasLowerBound)
        hasError = !validateInputField(applyFromInput, true, (val) => {
            let applyFrom = moment(val, "YYYY-MM-DD");
            return applyFrom.isValid() && (!applyFromHasLowerBound || applyFrom.isAfter(prevConfigApplyUntil));
        }) || hasError;
        // applyUntil must be a valid date that comes after applyFrom
        hasError = !validateInputField(applyUntilInput, true, (val) => {
            let applyFrom = moment(applyFromInput.val(), "YYYY-MM-DD");
            let applyUntil = moment(val, "YYYY-MM-DD");
            return applyFrom.isValid() && applyUntil.isValid() && applyUntil.isAfter(applyFrom);
        }) || hasError;

        // byECTS fields are only required if the checkbox is ticked
        // daysToPay must be a valid positive integer
        hasError = !validateInputField(tuitionByEctsDaysToPayInput, tuitionByEctsInput.prop('checked'), (val) => posIntRegex.test(val)) || hasError;
        // value must be a valid number with at most two decimal places
        hasError = !validateInputField(tuitionByEctsValueInput, tuitionByEctsInput.prop('checked'), (val) => amountRegex.test(val)) || hasError;

        // tuition ProductCode must exist
        hasError = !validateInputField(tuitionProductCodeInput, true, (val) => true) || hasError;
        // tuition ProductDescription must exist
        hasError = !validateInputField(tuitionProductDescriptionInput, true, (val) => true) || hasError;
        // tuition Account must be a valid integer
        hasError = !validateInputField(tuitionAccountInput, true, (val) => digitsRegex.test(val)) || hasError;

        // insurance ProductCode must exist
        hasError = !validateInputField(insuranceProductCodeInput, true, (val) => true) || hasError;
        // insurance ProductDescription must exist
        hasError = !validateInputField(insuranceProductDescriptionInput, true, (val) => true) || hasError;
        // insurance Account must be a valid integer
        hasError = !validateInputField(insuranceAccountInput, true, (val) => digitsRegex.test(val)) || hasError;

        // adminFees ProductCode must exist
        hasError = !validateInputField(adminFeesProductCodeInput, true, (val) => true) || hasError;
        // adminFees ProductDescription must exist
        hasError = !validateInputField(adminFeesProductDescriptionInput, true, (val) => true) || hasError;
        // adminFees Account must be a valid integer
        hasError = !validateInputField(adminFeesAccountInput, true, (val) => digitsRegex.test(val)) || hasError;

        // maxCredits, if exists, must be a valid number with at most two decimal places
        hasError = !validateInputField(maxCreditsInput, false, (val) => amountRegex.test(val)) || hasError;
        // semester, if exists, must be a valid integer
        hasError = !validateInputField(semesterInput, false, (val) => digitsRegex.test(val)) || hasError;
        
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

    function updateAlternativeData(alternativeId) {
        let replicateInput = $("#replicateTo" + alternativeId);

        if (!replicateInput.prop('checked')) {
            // there should be no replication to this alternative
            if (alternativeId in patches) {
                delete patches[alternativeId];
            }
            // force input fields to be considered valid
            $("#" + alternativeId + "patchModal input").each(function() {
                validateInputField($(this), false, (val) => true, alternativeId);
            });
            return true;
        }
        // we must include patching for this alternative

        let changeTuitionDueDateAmountMapInput = $("#" + alternativeId + "changeTuitionDueDateAmountMap");
        let changeInsuranceDueDateAmountMapInput = $("#" + alternativeId + "changeInsuranceDueDateAmountMap");
        let changeAdminFeesDueDateAmountMapInput = $("#" + alternativeId + "changeAdminFeesDueDateAmountMap");
        let changeAdminFeesPenaltyAmountMapInput = $("#" + alternativeId + "changeAdminFeesPenaltyAmountMap");
        let changeMaxCreditsInput = $("#" + alternativeId + "changeMaxCredits");
        let maxCreditsInput = $("#" + alternativeId + "maxCredits");
        let changeSemesterInput = $("#" + alternativeId + "changeSemester");
        let semesterInput = $("#" + alternativeId + "semester");

        // check input
        let hasError = false;

        let digitsRegex = /^[0-9]+$/;
        let posIntRegex = /^[1-9][0-9]*$/;
        let amountRegex = /^[0-9]+(\.[0-9][0-9]?)?$/;

        if (changeMaxCreditsInput.prop("checked")) {
            // maxCredits, if exists, must be a valid number with at most two decimal places
            hasError = !validateInputField(maxCreditsInput, false, (val) => amountRegex.test(val), alternativeId) || hasError;
        }
        if (changeSemesterInput.prop("checked")) {
            // semester, if exists, must be a valid integer
            hasError = !validateInputField(semesterInput, false, (val) => digitsRegex.test(val), alternativeId) || hasError;
        }
        
        if (hasError) {
            return false;
        }
        
        // populate data with input values
        patches[alternativeId] = [];
        
        if (changeTuitionDueDateAmountMapInput.prop("checked")) {
            // user chose to patch /TUITION/dueDateAmountMap in this alternative
            patches[alternativeId].push({
                op: "add",
                path: "/TUITION/dueDateAmountMap",
                value: alternativesDataHolder[alternativeId].TUITION.dueDateAmountMap
            });
        }
        if (changeInsuranceDueDateAmountMapInput.prop("checked")) {
            // user chose to patch /INSURANCE/dueDateAmountMap in this alternative
            patches[alternativeId].push({
                op: "add",
                path: "/INSURANCE/dueDateAmountMap",
                value: alternativesDataHolder[alternativeId].INSURANCE.dueDateAmountMap
            });
        }
        if (changeAdminFeesDueDateAmountMapInput.prop("checked")) {
            // user chose to patch /ADMIN_FEES/dueDateAmountMap in this alternative
            patches[alternativeId].push({
                op: "add",
                path: "/ADMIN_FEES/dueDateAmountMap",
                value: alternativesDataHolder[alternativeId].ADMIN_FEES.dueDateAmountMap
            });
        }
        if (changeAdminFeesPenaltyAmountMapInput.prop("checked")) {
            // user chose to patch /ADMIN_FEES/penaltyAmountMap in this alternative
            patches[alternativeId].push({
                op: "add",
                path: "/ADMIN_FEES/penaltyAmountMap",
                value: alternativesDataHolder[alternativeId].ADMIN_FEES.penaltyAmountMap
            });
        }

        if (changeMaxCreditsInput.prop("checked")) {
            // user chose to patch /maxCredits in this alternative
            if (!!maxCreditsInput.val()) {
                patches[alternativeId].push({
                    op: "add",
                    path: "/maxCredits",
                    value: JSON.parse(maxCreditsInput.val())
                });
            } else if (!!configData.maxCredits) {
                // user wants to delete /maxCredits from this alternative
                patches[alternativeId].push({
                    op: "remove",
                    path: "/maxCredits"
                });
            }
        }


        if (changeSemesterInput.prop("checked")) {
            // user chose to patch /semester in this alternative
            if (!!semesterInput.val()) {
                patches[alternativeId].push({
                    op: "add",
                    path: "/semester",
                    value: semesterInput.val()
                });
            } else if (!!configData.semester) {
                // user wants to delete /semester from this alternative
                patches[alternativeId].push({
                    op: "remove",
                    path: "/semester"
                });
            }
        }
        
        return true;
    }

    function updateAllData() {
        let updateSuccess = updateConfigData();
        $(".replicate-group").each(function() {
            updateSuccess = updateAlternativeData($(this).data("alternative")) && updateSuccess;
        });

        return updateSuccess;
    }

    function updateMapTable(table, entries, formatKey, formatValue, deleteEntry) {
        let emptyTableWarn = $(table).find(".map-table-empty");
        let tableEntries = $(table).find(".map-table-entries");
        
        emptyTableWarn.addClass("hidden");
        tableEntries.addClass("hidden");
        tableEntries.empty();

        if ($.isEmptyObject(entries)) {
            emptyTableWarn.removeClass("hidden");
        } else {
            for(e in entries) {
                let entryKeyCell = $("<td class='col-sm-3'>" + formatKey(e) + "</td>");
                let entryValueCell = $("<td class='col-sm-6'>" + formatValue(entries[e]) + "</td>")
                
                let entryActionDelete = $("<button type='button' class='btn btn-danger btn-delete-from-map' data-entry='" + e + "'><span class='glyphicon glyphicon-trash'></span></button>");
                entryActionDelete.click(function() {
                    deleteEntry(this);
                    updateMapTable(table, entries, formatKey, formatValue, deleteEntry);
                });
                let entryActionsCell = $("<td class='col-sm-3 text-right'></td>");
                entryActionDelete.appendTo(entryActionsCell);
                
                let entryRow = $("<tr></tr>");
                entryKeyCell.appendTo(entryRow);
                entryValueCell.appendTo(entryRow);
                entryActionsCell.appendTo(entryRow);
                
                tableEntries.append(entryRow);
            }
            tableEntries.removeClass("hidden");
        }
    }
    
    function hidderChange(hidder) {
        let hides = $($(hidder).data("hides"))
        if ($(hidder).prop('checked')) {
            hides.removeClass("hidden");
        } else {
            hides.addClass("hidden");
            hides.find("input").val("");
        }
    }

    $(document).ready(function() {

        $(".date-map-group .btn-add-to-map").click(function() {
            let mapGroup = $(this).parents(".date-map-group");
            let section = mapGroup.data("section");
            let mapKey = mapGroup.data("map");
            let mapTable = mapGroup.find(".map-table");
            let alternativeId = mapGroup.data("alternative");
            
            let firstDateInput = mapGroup.find(".first-date");
            let firstDateStr = firstDateInput.val();
            let repeatTimesInput = mapGroup.find(".nr-installments");
            let repeatTimes = repeatTimesInput.val();
            let intervalLengthInput = mapGroup.find(".interval-length");
            let intervalLengthStr = intervalLengthInput.val();
            let intervalUnitInput = mapGroup.find(".interval-unit");
            let intervalUnit = intervalUnitInput.val();
            let amountInput = mapGroup.find(".amount");
            let amount = amountInput.val();

            // validate input values
            let hasError = false;
            let posIntRegex = /^[1-9][0-9]*$/;
            let amountRegex = /^[0-9]+(\.[0-9][0-9]?)?$/;
            
            hasError = !validateInputField(firstDateInput, true, (val) => moment(val, "YYYY-MM-DD").isValid(), alternativeId) || hasError;
            hasError = !validateInputField(repeatTimesInput, true, (val) => posIntRegex.test(val), alternativeId) || hasError;
            hasError = !validateInputField(amountInput, true, (val) => amountRegex.test(val), alternativeId) || hasError;
            // intervalLength and intervalUnit are two parts of the same form-group
            // in order to avoid the second one hinding the error message of the first one, we're use short circuiting 
            hasError = !validateInputField(intervalLengthInput, true, (val) => posIntRegex.test(val), alternativeId)
                    || !validateInputField(intervalUnitInput, true, (val) => (val == "d" || val == "w" || val == "M" || val == "y"), alternativeId)
                    || hasError;
            
            if (hasError) {
                return;
            }

            // add entries to map
            let selectedData = !alternativeId ? configData : alternativesDataHolder[alternativeId];
            let firstDate = moment(firstDateStr, "YYYY-MM-DD");
            let intervalLength = parseInt(intervalLengthStr);
            for (let i = 0; i < repeatTimes; i++) {
                let dueDateStr = moment(firstDate).add(i * intervalLength, intervalUnit).format("DD/MM/YYYY");
                selectedData[section][mapKey][dueDateStr] = amount;
            }

            updateMapTable(mapTable, selectedData[section][mapKey], (key) => key, (value) => value + "&euro;",
                function(deleteBtn) {
                    let mapGroup = $(deleteBtn).parents(".date-map-group");
                    let section = mapGroup.data("section");
                    let mapKey = mapGroup.data("map");
                    let entryKey = $(deleteBtn).data("entry");

                    delete selectedData[section][mapKey][entryKey];
                }
            );
        });

        $(".map-group .btn-add-to-map").click(function() {
            let mapGroup = $(this).parents(".map-group");
            let mapTable = mapGroup.find(".map-table");
            
            let entryKeyInput = mapGroup.find(".entry-key");
            let entryKey = entryKeyInput.val();
            let entryValueInput = mapGroup.find(".entry-value");
            let entryValue = entryValueInput.val();

            // validate input values
            let hasError = false;
            let reserved = ["TUITION", "INSURANCE", "ADMIN_FEES", "maxCredits", "semester"];

            hasError = !validateInputField(entryKeyInput, true, (val) => !(reserved.includes(val) || (val in otherData))) || hasError;
            hasError = !validateInputField(entryValueInput, true, isValidJson) || hasError;

            if (hasError) {
                return;
            }

            // add entries to map
            otherData[entryKey] = JSON.parse(entryValue);
            configData[entryKey] = otherData[entryKey];

            updateMapTable(mapTable, otherData, (key) => key, (value) => "<pre>" + JSON.stringify(value) + "</pre>",
                function(deleteBtn) {
                    let entryKey = $(deleteBtn).data("entry");

                    delete otherData[entryKey];
                    delete configData[entryKey];
                }
            );
        });

        $('.update-preview').on('shown.bs.tab', function (e) {
            let rawDataPre = $($(this).data("preview"));
            let rawDataError = $($(this).data("error"));
            
            rawDataPre.addClass("hidden");
            rawDataError.addClass("hidden");
            
            let updateSuccess = updateConfigData();
            let selectedData;
            if ("alternative" in $(this).data()) {
                // show preview of an alternative json patch
                let rawDataAlternative = $(this).data("alternative");
                updateSuccess = updateAlternativeData(rawDataAlternative) && updateSuccess;
                selectedData = patches[rawDataAlternative];
            } else {
                // show preview of this template's config json data
                selectedData = configData;
            }
            
            if (!updateSuccess) {
                rawDataError.removeClass("hidden");
            } else {
                // update raw data preview
                rawDataPre.text(JSON.stringify(selectedData, null, 4));
                rawDataPre.removeClass("hidden");
            }
        });

        $(".hidder").change(function() {
            hidderChange(this);
        });
        // force hidders to do their thing according to their checked status at loading
        $(".hidder").each(function(index, element) {
            hidderChange(this); 
        });

        $('.replicate-group .modal').on('hide.bs.modal', function () {
            let replicateGroupData = $(this).parent().data();
            if ("alternative" in replicateGroupData) {
                updateAlternativeData(replicateGroupData["alternative"]);
                $(this).find('.nav a:first').tab('show');
            }
        });

        $("#validateBtn").click(function(event) {
            let alertSuccess = $('#validatedSuccess');
            let alertError = $('#validatedError');
            
            alertSuccess.addClass("hidden");
            alertError.addClass("hidden");

            let updateSuccess = updateAllData();
            
            if (updateSuccess) {
                alertSuccess.removeClass("hidden");
            } else {
                alertError.removeClass("hidden");
            }
        });

        $("#submitBtn").click(function(event) {
            let alertSuccess = $('#validatedSuccess');
            let alertError = $('#validatedError');
            
            alertSuccess.addClass("hidden");
            alertError.addClass("hidden");

            let updateSuccess = updateAllData();
            
            if (updateSuccess) {
                let form = $(this).parents("form");
                form.children("#configData").val(JSON.stringify(configData));
                form.children("#alternativesData").val(JSON.stringify(patches));
                form.submit();
            } else {
                alertError.removeClass("hidden");
            }
        });
    });
</script>

<div class="container-fluid">
    <main>
    
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1><spring:message code="accounting.management.event.templates.title"/></h1>
                </div>
            </div>
        </header>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2><spring:message code="accounting.management.event.templates.action.create.config"/></h2>
                </div>
                
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}"><spring:message code="accounting.management.event.templates.action.back"/></a>
                </div>
            </div>
        </section>

        
        <section>
            <div class="alert alert-info" role="alert">
                <spring:message code="accounting.management.event.templates.alert.creatingConfigToTemplate"/>
                <dl>
                    <dt><spring:message code="accounting.management.event.templates.label.code"/></dt>
                    <dd><c:out value='${template.code}'/></dd>
                    <dt><spring:message code="accounting.management.event.templates.label.title"/></dt>
                    <dd><c:out value='${template.title.content}'/></dd>
                    <dt><spring:message code="accounting.management.event.templates.label.description"/></dt>
                    <dd><c:out value='${template.description.content}'/></dd>
                </dl>
            </div>
        </section>

        <c:if test="${error}">
            <section>
                <div class="alert alert-danger" role="alert">
                    <spring:message code="accounting.management.event.templates.alert.errorCreatingConfig"/>
                </div>
            </section>
        </c:if>

        <form role="form" class="form-horizontal" action="" method="post">
            ${csrf.field()}
            <input type="hidden" name="configData" id="configData">
            <input type="hidden" name="alternativesData" id="alternativesData">

            <section>
                <div class="form-group">
                    <label for="applyFrom" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.label.config.apply.from"/></label>
                    <div class="col-sm-10 error-msg-container">
                        <input id="applyFrom" name="applyFrom" bennu-date required value="${prevConfigApplyUntil != null ? prevConfigApplyUntil.plusDays(1).toString("yyyy-MM-dd") : ""}">
                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.apply.from"/></span>
                    </div>
                </div>
                <div class="form-group">
                    <label for="applyUntil" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.label.config.apply.until"/></label>
                    <div class="col-sm-10 error-msg-container">
                        <input id="applyUntil" name="applyUntil" bennu-date required value="${prevConfigApplyUntil != null ? prevConfigApplyUntil.plusYears(1).toString("yyyy-MM-dd") : ""}">
                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.apply.until"/></span>
                    </div>
                </div>
            </section>
            
            <div>
                <ul class="nav nav-pills" id="configDataTabs" role="tablist">
                    <li role="presentation" class="active">
                        <a href="#tuition" aria-controls="tuition" role="tab" data-toggle="tab">
                            <spring:message code="accounting.management.event.templates.label.config.tuition"/>
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#insurance" aria-controls="insurance" role="tab" data-toggle="tab">
                            <spring:message code="accounting.management.event.templates.label.config.insurance"/>
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#adminFees" aria-controls="adminfees" role="tab" data-toggle="tab">
                            <spring:message code="accounting.management.event.templates.label.config.adminFees"/>
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#additional" aria-controls="additional" role="tab" data-toggle="tab">
                            <spring:message code="accounting.management.event.templates.label.config.additionalData"/>
                            <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                        </a>
                    </li>
                    <li role="presentation">
                        <a href="#raw" aria-controls="raw" role="tab" data-toggle="tab" class="update-preview" data-preview="#configDataPreview" data-error="#configDataError">
                            <spring:message code="accounting.management.event.templates.label.config.data.preview"/>
                        </a>
                    </li>
                    <c:if test="${!children.isEmpty()}">
                        <li role="presentation">
                            <a href="#alternatives" aria-controls="alternatives" role="tab" data-toggle="tab">
                                <spring:message code="accounting.management.event.templates.action.replicate.config"/>
                                <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                            </a>
                        </li>
                    </c:if>
                </ul>

                <div class="tab-content">
                    <div role="tabpanel" class="tab-pane active" id="tuition">
                        <div class="row">
                            <div class="col-sm-12">
                                <h3><spring:message code="accounting.management.event.templates.label.config.tuition"/></h3>
                            </div>
                        </div>


                        <div class="date-map-group"  data-section="TUITION" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label for="tuitionFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <input id="tuitionFirstDueDate" class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                </div>

                                <label for="tuitionNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <input id="tuitionNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="tuitionIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <input id="tuitionIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                    <select class="form-control interval-unit">
                                        <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                        <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                        <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                        <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                    </select>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                </div>
                                
                                <label for="tuitionInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="tuition">
                                    <div class="input-group">
                                        <input id="tuitionInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-8">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                            <input type="checkbox" id="tuitionByEcts" class="hidder" data-hides="#ects-fields">
                                            <spring:message code="accounting.management.event.templates.label.config.byEcts"/>?
                                        </label>
                                    </div>
                                </div>

                                <div id="ects-fields" class="hidden">
                                    <label for="tuitionByEctsDaysToPay" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.byEcts.daysToPay"/></label>
                                    <div class="col-md-2 error-msg-container" data-tab="tuition">
                                        <input id="tuitionByEctsDaysToPay" class="form-control" type="number" min="1" step="1">
                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.byEcts.daysToPay"/></span>
                                    </div>
                                    
                                    <label for="tuitionByEctsValue" class="control-label col-md-1"><spring:message code="accounting.management.event.templates.label.config.byEcts.value"/></label>
                                    <div class="col-md-3 error-msg-container" data-tab="tuition">
                                        <div class="input-group">
                                            <input id="tuitionByEctsValue" class="form-control" type="number" step="0.01" min="0">
                                            <span class="input-group-addon">&euro;</span>
                                        </div>
                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.byEcts.value"/></span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="tuitionProductCode" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.code"/></label>
                            <div class="col-md-2 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionProductCode" class="form-control" value="${prevConfigTuitionProductCode}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.code"/></span>
                            </div>

                            <label for="tuitionProductDescription" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.description"/></label>
                            <div class="col-md-6 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionProductDescription" class="form-control" value="${prevConfigTuitionProductDescription}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.description"/></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="tuitionAccount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                            <div class="col-md-10 error-msg-container" data-tab="tuition">
                                <input type="text" id="tuitionAccount" class="form-control" value="${prevConfigTuitionAccountId}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.account"/></span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="insurance">
                        <div class="row">
                            <div class="col-md-12">
                                <h3><spring:message code="accounting.management.event.templates.label.config.insurance"/></h3>
                            </div>
                        </div>

                        <div class="date-map-group"  data-section="INSURANCE" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label for="insuranceFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                            </div>
                            
                            <div class="form-group">
                                <label for="insuranceFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <input id="insuranceFirstDueDate" class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                </div>

                                <label for="insuranceNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <input id="insuranceNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="insuranceIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <input id="insuranceIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                    <select class="form-control interval-unit">
                                        <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                        <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                        <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                        <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                    </select>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                </div>
                                
                                <label for="insuranceInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="insurance">
                                    <div class="input-group">
                                        <input id="insuranceInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-8">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                            <label for="insuranceProductCode" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.code"/></label>
                            <div class="col-md-2 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceProductCode" class="form-control" value="${prevConfigInsuranceProductCode}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.code"/></span>
                            </div>

                            <label for="insuranceProductDescription" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.description"/></label>
                            <div class="col-md-6 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceProductDescription" class="form-control" value="${prevConfigInsuranceProductDescription}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.description"/></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="insuranceAccount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                            <div class="col-md-10 error-msg-container" data-tab="insurance">
                                <input type="text" id="insuranceAccount" class="form-control" value="${prevConfigInsuranceAccountId}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.account"/></span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="adminFees">
                        <div class="row">
                            <div class="col-md-12">
                                <h3><spring:message code="accounting.management.event.templates.label.config.adminFees"/></h3>
                            </div>
                        </div>

                        <div class="date-map-group"  data-section="ADMIN_FEES" data-map="dueDateAmountMap">
                            <div class="form-group">
                                <label for="adminFeesFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                            </div>
                            
                            <div class="form-group">
                                <label for="adminFeesFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesFirstDueDate" class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                </div>

                                <label for="adminFeesNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="adminFeesIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                    <select class="form-control interval-unit">
                                        <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                        <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                        <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                        <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                    </select>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                </div>
                                
                                <label for="adminFeesInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <div class="input-group">
                                        <input id="adminFeesInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-8">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                <label for="adminFeesPenaltyFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.penaltyAmountMap"/></label>
                            </div>
                            
                            <div class="form-group">
                                <label for="adminFeesPenaltyFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesPenaltyFirstDueDate" class="first-date" bennu-date/>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                </div>

                                <label for="adminFeesPenaltyNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesPenaltyNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <label for="adminFeesPenaltyIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <input id="adminFeesPenaltyIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                    <select class="form-control interval-unit">
                                        <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                        <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                        <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                        <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                    </select>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                </div>
                                
                                <label for="adminFeesPenaltyInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="adminFees">
                                    <div class="input-group">
                                        <input id="adminFeesPenaltyInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                        <span class="input-group-addon">&euro;</span>
                                    </div>
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                </div>
                            </div>
                            
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-8">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                            <label for="adminFeesProductCode" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.code"/></label>
                            <div class="col-md-2 error-msg-container" data-tab="adminFees">
                                <input type="text" id="adminFeesProductCode" class="form-control" value="${prevConfigAdminFeesProductCode}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.code"/></span>
                            </div>

                            <label for="adminFeesProductDescription" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.product.description"/></label>
                            <div class="col-md-6 error-msg-container" data-tab="adminFees">
                                <input type="text" id="adminFeesProductDescription" class="form-control" value="${prevConfigAdminFeesProductDescription}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.product.description"/></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="adminFeesAccount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                            <div class="col-md-10 error-msg-container" data-tab="adminFees">
                                <input type="text" id="adminFeesAccount" class="form-control" value="${prevConfigAdminFeesAccountId}">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.account"/></span>
                            </div>
                        </div>
                    </div>

                    <div role="tabpanel" class="tab-pane" id="additional">
                        <div class="row">
                            <div class="col-md-12">
                                <h3><spring:message code="accounting.management.event.templates.label.config.additionalData"/></h3>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="maxCredits" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.maxCredits"/></label>
                            <div class="col-md-10 error-msg-container" data-tab="additional">
                                <input id="maxCredits" class="form-control" type="number" step="1">
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.maxCredits"/></span>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="semester" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.semester"/></label>
                            <div class="col-md-10 error-msg-container" data-tab="additional">
                                <select class="form-control" id="semester" name="config">
                                    <option value=""><spring:message code="accounting.management.event.templates.label.config.empty"/></option>
                                    <c:forEach items="${semesters}" var="semester">
                                        <option value="${semester.externalId}">${semester.qualifiedName}</option>
                                    </c:forEach>
                                </select>
                                <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.semester"/></span>
                            </div>
                        </div>

                        <div class="map-group">
                            <div class="form-group">
                                <label for="otherKey" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.other"/></label>
                            </div>
                            <div class="form-group">
                                <label for="otherKey" class="control-label col-md-1 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.other.key"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="additional">
                                    <input id="otherKey" type="text" class="form-control entry-key">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.other.key"/></span>
                                </div>

                                <label for="otherValue" class="control-label col-md-1"><spring:message code="accounting.management.event.templates.label.config.other.value"/></label>
                                <div class="col-md-3 error-msg-container" data-tab="additional">
                                    <input id="otherValue" type="text" class="form-control entry-value">
                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.other.value"/></span>
                                </div>

                                <div class="col-md-2">
                                    <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add"/></button>
                                </div>
                            </div>

                            <div class="form-group map-table">
                                <div class="col-md-10 col-md-offset-2">
                                    <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                <h3><spring:message code="accounting.management.event.templates.label.config.data.preview"/></h3>
                            </div>
                        </div>
                        <pre id="configDataPreview"></pre>
                        <div id="configDataError" class="alert alert-danger hidden" role="alert"><spring:message code="accounting.management.event.templates.alert.unableToGenerateDataPreview"/></div>
                    </div>
                    
                    <c:if test="${!children.isEmpty()}">
                        <div role="tabpanel" class="tab-pane" id="alternatives">
                            <div class="row">
                                <div class="col-md-12">
                                    <h3><spring:message code="accounting.management.event.templates.action.replicate.config"/></h3>
                                </div>
                            </div>
                            <c:forEach items="${children}" var="child">
                                <div class="replicate-group" data-alternative="${child.externalId}">
                                    <div class="form-group">
                                        <div class="col-md-offset-2 col-md-10">
                                            <div class="checkbox">
                                                <label class="control-label">
                                                    <input type="checkbox" class="hidder" data-hides="#${child.externalId}modalToggler" id="replicateTo${child.externalId}">
                                                    <spring:message code="accounting.management.event.templates.action.replicate.to"/> ${child.title.content} (${child.code})
                                                    <span class="badge hidden" id="${child.externalId}error"><span class="glyphicon glyphicon-remove"></span></span>
                                                </label>
                                                <p id="${child.externalId}modalToggler" class="hidden">
                                                    (<a href="#" data-toggle="modal" data-target="#${child.externalId}patchModal"><spring:message code="accounting.management.event.templates.action.edit.replica"/></a>)
                                                </p>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal fade" id="${child.externalId}patchModal" tabindex="-1" role="dialog" aria-labelledby="label${child.externalId}patchModal">
                                        <div class="modal-dialog modal-lg" role="document">
                                            <div class="modal-content">
                                                <div class="modal-header">
                                                    <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                                                    <h3 class="modal-title" id="label${child.externalId}patchModal"><spring:message code="accounting.management.event.templates.action.edit.replica"/></h3>
                                                </div>
                                                <div class="modal-body">
                                                
                                                    <div class="alert alert-info" role="alert">
                                                        <spring:message code="accounting.management.event.templates.alert.replicatingConfigToAlternative"/>
                                                        <dl>
                                                            <dt><spring:message code="accounting.management.event.templates.label.code"/></dt>
                                                            <dd><c:out value='${child.code}'/></dd>
                                                            <dt><spring:message code="accounting.management.event.templates.label.title"/></dt>
                                                            <dd><c:out value='${child.title.content}'/></dd>
                                                            <dt><spring:message code="accounting.management.event.templates.label.description"/></dt>
                                                            <dd><c:out value='${child.description.content}'/></dd>
                                                        </dl>
                                                    </div>

                                                    <ul class="nav nav-pills" id="${child.externalId}dataTabs" role="tablist">
                                                        <li role="presentation" class="active">
                                                            <a href="#${child.externalId}tuition" aria-controls="${child.externalId}tuition" role="tab" data-toggle="tab">
                                                                <spring:message code="accounting.management.event.templates.label.config.tuition"/>
                                                                <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                                                            </a>
                                                        </li>
                                                        <li role="presentation">
                                                            <a href="#${child.externalId}insurance" aria-controls="${child.externalId}insurance" role="tab" data-toggle="tab">
                                                                <spring:message code="accounting.management.event.templates.label.config.insurance"/>
                                                                <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                                                            </a>
                                                        </li>
                                                        <li role="presentation">
                                                            <a href="#${child.externalId}adminFees" aria-controls="${child.externalId}adminFees" role="tab" data-toggle="tab">
                                                                <spring:message code="accounting.management.event.templates.label.config.adminFees"/>
                                                                <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                                                            </a>
                                                        </li>
                                                        <li role="presentation">
                                                            <a href="#${child.externalId}additional" aria-controls="${child.externalId}additional" role="tab" data-toggle="tab">
                                                                <spring:message code="accounting.management.event.templates.label.config.additionalData"/>
                                                                <span class="badge hidden"><span class="glyphicon glyphicon-remove"></span></span>
                                                            </a>
                                                        </li>
                                                        <li role="presentation">
                                                            <a href="#${child.externalId}raw" aria-controls="${child.externalId}raw" role="tab" data-toggle="tab" class="update-preview" data-preview="#${child.externalId}dataPreview" data-error="#${child.externalId}dataError" data-alternative="${child.externalId}">
                                                                <spring:message code="accounting.management.event.templates.label.config.data.preview"/>
                                                            </a>
                                                        </li>
                                                    </ul>

                                                    <div class="tab-content">
                                                        <div role="tabpanel" class="tab-pane active" id="${child.externalId}tuition">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <h3><spring:message code="accounting.management.event.templates.label.config.tuition"/></h3>
                                                                </div>
                                                            </div>


                                                            <div class="form-group">
                                                                <label for="${child.externalId}tuitionFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}tuitionDueDateAmountMapGroup" id="${child.externalId}changeTuitionDueDateAmountMap">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="date-map-group hidden" id="${child.externalId}tuitionDueDateAmountMapGroup" data-alternative="${child.externalId}" data-section="TUITION" data-map="dueDateAmountMap">
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}tuitionFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}tuition" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}tuitionFirstDueDate" class="first-date" bennu-date/>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                                                    </div>

                                                                    <label for="${child.externalId}tuitionNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}tuition" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}tuitionNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}tuitionIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}tuition" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}tuitionIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                                                        <select class="form-control interval-unit">
                                                                            <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                                                            <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                                                            <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                                                            <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                                                        </select>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                                                    </div>
                                                                    
                                                                    <label for="${child.externalId}tuitionInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}tuition" data-alternative="${child.externalId}">
                                                                         <div class="input-group">
                                                                            <input id="${child.externalId}tuitionInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                                                            <span class="input-group-addon">&euro;</span>
                                                                        </div>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <div class="col-md-4 col-md-offset-8">
                                                                        <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group map-table">
                                                                    <div class="col-md-10 col-md-offset-2">
                                                                        <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                                        
                                                        <div role="tabpanel" class="tab-pane" id="${child.externalId}insurance">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <h3><spring:message code="accounting.management.event.templates.label.config.insurance"/></h3>
                                                                </div>
                                                            </div>


                                                            <div class="form-group">
                                                                <label for="${child.externalId}insuranceFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}insuranceDueDateAmountMapGroup" id="${child.externalId}changeInsuranceDueDateAmountMap">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="date-map-group hidden" id="${child.externalId}insuranceDueDateAmountMapGroup" data-alternative="${child.externalId}" data-section="INSURANCE" data-map="dueDateAmountMap">
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}insuranceFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}insurance" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}insuranceFirstDueDate" class="first-date" bennu-date/>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                                                    </div>

                                                                    <label for="${child.externalId}insuranceNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}insurance" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}insuranceNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}insuranceIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}insurance" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}insuranceIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                                                        <select class="form-control interval-unit">
                                                                            <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                                                            <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                                                            <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                                                            <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                                                        </select>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                                                    </div>
                                                                    
                                                                    <label for="${child.externalId}insuranceInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}insurance" data-alternative="${child.externalId}">
                                                                         <div class="input-group">
                                                                            <input id="${child.externalId}insuranceInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                                                            <span class="input-group-addon">&euro;</span>
                                                                        </div>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <div class="col-md-4 col-md-offset-8">
                                                                        <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group map-table">
                                                                    <div class="col-md-10 col-md-offset-2">
                                                                        <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                                        
                                                        <div role="tabpanel" class="tab-pane" id="${child.externalId}adminFees">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <h3><spring:message code="accounting.management.event.templates.label.config.adminFees"/></h3>
                                                                </div>
                                                            </div>


                                                            <div class="form-group">
                                                                <label for="${child.externalId}adminFeesFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}adminFeesDueDateAmountMapGroup" id="${child.externalId}changeAdminFeesDueDateAmountMap">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="date-map-group hidden" id="${child.externalId}adminFeesDueDateAmountMapGroup" data-alternative="${child.externalId}" data-section="ADMIN_FEES" data-map="dueDateAmountMap">
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}adminFeesFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesFirstDueDate" class="first-date" bennu-date/>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                                                    </div>

                                                                    <label for="${child.externalId}adminFeesNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}adminFeesIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                                                        <select class="form-control interval-unit">
                                                                            <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                                                            <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                                                            <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                                                            <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                                                        </select>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                                                    </div>
                                                                    
                                                                    <label for="${child.externalId}adminFeesInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <div class="input-group">
                                                                            <input id="${child.externalId}adminFeesInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                                                            <span class="input-group-addon">&euro;</span>
                                                                        </div>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <div class="col-md-4 col-md-offset-8">
                                                                        <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group map-table">
                                                                    <div class="col-md-10 col-md-offset-2">
                                                                        <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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
                                                                <label for="${child.externalId}adminFeesPenaltyFirstDueDate" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.penaltyAmountMap"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}adminFeesPenaltyAmountMapGroup" id="${child.externalId}changeAdminFeesPenaltyAmountMap">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="date-map-group hidden" id="${child.externalId}adminFeesPenaltyAmountMapGroup" data-alternative="${child.externalId}" data-section="ADMIN_FEES" data-map="penaltyAmountMap">
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}adminFeesPenaltyFirstDueDate" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.firstDueDate"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesPenaltyFirstDueDate" class="first-date" bennu-date/>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.firstDueDate"/></span>
                                                                    </div>

                                                                    <label for="${child.externalId}adminFeesNumberOfInstallments" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.numberOfInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesNumberOfInstallments" class="form-control nr-installments" type="number" min="1" value="1">
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.numberOfInstallments"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <label for="${child.externalId}adminFeesPenaltyIntervalBetweenInstallments" class="control-label col-md-2 col-md-offset-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <input id="${child.externalId}adminFeesPenaltyIntervalBetweenInstallments" class="form-control interval-length" type="number" min="1" value="1">
                                                                        <select class="form-control interval-unit">
                                                                            <option value="d"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.days"/></option>
                                                                            <option value="w"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.weeks"/></option>
                                                                            <option value="M" selected><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.months"/></option>
                                                                            <option value="y"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.intervalBetweenInstallments.years"/></option>
                                                                        </select>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.intervalBetweenInstallments"/></span>
                                                                    </div>
                                                                    
                                                                    <label for="${child.externalId}adminFeesPenaltyInstallmentAmount" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap.amountPerInstallment"/></label>
                                                                    <div class="col-md-3 error-msg-container" data-tab="${child.externalId}adminFees" data-alternative="${child.externalId}">
                                                                         <div class="input-group">
                                                                            <input id="${child.externalId}adminFeesPenaltyInstallmentAmount" class="form-control amount" type="number" step="0.01" min="0" value="100">
                                                                            <span class="input-group-addon">&euro;</span>
                                                                        </div>
                                                                        <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.dueDateAmountMap.amountPerInstallment"/></span>
                                                                    </div>
                                                                </div>
                                                                
                                                                <div class="form-group">
                                                                    <div class="col-md-4 col-md-offset-8">
                                                                        <button type="button" class="btn btn-block btn-primary btn-add-to-map"><spring:message code="accounting.management.event.templates.action.add.installments"/></button>
                                                                    </div>
                                                                </div>

                                                                <div class="form-group map-table">
                                                                    <div class="col-md-10 col-md-offset-2">
                                                                        <p class="form-control-static map-table-empty"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
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

                                                        <div role="tabpanel" class="tab-pane" id="${child.externalId}additional">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <h3><spring:message code="accounting.management.event.templates.label.config.additionalData"/></h3>
                                                                </div>
                                                            </div>
                                                            

                                                            <div class="form-group">
                                                                <label for="${child.externalId}maxCredits" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.maxCredits"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}maxCreditsGroup" id="${child.externalId}changeMaxCredits">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="form-group hidden" id="${child.externalId}maxCreditsGroup">
                                                                <div class="col-md-10 col-md-offset-2 error-msg-container" data-tab="${child.externalId}additional" data-alternative="${child.externalId}">
                                                                    <input id="${child.externalId}maxCredits" class="form-control" type="number" step="1">
                                                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.maxCredits"/></span>
                                                                </div>
                                                            </div>

                                                            <div class="form-group">
                                                                <label for="${child.externalId}semester" class="control-label col-md-2"><spring:message code="accounting.management.event.templates.label.config.semester"/></label>
                                                                <div class="col-md-10">
                                                                    <div class="checkbox">
                                                                        <label class="control-label">
                                                                            <input type="checkbox" class="hidder" data-hides="#${child.externalId}semesterGroup" id="${child.externalId}changeSemester">
                                                                            <spring:message code="accounting.management.event.templates.action.edit.replica.field"/>
                                                                        </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="form-group hidden" id="${child.externalId}semesterGroup">
                                                                <div class="col-md-10 col-md-offset-2 error-msg-container" data-tab="${child.externalId}additional" data-alternative="${child.externalId}">
                                                                    <select class="form-control" id="${child.externalId}semester" name="config">
                                                                        <option value=""><spring:message code="accounting.management.event.templates.label.config.empty"/></option>
                                                                        <c:forEach items="${semesters}" var="semester">
                                                                            <option value="${semester.externalId}">${semester.qualifiedName}</option>
                                                                        </c:forEach>
                                                                    </select>
                                                                    <span class="help-block error-msg hidden"><spring:message code="accounting.management.event.templates.alert.validation.config.semester"/></span>
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div role="tabpanel" class="tab-pane" id="${child.externalId}raw">
                                                            <div class="row">
                                                                <div class="col-sm-12">
                                                                    <h3><spring:message code="accounting.management.event.templates.label.config.data.preview"/></h3>
                                                                </div>
                                                            </div>
                                                            <pre id="${child.externalId}dataPreview"></pre>
                                                            <div id="${child.externalId}dataError" class="alert alert-danger hidden" role="alert"><spring:message code="accounting.management.event.templates.alert.unableToGenerateDataPreview"/></div>
                                                        </div>
                                                    </div>
                                            
                                                </div>
                                                <div class="modal-footer">
                                                    <button type="button" class="btn btn-default" data-dismiss="modal">
                                                        <spring:message code="accounting.management.event.templates.action.apply"/>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <script type="text/javascript">
                                        $(document).ready(function() {
                                            alternativesDataHolder["${child.externalId}"] = {
                                                TUITION: {
                                                    dueDateAmountMap: {}
                                                },
                                                INSURANCE: {
                                                    dueDateAmountMap: {}
                                                },
                                                ADMIN_FEES: {
                                                    dueDateAmountMap: {},
                                                    penaltyAmountMap: {}
                                                }
                                            };
                                        });
                                    </script>
                                </div>
                            </c:forEach>
                        </div>
                    </c:if>
                </div>
            </div>
            
            <div class="row actions">
                <div class="col-sm-offset-4 col-sm-4 col-md-offset-8 col-md-2">
                    <button id="validateBtn" class="btn btn-block" type="button">
                        <spring:message code="accounting.management.event.templates.action.validate"/>
                    </button>
                </div>
                <div class="col-sm-4 col-md-2">
                    <button id="submitBtn" class="btn btn-block btn-primary" type="button">
                        <spring:message code="accounting.management.event.templates.action.create"/>
                    </button>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-offset-4 col-sm-8 col-md-offset-8 col-md-4">
                    <div id="validatedSuccess" class="alert alert-success hidden" role="alert">
                        <spring:message code="accounting.management.event.templates.alert.validation.success"/>
                    </div>
                    <div id="validatedError" class="alert alert-danger hidden" role="alert">
                        <spring:message code="accounting.management.event.templates.alert.validation.error"/>
                    </div>
                </div>
            </div>

        </form>

    </main>
</div>
