<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
  <li>
  	<html:link page='/manageExecutionCourses.do'>
  		<bean:message key="link.manage.executionCourse"/>
  	</html:link>
  </li>
  <li>
  	<html:link page='/creditsManagement.do?page=0&amp;method=prepare'>
  		<bean:message key="link.manage.credits"/>
  	</html:link>
  </li> 
</ul>
