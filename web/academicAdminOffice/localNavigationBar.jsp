<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="pt.ist.fenixWebFramework.security.UserView"%><html:xhtml/>

<%@page import="net.sourceforge.fenixedu.predicates.PermissionPredicates" %>
<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl" %>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">
 

	<ul>
		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.administrativeOffice.administrativeOfficeType" value="MASTER_DEGREE">
			<li class="navheader"><bean:message key="label.phds" bundle="PHD_RESOURCES"/></li>
			<li><html:link page="/phdIndividualProgramProcess.do?method=manageProcesses"><bean:message key="label.phd.manageProcesses" bundle="PHD_RESOURCES"/></html:link></li>
			<li><html:link page="/phdCandidacyPeriodManagement.do?method=list"><bean:message key="label.phd.candidacy.periods.management" bundle="PHD_RESOURCES"/></html:link></li>
			<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.unitCoordinator" value="true">
				<li><html:link page="/phdPermissionsManagement.do?method=showPermissions"><bean:message key="label.phd.permissions.management" bundle="PHD_RESOURCES"/></html:link></li>
				<li><html:link page="/phdIndividualProgramProcess.do?method=manageEnrolmentPeriods"><bean:message key="label.phd.manage.enrolment.periods" bundle="PHD_RESOURCES"/></html:link></li>
			</logic:equal>
		</logic:equal>
		
		<li class="navheader"><bean:message key="link.studentOperations" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/createStudent.do?method=prepareCreateStudent"><bean:message key="link.studentOperations.createStudent" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/students.do?method=prepareSearch"><bean:message key="link.studentOperations.viewStudents" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
<%--
		<li>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan" %>">
			    <bean:message key="link.equivalency.plan" bundle="APPLICATION_RESOURCES"/>
			</html:link>

			<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan" %>">
			    <bean:message key="link.equivalency.plan" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
--%>

		<li class="navheader"><bean:message key="academic.services" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link action="/academicServiceRequestsManagement.do?method=search&amp;academicSituationType=NEW"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="new.requests" /></html:link></li>
		<li><html:link action="/academicServiceRequestsManagement.do?method=search&amp;academicSituationType=PROCESSING"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="processing.requests" /></html:link></li>
		<li><html:link action="/academicServiceRequestsManagement.do?method=search&amp;academicSituationType=CONCLUDED"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="concluded.requests"/></html:link></li>
        <li><html:link action="/rectorateDocumentSubmission.do?method=index"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.rectorateSubmission"/></html:link></li>

		<li class="navheader"><bean:message key="label.navheader.marksSheet" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/markSheetManagement.do?method=prepareSearchMarkSheet"><bean:message key="link.markSheet.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/oldMarkSheetManagement.do?method=prepareSearchMarkSheet"><bean:message key="link.old.markSheet.management" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.consult" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>

		<li class="navheader"><bean:message key="label.masterDegree.administrativeOffice.contributor" bundle="APPLICATION_RESOURCES"/></li>
		<li><html:link page="/createContributorDispatchAction.do?method=prepare"><bean:message key="link.masterDegree.administrativeOffice.createContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/visualizeContributors.do?method=prepare&amp;action=visualize&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.visualizeContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>
		<li><html:link page="/editContributors.do?method=prepare&amp;action=edit&amp;page=0"><bean:message key="link.masterDegree.administrativeOffice.editContributor" bundle="APPLICATION_RESOURCES" /></html:link></li>

		<li class="navheader"><bean:message key="label.payments" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/pricesManagement.do?method=viewPrices"><bean:message key="link.pricesManagement" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>

		<li class="navheader"><bean:message key="label.institutions" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/externalUnits.do?method=prepareSearch"><bean:message key="label.externalUnits" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>

		<%-- Not used 
		<li class="navheader"><bean:message key="label.notNeedToEnrol" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/notNeedToEnrolEnrolments.do?method=prepare"><bean:message key="link.notNeedToEnrol.enrolment" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		--%>
	</ul>
	
	<ul>
		<li class="navheader"><bean:message key="label.lists" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/studentsListByDegree.do?method=prepareByDegree"><bean:message key="link.studentsListByDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		<li><html:link page="/studentsListByCurricularCourse.do?method=prepareByCurricularCourse"><bean:message key="link.studentsListByCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		<li><html:link page="/requestListByDegree.do?method=prepareSearch"><bean:message key="lists.serviceRequestsByDegree" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		<li><html:link page="/diplomasListBySituation.do?method=prepareBySituation"><bean:message key="link.diplomasListBySituation" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="label.candidacies" bundle="APPLICATION_RESOURCES"/></li>
		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.administrativeOffice.administrativeOfficeType" value="DEGREE">
			<li><html:link page="/caseHandlingOver23CandidacyProcess.do?method=intro"><bean:message key="label.candidacy.over23" bundle="APPLICATION_RESOURCES"/></html:link></li>
			<li><html:link page="/caseHandlingSecondCycleCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.secondCycle" bundle="APPLICATION_RESOURCES"/></html:link></li>
		 	<li><html:link page="/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro"><bean:message key="label.candidacy.graduatedPerson" bundle="APPLICATION_RESOURCES"/></html:link></li> 
			<li><html:link page="/caseHandlingDegreeChangeCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.degreeChange" bundle="APPLICATION_RESOURCES"/></html:link></li> 
			<li><html:link page="/caseHandlingDegreeTransferCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.degreeTransfer" bundle="APPLICATION_RESOURCES"/></html:link></li>
		</logic:equal>
 		<li><html:link page="/caseHandlingStandaloneCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.standalone" bundle="APPLICATION_RESOURCES"/></html:link></li>
 		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.administrativeOffice.administrativeOfficeType" value="DEGREE">
 			<li><html:link page="/caseHandlingErasmusCandidacyProcess.do?method=intro"><bean:message key="label.candidacy.eramus" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
 		</logic:equal>
	</ul>

	<ul>
		<li class="navheader"><bean:message key="label.documents" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
		<li><html:link page="/generatedDocuments.do?method=prepareSearchPerson"><bean:message key="label.documents.anualIRS" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.administrativeOffice.administrativeOfficeType" value="MASTER_DEGREE">
			<li><html:link page="/exportMasterAndPhdStudentDiploma.do?method=prepareExportation"><bean:message key="utilities.export.old.diplomas" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link> </li>
		</logic:equal>
	</ul>

	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.unitCoordinator" value="true">
		<ul>
			<li class="navheader"><bean:message key="label.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
			<li><html:link page="/manageExtraCurricularActivities.do?method=list"><bean:message key="label.manage.extraCurricularActivityTypes" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>

			<li class="navheader"><bean:message key="label.permissions" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
			<li><html:link page="/permissionManagement.do?method=showPermissions"><bean:message key="label.manage.permissions" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>
		</ul>
	</logic:equal>
	<logic:equal name="<%= pt.ist.fenixWebFramework.servlets.filters.SetUserViewFilter.USER_SESSION_ATTRIBUTE %>" property="person.employee.unitCoordinator" value="true">
		<ul>
			<li class="navheader"><bean:message key="label.registeredDegreeCandidacies.first.time.student.registration" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
			<li><html:link page="/registeredDegreeCandidacies.do?method=view"><bean:message key="label.registeredDegreeCandidacies.first.time.list" bundle="ACADEMIC_OFFICE_RESOURCES" /></html:link></li>
		</ul>
	</logic:equal>
</logic:present>
