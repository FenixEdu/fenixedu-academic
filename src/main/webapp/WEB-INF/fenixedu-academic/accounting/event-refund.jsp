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
                    <dd>${totalUnusedAmount}</dd>
                </dl>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12">
            <c:if test="${totalUnusedAmount > 0}">
                <form method="post" action="<%= request.getContextPath() %>/accounting-management/${event.externalId}/refundEvent" style="display: inline;">
                    ${csrf.field()}
                    <button type="submit" class="btn btn-default"><spring:message code="label.create.refund"/></button>
                </form>
                <form method="post" action="<%= request.getContextPath() %>/accounting-management/${event.externalId}/refundExcessPayment" style="display: inline;">
                    ${csrf.field()}
                    <button type="submit" class="btn btn-primary"><spring:message code="label.create.excess.refund"/></button>
                </form>
            </c:if>
            <c:if test="${not (totalUnusedAmount > 0)}">
                <form method="post" action="<%= request.getContextPath() %>/accounting-management/${event.externalId}/refundEvent" style="display: inline;">
                    ${csrf.field()}
                    <button type="submit" class="btn btn-primary"><spring:message code="label.create.refund"/></button>
                </form>
            </c:if>
        </div>
    </div>

</div>
