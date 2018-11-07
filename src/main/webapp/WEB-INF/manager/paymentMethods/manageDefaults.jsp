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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:url var="backUrl" value="/payment-methods-management"/>
<spring:url var="actionUrl" value="/payment-methods-management/manageDefaults"/>

<style>
    label {
        width: 140px !important;
        text-align: left !important;
    }

    selected, option {
        width: 200px;
    }
</style>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.paymentMethods"/>
        <small><spring:message code="label.create"/></small>
    </h2>
</div>

<div class="btn-group">
    <a class="btn btn-default" href="${backUrl}"><spring:message code="label.back"/></a>
</div>

<section>
    <c:choose>
        <c:when test="${fn:length(paymentMethods) >= 2}">
            <form:form role="form" method="POST" class="form-horizontal" action="${actionUrl}">
                ${csrf.field()}
                <div class="form-group">
                    <label for="defaultCashPaymentMethod" class="col-sm-1 control-label"><spring:message code="label.paymentMethods.defaultCash" /></label>
                    <div class="col-sm-1">
                        <select name="defaultCashPaymentMethod" id="defaultCashPaymentMethod">
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option value="${paymentMethod.externalId}" <c:if test="${paymentMethod.cash}">selected="selected"</c:if> >${paymentMethod.code} - ${paymentMethod.localizedName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <label for="defaultSibsPaymentMethod" class="col-sm-1 control-label"><spring:message code="label.paymentMethods.defaultSibs" /></label>
                    <div class="col-sm-1">
                        <select name="defaultSibsPaymentMethod" id="defaultSibsPaymentMethod">
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option value="${paymentMethod.externalId}" <c:if test="${paymentMethod.sibs}">selected="selected"</c:if> >${paymentMethod.code} - ${paymentMethod.localizedName}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-1">
                        <a class="btn btn-default" href="${backUrl}"><spring:message code="label.cancel"/></a>
                        <button type="submit" id="submitButton" class="btn btn-primary"><spring:message code="label.save"/></button>
                    </div>
                </div>
            </form:form>
        </c:when>
        <c:otherwise>
            <p><spring:message code="label.paymentMethods.manageDefaults.notEnoughPaymentMethods" /></p>
        </c:otherwise>
    </c:choose>
</section>