<%@ page import="org.fenixedu.academic.domain.accounting.Event" %>
<%@ page import="org.fenixedu.bennu.FenixSpringConfiguration" %><%--

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

<div class="container-fluid">
    <main>
        <header>
            <div class="row">
                <div class="col-md-12">
                    <h1>
                        <jsp:include page="heading-event.jsp"/>
                    </h1>
                </div>
            </div>
        </header>
        <c:set var="person" scope="request" value="${event.person}"/>
        <jsp:include page="heading-person.jsp"/>
        <c:if test="${not empty error}">
            <div class="row">
                <br/>
                <div class="col-md-8 col-sm-12">
                    <div class="alert alert-danger">
                        <span><c:out value="${error}"/></span>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="row">
            <h2>
                <spring:message code="accounting.event.allocate.iban" text="Pay via SEPA Transfer"/>
            </h2>
        </div>
        <% final Event event = (Event) request.getAttribute("event"); %>
        <% if (event.getIBAN() == null) { %>
            <div class="row">
                <div class="col-md-8 col-sm-12">
                    <div class="alert alert-warning">
                        <spring:message code="accounting.event.allocate.iban.not.available" text="Payment via SEPA Transfer is not available."/>
                    </div>
                </div>
            </div>
        <% } else { %>
            <div class="row">
                <div class="alert alert-warning">
                    <spring:message code="accounting.event.allocate.iban.only.for.this.event" text="This IBAN is only for this event."/>
                </div>
                <ul>
                    <li>
                        <spring:message code="accounting.event.allocate.iban.bank" text="Bank"/>
                        :
                        <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBank() %>
                    </li>
                    <li>
                        <spring:message code="accounting.event.allocate.iban.bic" text="BIC"/>
                        :
                        <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBIC() %>
                    </li>
                    <li>
                        <spring:message code="accounting.event.allocate.iban.number" text="IBAN"/>
                        :
                        <%= event.getIBAN().getIBANNumber() %>
                    </li>
                </ul>
            </div>
        <% } %>
    </main>
</div>