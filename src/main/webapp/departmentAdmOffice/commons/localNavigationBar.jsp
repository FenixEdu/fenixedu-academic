<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app"%>
<%@page import="org.fenixedu.bennu.core.security.Authenticate"%>
<%@page import="org.fenixedu.bennu.core.domain.User"%>
<html:xhtml/>

<%
	final User userView = (User) Authenticate.getUser();
	request.setAttribute("userView", userView);
%>

<logic:present role="role(DEPARTMENT_ADMINISTRATIVE_OFFICE)">
	<ul>
		<li class="navheader">
			<bean:message key="link.group.view.title"/>
		</li>

		<li>
			<html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare">
				<bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			</html:link>
		</li>
		<li>
			<html:link page="/viewTeacherService/viewTeacherService.faces">
				<bean:message key="link.teacherService" />
			</html:link>
		</li>
		<li>
			<html:link page="/summariesControl.do?method=prepareSummariesControl">
				<bean:message key="link.summaries.control" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>		
	</ul>
	
	<ul>
		<li class="navheader">
			<strong><bean:message key="link.group.teacher.title"/></strong>
		</li>
		<li>
			<a href="http://fenix-ashes.ist.utl.pt/professoresexternos/">
				<bean:message key="link.teachers.externalTeachers"/>
			</a>
		</li>
		
		<logic:present role="role(DEPARTMENT_CREDITS_MANAGER)">

			<li>
				<html:link page="/teacherSearchForExecutionCourseAssociation.do?method=searchForm&amp;page=0">
					<bean:message key="link.teacherExecutionCourseAssociation"/>
				</html:link>
			</li>
			<%--<li>
				<html:link page="/creditsManagementIndex.do">
					<bean:message key="link.teacherCreditsManagement"/>
				</html:link>
			</li>--%>
			<li>
				<html:link page="/teacherSearchForSummariesManagement.do?method=searchForm&amp;page=0">
					<bean:message key="link.summaries"/>
				</html:link>
			</li>						
			<li>
				<html:link page="/searchTeachers.do?method=download">
					<bean:message key="link.teachers.search"/>
				</html:link>
			</li>
			

			<li class="navheader">
				<strong><bean:message key="label.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/></strong>
			</li>
			<li>
				<html:link page="/credits.do?method=prepareTeacherSearch">
					<bean:message key="label.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link page="/exportCredits.do?method=prepareExportDepartmentCredits">
					<bean:message key="label.department.credits" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link page="/exportCredits.do?method=prepareExportDepartmentCourses">
					<bean:message key="label.executionCourses.types" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</li>
			<li>
				<html:link page="/creditsPool.do?method=prepareManageUnitCredits">
					<bean:message key="label.departmentCreditsPool" bundle="TEACHER_CREDITS_SHEET_RESOURCES"/>
				</html:link>
			</li>

			<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
			<% String deiCode = "28"; %>
			<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">
				<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deiCode %>">										
					<li class="navheader">
						<strong><bean:message key="link.group.teacherPersonalExpectations.title"/></strong>
					</li>		
					
					<li><span><bean:message key="label.periodDefinition"/></span>
						<ul>
							<li>
								<html:link page="/teacherPersonalExpectationsDefinitionPeriod.do?method=showPeriod">
									<bean:message key="link.teacherExpectationDefinitionPeriodManagement"/>
								</html:link>
							</li>
							<li>
								<html:link page="/autoEvaluationTeacherExpectationManagementAction.do?method=showPeriod">
									<bean:message key="link.defineAutoAvaliationPeriod"/>
								</html:link>
							</li>
							<li>
								<html:link page="/teacherPersonalExpectationsEvaluationPeriod.do?method=showPeriod">
									<bean:message key="link.defineTeacherPersonalExpectationsEvaluationPeriod"/>
								</html:link>
							</li>
							<li>
								<html:link page="/teacherPersonalExpectationsVisualizationPeriod.do?method=showPeriod">
									<bean:message key="link.defineTeacherPersonalExpectationsVisualizationPeriod"/>
								</html:link>
							</li>							
						</ul>
					</li>
					<li>
						<html:link page="/defineExpectationEvaluationGroups.do?method=listGroups">
							<bean:message key="link.define.expectations.evaluation.groups"/>
						</html:link>
					</li>
					<li>
						<html:link page="/listTeachersPersonalExpectations.do?method=listTeachersPersonalExpectations">
							<bean:message key="link.see.teachers.personal.expectations"/>
						</html:link>
					</li>					
				</logic:equal>
			</logic:notEmpty>
	</logic:present>

	<li class="navheader">
		<strong><bean:message key="link.group.students.title"/></strong>
	</li>
	<li>
		<html:link page="/searchStudents.do?method=search">
			<bean:message key="link.students.search"/>
		</html:link>
	</li>

	<li class="navheader"><bean:message key="label.lists" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
	<li><html:link page="/specialSeason/specialSeasonStatusTracker.do?method=selectCourses"><bean:message key="label.course.specialSeasonEnrolments" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link>
	<li><html:link page="/listCourseResponsibles.do?method=prepareByCurricularCourse"><bean:message key="link.listCourseResponsibles" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:link></li>	

		<%-- Another Temporary solution :-( --%>	
		
		<% String deecCode = "21"; %>
		<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">
		 	<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deecCode %>">
		  		<li>
				  	<html:link page="/tsdProcess.do?method=prepareTSDProcess">
				  		<bean:message key="link.teacherServiceDistribution"/>
				  	</html:link>  
				</li>
			</logic:equal>
		</logic:notEmpty>

		<li class="navheader">
			<strong><bean:message key="link.group.managementGroups.title"/></strong>
		</li>

		<li>
			<html:link page="/managementGroups/competenceCoursesManagementGroup.faces">
				<bean:message key="link.competenceCoursesManagementGroup"/>
			</html:link>
		</li>

<!-- 		<li class="navheader"> -->
<%-- 			<strong><bean:message key="title.final.degree.works"/></strong> --%>
<!-- 		</li> -->

<!--        	<li> -->
<%--         	<html:link page="/manageFinalDegreeWork.do?method=showChooseExecutionDegreeFormForDepartment"> --%>
<%-- 				<bean:message key="link.manage.final.degree.works"/> --%>
<%-- 			</html:link> --%>
<!-- 		</li>	 -->

		<li class="navheader">
			<strong><bean:message key="label.navheader.person.examCoordinator" bundle="VIGILANCY_RESOURCES"/></strong>
		</li>

		<li><html:link  page="/vigilancy/examCoordinatorManagement.do?method=prepareExamCoordinator"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageExamCoordinator"/></html:link></li>

<logic:present name="userView" property="person.employee.currentDepartmentWorkingPlace">
    <li class="navheader">
        <bean:message key="label.site.title"/>
    </li>
    
    <logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.site">
    <li>
        <bean:define id="site" name="userView" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.site"/>
        <app:contentLink name="site">
            <bean:message key="link.site.department.view"/>
        </app:contentLink>
    </li>
    <bean:define id="siteId" name="site" property="externalId"/>
    
    <li>
        <html:link page="<%= "/departmentSite.do?method=chooseManagers&amp;oid=" + siteId %>">
            <bean:message key="link.site.managers.choose"/>
        </html:link>
    </li>
    </logic:notEmpty>
</logic:present>
    
	</ul>
	
	<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">	
		<bean:define id="unit" name="userView" property="person.employee.currentDepartmentWorkingPlace.departmentUnit"/>
		<bean:define id="unitID" name="unit" property="externalId"/>
		<bean:define id="unitExternalId" name="unit" property="externalId"/>
		
		<ul>	
		<li class="navheader"><fr:view name="unit" property="acronym"/></li>
			<ul>
				<li>
					<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&unitExternalId=" + unitExternalId %>">
						<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
					 </html:link>
				 </li>	
				  <logic:equal name="unit" property="currentUserAbleToDefineGroups" value="true">
				  <li>
					 <html:link page="<%= "/departmentFunctionalities.do?method=configureGroups&unitId=" + unitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
					 </html:link>
				  </li>
				  </logic:equal>
				  <li><html:link page="<%= "/departmentFunctionalities.do?method=manageFiles&unitId=" + unitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
			</ul>
		</ul>
		
			<logic:notEmpty name="unit" property="allSubUnits">
				<ul>
				<logic:iterate id="subUnit" name="unit" property="allSubUnits">
					<logic:equal name="subUnit" property="scientificAreaUnit"  value="true">
						<logic:equal name="subUnit" property="currentUserMemberOfScientificArea" value="true">
							<bean:define id="subUnitID" name="subUnit" property="externalId"/>
							<bean:define id="subUnitExternalId" name="subUnit" property="externalId"/>
							<li class="navheader"><fr:view name="subUnit" property="name"/></li>
									<ul>
										<li>
											<html:link page="<%= "/sendEmailToDepartmentGroups.do?method=prepare&unitExternalId=" + subUnitExternalId %>">
												<bean:message key="label.sendEmailToGroups" bundle="RESEARCHER_RESOURCES"/>
											 </html:link>
										 </li>
										  <logic:equal name="subUnit" property="currentUserAbleToDefineGroups" value="true">
										  <li>
											 <html:link page="<%= "/departmentFunctionalities.do?method=configureGroups&unitId=" + subUnitID %>"><bean:message key="label.configurePersistentGroups" bundle="RESEARCHER_RESOURCES"/>
											 </html:link>
										  </li>
										  </logic:equal>
										  <li><html:link page="<%= "/departmentFunctionalities.do?method=manageFiles&unitId=" + subUnitID %>"><bean:message key="label.manageFiles" bundle="RESEARCHER_RESOURCES"/></html:link></li>						
									</ul>
						</logic:equal>
					</logic:equal>
				</logic:iterate>
				</ul>
			</logic:notEmpty>
			
		</logic:notEmpty>
</logic:present>