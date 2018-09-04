<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="createUrl" value="/interest-management/create"/>
<spring:url var="baseUrl" value="/interest-management/"/>
<spring:url var="logsUrl" value="/interest-management/logs"/>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.interestRate"/>
    </h2>
</div>

<div class="btn-group">
        <a class="btn btn-default" href="${createUrl}"><spring:message code="label.create"/></a>
        <a class="btn btn-default" href="${logsUrl}"><spring:message code="label.show.logs"/></a>
</div>

<table class="table results">
    <thead>
    <th><spring:message code="label.interestRate.start" /></th>
    <th><spring:message code="label.interestRate.end" /></th>
    <th><spring:message code="label.interestRate.value"/></th>
    </thead>
    <tbody>
    <c:forEach var="interestRate" items="${interestRates}">
        <tr>
            <td>
                <c:out value="${interestRate.start}"/>
            </td>
            <td>
                <c:out value="${interestRate.end}"/>
            </td>
            <td>
                <c:out value="${interestRate.value} %"/>
            </td>
            <td>
                <a href="${baseUrl}${interestRate.externalId}" class="btn btn-default"><spring:message code="label.edit"/></a>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>