<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page trimDirectiveWhitespaces="true" %>

<link rel="stylesheet" type="text/css" media="screen" href="<%= request.getContextPath() %>/CSS/accounting.css"/>

${portal.toolkit()}

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
    <jsp:include page="heading-person.jsp"/>

    <div class="row">
        <div class="col-md-4">
            <h2><spring:message code="label.org.fenixedu.academic.dto.accounting.CreateRefund" text="Create Refund"/></h2>
            <div class="overall-description">
                <dl>
                    <dt><spring:message code="accounting.event.details.payedDebtAmount" text="Total Payed Debt"/></dt>
                    <dd>${payedDebtAmount}</dd>
                </dl>
                <dl>
                    <dt><spring:message code="accounting.event.details.excess.payment" text="Excess Payment"/></dt>
                    <dd>${paidUnusedAmount}</dd>
                </dl>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
               <form method="post" class="form-horizontal" action="<%= request.getContextPath() %>/accounting-management/${event.externalId}/refundEvent" style="display: inline;">
                    ${csrf.field()}
                    <c:if test="${not (paidUnusedAmount > 0)}">
                        <div class="form-group">
                            <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.DepositAmountBean.amount"/></label>
                            <div class="col-sm-4">
                                <input name="amount" type="text" pattern="[0-9]+([\.][0-9]{0,2})?" required value="${payedDebtAmount}"><span> €</span>
                            </div>
                        </div>
                    </c:if>
                    <div class="form-group">
                        <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.CreateExemptionBean.justificationType"/></label>
                        <div class="col-sm-4">
                            <select class="form-control" name="justificationType" required>
                                <option value=""><spring:message code="label.org.fenixedu.academic.dto.accounting.CreateExemptionBean.justificationType.placeholder"/></option>
                                <c:forEach items="${eventExemptionJustificationTypes}" var="eventExemptionJustificationType">
                                    <option class="justificationTypeOption" value="${eventExemptionJustificationType}">${fr:message('resources.EnumerationResources', eventExemptionJustificationType.qualifiedName)}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.bankAccountNumber"/></label>
                        <div class="col-sm-4">
                            <input name="bankAccountNumber" class="form-control" required></input>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.CreateExemptionBean.reason"/></label>
                        <div class="col-sm-4">
                            <textarea name="reason" class="form-control" rows="4" required></textarea>
                        </div>
                    </div>
                    <c:if test="${not (paidUnusedAmount > 0)}">
                        <button type="submit" class="btn btn-primary"><spring:message code="label.create.refund"/></button>
                    </c:if>
                </form>
                <c:if test="${paidUnusedAmount > 0}">
                    <form method="post" action="<%= request.getContextPath() %>/accounting-management/${event.externalId}/refundExcessPayment" style="display: inline;">
                        ${csrf.field()}
                        <div class="form-group">
                            <label class="control-label col-sm-1"><spring:message code="label.org.fenixedu.academic.dto.accounting.bankAccountNumber"/></label>
                            <div class="col-sm-4">
                               <input name="bankAccountNumber" class="form-control" required></input>
                            </div>
                        </div>
                        <button type="submit" class="btn btn-primary"><spring:message code="label.create.excess.refund"/></button>
                    </form>
                </c:if>
        </div>                           
    </div>

</div>
