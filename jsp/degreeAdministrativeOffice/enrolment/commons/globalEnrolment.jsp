<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.student.curricularEnrollments"/></h2>
<ul>
	<li>
		<html:link page="/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.enrollment"/></html:link>
	</li>
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=DEGREE&amp;userType=0"><bean:message key="link.student.enrollment.without.rules"/></html:link>
	</li>
	<li>
		<html:link page="/improvmentEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.enrollment.improvment"/></html:link>
	</li>
	<br />
	<br />
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=DEGREE&amp;userType=1"><bean:message key="link.student.enrollment.without.rules.super.user"/></html:link>
	</li>
</ul>