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
		<html:link forward="equivalenceForDegreeAdministrativeOffice"><bean:message key="link.manual.equivalence"/></html:link>
	</li>
</ul>     