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
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<style>
.hide {
	display: none;
}
</style>

<c:set var="base" value="${pageContext.request.contextPath}/teacher" />
<c:set var="req" value="${pageContext.request}" />
<div class="row">
	<main class="col-sm-10 col-sm-push-2">
		<ol class="breadcrumb">
			<em><c:out value="${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}" />
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> <c:out value="${degree.sigla}" /> </c:forEach>)
			</em>
		</ol>
		<jsp:include page="${teacher$actual$page}" />
	</main>
	<nav class="col-sm-2 col-sm-pull-10" id="context" style="background-color: #f1f1f1; padding-bottom: 20px">
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong><c:out value="${executionCourse.prettyAcronym}" /></strong>
			</li>
			<c:if test="${not empty executionCourse.siteUrl}">
				<li>
				    <!-- NO_CHECKSUM --><a href="${executionCourse.siteUrl}" target="_blank">
						${fr:message('resources.ApplicationResources', 'link.executionCourseManagement.menu.view.course.page')}
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.summaries}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/searchECLog.do?method=prepareInit&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.executionCourse.log')}
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.communication')}</strong>
			</li>
			<c:if test="${professorship.permissions.personalization}">
				<li>
					<!-- NO_CHECKSUM --><a href="${base}/${executionCourse.externalId}/communication">
					${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.communication')}
				</a>
				</li>
			</c:if>
			<c:if test="${not empty executionCourse.site}">
				<c:if test="${professorship.permissions.announcements}">
					<li>
					<!-- NO_CHECKSUM --><a href="${base}/${executionCourse.externalId}/announcements">
							${fr:message('resources.ApplicationResources', 'link.announcements')}
						</a>
					</li>
				</c:if>
				<c:if test="${professorship.permissions.sections}">
					<li>
						<!-- NO_CHECKSUM --><a href="${base}/${executionCourse.externalId}/pages">
							${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.sections')}
						</a>
					</li>
				</c:if>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/executionCourseForumManagement.do?method=viewForuns&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.teacher.executionCourseManagement.foruns')}
				</a>
			</li>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.management')}</strong>
			</li>
			<c:if test="${professorship.permissions.summaries}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/summariesManagement.do?method=prepareShowSummaries&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.summaries')}
					</a>
				</li>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/teachersManagerDA.do?method=viewTeachersByProfessorship&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.teachers')}
				</a>
			</li>
			<c:if test="${professorship.permissions.students}">
				<li>
                <a href="${base}/${executionCourse.externalId}/attends/show">
						${fr:message('resources.ApplicationResources', 'link.students')}
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.planning}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageExecutionCourse.do?method=lessonPlannings&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.lessonPlannings')}
					</a>
				</li>
			</c:if>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/markSheetManagement.do?method=evaluationIndex&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.evaluation')}
				</a>
			</li>
			<c:if test="${professorship.permissions.groups}">
				<li>
                <a href="${base}/${executionCourse.externalId}/student-groups/show">
						${fr:message('resources.ApplicationResources', 'link.groupsManagement')}
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.shift}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageExecutionCourse.do?method=manageShifts&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'label.shifts')}
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.curricularInfo')}</strong>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageObjectives.do?method=objectives&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.objectives')}
				</a>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageProgram.do?method=program&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.program')}
				</a>
			</li>
			<c:if test="${professorship.permissions.evaluationMethod}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageEvaluationMethod.do?method=evaluationMethod&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.evaluationMethod')}
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.bibliografy}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/manageBibliographicReference.do?method=bibliographicReference&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.bibliography')}
					</a>
				</li>
			</c:if>
		</ul>
		<ul class="nav nav-pills nav-stacked">
			<li class="navheader">
				<strong>${fr:message('resources.ApplicationResources', 'label.executionCourseManagement.menu.curricularUnitsQuality')}</strong>
			</li>
			<li>
			<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/teachingInquiry.do?method=showInquiriesPrePage&executionCourseID='.concat(executionCourse.externalId))}">
					${fr:message('resources.ApplicationResources', 'link.teachingReportManagement')}
				</a>
			</li>
			<c:if test="${professorship.responsibleFor}">
				<li>
				<!-- NO_CHECKSUM --><a href="${fr:checksumLink(req, '/teacher/regentInquiry.do?method=showInquiriesPrePage&executionCourseID='.concat(executionCourse.externalId))}">
						${fr:message('resources.ApplicationResources', 'link.regentReportManagement')}
					</a>
				</li>
			</c:if>
		</ul>
	</nav>
</div>


<script src="${pageContext.request.contextPath}/javaScript/latinize.min.js" defer></script>
<script src="${pageContext.request.contextPath}/javaScript/jquery.lazyload.min.js" defer></script>
<script src="${pageContext.request.contextPath}/teacher/executionCourse/scripts.js" defer></script>

