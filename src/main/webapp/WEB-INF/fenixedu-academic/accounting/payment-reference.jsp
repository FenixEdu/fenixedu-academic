<%@ page import="org.fenixedu.commons.i18n.I18N" %>
<%@ page import="org.fenixedu.bennu.portal.domain.PortalConfiguration" %>
<%@ page import="org.fenixedu.academic.domain.accounting.Event" %>
<%@ page import="org.fenixedu.academic.domain.accounting.ProofOfPayment" %>
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
<%@ taglib uri="http://fenixedu.org/taglib/intersection" prefix="modular" %>
<%@ page trimDirectiveWhitespaces="true" %>

<%
    final String contextPath = request.getContextPath();
    final Event event = (Event) request.getAttribute("event");
%>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>
<spring:url var="backUrl" value="../details">
</spring:url>

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

        <div class="row">
            <div class="col-md-12">
                <c:set var="person" scope="request" value="${event.person}"/>
                <jsp:include page="heading-person.jsp"/>
            </div>
        </div>

        <div class="row">
            <div class="col-md-12">
                <br/>
                <modular:intersect location="event.payment.reference.extra.info" position="info">
                    <modular:arg key="event" value="${paymentCodeEntry.event}"/>
                </modular:intersect>
            </div>
        </div>

        <div class="row">
            <h2>
                <spring:message code="label.payment.methods" text="Payment Methods"/>
            </h2>
        </div>

        <c:if test="${not empty availableAdvancements}">
            <div class="row">
                <div class="col-md-12">
                    <div class="overall-description">
                        <spring:url value="owner-accounting-events" var="eventContextPrefix" scope="request"/>
                        <jsp:include page="event-depositAdvancment.jsp"/>
                    </div>
                </div>
            </div>
        </c:if>


        <div class="row">
            <div class="col-md-12">
                <div class="overall-description">
                    <h4><b><spring:message code="label.payment.method.sibs" text="Payment Via SIBS"/></b></h4>

                    <div class="alert alert-warning">
                        <c:choose>
                            <c:when test="${empty paymentCodeEntry.amount}">
                                <p style="white-space: pre-line;"><spring:message code="payment.reference.reusable.information" text="Reusable Payment Reference Information"/></p>
                            </c:when>
                            <c:otherwise>
                                <p style="white-space: pre-line;"><spring:message code="payment.reference.for.interests.information" text="Payment Reference for Interests Information" arguments="${maxDaysBetweenPromiseAndPayment}"/></p>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div>
                        <dl>
                            <dt><spring:message code="payment.reference.card.entity" text="Entity"/></dt>
                            <dd><c:out value="${paymentCodeEntry.paymentCode.entityCode}"/></dd>
                        </dl>
                        <dl>
                            <dt><spring:message code="payment.reference.card.reference" text="Reference"/></dt>
                            <dd><c:out value="${paymentCodeEntry.paymentCode.formattedCode}"/></dd>
                        </dl>
                        <c:if test="${not empty paymentCodeEntry.amount}">
                            <dl>
                                <dt><spring:message code="payment.reference.card.amount" text="Amount"/></dt>
                                <dd><c:out value="${paymentCodeEntry.amount}"/><span><spring:message code="label.euro" text="EUR"/></span></dd>
                            </dl>
                        </c:if>
                    </div>
                </div>
            </div>
        </div>

        <% if (event.allowBankTransfer()) { %>
        <div class="row">
            <div class="col-md-12">
                <div class="overall-description">
                    <h4><b><spring:message code="label.payment.method.bankTrasnfer" text="Payment Via Bank Transfer"/></b></h4>
                    <div class="alert alert-warning">
                        <p>
                            <spring:message code="label.payment.method.bankTrasnfer.info" text="Upload the proof of payment after transfering funds"/>
                        </p>
                    </div>
                    <ul>
                        <li>
                            <spring:message code="label.payment.method.bankTrasnfer.institution" text="Institution"/>
                            :
                            <%= PortalConfiguration.getInstance().getApplicationCopyright().getContent().substring(2) %>
                        </li>
                        <li>
                            <spring:message code="label.payment.method.bankTrasnfer.bank" text="Bank"/>
                            :
                            <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBank() %>
                        </li>
                        <li>
                            <spring:message code="label.payment.method.bankTrasnfer.iban" text="IBAN"/>
                            :
                            <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferIBAN() %>
                        </li>
                        <li>
                            <spring:message code="label.payment.method.bankTrasnfer.bic" text="BIC/SWIFT"/>
                            :
                            <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBIC() %>
                        </li>
                    </ul>

                    <h5 style="padding-top: 25px;"><b><spring:message code="label.payment.method.bankTrasnfer.proof.of.payment" text="Proof of Payment"/></b></h5>
                    <%
                        ProofOfPayment latestProofOfPayment = null;
                        for (final ProofOfPayment proofOfPayment : event.getProofOfPaymentSet()) {
                            if (latestProofOfPayment == null || latestProofOfPayment.getUploadDate().isBefore(proofOfPayment.getUploadDate())) {
                                latestProofOfPayment = proofOfPayment;
                            }
                        }
                    %>
                    <% if (latestProofOfPayment != null) { %>
                        <ul>
                            <li>
                                <%= latestProofOfPayment.getUploadDate().toString("yyyy-MM-dd HH:mm:ss") %>
                                -
                                <%= latestProofOfPayment.getProofOfPaymentFile().getDisplayName() %>
                                <% final String color = latestProofOfPayment.getBennu() != null ? "darkorange"
                                        : latestProofOfPayment.getAccountingTransactionDetail() == null ? "darkred"
                                        : "darkgreen"; %>
                                <span style="padding-left: 5px; color: <%= color %>">
                                <% if (latestProofOfPayment.getBennu() != null) { %>
                                    <spring:message code="label.payment.method.bankTrasnfer.proof.of.payment.pending.verification" text="Pending Verification"/>
                                <% } else if (latestProofOfPayment.getAccountingTransactionDetail() == null) { %>
                                    <spring:message code="label.payment.method.bankTrasnfer.proof.of.payment.rejected" text="Rejected"/>
                                <% } else { %>
                                    <spring:message code="label.payment.method.bankTrasnfer.proof.of.payment.accepted" text="Accepted"/>
                                <% } %>
                                </span>
                            </li>
                        </ul>
                    <% } %>

                    <p style="padding-top: 25px;">
                    <form method="post" id="form" action="<%= contextPath %>/owner-accounting-events/<%= event.getExternalId() %>/proofOfPayment" enctype="multipart/form-data">
                        ${csrf.field()}

                        <div class="form-group">
                            <input id="proofOfPayment" name="proofOfPayment" type="file"
                                   accept="image/png, image/jpeg, image/jpg, application/pdf"/>
                        </div>
                        <button class="btn btn--primary">
                            <spring:message code="label.payment.method.bankTrasnfer.proof.of.payment.upload" text="Upload Proof of Payment"/>
                        </button>
                    </form>
                    </p>
                </div>
            </diV>
        </diV>
        <% } %>

        <div class="row">
            <modular:intersect location="event.payment.reference.extra.info" position="paymentMethods">
                <modular:arg key="event" value="${paymentCodeEntry.event}"/>
            </modular:intersect>
        </div>

    </main>
</div>