<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:notPresent name="executionCourse">
	<logic:present name="siteView">
		<bean:define id="component" name="siteView" property="commonComponent"/>
		<bean:define id="executionCourse" name="component" property="executionCourse"/>
	</logic:present>
</logic:notPresent>

<logic:present name="executionCourse">
	<ul>
		<li>
			<html:link page="/manageExecutionCourse.do?method=instructions" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.home"/>
			</html:link>
		</li>
		<li>
			<html:link page="/copySiteExecutionCourse.do?method=prepareChooseExecutionPeriod" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.copySiteExecutionCourse"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link page="/alternativeSite.do?method=prepareCustomizationOptions" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.personalizationOptions"/>
			</html:link>
		</li>
		<li>
			<html:link page="/announcementManagementAction.do?method=showAnnouncements" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.announcements"/>
			</html:link>
		</li>
		<li>
			<html:link page="/showSummaries.do?method=showSummaries&amp;page=0" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.summaries"/>
			</html:link>
		</li>
		<li>
			<html:link page="/sectionViewer.do?method=sectionsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.sectionsManagement"/>
			</html:link>
		</li>
		<li>
			<html:link page="/executionCourseForumManagement.do?method=viewForuns" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.teacher.executionCourseManagement.foruns"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link page="/manageExecutionCourse.do?method=objectives" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.objectives"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=program" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.program"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=evaluationMethod" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.evaluationMethod"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=bibliographicReference" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.bibliography"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.teachers"/>
			</html:link>
		</li>
		<li>
			<html:link page="/studentsByCurricularCourse.do?method=prepare" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.students"/>
			</html:link>
		</li>
		<li>
			<html:link page="/evaluationManagement.do?method=evaluationIndex" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.evaluation"/>
			</html:link>
		</li>
		<li>
			<html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.testsManagement"/>
			</html:link>
		</li>
		<li>
			<html:link page="/viewExecutionCourseProjects.do?method=prepareViewExecutionCourseProjects" paramId="objectCode" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.groupsManagement"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link page="/weeklyWorkLoad.do?method=prepare" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.weekly.work.load"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link page="/viewCourseInformation.do" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.courseInformationManagement"/>
			</html:link>
		</li>
		<li>
			<html:link page="/teachingReport.do?method=prepareEdit&amp;page=0" paramId="executionCourseId" paramName="executionCourse" paramProperty="idInternal">
				<bean:message key="link.teachingReportManagement"/>
			</html:link>
		</li>
	</ul>
	<br/>
	<ul>
		<li>
			<html:link href="<%= request.getContextPath()+"/publico/executionCourse.do?method=firstPage" %>" paramId="executionCourseID" paramName="executionCourse" paramProperty="idInternal" target="_blank">
				Ver Página da Disciplina
			</html:link>
		</li>
	</ul>
</logic:present>