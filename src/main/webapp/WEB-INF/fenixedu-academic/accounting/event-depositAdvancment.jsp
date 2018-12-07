<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:if test="${not empty availableAdvancements} }">
<h3><spring:message code="label.events.available.advancements" text="Available Advancements"/></h3>

<div class="alert alert-warning">
    <p style="white-space: pre-line;"><spring:message code="payment.advancement.values.available" text="Advancement Values Are Available"/></p>
</div>

<table class="table">
    <thead>
        <tr>
            <th style="width: 100px;"><spring:message code="accounting.event.details.creation.date" text="Creation Date"/></th>
            <th style="width: 70%;"><spring:message code="label.description" text="Description"/></th>
            <th><spring:message code="label.event.advancement" text="Advancement Value"/></th>
            <th><span class="sr-only"><spring:message code="label.actions" text="Actions"/></span></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="availableAdvancement" items="${availableAdvancements}">
            <tr>
                <td style="width: 100px;">
                    <time datetime="${availableAdvancement.key.whenOccured.toString('yyyy-MM-dd')}">${availableAdvancement.key.whenOccured.toString('dd/MM/yyyy')}</time>
                </td>
                <td>
                    <spring:url var="eventDetailsUrl" value="../{event}/details">
                        <spring:param name="event" value="${availableAdvancement.key.externalId}"/>
                    </spring:url>

                    <a href="${eventDetailsUrl}"><c:out value="${availableAdvancement.key.description}"/></a>
                </td>
                <td><c:out value="${availableAdvancement.value}"/></td>
                <td>
                    <spring:url var="depositAdvancementUrl" value="../{event}/depositAdvancement">
                        <spring:param name="event" value="${event.externalId}"/>

                    </spring:url>

                    <form:form role="form" class="form-horizontal" action="${depositAdvancementUrl}" method="post">
                        ${csrf.field()}
                        <input hidden name="eventToRefund" value="${availableAdvancement.key.externalId}"/>
                        <button class="btn btn-primary" type="submit">
                            <spring:message code="label.use.value" text="Use Value"/>
                        </button>
                    </form:form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
</c:if>
