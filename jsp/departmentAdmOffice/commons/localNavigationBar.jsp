<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present role="DEPARTMENT_MEMBER">
	<strong>&raquo; <bean:message key="link.group.view.title"/></strong>
	<br />
	<br />
		
	<ul>
		<li>
			<html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare">
				<bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	<br />
	<br />

	<logic:present role="DEPARTMENT_CREDITS_MANAGER">
		<strong>&raquo; <bean:message key="link.group.teacher.title"/></strong>
		<br />
		<br />
			
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
</logic:present>
