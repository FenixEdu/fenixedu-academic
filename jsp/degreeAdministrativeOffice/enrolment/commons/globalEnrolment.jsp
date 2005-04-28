<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.welcome"/></h2>
<ul>
	<li>
		<html:link page="/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.enrollment"/></html:link>
	</li>
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=1&amp;userType=0"><bean:message key="link.student.enrollment.without.rules"/></html:link>
	</li>
	<li>
		<html:link page="/prepareEnrollmentChooseStudent.do"><bean:message key="link.student.enrollment.improvment"/></html:link>
	</li>
	<br />
	<br />
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=1&amp;userType=1"><bean:message key="link.student.enrollment.without.rules.super.user"/></html:link>
	</li>
</ul>