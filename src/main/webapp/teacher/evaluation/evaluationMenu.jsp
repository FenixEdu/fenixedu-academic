<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenixedu.org/taglib/jsf-portal" prefix="fp"%>

<c:set var="base" value="${pageContext.request.contextPath}/teacher" />
<c:set var="professorship" value="${executionCourse.professorshipForCurrentUser}" />

<fp:select actionClass="net.sourceforge.fenixedu.presentationTier.Action.teacher.ManageExecutionCourseDA" />

<div class="row">
	<nav class="col-lg-2" id="context">
		<ul class="nav nav-pills nav-stacked">
			<li>
				<a href="${base}/manageExecutionCourse.do?method=instructions&executionCourseID=${executionCourse.externalId}">
					<bean:message key="label.back"/>
				</a>
			</li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><bean:message key="link.evaluation"/></strong>
			</li>
			<c:if test="${professorship.permissions.evaluationSpecific}">
				<li>
					<a href="${base}/evaluation/adHocEvaluationIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.adHocEvaluations"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.evaluationWorksheets}">
				<li>
					<a href="${base}/evaluation/onlineTestsIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.onlineTests"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.evaluationProject}">
				<li>
					<a href="${base}/evaluation/projectsIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.projects"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.evaluationTests}">
				<li>
					<a href="${base}/evaluation/writtenTestsIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.writtenTests"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.evaluationExams}">
				<li>
					<a href="${base}/evaluation/examsIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.exams"/>
					</a>
				</li>
			</c:if>

			<c:if test="${professorship.permissions.evaluationFinal}">
				<li>
					<a href="${base}/evaluation/finalEvaluationIndex.faces?executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.finalEvaluation"/>
					</a>
				</li>
			</c:if>
		</ul>
	</nav>
	<main class="col-lg-10">
		<ol class="breadcrumb">
			<em>${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> ${degree.sigla} </c:forEach>)
			</em>
		</ol>