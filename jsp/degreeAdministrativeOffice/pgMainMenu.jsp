<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li>
		<html:link page="/curricularCourseEnrolmentWithRulesManager.do?method=preStart"><bean:message key="link.student.enrolment.with.rules"/></html:link>
	</li>
	<li>
		<html:link page="/home.do"><bean:message key="link.student.enrolment.without.rules"/></html:link>
	</li>
</ul>     