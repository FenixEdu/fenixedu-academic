<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
	<li class="navheader"><bean:message key="consult"/></li>
  	<li><html:link page="/viewCurriculum.do?method=prepare" titleKey="link.title.curriculum"><bean:message key="link.student.curriculum"/></html:link></li>
	<li><html:link page="/studentTimeTable.do?method=prepare" titleKey="link.title.timetable"><bean:message key="link.my.timetable"/></html:link></li>
	<li><html:link page="/studentCalendar.faces" titleKey="link.title.calendar"><bean:message key="link.title.calendar"/></html:link></li>
	<li><html:link page="/administrativeOfficeServicesSection.do" titleKey="administrative.office.services"><bean:message key="administrative.office.services"/></html:link></li>

	<li class="navheader"><bean:message key="participate"/></li>
	<li><html:link page="/viewExecutionCourseForuns.do?method=prepare" titleKey="link.viewExecutionCourseForuns"><bean:message key="link.viewExecutionCourseForuns"/></html:link></li>
	<li><html:link page="/weeklyWorkLoad.do?method=prepare" titleKey="link.weekly.work.load"><bean:message key="link.weekly.work.load"/></html:link></li>
	<li><html:link page="/fillInquiries.do?method=prepareCourses&amp;page=0" titleKey="link.title.inquiry.students.courses"><bean:message key="link.inquiries" bundle="INQUIRIES_RESOURCES"/></html:link></li>

	<li class="navheader"><bean:message key="submit"/></li>
	<li><html:link page="/studentTests.do?method=viewStudentExecutionCoursesWithTests" titleKey="link.testsSubmissions"><bean:message key="link.tests"/></html:link></li>
	<li><html:link page="/projectSubmission.do?method=viewProjectsWithOnlineSubmission" titleKey="link.projectSubmissions"><bean:message key="projects"/></html:link></li>
<%-- 	<li><html:link page="/tests/tests.do?method=viewTests" titleKey="title.tests.view" bundle="TESTS_RESOURCES"><bean:message key="link.tests.view" bundle="TESTS_RESOURCES"/></html:link></li>

	<li><html:link page="/studentGaugingTestResults.do" titleKey="link.title.results.test"><bean:message key="link.results.test"/></html:link></li>
--%>
	
	<li class="navheader"><bean:message key="enroll"/></li>
	<li><html:link page="/warningFirst.do" titleKey="link.title.student.enrollment"><bean:message key="link.student.enrollment"/></html:link></li>
	<li><html:link page="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link></li>
	<li><html:link page="/viewEnroledExecutionCourses.do" titleKey="link.title.groupEnrolment"><bean:message key="link.groupEnrolment" /></html:link></li>	
	<li><html:link page="/enrollment/evaluations/showEvaluations.faces" titleKey="link.title.evaluations.enrolment" ><bean:message key="link.evaluations.enrolment"/></html:link></li>
	<li class="sub">
		<ul>
		<li><html:link page="/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=1" titleKey="link.title.exams.enrolment" ><bean:message key="link.exams.enrolment"/></html:link></li>
		<li><html:link page="/enrollment/evaluations/showWrittenEvaluations.faces?evaluationType=2" titleKey="link.title.writtenTests.enrolment" ><bean:message key="link.writtenTests.enrolment"/></html:link></li>
		</ul>
	</li>
	<li><html:link page="/listAllSeminaries.do" titleKey="link.title.seminaries.enrolment" ><bean:message key="link.seminaries.enrolment"/></html:link>	<a href="<bean:message key="link.seminaries.rules"/>" title="<bean:message key="link.title.seminaries.rules"/>" target="_blank"><bean:message key="label.seminairies.seeRules"/></a></li> 
	<li class="navheader"><bean:message key="link.student.seniorTitle"/></li>
	<li><html:link page="/seniorInformation.do?method=prepareEdit&amp;page=0" ><bean:message key="link.senior.info"/></html:link></li>			  	
	<li><acronym title="<bean:message key="link.title.finalWorkTitle"/>"><bean:message key="link.student.finalWorkTitle"/></acronym></li>
	<li class="sub">
		<ul>
		<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
		<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
		<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
		</ul>
	</li>
</ul>