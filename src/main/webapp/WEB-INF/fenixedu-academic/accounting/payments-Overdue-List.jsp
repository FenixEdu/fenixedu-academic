<%--

    Copyright Â© 2020 Instituto Superior TÃ©cnico

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

<%@ page import="org.fenixedu.academic.domain.Person"%>
<%@ page import="org.joda.time.LocalDate"%>
<%@ page import="org.fenixedu.academic.domain.accounting.Event" %>

<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers"
	prefix="fr"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

${portal.toolkit()}

<header>
	<div class="row">
		<div class="col-sm-12">
			<h2>
				<spring:message
					code="label.accounting.management.overdue.payments.title" />
			</h2>
		</div>
	</div>
</header>

<c:if test="${not empty message}">
	<div class="row">
		<div class="col-sm-12">
			<div class="alert alert-danger">
				<c:out value="${message}" />
			</div>
		</div>
	</div>
</c:if>

<spring:url value="/accounting-management/overdue-payments/generate"
	var="reportUrl" />

	<div class="table-responsive">
		<table class="table table-stripped">
			<thead>
				<tr>
					<th class="col-sm-2 col-md-3"><spring:message
							code="label.username" /></th>
					<th class="col-sm-4 col-md-5"><spring:message
							code="label.name"/></th>
					<th class="col-sm-2 col-md-1"><spring:message
							code="label.accounting.management.report.start" /></th>
					<th class="col-sm-2 col-md-1"><spring:message
							code="label.paymentMethods.description" /></th>					
					<th class="col-sm-2 col-md-1"><spring:message
							code="accounting.event.details.total" /></th>					
					<th class="col-sm-2 col-md-1"><spring:message
							code="accounting.event.details.debt.to.pay" /></th>
					<th class="col-sm-2"></th>
				</tr>
			</thead>
			<tbody>
				<c:forEach  items="${bean.eventList}" var="e">
					<spring:url var="detailsUrl"
						value="/accounting-management/overdue-payments/details">
						<spring:param name="eventId"
							value="${e.externalId}" />
					</spring:url>

					<td><c:out value="${e.person.user.username}" /></td>
					<td><c:out value="${e.person.presentationName}" /></td>
					<td><c:out value="${e.eventStartDate}" /></td>
					<td><c:out value="${e.eventType}" /></td>
					<td><c:out value="" /></td>
					<td class="text-right"><a class="btn btn-primary"
						href="${detailsUrl}"><spring:message
								code="label.details" /></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>


