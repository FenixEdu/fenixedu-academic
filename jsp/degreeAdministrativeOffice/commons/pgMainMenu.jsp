<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<ul>
	<li>
		<html:link page="<%="/viewStudentCurriculum.do?method=prepareView"%>"><bean:message key="link.student.curriculum"/></html:link>
	</li>

	<li>
		<html:link forward="enrolment"><bean:message key="link.student.enrolment"/></html:link>
	</li>

	<li>
		<html:link page="/changeStudentAreas.do?method=chooseStudent&amp;degreeType=1"><bean:message key="title.student.change.areas"/></html:link>
	</li>

	<li> 
		<html:link forward="equivalence"><bean:message key="link.manual.equivalence"/></html:link>
	</li>
	<li>
		<html:link page="/generateDeclaration.do?method=prepare&amp;page=0"><bean:message key="title.student.print.registrationDeclaration"/></html:link>
	</li>
	<li>
		<html:link page="/generateEnrollmentsDeclaration.do?method=prepare&amp;page=0"><bean:message key="title.student.print.enrollmentsDeclaration"/></html:link>
	</li>
</ul>     