<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="baseUrl" value="/registrationProtocols/"/>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.registrationProtocols"/>
        <small><spring:message code="label.history" /></small>
    </h2>
</div>

<div class="btn-group">
    <a class="btn btn-default" href="${baseUrl}"><spring:message code="label.back"/></a>
</div>

<table class="table results">
    <thead>
    <th><spring:message code="label.log.datetime" /></th>
    <th><spring:message code="label.log.person" /></th>
    <th><spring:message code="label.log.description" /></th>
    </thead>
    <tbody>
    <c:forEach var="registrationProtocolLog" items="${registrationProtocolLogs}">
        <tr>
            <td>
                <c:out value="${registrationProtocolLog.whenDateTime}"/>
            </td>
            <td>
                <c:out value="${registrationProtocolLog.person.profile.displayName}"/>
            </td>
            <td>
                <c:out value="${registrationProtocolLog.description}"/>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>