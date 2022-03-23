<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

<spring:url var="createChildUrl" value="/event-template-management/{template}/create-alternative">
    <spring:param name="template" value="${template.externalId}"/>
</spring:url>
<spring:url var="createConfigUrl" value="/event-template-management/{template}/create-config">
    <spring:param name="template" value="${template.externalId}"/>
</spring:url>
<spring:url var="backUrl" value="/event-template-management/"></spring:url>


<style>
.actions {
    margin-top: 18px;
    margin-bottom: 9px;
}
</style>

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
                    <h2>
                        ${template.title.content}&ensp;
                        <small>${template.code}</small>
                    </h2>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}">Back</a>
                </div>
            </div>
            <div class="row">
                <div class="col-md-12">
                    <p>${template.description.content}</p>
                </div>
            </div>
        </section>
        
        <section>
            <div class="row">
                <div class="col-md-12">
                    <h2>Parent Event Templates</h2>
                </div>
            </div>
            <c:choose>
                <c:when test="${parents.isEmpty()}">
                    <div class="alert alert-info" role="alert">There are no parent event templates.</div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-stripped">
                            <thead>
                                <tr>
                                    <th class="col-sm-2 col-md-3">Code</th>
                                    <th class="col-sm-4 col-md-5">Title</th>
                                    <th class="col-sm-2 col-md-1">From</th>
                                    <th class="col-sm-2 col-md-1">Until</th>
                                    <th class="col-sm-2"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${parents}" var="parent">
                                    <spring:url var="detailsUrl" value="/event-template-management/{parent}">
                                        <spring:param name="parent" value="${parent.externalId}"/>
                                    </spring:url>
                                    <c:set var="appliedInterval" value="${parent.appliedInterval}"/>
                                    <tr>
                                        <td>${parent.code}</td>
                                        <td>${parent.title.content}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.start.toString("yyyy-MM-dd")}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.end.toString("yyyy-MM-dd")}</td>
                                        <td class="text-right"><a href="${detailsUrl}">Details</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>Alternative Event Templates</h2>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-primary" href="${createChildUrl}">Create Alternative</a>
                </div>
            </div>
            <c:choose>
                <c:when test="${children.isEmpty()}">
                    <div class="alert alert-info" role="alert">There are no alternative event templates.</div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-stripped">
                            <thead>
                                <tr>
                                    <th class="col-sm-2 col-md-3">Code</th>
                                    <th class="col-sm-4 col-md-5">Title</th>
                                    <th class="col-sm-2 col-md-1">From</th>
                                    <th class="col-sm-2 col-md-1">Until</th>
                                    <th class="col-sm-2"></th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach items="${children}" var="child">
                                    <spring:url var="detailsUrl" value="/event-template-management/{child}">
                                        <spring:param name="child" value="${child.externalId}"/>
                                    </spring:url>
                                    <c:set var="appliedInterval" value="${child.appliedInterval}"/>
                                    <tr>
                                        <td>${child.code}</td>
                                        <td>${child.title.content}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.start.toString("yyyy-MM-dd")}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.end.toString("yyyy-MM-dd")}</td>
                                        <td class="text-right"><a href="${detailsUrl}">Details</a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
        
        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>Configurations</h2>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-primary" href="${createConfigUrl}">Create Configuration</a>
                </div>
            </div>
            <c:choose>
                <c:when test="${configs.isEmpty()}">
                    <div class="alert alert-info" role="alert">There are no configurations.</div>
                </c:when>
                <c:otherwise>
                    <form role="form" class="form-horizontal" action="" method="POST">
                        ${csrf.field()}
                        <div class="form-group">
                            <label for="config" class="control-label col-sm-2">View Configuration:</label>
                            <div class="col-sm-10">
                                <select class="form-control" id="selectConfig" name="config">
                                    <c:forEach items="${configs}" var="config">
                                        <option value="${config.externalId}" ${config.equals(selectedConfig) ? "selected" : ""}>${config.applyFrom.toString("yyyy-MM-dd")} to ${config.applyUntil.toString("yyyy-MM-dd")}</option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div>
                            <ul class="nav nav-tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#formatted" aria-controls="formatted" role="tab" data-toggle="tab">Formatted Data</a></li>
                                <li role="presentation"><a href="#raw" aria-controls="raw" role="tab" data-toggle="tab">Raw Data</a></li>
                            </ul>

                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane active" id="formatted">
                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3>Tuition</h3>
                                        </div>
                                    </div>
                                    <c:set var="tuitionData" value='${selectedConfigData.getAsJsonObject("TUITION")}'/>
                                    
                                    <c:if test='${tuitionData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Due Dates and Amounts</label>
                                            <c:set var="tuitionDueDates" value='${tuitionData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${tuitionDueDates.isEmpty()}">
                                                        <p class="form-control-static">(Empty)</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${tuitionDueDates}" var="tuitionDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3">${tuitionDueDateEntry.key}</td>
                                                                            <td class="col-sm-9">${tuitionDueDateEntry.value.asString}&euro;</td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${tuitionData.has("byECTS")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">By ECTs</label>
                                            <c:set var="tuitionByEcts" value='${tuitionData.getAsJsonObject("byECTS")}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${tuitionByEcts.size() == 0}">
                                                        <p class="form-control-static">(Empty)</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:if test='${tuitionByEcts.has("daysToPay")}'>
                                                                        <tr>
                                                                            <td class="col-sm-3">Days to pay</td>
                                                                            <td class="col-sm-9">${tuitionByEcts.get("daysToPay").asString}</td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:if test='${tuitionByEcts.has("value")}'>
                                                                        <tr>
                                                                            <td class="col-sm-3">Value</td>
                                                                            <td class="col-sm-9">${tuitionByEcts.get("value").asString}&euro;</td>
                                                                        </tr>
                                                                    </c:if>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${tuitionData.has("productCode") || tuitionData.has("productDescription")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Product</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${tuitionData.has("productCode")}'>
                                                        [${tuitionData.get("productCode").asString}]
                                                    </c:if>
                                                    <c:if test='${tuitionData.has("productDescription")}'>
                                                        ${tuitionData.get("productDescription").asString}
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${tuitionAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Account</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">${tuitionAccount.party.name} (${tuitionAccount.accountType.name})</p>
                                            </div>
                                        </div>
                                    </c:if>

                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3>Insurance</h3>
                                        </div>
                                    </div>
                                    <c:set var="insuranceData" value='${selectedConfigData.getAsJsonObject("INSURANCE")}'/>
                                    <c:if test='${insuranceData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Due Dates and Amounts</label>
                                            <c:set var="insuranceDueDates" value='${insuranceData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${insuranceDueDates.isEmpty()}">
                                                        <p class="form-control-static">(Empty)</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${insuranceDueDates}" var="insuranceDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3">${insuranceDueDateEntry.key}</td>
                                                                            <td class="col-sm-9">${insuranceDueDateEntry.value.asString}&euro;</td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${insuranceData.has("productCode") || insuranceData.has("productDescription")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Product</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${insuranceData.has("productCode")}'>
                                                        [${insuranceData.get("productCode").asString}]
                                                    </c:if>
                                                    <c:if test='${insuranceData.has("productDescription")}'>
                                                        ${insuranceData.get("productDescription").asString}
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${insuranceAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Account</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">${insuranceAccount.party.name} (${insuranceAccount.accountType.name})</p>
                                            </div>
                                        </div>
                                    </c:if>
                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3>Administration Fees</h3>
                                        </div>
                                    </div>
                                    <c:set var="adminFeesData" value='${selectedConfigData.getAsJsonObject("ADMIN_FEES")}'/>
                                    <c:if test='${adminFeesData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Due Dates and Amounts</label>
                                            <c:set var="adminFeesDueDates" value='${adminFeesData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${adminFeesDueDates.isEmpty()}">
                                                        <p class="form-control-static">(Empty)</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${adminFeesDueDates}" var="adminFeesDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3">${adminFeesDueDateEntry.key}</td>
                                                                            <td class="col-sm-9">${adminFeesDueDateEntry.value.asString}&euro;</td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${adminFeesData.has("penaltyAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Penalty Amounts</label>
                                            <c:set var="adminFeesPenalties" value='${adminFeesData.getAsJsonObject("penaltyAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${adminFeesPenalties.isEmpty()}">
                                                        <p class="form-control-static">(Empty)</p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${adminFeesPenalties}" var="adminFeesPenaltyEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3">${adminFeesPenaltyEntry.key}</td>
                                                                            <td class="col-sm-9">${adminFeesPenaltyEntry.value.asString}&euro;</td>
                                                                        </tr>
                                                                    </c:forEach>
                                                                </tbody>
                                                            </table>
                                                        </div>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${adminFeesData.has("productCode") || adminFeesData.has("productDescription")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Product</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${adminFeesData.has("productCode")}'>
                                                        [${adminFeesData.get("productCode").asString}]
                                                    </c:if>
                                                    <c:if test='${adminFeesData.has("productDescription")}'>
                                                        ${adminFeesData.get("productDescription").asString}
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${adminFeesAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label">Account</label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">${adminFeesAccount.party.name} (${adminFeesAccount.accountType.name})</p>
                                            </div>
                                        </div>
                                    </c:if>
                                
                                    <c:if test='${selectedConfigData.has("maxCredits") || semester != null}'>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <h3>Additional Data</h3>
                                            </div>
                                        </div>
                                        <c:if test='${selectedConfigData.has("maxCredits")}'>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Maximum Credits</label>
                                                <div class="col-sm-10">
                                                    <p class="form-control-static">${selectedConfigData.get("maxCredits").asString}</p>
                                                </div>
                                            </div>
                                        </c:if>
                                        <c:if test='${semester != null}'>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label">Semester</label>
                                                <div class="col-sm-10">
                                                    <p class="form-control-static">${semester.qualifiedName}</p>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:if>
                                </div>

                                <div role="tabpanel" class="tab-pane" id="raw">
                                    <pre>${selectedConfigDataRaw}</pre>
                                </div>
                            </div>
                        </div>
                    </form>
                </c:otherwise>
            </c:choose>
        </section>

    </main>
</div>

<script type="text/javascript">
    $(document).ready(function() {
        $("#selectConfig").change(function() {
            $(this).parents("form").submit();
        })
    });
</script>
