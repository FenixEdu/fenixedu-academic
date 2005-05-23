<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
	<li class="navheader"><bean:message key="link.student.portalTitle"/></li>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/student&amp;page=/index.do" %>'><bean:message key="link.student.portal.home"/></html:link></li>
  	<li><html:link page="/viewCurriculum.do?method=getStudentCP" titleKey="link.title.curriculum"><bean:message key="link.student.curriculum"/></html:link></li>
	<li><html:link page="/studentTimeTable.do" target="_blank" titleKey="link.title.timetable"><bean:message key="link.my.timetable"/></html:link></li>
	<li><html:link page="/studentTests.do?method=viewStudentExecutionCoursesWithTests" ><bean:message key="link.tests"/></html:link></li>
	<li><html:link page="/studentGaugingTestResults.do" titleKey="link.title.results.test"><bean:message key="link.results.test"/></html:link></li>
	<li><html:link page="/fillInquiries.do?method=prepareCourses&amp;page=0" titleKey="link.title.inquiries"><bean:message key="link.inquiries" bundle="INQUIRIES_RESOURCES"/></html:link></li>  			
	<li class="navheader"><bean:message key="link.student.enrollmentTitle"/></li>
	<li><html:link page="/warningFirst.do"><bean:message key="link.student.enrollment"/></html:link></li>
	<li><html:link page="/studentShiftEnrollmentManager.do?method=prepareStartViewWarning" titleKey="link.title.shift.enrolment"><bean:message key="link.shift.enrolment"/></html:link></li>
	<li><html:link page="/viewEnroledExecutionCourses.do" titleKey="link.title.groupEnrolment"><bean:message key="link.groupEnrolment" /></html:link></li>
	<li><html:link page="/examEnrollmentManager.do?method=viewExamsToEnroll" titleKey="link.title.exams.enrolment" ><bean:message key="link.exams.enrolment"/></html:link></li>
	<li><html:link page="/listAllSeminaries.do" titleKey="link.title.seminaries.enrolment" ><bean:message key="link.seminaries.enrolment"/></html:link><a href='<bean:message key="link.seminaries.rules" />' target="_blank"><bean:message key="label.seminairies.seeRules"/></a></li>		
	<li class="navheader"><bean:message key="link.student.finalWorkTitle"/></li>  
	<li><html:link target="_blank" href='<%= request.getContextPath() + "/publico/viewFinalDegreeWorkProposals.do" %>'><bean:message key="link.finalDegreeWork.proposal.listings"/></html:link></li>
	<li><html:link page="/finalDegreeWorkCandidacy.do?method=prepareCandidacy&amp;page=0"><bean:message key="link.finalDegreeWork.candidacy"/></html:link></li>
	<li><html:link page="/finalDegreeWorkAttribution.do?method=prepare&amp;page=0"><bean:message key="link.finalDegreeWork.confirmAttribution"/></html:link></li>
	<li class="navheader"><bean:message key="link.student.seniorTitle"/></li> 
	<li><html:link page="/seniorInformation.do?method=prepareEdit&amp;page=0" ><bean:message key="link.senior.info"/></html:link></li>			  	
</ul>
