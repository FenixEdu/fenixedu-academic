<%--

    Copyright © 2002 Instituto Superior Técnico

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



<spring:url value="../../{event}/deleteTransaction" var="deleteUrl">
	<spring:param name="event" value="${event.externalId}"/>
</spring:url>

<spring:url value="../../{event}/details" var="detailsUrl">
	<spring:param name="event" value="${event.externalId}"/>
</spring:url>

<header>
	<div class="row">
		<div class="col-sm-12">
			<p><a href="${detailsUrl}" class="btn btn-default"><spring:message code="label.back" text="Back"/></a></p>
			<c:if test="${not empty error}">
				<div class="alert alert-danger">
					<c:out value="${error}" />
				</div>
				<hr />
			</c:if>
			<h2><spring:message code="label.payments.annulTransaction"/></h2>
		</div>
	</div>
</header>

<form:form modelAttribute="annulAccountingTransactionBean" role="form" class="form-horizontal"
		   action="${deleteUrl}" method="post">
	${csrf.field()}
	<input type="hidden" id="transaction" name="transaction" value="${annulAccountingTransactionBean.transaction.externalId}"/>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean.whenRegistered"/></label>
		<div class="col-sm-8">
			<c:out value="${annulAccountingTransactionBean.whenRegistered}"/>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean.paymentMethod"/></label>
		<div class="col-sm-8">
			<c:out value="${annulAccountingTransactionBean.paymentMethod.localizedName}"/>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean.paymentReference" text="Referência de Pagamento"/></label>
		<div class="col-sm-8">
			<c:out value="${annulAccountingTransactionBean.paymentReference}"/>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean.amountWithAdjustment"/></label>
		<div class="col-sm-8">
			<c:out value="${annulAccountingTransactionBean.amountWithAdjustment}"/>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.org.fenixedu.academic.dto.accounting.AnnulAccountingTransactionBean.reason"/></label>
		<div class="col-sm-8">
			<textarea id="reason" name="reason" class="form-control" required></textarea>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-8">
			<button class="btn btn-primary" type="submit">
				<spring:message code="label.submit"/>
			</button>
            <a href="${detailsUrl}" class="btn btn-default"><spring:message code="label.cancel"/><a/>
		</div>
	</div>
</form:form>
