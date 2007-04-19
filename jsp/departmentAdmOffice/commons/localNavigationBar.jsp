<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
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
				<bean:message key="link.summaries.control" />
			</html:link>
		</li>		
	</ul>
	
	<ul>
		<logic:present role="DEPARTMENT_CREDITS_MANAGER">

			<li class="navheader">
				<strong><bean:message key="link.group.teacher.title"/></strong>
			</li>

			<li>
				<html:link page="/teacherSearchForExecutionCourseAssociation.do?method=searchForm&amp;page=0">
					<bean:message key="link.teacherExecutionCourseAssociation"/>
				</html:link>
			</li>
			<li>
				<html:link page="/creditsManagementIndex.do">
					<bean:message key="link.teacherCreditsManagement"/>
				</html:link>
			</li>
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
											
			<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>		
			<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
			<% String deiCode = "28"; %>
			<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">
				<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deiCode %>">										
					<li class="navheader">
						<strong><bean:message key="link.group.teacherPersonalExpectations.title"/></strong>
					</li>		
					
					<li><acronym><bean:message key="label.periodDefinition"/></acronym></li>

					<li class="sub">
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
	
		<%-- TeacherServiceDistribution entry point
  		<li>
		  	<html:link page="/teacherServiceDistribution.do?method=prepareTeacherServiceDistribution">
		  		<bean:message key="link.teacherServiceDistribution"/>
		  	</html:link>  
		</li> 				
		--%>
					

		<li class="navheader">
			<strong><bean:message key="link.group.managementGroups.title"/></strong>
		</li>

		<li>
			<html:link page="/managementGroups/competenceCoursesManagementGroup.faces">
				<bean:message key="link.competenceCoursesManagementGroup"/>
			</html:link>
		</li>

		<li class="navheader">
			<strong><bean:message key="title.final.degree.works"/></strong>
		</li>

       	<li>
        	<html:link page="/manageFinalDegreeWork.do?method=showChooseExecutionDegreeFormForDepartment">
				<bean:message key="link.manage.final.degree.works"/>
			</html:link>
		</li>	

		<li class="navheader">
			<strong><bean:message key="label.navheader.person.examCoordinator" bundle="VIGILANCY_RESOURCES"/></strong>
		</li>

		<li><html:link  page="/vigilancy/examCoordinatorManagement.do?method=prepareExamCoordinator"><bean:message bundle="VIGILANCY_RESOURCES" key="label.vigilancy.manageExamCoordinator"/></html:link></li>	

    <li class="navheader">
        <bean:message key="label.site.title"/>
    </li>
    <li>
        <bean:define id="unitId" name="userView" property="person.employee.currentDepartmentWorkingPlace.departmentUnit.idInternal"/>
        <html:link page="<%= "/department/departmentSite.do?method=presentation&amp;selectedDepartmentUnitID=" + unitId %>" module="/publico" target="_blank">
            <bean:message key="link.site.department.view"/>
        </html:link>
    </li>
    
    <bean:define id="person" name="UserView" property="person" type="net.sourceforge.fenixedu.domain.Person"/>
    <bean:define id="site" name="person" property="employee.currentDepartmentWorkingPlace.departmentUnit.site" type="net.sourceforge.fenixedu.domain.UnitSite"/>
    <bean:define id="siteId" name="site" property="idInternal"/>
    
    <%
        if (site.hasManagers(person)) {
    %>
    <li>
        <html:link page="<%= "/manageDepartmentSite.do?method=prepare&amp;oid=" + siteId %>" module="/webSiteManager">
            <bean:message key="link.site.department.manage"/>
        </html:link>
    </li>
    <%
        }
    %>
    <li>
        <html:link page="/departmentSite.do?method=chooseManagers">
            <bean:message key="link.site.managers.choose"/>
        </html:link>
    </li>
    
	</ul>
</logic:present>