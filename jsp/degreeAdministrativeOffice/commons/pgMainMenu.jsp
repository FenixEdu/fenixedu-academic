<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<ul>
	<li>
		<html:link page="/setStartMode.do?method=withRules"><bean:message key="link.student.enrolment.with.rules"/></html:link>
	</li>
	<li>
		<html:link page="/setStartMode.do?method=withoutRules"><bean:message key="link.student.enrolment.without.rules"/></html:link>
	</li>
	<li>
		<html:link page="/setStartMode.do?method=optionalWithoutRules"><bean:message key="link.student.enrolment.in.optional.curricular.course.without.rules"/></html:link>
	</li>
</ul>     