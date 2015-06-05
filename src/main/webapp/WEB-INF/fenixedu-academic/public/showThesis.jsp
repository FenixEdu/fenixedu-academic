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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>

	<head>
		<title><%=org.fenixedu.academic.domain.organizationalStructure.Unit.getInstitutionName()%></title>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

		<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/bennu-core/css/bootstrap.min.css">
	</head>

	<body>
		<main class="container">
			<div class="row">
				<div class="col-sm-6">
					<h2>${fr:message('resources.ApplicationResources', 'title.coordinator.thesis.confirm')}</h2>
				</div>
				<div class="col-sm-6 text-right">
					<h2>
						<a href="<%= org.fenixedu.academic.domain.Installation.getInstance().getInstituitionURL() %>" target="_blank">
							<img src="${pageContext.request.contextPath}/api/bennu-portal/configuration/logo" />
						</a>
					</h2>
				</div>
			</div>
			<c:if test="${thesis != null}">
			<h4>${fr:message('resources.ApplicationResources', 'title.thesis.details.details')}</h4>
			<div class="main-table">
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'student')}</label>
					<div class="col-sm-10">
						<c:out value="${thesis.student.person.name} (${thesis.student.person.username})"/>
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.thesis.state')}</label>
					<div class="col-sm-10">
						${fr:message('resources.EnumerationResources', 'ThesisState.'.concat(thesis.state))}
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.thesis.fullTitle')}</label>
					<div class="col-sm-10">
						<strong><c:out value="${thesis.finalFullTitle.content}" /></strong>
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.thesis.abstract')}</label>
					<div class="col-sm-10" id="abstract">
						<c:out value="${thesis.thesisAbstract.content}" />
					</div>
				</div>
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.thesis.keywords')}</label>
					<div class="col-sm-10">
						<c:out value="${thesis.keywords}" />
					</div>
				</div>
			</div>

			<h4>${fr:message('resources.ApplicationResources', 'title.thesis.details.discussion')}</h4>
			<div class="main-table">
				<div class="row">
					<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.coordinator.thesis.discussed')}</label>
					<div class="col-sm-10">
						${thesis.currentDiscussedDate.toString('dd-MM-yyyy HH:mm')}
					</div>
				</div>
			</div>

			<h4>${fr:message('resources.ApplicationResources', 'title.thesis.details.orientation')}</h4>
			<c:if test="${not empty thesis.orientation}">
				<c:forEach var="advisor" items="${thesis.orientation}">
				   	<h5>${fr:message('resources.ApplicationResources', 'title.public.thesis.section.orientation.orientator')}</h5>
					<div class="main-table">
						<div class="row">
							<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.person.name')}</label>
							<div class="col-sm-10">
								<c:out value="${advisor.person.name}" />
							</div>
						</div>
						<div class="row">
							<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.teacher.category')}</label>
							<div class="col-sm-10">
								<c:out value="${advisor.category}" />
							</div>
						</div>
						<div class="row">
							<label class="col-sm-2 text-right">${fr:message('resources.ApplicationResources', 'label.coordinator.thesis.edit.teacher.department')}</label>
							<div class="col-sm-10">
								<c:out value="${advisor.affiliation}" />
							</div>
						</div>
					</div>
				</c:forEach>
			</c:if>

			<h4>${fr:message('resources.ApplicationResources', 'title.thesis.details.publication')}</h4>
			<c:if test="${thesis.evaluated}">
				<c:out value="${thesis.finalTitle}, ${thesis.student.name}, ${thesis.discussed.year}"/>
				<p>
					(<a href="${thesis.extendedAbstract.downloadUrl}">
						<img src="${pageContext.request.contextPath}/images/icon_pdf.gif"/>
						${fr:message('resources.ResearcherResources', 'link.dissertation.download.extendedAbstract')}
						${thesis.extendedAbstract.prettyFileSize}
					</a>)

					(<a href="${thesis.dissertation.downloadUrl}">
						<img src="${pageContext.request.contextPath}/images/icon_pdf.gif"/>
						${fr:message('resources.ResearcherResources', 'link.dissertation.download.thesis')}
						${thesis.dissertation.prettyFileSize}
					</a>)						
				</p>
				<p>
					<em>${fr:message('resources.ResearcherResources', 'label.publication.subject.to.copyright')}</em>
				</p>
			</c:if>

			<c:if test="${not thesis.evaluated}">
				<em>${fr:message('resources.ApplicationResources', 'message.thesis.publication.notAvailable')}</em>
			</c:if>

			</c:if>
			<c:if test="${thesis == null}">
				<h4 class="text-center">${fr:message('resources.ApplicationResources', 'error.not.found')}</h4>
			</c:if>
		</main>
	</body>

	<style>
		body {
			color: #454545;
			background: #e8e8e8;
			font: 12px/21px 'Lato', sans-serif;
			font-weight: 400;
		}
		main {
			margin-top: 40px !important;
			margin-bottom: 50px;
			background-color: white;
			margin-top: 10px;
			padding: 10px;
			margin-left: auto;
			margin-right: auto;
			border-radius: 7px;
		}
		.main-table > .row {
			margin-left: 0;
			margin-right: 0;
			padding-bottom: 2px;
			padding-top: 7px;
			border-bottom: 1px #eee solid;
			border-top: 1px #eee solid;
		}
		.main-table > .row ~ .row {
			border-top: none;
		}
	</style>
	<script>
	var element = document.querySelector('#abstract');
	element.innerHTML = element.innerHTML.trim().replace(/\n/g, '<br />');
	</script>
</html>