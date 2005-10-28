<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

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
	<br />
	
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
		</ul>
		<br/>
	</logic:present>
	<!-- 
	<ul>
		<li class="navheader">
			<strong> <bean:message key="link.functions.management"/> </strong>
		</li>
	</ul>
	
	<ul>
		<li>
			<html:link page="/teacher/functionsManagement/personSearchForFunctionsManagement.faces?link=chooseUnit">
				<bean:message key="link.functions.management.new"/>
			</html:link>
		</li>		
		<li>	
			<html:link page="/teacher/functionsManagement/personSearchForFunctionsManagement.faces?link=listPersonFunctions">		
				<bean:message key="link.functions.management.edit"/>			
			</html:link>	
		</li>
	</ul>
	-->
</logic:present>
