<%--

    Copyright © 2017 Instituto Superior Técnico

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
        <div class="row">
            <div class="col-md-10">
                <p><a href="${detailsUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
            </div>
        </div>
        <div class="row">
            <div class="col-md-5 col-sm-12">
                <h3><c:out value="${event.description}"/></h3>
                <div class="overall-description">
                    <dl>
                        <dt><spring:message code="label.name" text="Name"/></dt>
                        <dd><c:out value="${person.presentationName}"/></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="label.document.id.type" text="ID Document Type"/></dt>
                        <dd><c:out value="${person.idDocumentType.localizedName}"/></dd>
                    </dl>
                    <dl>
                        <dt><spring:message code="label.document.id" text="ID Document"/></dt>
                        <dd><c:out value="${person.documentIdNumber}"/></dd>
                    </dl>
                </div>
            </div>
        </div>
        <div class="row">
            <c:if test="${not empty error}">
                <section>
                    <ul class="nobullet list6">
                        <li><span class="error0"><c:out value="${error}"/></span></li>
                    </ul>
                </section>
            </c:if>
        </div>
    </header>

    <div class="row">
        <h3>Depositar Valor</h3>
        <form:form modelAttribute="depositAmountBean" role="form" class="form-horizontal" action="${depositUrl}" method="post">
            ${csrf.field()}
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.entryType"/></label>
                <div class="col-sm-4">
                    <select class="form-control" name="entryType" required>
                        <c:forEach items="${event.possibleEntryTypesForDeposit}" var="entryType">
                            <option value="${entryType}">${fr:message('resources.EnumerationResources', entryType.name)}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.whenRegistered"/></label>
                <div class="col-sm-4">
                    <input hidden name="whenRegistered" value="${depositAmountBean.whenRegistered}" bennu-datetime requires-past required>
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
