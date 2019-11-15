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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%@ include file="update-payment-reference.jsp" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

${portal.toolkit()}

<spring:url value="../{event}/depositAmount" var="depositUrl">
    <spring:param name="event" value="${event.externalId}"/>
</spring:url>
<spring:url value="../{event}/details" var="detailsUrl">
    <spring:param name="event" value="${event.externalId}"/>
</spring:url>

<div class="container-fluid">
    <header>
        <h1>
            <jsp:include page="heading-event.jsp"/>
        </h1>
    </header>
        <div class="row">
            <c:if test="${not empty error}">
                <section>
                    <ul class="nobullet list6">
                        <li><span class="error0"><c:out value="${error}"/></span></li>
                    </ul>
                </section>
            </c:if>
        </div>
    <c:set var="person" scope="request" value="${event.person}"/>
    <jsp:include page="heading-person.jsp"/>

    <div class="row">
<%--
        <spring:url value="accounting-management" var="eventContextPrefix" scope="request"/>
        <jsp:include page="event-depositAdvancment.jsp"/>
--%>

        <h3><spring:message code="label.event.deposit" text="Register Deposit"/></h3>
        <c:if test="${not event.totalAmountToPay.positive or event.cancelled}">
            <div class="alert alert-danger" role="alert">
                <c:if test="${not event.totalAmountToPay.positive}">
                    <spring:message code="warning.event.overflow.deposit" text="There is no debt amount."/>
                </c:if>
                <c:if test="${event.cancelled}">
                    <spring:message code="warning.event.cancelled.overflow.deposit" text="The event is cancelled."/>
                </c:if>
            </div>
        </c:if>
        <form:form modelAttribute="depositAmountBean" role="form" class="form-horizontal" action="${depositUrl}" method="post">
            ${csrf.field()}
            <input hidden name="entryType" value="${event.entryType}"/>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.whenRegistered"/></label>
                <div class="col-sm-4">
                    <input id="whenRegistered" name="whenRegistered" value="${depositAmountBean.whenRegistered}" bennu-datetime required>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentMethod"/></label>
                <div class="col-sm-4">
                    <form:select path="paymentMethod" class="form-control" required="true">
                        <form:option value="" selected="true" disabled="true" hidden="true"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentMethod.placeholder"/></form:option>
                        <form:options items="${paymentMethods}" itemLabel="localizedName" itemValue="externalId" />
                    </form:select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.paymentReference"/></label>
                <div class="col-sm-4">
                    <input id="paymentReference" name="paymentReference" type="text" />
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.amount"/></label>
                <div class="col-sm-4">
                    <input name="amount" type="text" min="0.01" pattern="[0-9]+([\.][0-9]{0,2})?" placeholder="ex: xxxx.yy" required><span> €</span>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.reason"/></label>
                <div class="col-sm-4">
                    <textarea name="reason" class="form-control" rows="4" required></textarea>
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-1 col-sm-4">
                    <button class="btn btn-primary" type="submit">
                        <spring:message code="label.submit"/>
                    </button>
                    <a href="${detailsUrl}" class="btn btn-default"><spring:message code="label.cancel"/><a/>
                </div>
            </div>
        </form:form>
    </div>
</div>

<script type="text/javascript">

    $(function () {
        !$("#paymentMethod").val() ? $("#paymentReference").val("") : updatePaymentReference($("input[name='whenRegistered']").val());
    });

    $("#whenRegistered, #paymentMethod").change(function () {
        !$("#paymentMethod").val() ? $("#paymentReference").val("") : updatePaymentReference($("input[name='whenRegistered']").val());
    });

</script>
