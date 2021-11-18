<%--

    Copyright © 2018 Instituto Superior Técnico

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
<!DOCTYPE html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<spring:url var="baseUrl" value="/payment-methods-management/"/>
<spring:url var="createUrl" value="/payment-methods-management/create"/>
<spring:url var="manageDefaultsUrl" value="/payment-methods-management/manageDefaults"/>
<spring:url var="logsUrl" value="/payment-methods-management/logs"/>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.paymentMethods"/>
    </h2>
</div>

<div class="btn-group">
    <a class="btn btn-default" href="${createUrl}"><spring:message code="label.create"/></a>
    <a class="btn btn-default" href="${manageDefaultsUrl}"><spring:message code="label.paymentMethods.manageDefaults"/></a>
    <a class="btn btn-default" href="${logsUrl}"><spring:message code="label.show.logs"/></a>
</div>

<table class="table results">
    <thead>
        <th><spring:message code="label.paymentMethods.code" /></th>
        <th><spring:message code="label.paymentMethods.description" /></th>
        <th><spring:message code="label.paymentMethods.allowManualUse" /></th>
        <th><spring:message code="label.paymentMethods.paymentReferenceFormat" /></th>
    </thead>
    <tbody>
        ${csrf.field()}
        <c:forEach var="paymentMethod" items="${paymentMethods}">
            <tr>
                <td>
                    <c:out value="${paymentMethod.code}"/>
                </td>
                <td>
                    <c:out value="${paymentMethod.localizedName}"/>
                </td>
                <td>
                    <c:out value="${paymentMethod.allowManualUse}"/>
                </td>
                <td>
                    <c:out value="${paymentMethod.paymentReferenceFormat}"/>
                </td>
                <td>
                    <a href="${baseUrl}${paymentMethod.externalId}" class="btn btn-default"><spring:message code="label.edit"/></a>
                </td>
                <td>
                    <a href="${baseUrl}${paymentMethod.externalId}" class="btn btn-danger deletePaymentMethod"><spring:message code="label.delete"/></a>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<script type="text/javascript">
    $(".deletePaymentMethod").click(function(e) {
        e.preventDefault();
        const paymentMethod = $(this).closest("tr");
        const url = $(this).attr("href");
        $.ajax({
            url: url,
            type: "DELETE",
            headers: {"X-CSRF-TOKEN": $("input[name='_csrf']").val()},
            success: function(data) {
                paymentMethod.remove();
            },
            error: function(data) {
                alert(data);
            }
        });
    });
</script>