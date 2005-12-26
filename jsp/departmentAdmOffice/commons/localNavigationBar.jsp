<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	

<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="DEPARTMENT_ADMINISTRATIVE_OFFICE">
	<ul>
		<li class="navheader">
			<strong><bean:message key="link.group.view.title"/></strong>
		</li>
	</ul>
	
	<ul>
		<li>
			<html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare">
				<bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			</html:link>
		</li>
	</ul>

	<logic:present role="DEPARTMENT_CREDITS_MANAGER">
		<ul>
			<li class="navheader">
				<strong><bean:message key="link.group.teacher.title"/></strong>
			</li>
		</ul>
		
		<ul>
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
			
			<bean:define id="userView" name="<%= SessionConstants.U_VIEW %>" scope="session"/>
		
			<!-- Temporary solution (until we make expectations available for all departments) DEI Code = 28 -->
			<% String deiCode = "28"; %>

			<logic:notEmpty name="userView" property="person.employee.currentDepartmentWorkingPlace">
				<logic:equal name="userView" property="person.employee.currentDepartmentWorkingPlace.code" value="<%= deiCode %>">
					<li>
						<html:link page="/teacher/teacherExpectationDefinitionPeriod/viewTeacherExpectationDefinitionPeriod.faces">
							<bean:message key="link.teacherExpectationDefinitionPeriodManagement"/>
						</html:link>
					</li>
				</logic:equal>
			</logic:notEmpty>
		</ul>
	</logic:present>
	<ul>
		<li class="navheader">
			<strong><bean:message key="link.group.managementGroups.title"/></strong>
		</li>
	</ul>
	<ul>
		<li>
			<html:link page="/managementGroups/curricularPlansManagementGroup.faces">
				<bean:message key="link.curricularPlansManagementGroup"/>
			</html:link>
		</li>
		<li>
			<html:link page="/managementGroups/competenceCoursesManagementGroup.faces">
				<bean:message key="link.competenceCoursesManagementGroup"/>
			</html:link>
		</li>
	</ul>
</logic:present>
