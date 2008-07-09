<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="org.joda.time.YearMonthDay"%>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">


	<ul>
		<li class="navheader"><bean:message key="link.studentOperations" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/createStudent.do?method=prepareCreateStudent"><bean:message key="link.studentOperations.createStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/students.do?method=prepareSearch"><bean:message key="link.studentOperations.viewStudents" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li>
<%--
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan" %>">
			    <bean:message key="link.equivalency.plan" bundle="APPLICATION_RESOURCES"/>
			</html:link>
--%>
			<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan" %>">
			    <bean:message key="link.equivalency.plan" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>

		<li class="navheader"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<%
			String serviceRequestYear = request.getParameter("serviceRequestYear");
		    if (serviceRequestYear == null) {
				serviceRequestYear = (String) request.getAttribute("serviceRequestYear");
				if (serviceRequestYear == null) {
					serviceRequestYear = String.valueOf(new YearMonthDay().year().get());
				}
		    }
		%>
		<li><html:link action="<%="/academicServiceRequestsManagement.do?method=search&amp;serviceRequestYear=" + serviceRequestYear + "&amp;academicSituationType=NEW"%>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="new.requests" /></html:link></li>
		<li><html:link action="<%="/academicServiceRequestsManagement.do?method=search&amp;serviceRequestYear=" + serviceRequestYear + "&amp;academicSituationType=PROCESSING"%>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests" /></html:link></li>
		<li><html:link action="<%="/academicServiceRequestsManagement.do?method=search&amp;serviceRequestYear=" + serviceRequestYear + "&amp;academicSituationType=CONCLUDED"%>"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="concluded.requests"/></html:link></li>

		<li class="navheader"><bean:message key="label.navheader.marksSheet" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/markSheetManagement.do?method=prepareSearchMarkSheet"><bean:message key="link.markSheet.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.consult" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>

		<li class="navheader"><bean:message key="label.masterDegree.administrativeOffice.contributor" bundle="APPLICATION_RESOURCES"/></li>
		<li><html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/visualizeContributors.do?method=prepare&amp;action=visualize&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/editContributors.do?method=prepare&amp;action=edit&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.editContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>

		<li class="navheader"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/pricesManagement.do?method=viewPrices"><bean:message key="link.pricesManagement" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>

		<li class="navheader"><bean:message key="label.institutions" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/externalUnits.do?method=prepareSearch"><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>

	
		<li class="navheader"><bean:message key="label.notNeedToEnrol" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/notNeedToEnrolEnrolments.do?method=prepare"><bean:message key="link.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>
	
	<ul>
		<li class="navheader"><bean:message key="label.lists" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/studentsListByDegree.do?method=prepareByDegree"><bean:message key="link.studentsListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		<li><html:link page="/studentsListByCurricularCourse.do?method=prepareByCurricularCourse"><bean:message key="link.studentsListByCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></li>
		<li><html:link page="/caseHandlingOver23CandidacyProcess.do?method=intro"><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></html:link></li>
		<li><html:link page="/caseHandlingSecondCycleCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.secondCycle" bundle="APPLICATION_RESOURCES"/></html:link></li>
<%-- 		<li><html:link page="/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro"><bean:message key="label.candidacy.graduatedPerson" bundle="APPLICATION_RESOURCES"/></html:link></li> --%>
	</ul>

</logic:present>