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
                    <h1><spring:message code="accounting.management.event.templates.title"/></h1>
                </div>
            </div>
        </header>

        <section>
            <div class="row">
                <div class="col-sm-8">
                    <h2>
                        <c:out value='${template.title.content}'/><br>
                        <small><c:out value='${template.code}'/></small>
                    </h2>
                    <p><c:out value='${template.description.content}'/></p>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${backUrl}"><spring:message code="accounting.management.event.templates.action.back"/></a>
                </div>
            </div>
        </section>
        
        <section>
            <div class="row">
                <div class="col-md-12">
                    <h2><spring:message code="accounting.management.event.templates.label.parents"/></h2>
                </div>
            </div>
            <c:choose>
                <c:when test="${parents.isEmpty()}">
                    <div class="alert alert-info" role="alert"><spring:message code="accounting.management.event.templates.alert.noParents"/></div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-stripped">
                            <thead>
                                <tr>
                                    <th class="col-sm-2 col-md-3"><spring:message code="accounting.management.event.templates.label.code"/></th>
                                    <th class="col-sm-4 col-md-5"><spring:message code="accounting.management.event.templates.label.title"/></th>
                                    <th class="col-sm-2 col-md-1"><spring:message code="accounting.management.event.templates.label.start"/></th>
                                    <th class="col-sm-2 col-md-1"><spring:message code="accounting.management.event.templates.label.end"/></th>
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
                                        <td><c:out value='${parent.code}'/></td>
                                        <td><c:out value='${parent.title.content}'/></td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.start.toString("yyyy-MM-dd")}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.end.toString("yyyy-MM-dd")}</td>
                                        <td class="text-right"><a class="btn btn-default" href="${detailsUrl}"><spring:message code="accounting.management.event.templates.action.edit"/></a></td>
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
                    <h2><spring:message code="accounting.management.event.templates.label.alternatives"/></h2>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-default" href="${createChildUrl}"><spring:message code="accounting.management.event.templates.action.create.alternative"/></a>
                </div>
            </div>
            <c:choose>
                <c:when test="${children.isEmpty()}">
                    <div class="alert alert-info" role="alert"><spring:message code="accounting.management.event.templates.alert.noAlternatives"/></div>
                </c:when>
                <c:otherwise>
                    <div class="table-responsive">
                        <table class="table table-stripped">
                            <thead>
                                <tr>
                                    <th class="col-sm-2 col-md-3"><spring:message code="accounting.management.event.templates.label.code"/></th>
                                    <th class="col-sm-4 col-md-5"><spring:message code="accounting.management.event.templates.label.title"/></th>
                                    <th class="col-sm-2 col-md-1"><spring:message code="accounting.management.event.templates.label.start"/></th>
                                    <th class="col-sm-2 col-md-1"><spring:message code="accounting.management.event.templates.label.end"/></th>
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
                                        <td><c:out value='${child.code}'/></td>
                                        <td><c:out value='${child.title.content}'/></td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.start.toString("yyyy-MM-dd")}</td>
                                        <td>${appliedInterval == null ? "N/A" : appliedInterval.end.toString("yyyy-MM-dd")}</td>
                                        <td class="text-right"><a class="btn btn-default" href="${detailsUrl}"><spring:message code="accounting.management.event.templates.action.edit"/></a></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </section>
        
        <section id="configs">
            <div class="row">
                <div class="col-sm-8">
                    <h2><spring:message code="accounting.management.event.templates.label.configs"/></h2>
                </div>
                <div class="col-sm-4 actions">
                    <a class="btn btn-block btn-primary" href="${createConfigUrl}"><spring:message code="accounting.management.event.templates.action.create.config"/></a>
                </div>
            </div>
            <c:choose>
                <c:when test="${configs.isEmpty()}">
                    <div class="alert alert-info" role="alert"><spring:message code="accounting.management.event.templates.alert.noConfigs"/></div>
                </c:when>
                <c:otherwise>
                    <form role="form" class="form-horizontal" action="#configs" method="GET">
                        ${csrf.field()}
                        <div class="form-group">
                            <label for="config" class="control-label col-sm-2"><spring:message code="accounting.management.event.templates.action.view.config"/>:</label>
                            <div class="col-sm-10">
                                <select class="form-control" id="selectConfig" name="config">
                                    <c:forEach items="${configs}" var="config">
                                        <option value="${config.externalId}" ${config.equals(selectedConfig) ? "selected" : ""}>
                                            ${config.applyFrom.toString("yyyy-MM-dd")}&nbsp;<spring:message code="accounting.management.event.templates.label.config.apply.to"/>&nbsp;${config.applyUntil.toString("yyyy-MM-dd")}
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </div>

                        <div>
                            <ul class="nav nav-pills" role="tablist">
                                <li role="presentation" class="active"><a href="#formatted" aria-controls="formatted" role="tab" data-toggle="tab"><spring:message code="accounting.management.event.templates.label.config.data.formatted"/></a></li>
                                <li role="presentation"><a href="#raw" aria-controls="raw" role="tab" data-toggle="tab"><spring:message code="accounting.management.event.templates.label.config.data.raw"/></a></li>
                            </ul>

                            <div class="tab-content">
                                <div role="tabpanel" class="tab-pane active" id="formatted">
                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3><spring:message code="accounting.management.event.templates.label.config.tuition"/></h3>
                                        </div>
                                    </div>
                                    <c:set var="tuitionData" value='${selectedConfigData.getAsJsonObject("TUITION")}'/>
                                    
                                    <c:if test='${tuitionData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                            <c:set var="tuitionDueDates" value='${tuitionData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${tuitionDueDates.isEmpty()}">
                                                        <p class="form-control-static"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${tuitionDueDates}" var="tuitionDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3"><c:out value='${tuitionDueDateEntry.key}'/></td>
                                                                            <td class="col-sm-9"><c:out value='${tuitionDueDateEntry.value.asString}'/>&euro;</td>
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
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.byEcts"/></label>
                                            <c:set var="tuitionByEcts" value='${tuitionData.getAsJsonObject("byECTS")}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${tuitionByEcts.size() == 0}">
                                                        <p class="form-control-static"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:if test='${tuitionByEcts.has("daysToPay")}'>
                                                                        <tr>
                                                                            <td class="col-sm-3"><spring:message code="accounting.management.event.templates.label.config.byEcts.daysToPay"/></td>
                                                                            <td class="col-sm-9"><c:out value='${tuitionByEcts.get("daysToPay").asString}'/></td>
                                                                        </tr>
                                                                    </c:if>
                                                                    <c:if test='${tuitionByEcts.has("value")}'>
                                                                        <tr>
                                                                            <td class="col-sm-3"><spring:message code="accounting.management.event.templates.label.config.byEcts.value"/></td>
                                                                            <td class="col-sm-9"><c:out value='${tuitionByEcts.get("value").asString}'/>&euro;</td>
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
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.product"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${tuitionData.has("productCode")}'>
                                                        [<c:out value='${tuitionData.get("productCode").asString}'/>]
                                                    </c:if>
                                                    <c:if test='${tuitionData.has("productDescription")}'>
                                                        <c:out value='${tuitionData.get("productDescription").asString}'/>
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${tuitionAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static"><c:out value='${tuitionAccount.party.name}'/> (<c:out value='${tuitionAccount.accountType.name}'/>)</p>
                                            </div>
                                        </div>
                                    </c:if>

                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3><spring:message code="accounting.management.event.templates.label.config.insurance"/></h3>
                                        </div>
                                    </div>
                                    <c:set var="insuranceData" value='${selectedConfigData.getAsJsonObject("INSURANCE")}'/>
                                    <c:if test='${insuranceData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                            <c:set var="insuranceDueDates" value='${insuranceData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${insuranceDueDates.isEmpty()}">
                                                        <p class="form-control-static"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${insuranceDueDates}" var="insuranceDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3"><c:out value='${insuranceDueDateEntry.key}'/></td>
                                                                            <td class="col-sm-9"><c:out value='${insuranceDueDateEntry.value.asString}'/>&euro;</td>
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
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.product"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${insuranceData.has("productCode")}'>
                                                        [<c:out value='${insuranceData.get("productCode").asString}'/>]
                                                    </c:if>
                                                    <c:if test='${insuranceData.has("productDescription")}'>
                                                        <c:out value='${insuranceData.get("productDescription").asString}'/>
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${insuranceAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static"><c:out value='${insuranceAccount.party.name}'/> (<c:out value='${insuranceAccount.accountType.name}'/>)</p>
                                            </div>
                                        </div>
                                    </c:if>
                                
                                    <div class="row">
                                        <div class="col-md-12">
                                            <h3><spring:message code="accounting.management.event.templates.label.config.adminFees"/></h3>
                                        </div>
                                    </div>
                                    <c:set var="adminFeesData" value='${selectedConfigData.getAsJsonObject("ADMIN_FEES")}'/>
                                    <c:if test='${adminFeesData.has("dueDateAmountMap")}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.dueDateAmountMap"/></label>
                                            <c:set var="adminFeesDueDates" value='${adminFeesData.getAsJsonObject("dueDateAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${adminFeesDueDates.isEmpty()}">
                                                        <p class="form-control-static"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${adminFeesDueDates}" var="adminFeesDueDateEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3"><c:out value='${adminFeesDueDateEntry.key}'/></td>
                                                                            <td class="col-sm-9"><c:out value='${adminFeesDueDateEntry.value.asString}'/>&euro;</td>
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
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.penaltyAmountMap"/></label>
                                            <c:set var="adminFeesPenalties" value='${adminFeesData.getAsJsonObject("penaltyAmountMap").entrySet()}'/>
                                            <div class="col-sm-10">
                                                <c:choose>
                                                    <c:when test="${adminFeesPenalties.isEmpty()}">
                                                        <p class="form-control-static"><spring:message code="accounting.management.event.templates.label.config.empty"/></p>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <div class="table-responsive">
                                                            <table class="table table-stripped">
                                                                <tbody>
                                                                    <c:forEach items="${adminFeesPenalties}" var="adminFeesPenaltyEntry">
                                                                        <tr>
                                                                            <td class="col-sm-3"><c:out value='${adminFeesPenaltyEntry.key}'/></td>
                                                                            <td class="col-sm-9"><c:out value='${adminFeesPenaltyEntry.value.asString}'/>&euro;</td>
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
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.product"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static">
                                                    <c:if test='${adminFeesData.has("productCode")}'>
                                                        [<c:out value='${adminFeesData.get("productCode").asString}'/>]
                                                    </c:if>
                                                    <c:if test='${adminFeesData.has("productDescription")}'>
                                                        <c:out value='${adminFeesData.get("productDescription").asString}'/>
                                                    </c:if>
                                                </p>
                                            </div>
                                        </div>
                                    </c:if>
                                    <c:if test='${adminFeesAccount != null}'>
                                        <div class="form-group">
                                            <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.account"/></label>
                                            <div class="col-sm-10">
                                                <p class="form-control-static"><c:out value='${adminFeesAccount.party.name}'/> (<c:out value='${adminFeesAccount.accountType.name}'/>)</p>
                                            </div>
                                        </div>
                                    </c:if>
                                
                                    <c:if test='${selectedConfigData.has("maxCredits") || semester != null}'>
                                        <div class="row">
                                            <div class="col-md-12">
                                                <h3><spring:message code="accounting.management.event.templates.label.config.additionalData"/></h3>
                                            </div>
                                        </div>
                                        <c:if test='${selectedConfigData.has("maxCredits")}'>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.maxCredits"/></label>
                                                <div class="col-sm-10">
                                                    <p class="form-control-static"><c:out value='${selectedConfigData.get("maxCredits").asString}'/></p>
                                                </div>
                                            </div>
                                        </c:if>
                                        <c:if test='${semester != null}'>
                                            <div class="form-group">
                                                <label class="col-sm-2 control-label"><spring:message code="accounting.management.event.templates.label.config.semester"/></label>
                                                <div class="col-sm-10">
                                                    <p class="form-control-static"><c:out value='${semester.qualifiedName}'/></p>
                                                </div>
                                            </div>
                                        </c:if>
                                    </c:if>
                                </div>

                                <div role="tabpanel" class="tab-pane" id="raw">
                                    <pre><c:out value='${selectedConfigDataRaw}'/></pre>
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
