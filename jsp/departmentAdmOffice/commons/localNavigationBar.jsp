<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present role="role.department.member">
	<logic:present role="role.department.credits.manager">
		<strong>&raquo; <bean:message key="link.group.teacher.title"/></strong>
		<ul>
			<li>
				<html:link page="/teacherSearchForShiftManagement.do?method=searchForm&amp;page=0">
					<bean:message key="link.lessons"/>
				</html:link>
			</li>
			<li>
				<html:link page="/teacherSearchForSupportLessonsManagement.do?method=searchForm&amp;page=0">			
					<bean:message key="link.support.lessons"/>
				</html:link>					
			</li>						
			<li>
				<html:link page="/teacherSearchForDFPStudentManagement.do?method=searchForm&amp;page=0">			
					<bean:message key="link.degree.final.project.students"/>
				</html:link>					
			</li>
			<li>
				<html:link page="/teacherSearchForTeacherInstitutionWorkingTimeManagement.do?method=searchForm&amp;page=0">
					<bean:message key="link.teacher-working-time-management"/>
				</html:link>
			</li>
			<li><bean:message key="link.teacher.sheet"/></li>
			<li>
				<html:link page="/prepareListDepartmentTeachers.do">
					<bean:message key="link.list-department-teachers"/>
				</html:link>
			</li>
		</ul>
		<br/>
	</logic:present>
</logic:present>
