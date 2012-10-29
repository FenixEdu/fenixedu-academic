<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

<ul>
	<%-- 
	<li class="navheader"><bean:message key="label.transition" bundle="STUDENT_RESOURCES"/></li>
	<li><html:link page="/bolonhaTransitionManagement.do?method=prepare" titleKey="label.bolonha"><bean:message key="label.bolonha"/></html:link></li>
	--%>
	<li class="navheader"><bean:message key="consult"/></li>
  	<li><html:link page="/viewCurriculum.do?method=prepare" titleKey="link.title.curriculum"><bean:message key="link.student.curriculum"/></html:link></li>
	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.phdIndividualProgramProcesses">
		<li><html:link page="/phdIndividualProgramProcess.do?method=viewProcess"><bean:message key="label.phds" bundle="PHD_RESOURCES"/></html:link></li>
	</logic:notEmpty>
	<li><html:link page="/studentTimeTable.do?method=prepare" titleKey="link.title.timetable"><bean:message key="link.my.timetable"/></html:link></li>
	<li><html:link page="/ShowStudentStatutes.do?method=execute" titleKey="link.title.statutes"><bean:message key="label.student.statutes"/></html:link></li>
	<ul>
	<li>
		<html:link page="/ICalTimeTable.do?method=prepare" bundle="MESSAGING_RESOURCES" titleKey="label.title.sync">
			<bean:message bundle="MESSAGING_RESOURCES" key="label.title.sync"/>
		</html:link>
	</li>
	</ul>
	</li>
	<li><html:link page="/studentCalendar.faces" titleKey="link.title.calendar"><bean:message key="link.title.calendar"/></html:link></li>
	<li><html:link page="/viewTutorInfo.do?method=prepare" titleKey="link.title.tutorInfo"><bean:message key="link.student.tutorInfo"/></html:link></li>
	<li><html:link page="/administrativeOfficeServicesSection.do" titleKey="administrative.office.services"><bean:message key="administrative.office.services"/></html:link></li>
	<li><html:link page="/studentDataShareAuthorization.do?method=manageAuthorizations" titleKey="title.student.dataShareAuthorizations"><bean:message key="title.student.dataShareAuthorizations.short"/></html:link></li>
	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.residencePaymentEvents">
		<li><html:link page="/viewResidencePayments.do?method=listEvents" titleKey="link.title.residencePayments"><bean:message key="link.title.residencePayments"/></html:link></li>
	</logic:notEmpty>
	<li><html:link page="/delegatesInfo.do?method=prepare" titleKey="link.title.delegatesInfo"><bean:message key="link.student.delegatesInfo"/></html:link></li>

	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.student">
    	<logic:notEmpty name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.student.executionCourseAudits">
			<li class="navheader"><bean:message key="link.inquiry.audit" bundle="INQUIRIES_RESOURCES"/></li>
			<li>
				<html:link page="/qucAudit.do?method=showAuditProcesses" titleKey="link.inquiry.auditProcesses" bundle="INQUIRIES_RESOURCES">
					<bean:message key="link.inquiry.auditProcesses" bundle="INQUIRIES_RESOURCES"/>
				</html:link>
			</li>
		</logic:notEmpty>
	</logic:notEmpty>
	
	<li class="navheader"><bean:message key="participate"/></li>
	<li><html:link page="/viewExecutionCourseForuns.do?method=prepare" titleKey="link.viewExecutionCourseForuns"><bean:message key="link.viewExecutionCourseForuns"/></html:link></li>
	<li><html:link page="/weeklyWorkLoad.do?method=prepare" titleKey="link.weekly.work.load"><bean:message key="link.weekly.work.load"/></html:link></li>
	<li><html:link page="/studentInquiry.do?method=showCoursesToAnswer" titleKey="link.title.inquiry.students.courses"><bean:message key="link.inquiries" bundle="INQUIRIES_RESOURCES"/></html:link></li>
	<li><html:link page="/yearDelegateManagement.do?method=prepare" titleKey="link.title.yearDelegateElections"><bean:message key="link.student.yearDelegateElections"/></html:link></li>
	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.student.eligibleForCareerWorkshopApplication" value="true">
		<li><html:link page="/careerWorkshopApplication.do?method=prepare" titleKey="link.title.careerWorkshop"><bean:message key="link.student.careerWorkshop"/></html:link></li>
	</logic:equal>
