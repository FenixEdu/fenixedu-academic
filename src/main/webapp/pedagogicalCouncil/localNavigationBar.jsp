<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="net.sourceforge.fenixedu.domain.PedagogicalCouncilSite"%>

<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<logic:present role="PEDAGOGICAL_COUNCIL">
	<%
		request.setAttribute("site", PedagogicalCouncilSite.getSite());
		request.setAttribute("unit", PedagogicalCouncilSite.getSite().getUnit());
	%>

	<ul>
		<li>
			<html:link page="/weeklyWorkLoad.do?method=prepare&amp;page=0">
			    <bean:message key="link.weekly.work.load"/>
			</html:link>
		</li>
		<li class="navheader">
			<bean:message bundle="PEDAGOGICAL_COUNCIL" key="bolonha.process"/>
		</li>
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message bundle="PEDAGOGICAL_COUNCIL" key="navigation.competenceCoursesManagement"/>
			</html:link>
		</li>
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message bundle="PEDAGOGICAL_COUNCIL" key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>
		
		<li class="navheader">
			<bean:message key="title.unit.communication.section" bundle="RESEARCHER_RESOURCES"/>
		</li>
		<bean:define id="unitId" name="unit" property="idInternal"/>
		<li>
			<html:link page="<%= "/sendEmailRedirect.do?method=prepare&unitId=" + unitId %>">
				<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>	
		<li>
			<html:link page="/sendEmailToStudents.do?method=prepare">
				<bean:message key="link.sendEmailToStudents" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
			<li>
				<html:link page="<%= "/pedagogicalCouncilFiles.do?method=configureGroups&unitId=" + unitId %>">
					<bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<li>
			<html:link page="<%= "/pedagogicalCouncilFiles.do?method=manageFiles&unitId=" + unitId %>">
				<bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
		
		<li class="navheader">
			<bean:message key="delegates.section" bundle="PEDAGOGICAL_COUNCIL"/>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditCandidacyPeriods">
				<bean:message key="link.createEditCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=createEditVotingPeriods">
				<bean:message key="link.createEditVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showCandidacyPeriods">
				<bean:message key="link.showCandidacyPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/electionsPeriodsManagement.do?method=prepare&forwardTo=showVotingPeriods">
				<bean:message key="link.showVotingPeriods" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<li>
			<html:link page="/delegatesManagement.do?method=prepare">
				<bean:message key="link.delegatesManagement" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		<%--
		<li>
			<html:link page="/delegatesManagement.do?method=prepareViewGGAEDelegates">
				<bean:message key="link.delegatesManagement.GGAE" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li> --%>
		<li>
			<html:link page="/findDelegates.do?method=prepare&searchByName=true">
				<bean:message key="link.findDelegates" bundle="PEDAGOGICAL_COUNCIL"/>
			</html:link>
		</li>
		
		
		<li class="navheader">
			<bean:message key="link.control" bundle="APPLICATION_RESOURCES"/>
		</li>
		<li>
			<html:link page="/summariesControl.do?method=prepareSummariesControl&page=0"><bean:message key="link.summaries.control" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
		<li>
			<html:link page="/evaluationMethodControl.do?method=search"><bean:message key="label.evaluationMethodControl" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
        <li>
            <html:link page="/viewInquiriesResults.do?method=chooseDegreeCurricularPlan">
                <bean:message key="link.inquiry.results.version1" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/viewQucResults.do?method=chooseDepartment">
                <bean:message key="link.inquiry.results.version2" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/qucAudit.do?method=searchExecutionCourse">
                <bean:message key="link.inquiry.audit" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/qucDelegatesStatus.do?method=prepare">
                <bean:message key="title.inquiries.delegates.status" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/qucTeachersStatus.do?method=prepare">
                <bean:message key="title.inquiries.teachers.status" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/qucRegentsStatus.do?method=prepare">
                <bean:message key="title.inquiries.regents.status" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        <li>
            <html:link page="/qucCoordinatorsStatus.do?method=prepare">
                <bean:message key="title.inquiries.coordinators.status" bundle="INQUIRIES_RESOURCES"/>
            </html:link>
        </li>
        

		<li class="navheader">
			<bean:message key="link.tutorship" bundle="PEDAGOGICAL_COUNCIL"/>
		</li>
		<li>
			<html:link page="/studentTutorship.do?method=prepareStudentSearch"><bean:message key="link.teacher.tutorship.history" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
		<li>
			<html:link page="/viewStudentsByTutor.do?method=prepareTutorSearch"><bean:message key="label.attends.shifts.tutorialorientation" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
        <li>
            <html:link page="/tutorStudentsPerformanceGrid.do?method=prepareTutorSearch"><bean:message key="label.attends.shifts.tutorialperformance" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
		<li>
			<html:link page="/studentTutorship.do?method=prepareStudentCurriculum"><bean:message key="link.teacher.tutorship.students.viewCurriculum" bundle="APPLICATION_RESOURCES"/></html:link>
		</li>
		<li>
            <html:link page="/studentLowPerformance.do?method=viewStudentsState"><bean:message key="link.tutorship.students.ListLowPerformance" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/tutorshipSummary.do?method=searchTeacher"><bean:message key="link.teacher.tutorship.summary" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
		<li>
            <html:link page="/viewTutors.do?method=listTutors"><bean:message key="title.tutorship.view" bundle="PEDAGOGICAL_COUNCIL"/></html:link>
        </li>
	</ul>
</logic:present>

<logic:present role="TUTORSHIP">

    <ul>
        <li class="navheader">
            <bean:message key="link.tutorship" bundle="PEDAGOGICAL_COUNCIL"/>
        </li>
        <li>
            <html:link page="/studentTutorship.do?method=prepareStudentSearch"><bean:message key="link.teacher.tutorship.history" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
		<li>
            <html:link page="/createTutorships.do?method=prepareCreation"><bean:message key="link.tutorship.create" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/viewStudentsByTutor.do?method=prepareTutorSearch"><bean:message key="label.attends.shifts.tutorialorientation" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/tutorStudentsPerformanceGrid.do?method=prepareTutorSearch"><bean:message key="label.attends.shifts.tutorialperformance" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/studentTutorship.do?method=prepareStudentCurriculum"><bean:message key="link.teacher.tutorship.students.viewCurriculum" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/studentHighPerformance.do?method=listRequests"><bean:message key="link.tutorship.students.ListHighPerformance" bundle="PEDAGOGICAL_COUNCIL"/></html:link>
        </li>
        <li>
            <html:link page="/studentLowPerformance.do?method=viewStudentsState"><bean:message key="link.tutorship.students.ListLowPerformance" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/tutorshipSummary.do?method=searchTeacher"><bean:message key="link.teacher.tutorship.summary" bundle="APPLICATION_RESOURCES"/></html:link>
        </li>
        <li>
            <html:link page="/viewTutors.do?method=listTutors"><bean:message key="title.tutorship.view" bundle="PEDAGOGICAL_COUNCIL"/></html:link>
        </li>
    </ul>

</logic:present>

	<% if(AccessControl.getPerson().getIstUsername().equals("ist12177") || AccessControl.getPerson().getIstUsername().equals("ist24616")) { %>
		<ul>
	        <li class="navheader">
	            <bean:message key="title.firstTimeStudents.menu" bundle="SOP_RESOURCES"/>
	        </li>
	        <li>
	            <html:link page="/shiftDistributionFirstYear.do?method=prepareShiftDistribution">
	            	<bean:message key="link.firstTimeStudents.shiftDistribution" bundle="SOP_RESOURCES"/>
	            </html:link>
	        </li>      
	    </ul>
    <% } %>