<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>	


<!-- Import new CSS for this section: #navlateral  -->
<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style>

<logic:present role="DEPARTMENT_MEMBER">
	<ul>
		<li>
			<html:link page="/viewDepartmentTeachers/listDepartmentTeachers.faces">
				<bean:message key="link.departmentTeachers"/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			</html:link>
		</li>
		<li>
			<html:link page="/courseStatistics/viewCompetenceCourses.faces">
				<bean:message key="link.departmentCourses"/>
			</html:link>
		</li>	
		<logic:equal scope="session" name='<%=SessionConstants.U_VIEW%>' property="person.username" value="D2023">
		<li>
			<html:link page="/expectationManagement/viewPersonalExpectation.faces">
				<bean:message key="link.personalExpectationsManagement"/>
			</html:link>
		</li>
  		<li>
		  	<html:link page="/viewDepartmentTeachersExpectations.do?method=prepare">
		  		<bean:message key="link.departmentTeachersExpectations"/>
		  	</html:link>  
		</li> 				
		</logic:equal>
		<li>
			<html:link page="/viewTeacherService/viewTeacherService.faces">
				<bean:message key="link.teacherService"/>
			</html:link>
		</li>
		<br/>
  		<li>
		  	<html:link page="/showAllTeacherCreditsResume.do">
		  		<bean:message key="link.teacher.credits"/>
		  	</html:link>  
		</li>
	</ul>
	
	<br />
</logic:present>

