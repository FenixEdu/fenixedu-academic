<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/manageExecutionCourses.do" %>">
  		<bean:message key="link.manage.executionCourse"/>
  	</html:link>
  </li>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/tutorSection.do" %>">
  		<bean:message key="link.teacher.tutor.operations"/>
  	</html:link>  
  </li>  
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/teacherInformation.do?method=prepareEdit&amp;page=0" %>">
  		<bean:message key="link.manage.teacherInformation"/>
  	</html:link>
  </li>
  <li>
  	<html:link href="<%= request.getContextPath() + "/teacher/prepareTeacherCreditsSheet.do" %>">
  		<bean:message key="link.view.teacher.credits.sheet"/>
  	</html:link>  
  </li>
<%--  <li>
  	<html:link forward="creditsManagement" paramId="teacherOID" paramName="infoTeacher" paramProperty="idInternal">
  		<bean:message key="link.manage.credits"/>
  	</html:link>
  </li> --%>
</ul>
