<%@ page import="org.fenixedu.academic.domain.accounting.ProofOfPayment" %>
<%@ page import="org.fenixedu.bennu.core.domain.Bennu" %>
<%@ page import="org.fenixedu.bennu.core.util.CoreConfiguration" %>
<%@ page import="org.fenixedu.bennu.io.servlet.FileDownloadServlet" %>
<%@ page import="org.fenixedu.academic.domain.accounting.AccountingTransaction" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%
    final String contextPath = request.getContextPath();
%>

<h2>
    <spring:message code="label.accounting.management.proof.of.payment.title" text="Proof of Payment"/>
</h2>

<div></div>

<table class="table">
    <tr>
        <th>
            <spring:message code="label.accounting.management.proof.of.payment.upload.date" text="Upload Date"/>
        </th>
        <th>
            <spring:message code="label.accounting.management.proof.of.payment.document" text="Document"/>
        </th>
        <th>
            <spring:message code="label.accounting.management.proof.of.payment.upload.date" text="Upload Date"/>
        </th>
        <th>
        </th>
        <th>
        </th>
    </tr>
    <% for (final ProofOfPayment proofOfPayment : Bennu.getInstance().getProofOfPaymentSet()) { %>
        <tr>
            <td>
                <%= proofOfPayment.getUploadDate().toString("yyyy-mm-dd HH:mm") %>
            </td>
            <td>
                <a href="<%= FileDownloadServlet.getDownloadUrl(proofOfPayment.getProofOfPaymentFile()) %>">
                    <spring:message code="label.accounting.management.proof.of.payment.document.view" text="View Document"/>
                </a>
            </td>
            <td>
                <a href="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/accounting-management/<%= proofOfPayment.getEvent().getExternalId() %>/details"
                   target="_blank">
                    <spring:message code="label.accounting.management.proof.of.payment.event" text="View Event"/>
                </a>
            </td>
            <td>
                <a href="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/accounting-management/proof-of-payment/<%= proofOfPayment.getExternalId() %>/markAsProcessed"
                   class="btn btn-primary">
                    <spring:message code="label.accounting.management.proof.of.payment.process" text="Mark As Processed"/>
                </a>
            </td>
            <td>
                <form method="post" enctype="multipart/form-data"
                      action="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/accounting-management/proof-of-payment/<%= proofOfPayment.getExternalId() %>/reject">
                    ${csrf.field()}
                    <button class="btn btn-danger" onclick="return confirm('Are you sure?');">
                        <spring:message code="label.accounting.management.proof.of.payment.process" text="Mark As Processed"/>
                    </button>
                </form>
            </td>
        </tr>
        <tr id="transactions<%= proofOfPayment.getExternalId() %>" style="display: none; background-color: oldlace;">
            <td colspan="5">
                <% if (proofOfPayment.getEvent().getAccountingTransactionsSet().isEmpty()) { %>
                    <spring:message code="label.accounting.management.proof.of.payment.process.no.transactions" text="No payments found"/>
                <% } else { %>
                    <table style="width: 100%;">
                        <tr>
                            <th>
                                <spring:message code="accounting.event.details.transactionDetail.paymentMethod" text="Payment Method"/>
                            </th>
                            <th>
                                <spring:message code="accounting.event.details.transactionDetail.paymentReference" text="Payment Reference"/>
                            </th>
                            <th>
                                <spring:message code="accounting.event.details.transactions.paymentDate" text="Payment Date"/>
                            </th>
                            <th>
                                <spring:message code="label.payments.payedAmount" text="Payed Amount"/>
                            </th>
                            <th>
                            </th>
                        </tr>
                        <% for (final AccountingTransaction accountingTransaction : proofOfPayment.getEvent().getAccountingTransactionsSet()) { %>
                            <tr>
                                <td>
                                    <%= accountingTransaction.getPaymentMethod().getLocalizedName() %>
                                </td>
                                <td>
                                    <%= accountingTransaction.getTransactionDetail().getPaymentReference() %>
                                </td>
                                <td>
                                    <%= accountingTransaction.getWhenRegistered().toString("yyyy-MM-dd") %>
                                </td>
                                <td>
                                    <%= accountingTransaction.getAmountWithAdjustment().toPlainString() %>
                                </td>
                                <td>
                                    <form method="post" enctype="multipart/form-data"
                                          action="<%= CoreConfiguration.getConfiguration().applicationUrl() %>/accounting-management/proof-of-payment/<%= proofOfPayment.getExternalId() %>/markAsProcessed/<%= accountingTransaction.getExternalId() %>">
                                        ${csrf.field()}
                                        <button class="btn btn-default">
                                            <spring:message code="label.accounting.management.proof.of.payment.process.transaction" text="Select"/>
                                        </button>
                                    </form>
                                </td>
                            </tr>
                        <% } %>
                    </table>
                <% } %>
            </td>
        </tr>
    <% } %>
</table>

<script type='text/javascript'>
    if (window.location.hash) {
        var hashId = window.location.hash.substring(1);
        document.getElementById(hashId).style.display = 'table-row';
    }
</script>
