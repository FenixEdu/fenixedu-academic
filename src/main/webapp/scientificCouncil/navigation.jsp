<%@ page language="java"%>
<%@ page import="net.sourceforge.fenixedu.domain.ScientificCouncilSite"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<html:xhtml/>

<logic:present role="SCIENTIFIC_COUNCIL">
	<%
		request.setAttribute("site", ScientificCouncilSite.getSite());
		request.setAttribute("unit", ScientificCouncilSite.getSite().getUnit());
	%>

	<ul>
		<li class="navheader">
			<bean:message key="bolonha.process"/>
		</li>
		
		<li>
			<html:link page="/competenceCourses/competenceCoursesManagement.faces">
				<bean:message key="navigation.competenceCoursesManagement"/>
			</html:link>
		</li>

		 
		<li>
			<html:link page="/competenceCourses/manageVersions.do?method=prepare">
				<bean:message key="navigation.competenceCourseVersionManagement"/>
			</html:link>
		</li>
		
		
		<li>
			<html:link page="/curricularPlans/curricularPlansManagement.faces">
				<bean:message key="navigation.curricularPlansManagement"/>
			</html:link>
		</li>

		<li>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan" %>">
			    <bean:message key="link.equivalency.plan" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
		
		<li>
			<html:link page="/curricularPlans/editExecutionDegreeCoordination.do?method=editByYears">
				<bean:message key="navigation.manageCoordinationTeams"/>
			</html:link>
		</li>

        <li class="navheader">
            <bean:message key="scientificCouncil.thesis.process"/>
        </li>
        <li>
   	        <html:link page="/manageSecondCycleThesis.do?method=firstPage">
       	        <bean:message key="navigation.list.jury.proposals"/> (novo)
           	</html:link>  
       	</li>
        <li>
            <html:link page="/scientificCouncilManageThesis.do?method=listThesis">
                <bean:message key="navigation.list.jury.proposals"/>
            </html:link>  
        </li>
        <li>
            <html:link page="/scientificCouncilManageThesis.do?method=listThesisCreationPeriods">
                <bean:message key="navigation.list.thesis.creation.periods"/>
            </html:link>  
        </li>
        <li>
            <html:link page="/scientificCouncilManageThesis.do?method=dissertations">
                <bean:message key="navigation.thesis.info"/>
            </html:link>  
        </li>

		<li class="navheader">
			<bean:message key="label.credits.navigation.header"/>
		</li>
		<li>
		  	<html:link page="/defineCreditsPeriods.do?method=showPeriods">
		  		<bean:message key="link.define.periods"/>
		  	</html:link>  
		</li>	
		<%--	
		<li>
			<html:link page="/functionsManagement/personSearchForFunctionsManagement.faces" >
				<bean:message key="link.managementPositions.management"/>
			</html:link>
		</li>
		--%>
		<li>
			<html:link page="/masterDegreeCreditsManagement.do?method=prepare">
		  		<bean:message key="link.credits.masterDegree"/>
		  	</html:link>
		</li>
		<li>
		  	<html:link page="/credits.do?method=prepareTeacherSearch">
		  		<bean:message key="link.teacher.sheet"/>
		  	</html:link>  
		</li>
		<li>
			<html:link page="/projectTutorialCourses.do?method=showDepartmentExecutionCourses">
				<bean:message key="label.executionCourses.types" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/creditsPool.do?method=prepareManageUnitCredits">
				<bean:message key="label.departmentCreditsPool" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/exportCredits.do?method=prepareExportDepartmentCredits">
				<bean:message key="label.department.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
			</html:link>
		</li>
		<%--
		<li>
		  	<html:link page="/showAllTeacherCreditsResume.do?method=prepareTeacherSearch">
		  		<bean:message key="link.teacher.sheet"/>
		  	</html:link>  
		</li>
		<%--
		<li>
		  	<html:link page="/manageNonRegularTeachingService.do?method=chooseNonRegularTeacher">
		  		<bean:message key="link.nonRegularTeacher.sheet"/>
		  	</html:link>  
		</li>	 --%>		
		<li>
			<html:link page="/creditsReport.do?method=prepare">
		  		<bean:message key="link.credits.viewReport"/>
		  	</html:link>
		</li>
		<li>
			<html:link page="/managePersonFunctionsShared.do?method=showDepartmentPersonFunctions" >
				<bean:message key="link.managementPositions.management"/>
			</html:link>
		</li>
		<li>
			<html:link page="/departmentCredits.do?method=prepareDepartmentCredits">
				<bean:message key="label.departmentAdmOffice" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
		  	</html:link>
		</li>		
		
		<li class="navheader">
			<bean:message key="label.research.activity.navigation.header"/>
		</li>
		<li>
			<html:link page="/editScientificJournal.do?method=prepare">
		  		<bean:message key="link.research.activity.edit.scientific.journal"/>
		  	</html:link>
		</li>		
		<li>
			<html:link page="/editEvent.do?method=prepare">
		  		<bean:message key="link.research.activity.edit.event"/>
		  	</html:link>
		</li>							
		<li>
			<html:link page="/mergeScientificJournal.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.scientific.journal"/>
		  	</html:link>
		</li>

		<li>
			<html:link page="/editScientificJournalMergeJournalIssues.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.journal.issues"/>
		  	</html:link>
		</li>

		<li>
			<html:link page="/mergeEvents.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.event"/>
		  	</html:link>
		</li>

		<li>
			<html:link page="/editEventMergeEventEditions.do?method=prepare">
		  		<bean:message key="link.research.activity.merge.event.editions"/>
		  	</html:link>
		</li>

		<%-- Communication --%>
		<li class="navheader">
			<bean:message key="title.unit.communication.section" bundle="RESEARCHER_RESOURCES"/>
		</li>
		
		<bean:define id="unitId" name="unit" property="externalId"/>
		<li>
			<html:link page="<%= "/sendEmail.do?method=prepare&unitId=" + unitId %>">
				<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>	
		<logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
			<li>
				<html:link page="<%= "/scientificCouncilFiles.do?method=configureGroups&unitId=" + unitId %>">
					<bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
				</html:link>
			</li>
		</logic:equal>
		<li>
			<html:link page="<%= "/scientificCouncilFiles.do?method=manageFiles&unitId=" + unitId %>">
				<bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
		<%-- 
		<li>
			<html:link page="/caseHandlingOver23CandidacyProcess.do?method=listProcesses">
				<bean:message key="label.processes" bundle="RESEARCHER_RESOURCES"/>
			</html:link>
		</li>
		--%>

		<li class="navheader">
			<bean:message key="title.applications" bundle="CANDIDATE_RESOURCES" />
		</li>
		<li>
			<html:link page="/caseHandlingDegreeChangeCandidacyProcess.do?method=intro">
				<bean:message key="title.application.name.degreeChange" bundle="CANDIDATE_RESOURCES" />
			</html:link>		
		</li>
		<li>
			<html:link page="/caseHandlingDegreeTransferCandidacyProcess.do?method=intro">
				<bean:message key="title.application.name.degreeTransfer" bundle="CANDIDATE_RESOURCES" />
			</html:link>		
		</li>
		<li>
			<html:link page="/caseHandlingDegreeCandidacyForGraduatedPersonProcess.do?method=intro">
				<bean:message key="title.application.name.degreeCandidacyForGraduatedPerson" bundle="CANDIDATE_RESOURCES" />
			</html:link>		
		</li>
		<li>
			<html:link page="/caseHandlingSecondCycleCandidacyProcess.do?method=intro">
				<bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES" />
			</html:link>		
		</li>
		
		<li class="navheader">
			Professores
		</li>
		<li>
			<html:link page="/teacherAuthorization.do?method=list">
				Autorizações
			</html:link>		
		</li>
	</ul> 
</logic:present>
