<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="backUrl" value="/interest-management"/>

<spring:url var="actionUrl" value="/interest-management/${empty interestRate ? 'create' : interestRate.externalId}"/>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.interestRate"/>
        <small><spring:message code="label.create"/></small>
    </h2>
</div>

<section>
    <form:form role="form" method="POST" class="form-horizontal" action="${actionUrl}">
        ${csrf.field()}
        <div class="form-group">
            <label for="startDate" class="col-sm-1 control-label"><spring:message code="label.interestRate.start" /></label>
            <div class="col-sm-1">
                <input id="startDate" name="startDate" type="date" value='<c:out value="${interestRate.start}"/>' required/>
            </div>
        </div>
        <div class="form-group">
            <label for="endDate" class="col-sm-1 control-label"><spring:message code="label.interestRate.end" /></label>
            <div class="col-sm-1">
                <input id="endDate" name="endDate" type="date" value='<c:out value="${interestRate.end}"/>' required/>
            </div>
        </div>
        <div class="form-group">
            <label for="value" class="col-sm-1 control-label"><spring:message code="label.interestRate.value" /></label>
            <div class="col-sm-1">
                <input id="value" name="value" type="number" step="any" min="0" max="100" value='<c:out value="${interestRate.value}"/>' required/>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-push-1 col-sm-11">
                <a class="btn btn-default" href="${backUrl}"><spring:message code="label.cancel"/></a>
                <button type="submit" class="btn btn-primary"><spring:message code="label.save"/></button>
            </div>
        </div>
    </form:form>
</section>