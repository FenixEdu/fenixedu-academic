<%--

    Copyright © 2020 Instituto Superior Técnico

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

${portal.toolkit()}

<header>
	<div class="row">
		<div class="col-sm-12">
			<h2><spring:message code="label.accounting.management.report.title"/></h2>
		</div>
	</div>
</header>

<c:if test="${not empty message}">
	<div class="row">
		<div class="col-sm-12">
			<div class="alert alert-danger">
				<c:out value="${message}"/>
			</div>
		</div>
	</div>
</c:if>

<spring:url value="/accounting-management/payment-report/transaction-report-list/generate" var="reportUrl"/>
<form role="form" class="form-horizontal" action="${reportUrl}" method="GET">
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.accounting.management.report.start"/></label>
		<div class="col-sm-8">
			<input type="text" name="start" bennu-date>
		</div>
	</div>
	<div class="form-group">
		<label class="control-label col-sm-2"><spring:message code="label.accounting.management.report.end"/></label>
		<div class="col-sm-8">
			<input type="text" name="end" bennu-date>
		</div>
	</div>
	<div class="form-group">
		<div class="col-sm-offset-2 col-sm-8">
			<button class="btn btn-primary" type="submit">
				<spring:message code="label.submit"/>
			</button>
		</div>
	</div>
</form>
