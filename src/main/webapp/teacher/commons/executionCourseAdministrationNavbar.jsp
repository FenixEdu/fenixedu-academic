<%@page import="net.sourceforge.fenixedu.injectionCode.AccessControl"%>
<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<html:xhtml/>

<logic:notPresent name="executionCourse">
	<logic:present name="siteView">
		<bean:define id="component" name="siteView" property="commonComponent"/>
		<bean:define id="executionCourse" toScope="request" name="component" property="executionCourse.executionCourse"/>		
	</logic:present>
</logic:notPresent>

<bean:define id="professorship" name="executionCourse" property="professorshipForCurrentUser"/>
<bean:define id="professorshipPermissions" name="professorship" property="permissions"/>

<logic:present name="executionCourse">
	<ul>
		
		<li>
			<html:link page="/manageExecutionCourse.do?method=instructions" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.home"/>
			</html:link>
		</li>
		<logic:equal name="professorshipPermissions" property="personalization" value="true">
		<li>
			<html:link page="/alternativeSite.do?method=prepareCustomizationOptions" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.personalizationOptions"/>
			</html:link>
		</li>
		</logic:equal>
		<logic:notEmpty name="executionCourse" property="site">
			<li>							
				<app:contentLink name="executionCourse" property="site" scope="request" target="_blank">
					<bean:message key="link.executionCourseManagement.menu.view.course.page"/>	
				</app:contentLink>					
			</li>
		</logic:notEmpty>
		<logic:equal name="professorshipPermissions" property="siteArchive" value="true">
		<li>
            <html:link page="/generateArchive.do?method=prepare" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
                <bean:message key="link.executionCourse.archive.generate"/>
            </html:link>
        </li>
        </logic:equal>
        <logic:equal name="professorshipPermissions" property="summaries" value="true">
		<li>
			<html:link page="/searchECLog.do?method=prepareInit" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.executionCourse.log"/>
			</html:link>
		</li>
		</logic:equal>
		
	<li class="navheader"><bean:message key="label.executionCourseManagement.menu.communication"/></li>
		<logic:equal name="professorshipPermissions" property="announcements" value="true">
		<li>
			<html:link page="/announcementManagement.do?method=start" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.announcements"/>
			</html:link>
		</li>
		</logic:equal>
		<li>
			<html:link page="/executionCourseForumManagement.do?method=viewForuns" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.teacher.executionCourseManagement.foruns"/>
			</html:link>
		</li>
		<logic:equal name="professorshipPermissions" property="sections" value="true">
		<li>
			<html:link page="/manageExecutionCourseSite.do?method=sections" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="label.executionCourseManagement.menu.sections"/>
			</html:link>
		</li>
		</logic:equal>

	<li class="navheader"><bean:message key="label.executionCourseManagement.menu.management"/></li>
		<logic:equal name="professorshipPermissions" property="summaries" value="true">
		<li>
			<html:link page="/summariesManagement.do?method=prepareShowSummaries&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.summaries"/>
			</html:link>
		</li>
		</logic:equal>
		<li>
			<html:link page="/teachersManagerDA.do?method=viewTeachersByProfessorship" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.teachers"/>
			</html:link>
		</li>
		<logic:equal name="professorshipPermissions" property="summaries" value="true">
		<li>
			<html:link page="/searchECAttends.do?method=prepare" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.students"/>
			</html:link>
		</li>
		</logic:equal>
		<logic:equal name="professorshipPermissions" property="planning" value="true">
		<li>
			<html:link page="/manageExecutionCourse.do?method=lessonPlannings&amp;page=0" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.lessonPlannings"/>
			</html:link>
		</li>
		</logic:equal>
		<li>
			<html:link page="/evaluationManagement.do?method=evaluationIndex" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.evaluation"/>
			</html:link>
		</li>
		<logic:equal name="professorshipPermissions" property="worksheets" value="true">
		<li>
			<html:link page="/testsManagement.do?method=testsFirstPage" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.testsManagement"/>
			</html:link>
		</li>
		</logic:equal>
  <%-- 
		<li>
			<html:link page="/tests/tests.do?method=manageTests" paramId="oid" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="message.tests.manage" bundle="TESTS_RESOURCES" />
			</html:link>
		</li>
	--%>		
		<logic:equal name="professorshipPermissions" property="groups" value="true">
		<li>
			<html:link page="/viewExecutionCourseProjects.do?method=prepareViewExecutionCourseProjects" paramId="objectCode" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.groupsManagement"/>
			</html:link>
		</li>
		</logic:equal>
		<logic:equal name="professorshipPermissions" property="shift" value="true">
		<li>
			<html:link page="/manageExecutionCourse.do?method=manageShifts" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="label.shifts"/>
			</html:link>
		</li>	
		</logic:equal>
	<li class="navheader"><bean:message key="label.executionCourseManagement.menu.curricularInfo"/></li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=objectives" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.objectives"/>
			</html:link>
		</li>
		<li>
			<html:link page="/manageExecutionCourse.do?method=program" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.program"/>
			</html:link>
		</li>
		<logic:equal name="professorshipPermissions" property="evaluationMethod" value="true">
		<li>
			<html:link page="/manageExecutionCourse.do?method=evaluationMethod" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.evaluationMethod"/>
			</html:link>
		</li>
		</logic:equal>
		<logic:equal name="professorshipPermissions" property="bibliografy" value="true">
		<li>
			<html:link page="/manageExecutionCourse.do?method=bibliographicReference" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.bibliography"/>
			</html:link>
		</li>
		</logic:equal>
		
	<li class="navheader"><bean:message key="label.executionCourseManagement.menu.curricularUnitsQuality"/></li>
		<li>
			<html:link page="/teachingInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
				<bean:message key="link.teachingReportManagement"/>
			</html:link>
		</li>
		<logic:equal name="professorship" property="responsibleFor" value="true">
			<li>
				<html:link page="/regentInquiry.do?method=showInquiriesPrePage" paramId="executionCourseID" paramName="executionCourse" paramProperty="externalId">
					<bean:message key="link.regentReportManagement"/>
				</html:link>
			</li>
		</logic:equal>
	</ul>	
</logic:present>