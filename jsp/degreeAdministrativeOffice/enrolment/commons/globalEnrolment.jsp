<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="tilte.welcome"/></h2>
<ul>
	<li>
		<html:link page="/curricularCoursesEnrollment.do?method=prepareEnrollmentChooseStudent"><bean:message key="link.student.LEEC.enrollment"/></html:link>
	</li>
	<li>
		<html:link page="/courseEnrolmentWithoutRulesManagerDA.do?method=prepareEnrollmentChooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="link.student.LEEC.enrollment.without.rules"/></html:link>
	</li>

	<li>
		<html:link page="/optionalCoursesEnrolmentManagerDA.do?method=chooseStudentAndExecutionYear&amp;degreeType=1"><bean:message key="title.student.LEEC.optional.enrollment"/></html:link>
	</li>

</ul>