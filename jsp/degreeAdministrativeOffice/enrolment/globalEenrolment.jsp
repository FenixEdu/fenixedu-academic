<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message key="tilte.welcome"/></h2>
<ul>
	<li>
		<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentWithRules"><bean:message key="link.student.enrolment.with.rules"/></html:link>
	</li>
	<li>
		<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentWithoutRules"><bean:message key="link.student.enrolment.without.rules"/></html:link>
	</li>
	<li>
		<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForEnrolmentInOptionalWithoutRules"><bean:message key="link.student.enrolment.in.optional.curricular.course.without.rules"/></html:link>
	</li>
</ul>     