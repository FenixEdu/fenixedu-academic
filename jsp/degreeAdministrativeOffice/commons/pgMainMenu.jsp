<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li>
		<html:link page="/globalEnrolment.do"><bean:message key="link.student.enrolment"/></html:link>
	</li>
	<li>
		<html:link page="/functionRedirect.do?method=chooseStudentAndDegreeTypeForManualEquivalence"><bean:message key="link.manual.equivalence"/></html:link>
	</li>
</ul>     