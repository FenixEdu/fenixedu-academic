<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<br/>
<ul>
	<li class="navheader"><bean:message key="label.teacher.tutor.operations"/></li>
	<li>
		<html:link page="<%= "/viewStudentsByTutor.do?method=execute"%>">
			<bean:message key="link.students.tutor" /></html:link> 
	</li>
	<li>
		<html:link page="<%= "/viewStudentCurriculum.do?method=prepareView"%>">
		    <bean:message key="link.student.curriculum" /></html:link>
	</li>
	<li>
		<html:link page="<%= "/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"%>">
		    <bean:message key="link.student.enrollment" /></html:link>
	</li>		
</ul>
