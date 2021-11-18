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

${portal.toolkit()}

<spring:url var="backUrl" value="/payment-methods-management"/>
<spring:url var="actionUrl" value="/payment-methods-management/${empty paymentMethod ? 'create' : paymentMethod.externalId}"/>

<style>
    .form-control.bennu-localized-string-input {
        width: 190px;
    }
</style>

<div class="page-header">
    <h2>
        <spring:message code="title.manage.paymentMethods"/>
        <small><spring:message code="label.create"/></small>
    </h2>
</div>

<section>

    <form:form role="form" method="POST" class="form-horizontal" action="${actionUrl}">
        ${csrf.field()}
        <div class="form-group">
            <label for="code" class="col-sm-1 control-label"><spring:message code="label.paymentMethods.code" /></label>
            <div class="col-sm-1">
                <input id="code" name="code" type="text" value='<c:out value="${paymentMethod.code}"/>' autocomplete="off" required/>
            </div>
        </div>
        <div class="form-group">
            <label for="description" class="col-sm-1 control-label"><spring:message code="label.paymentMethods.description" /></label>
            <div class="col-sm-1">
                <input id="description" name="description" type="text" value='<c:out value="${paymentMethod.description.json()}"/>' bennu-localized-string required/>
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-1 control-label"><spring:message code="label.paymentMethods.allowManualUse" /></label>
            <div class="col-sm-1">
                <c:set var="checkedTrue" value=""/>
                <c:set var="checkedFalse" value=""/>
                <c:if var="checkedTrue" test="${paymentMethod.allowManualUse}">
                    <c:set var="checkedTrue" value="checked"/>
                </c:if>
                <c:if var="checkedFalse" test="${!paymentMethod.allowManualUse}">
                    <c:set var="checkedFalse" value="checked"/>
                </c:if>
                <div class="form-group">
                    <div>
                        <input id="allowManualUseFalse" class="col-sm-5" name="allowManualUse" type="radio" value="false" required ${checkedFalse}/>
                        <input id="allowManualUseTrue" class="col-sm-5" name="allowManualUse" type="radio" value="true" required  ${checkedTrue}/>
                    </div>
                    <div>
                        <label for="allowManualUseFalse" class="col-sm-5 control-label"><spring:message code="label.false" /></label>
                        <label for="allowManualUseTrue" class="col-sm-5 control-label"><spring:message code="label.yes" /></label>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-group">
            <label for="paymentReferenceFormat" class="col-sm-1 control-label"><spring:message code="label.paymentMethods.paymentReferenceFormat" /></label>
            <div class="col-sm-1">
                <input id="paymentReferenceFormat" name="paymentReferenceFormat" type="text" value='<c:out value="${paymentMethod.paymentReferenceFormat}"/>' placeholder="CC %YYYY%MM%DD" />
            </div>
            <br /><br />
            <div class="col-sm-10">
                <span><strong><spring:message code="label.info.paymentMethods.paymentReferenceFormat" /></strong></span>
            </div>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-1">
                <a class="btn btn-default" href="${backUrl}"><spring:message code="label.cancel"/></a>
                <button type="submit" id="submitButton" class="btn btn-primary"><spring:message code="label.save"/></button>
            </div>
        </div>
    </form:form>
</section>