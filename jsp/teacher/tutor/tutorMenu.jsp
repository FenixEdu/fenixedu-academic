<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<hr></hr>
<h2><center><bean:message key="label.teacher.tutor.operations"/></center></h2>
	
<p>
<ul>
	<li>
		<html:link page="<%= "/viewStudentCurriculum.do?method=prepareView"%>">
		    <bean:message key="link.student.curriculum" /></html:link>
		    <br/>
			<br/>
		</html:link>
	</li>
	<li>
		<html:link page="<%= "/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"%>">
		    <bean:message key="link.student.enrollment" /></html:link>
		    <br/>
			<br/>
		</html:link>
	</li>		
</ul>
</p>