<%--	<li><html:link page="/studentCycleInquiry.do?method=prepare" titleKey="link.title.inquiry.students.firstTimeCycle"><bean:message key="link.inquiries.firstTimeCycle" bundle="INQUIRIES_RESOURCES"/></html:link></li>	
--%>	
	<li class="navheader"><bean:message key="submit"/></li>
	<li><html:link page="/studentTests.do?method=viewStudentExecutionCoursesWithTests" titleKey="link.testsSubmissions"><bean:message key="link.tests"/></html:link></li>
	<li><html:link page="/projectSubmission.do?method=viewProjectsWithOnlineSubmission" titleKey="link.projectSubmissions"><bean:message key="projects"/></html:link></li>
<%-- 	<li><html:link page="/tests/tests.do?method=viewTests" titleKey="title.tests.view" bundle="TESTS_RESOURCES"><bean:message key="link.tests.view" bundle="TESTS_RESOURCES"/></html:link></li>

	<li><html:link page="/studentGaugingTestResults.do" titleKey="link.title.results.test"><bean:message key="link.results.test"/></html:link></li>
	<li><html:link page="/thesisSubmission.do?method=prepareThesisSubmission" titleKey="title.student.thesis.submission"><bean:message key="label.student.thesis.submission"/></html:link></li>
--%>

	<li><html:link page="/thesisSubmission.do?method=listThesis" titleKey="title.student.thesis.submission"><bean:message key="label.student.thesis.submission"/></html:link></li>

	<li class="navheader"><bean:message key="enroll"/></li>
	<li><html:link page="/studentEnrollmentManagement.do?method=prepare" titleKey="link.title.student.enrollment"><bean:message key="link.student.enrollment"/></html:link></li>
	<li><html:link page="/enrollment/evaluations/specialSeason.do?method=entryPoint" titleKey="link.title.specialSeason.enrolment"><bean:message key="link.specialSeason.enrolment"/></html:link></li>
	<li><html:link page="/studentShiftEnrollmentManager.do?method=prepare" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link></li>
	<li><html:link page="/viewEnroledExecutionCourses.do?method=prepare" titleKey="link.title.groupEnrolment"><bean:message key="link.groupEnrolment" /></html:link></li>	
	<li><html:link page="/enrollment/evaluations/showEvaluations.faces" titleKey="link.title.evaluations.enrolment" ><bean:message key="link.evaluations.enrolment"/></html:link>
		<ul>
			<li><html:link page="/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1" titleKey="link.title.exams.enrolment" ><bean:message key="link.exams.enrolment"/></html:link></li>
			<li><html:link page="/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2" titleKey="link.title.writtenTests.enrolment" ><bean:message key="link.writtenTests.enrolment"/></html:link></li>
		</ul>
	</li>
	<li><html:link page="/listAllSeminaries.do" titleKey="link.title.seminaries.enrolment" ><bean:message key="link.seminaries.enrolment"/></html:link>
		<ul>
			<li><a href="<bean:message key="link.seminaries.rules"/>" title="<bean:message key="link.title.seminaries.rules"/>" target="_blank"><bean:message key="label.seminairies.seeRules"/></a></li>
		</ul>
	</li>
	<li class="navheader"><bean:message key="link.student.seniorTitle"/></li>
	<li><html:link page="/seniorInformation.do?method=prepare&amp;page=0" ><bean:message key="link.senior.info"/></html:link></li>			  	
	<li><html:link page="/finalDegreeWorkCandidacy.do?method=dissertations&amp;page=0"><bean:message key="link.student.finalWorkTitle"/></html:link>
		<ul>
			<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
			<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
			<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
		</ul>
	</li>

	<li class="navheader"><bean:message key="label.information.export" bundle="STUDENT_RESOURCES"/></li>
	<li><html:link page="/managePasswords.do?method=managePasswords" titleKey="label.information.export.manage.passwords"><bean:message key="label.information.export.manage.passwords"/></html:link></li>
</ul>