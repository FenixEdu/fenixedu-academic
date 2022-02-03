<%@ page import="org.fenixedu.academic.domain.accounting.Event" %>
<%@ page import="org.fenixedu.bennu.portal.domain.PortalConfiguration" %>
<%@ page import="org.fenixedu.academic.domain.accounting.ProofOfPayment" %>
<%@ page import="org.fenixedu.bennu.FenixSpringConfiguration" %>
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
                <spring:message code="label.payment.method.bankTransfer" text="Pay via Non SEPA Transfer"/>
            </h2>
        </div>
        <%
            final Event event = (Event) request.getAttribute("event");
            final String contextPath = request.getContextPath();
        %>
        <div class="row">
            <div class="alert alert-warning">
                <p>
                    <spring:message code="label.payment.method.bankTransfer.info" text="Upload the proof of payment after transfering funds"/>
                </p>
            </div>
            <ul>
                <li>
                    <spring:message code="label.payment.method.bankTransfer.institution" text="Institution"/>
                    :
                    <%= PortalConfiguration.getInstance().getApplicationCopyright().getContent().substring(2) %>
                </li>
                <li>
                    <spring:message code="label.payment.method.bankTransfer.bank" text="Bank"/>
                    :
                    <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBank() %>
                </li>
                <li>
                    <spring:message code="label.payment.method.bankTransfer.iban" text="IBAN"/>
                    :
                    <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferIBAN() %>
                </li>
                <li>
                    <spring:message code="label.payment.method.bankTransfer.bic" text="BIC/SWIFT"/>
                    :
                    <%= FenixSpringConfiguration.getConfiguration().paymentMethodBankTransferBIC() %>
                </li>
            </ul>

            <h5 style="padding-top: 25px;"><b><spring:message code="label.payment.method.bankTransfer.proof.of.payment" text="Proof of Payment"/></b></h5>
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
                                <spring:message code="label.payment.method.bankTransfer.proof.of.payment.pending.verification" text="Pending Verification"/>
                            <% } else if (latestProofOfPayment.getAccountingTransactionDetail() == null) { %>
                                <spring:message code="label.payment.method.bankTransfer.proof.of.payment.rejected" text="Rejected"/>
                            <% } else { %>
                                <spring:message code="label.payment.method.bankTransfer.proof.of.payment.accepted" text="Accepted"/>
                            <% } %>
                            </span>
                </li>
            </ul>
            <% } %>

            <p style="padding-top: 10px;">
                <form method="post" id="form" action="<%= contextPath %>/owner-accounting-events/<%= event.getExternalId() %>/proofOfPayment" enctype="multipart/form-data">
                    ${csrf.field()}

                    <div class="form-group">
                        <input id="proofOfPayment" name="proofOfPayment" type="file"
                               accept="image/png, image/jpeg, image/jpg, application/pdf"/>
                    </div>
                    <button class="btn btn--primary">
                        <spring:message code="label.payment.method.bankTransfer.proof.of.payment.upload" text="Upload Proof of Payment"/>
                    </button>
                </form>
            </p>

        </div>
    </main>
</div>