<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<c:set var="base" value="${pageContext.request.contextPath}/teacher" />

<div class="row">
	<div class="col-lg-2">
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<strong>${executionCourse.prettyAcronym}</strong>
			</li>
			<li>
				<a href="${base}/manageExecutionCourse.do?method=instructions&executionCourseID=${executionCourse.externalId}">
					<bean:message key="label.instructions"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.personalization}">
				<li>
					<a href="${base}/alternativeSite.do?method=prepareCustomizationOptions&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.personalizationOptions"/>
					</a>
				</li>
			</c:if>
			<c:if test="${not empty executionCourse.site}">
				<li>
					<a href="${executionCourse.site.fullPath}" target="_blank">
						<bean:message key="link.executionCourseManagement.menu.view.course.page"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.siteArchive}">
				<li>
					<a href="${base}/generateArchive.do?method=prepare&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.executionCourse.archive.generate"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.summaries}">
				<li>
					<a href="${base}/searchECLog.do?method=prepareInit&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.executionCourse.log"/>
					</a>
				</li>
			</c:if>
		</ul>
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<strong><bean:message key="label.executionCourseManagement.menu.communication"/></strong>
			</li>
			<c:if test="${professorship.permissions.announcements}">
				<li>
					<a href="${base}/announcementManagement.do?method=viewAnnouncements&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.announcements"/>
					</a>
				</li>
			</c:if>
			<li>
				<a href="${base}/executionCourseForumManagement.do?method=viewForuns&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.teacher.executionCourseManagement.foruns"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.sections}">
				<li>
					<a href="${base}/manageExecutionCourseSite.do?method=sections&executionCourseID=${executionCourse.externalId}">
						<bean:message key="label.executionCourseManagement.menu.sections"/>
					</a>
				</li>
			</c:if>
		</ul>
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<strong><bean:message key="label.executionCourseManagement.menu.management"/></strong>
			</li>
			<c:if test="${professorship.permissions.summaries}">
				<li>
					<a href="${base}/summariesManagement.do?method=prepareShowSummaries&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.summaries"/>
					</a>
				</li>
			</c:if>
			<li>
				<a href="${base}/teachersManagerDA.do?method=viewTeachersByProfessorship&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.teachers"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.summaries}">
				<li>
					<a href="${base}/searchECAttends.do?method=prepare&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.students"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.planning}">
				<li>
					<a href="${base}/manageExecutionCourse.do?method=lessonPlannings&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.lessonPlannings"/>
					</a>
				</li>
			</c:if>
			<li>
				<a href="${base}/markSheetManagement.do?method=evaluationIndex&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.evaluation"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.worksheets}">
				<li>
					<a href="${base}/testsManagement.do?method=testsFirstPage&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.testsManagement"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.groups}">
				<li>
					<a href="${base}/studentGroupManagement.do?method=prepareViewExecutionCourseProjects&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.groupsManagement"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.shift}">
				<li>
					<a href="${base}/manageExecutionCourse.do?method=manageShifts&executionCourseID=${executionCourse.externalId}">
						<bean:message key="label.shifts"/>
					</a>
				</li>
			</c:if>
		</ul>
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<strong><bean:message key="label.executionCourseManagement.menu.curricularInfo"/></strong>
			</li>
			<li>
				<a href="${base}/manageObjectives.do?method=objectives&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.objectives"/>
				</a>
			</li>
			<li>
				<a href="${base}/manageProgram.do?method=program&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.program"/>
				</a>
			</li>
			<c:if test="${professorship.permissions.evaluationMethod}">
				<li>
					<a href="${base}/manageEvaluationMethod.do?method=evaluationMethod&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.evaluationMethod"/>
					</a>
				</li>
			</c:if>
			<c:if test="${professorship.permissions.bibliografy}">
				<li>
					<a href="${base}/manageBibliographicReference.do?method=bibliographicReference&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.bibliography"/>
					</a>
				</li>
			</c:if>
		</ul>
		<br />
		<ul class="nav nav-pills nav-stacked">
			<li>
				<strong><bean:message key="label.executionCourseManagement.menu.curricularUnitsQuality"/></strong>
			</li>
			<li>
				<a href="${base}/teachingInquiry.do?method=showInquiriesPrePage&executionCourseID=${executionCourse.externalId}">
					<bean:message key="link.teachingReportManagement"/>
				</a>
			</li>
			<c:if test="${professorship.responsibleFor}">
				<li>
					<a href="${base}/regentInquiry.do?method=showInquiriesPrePage&executionCourseID=${executionCourse.externalId}">
						<bean:message key="link.regentReportManagement"/>
					</a>
				</li>
			</c:if>
		</ul>
	</div>
	<div class="col-lg-10">
		<ol class="breadcrumb">
			<em>${executionCourse.name} - ${executionCourse.executionPeriod.qualifiedName}
				(<c:forEach var="degree" items="${executionCourse.degreesSortedByDegreeName}"> ${degree.sigla} </c:forEach>)
			</em>
		</ol>
		<jsp:include page="${teacher$actual$page}" />
	</div>
</div>
