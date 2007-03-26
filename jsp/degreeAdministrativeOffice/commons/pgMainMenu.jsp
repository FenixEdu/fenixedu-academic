<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<html:xhtml/>

<ul>
	<li><html:link href='<%= request.getContextPath() + "/dotIstPortal.do?prefix=/degreeAdministrativeOffice&amp;page=/index.do" %>'><bean:message key="begin"/></html:link></li>

	<li class="navheader"><bean:message key="label.navheader.student" /></li>
<%-- 
	<li><html:link page="<%="/viewStudentCurriculum.do?method=prepareView"%>"><bean:message key="link.student.curriculum"/></html:link></li>
	<li><html:link page="<%="/viewStudentInformation.do?method=prepareView"%>"><bean:message key="link.student.information"/></html:link></li>
--%>
	<li><html:link forward="enrolment"><bean:message key="link.student.enrolment"/></html:link></li>
	<li><html:link page="/changeStudentAreas.do?method=chooseStudent&amp;degreeType=DEGREE"><bean:message key="link.student.areas"/></html:link></li>

	<li class="navheader"><bean:message key="label.navheader.equivalences" /></li>
	<li><html:link page="/showNotNeedToEnroll.do?method=prepare"><bean:message key="link.notNeedToEnroll" /></html:link></li>
	<li><html:link page="/curricularCourseEquivalencies.do?method=prepare"><bean:message key="label.navheader.equivalences" /></html:link></li>
	<li class="navheader"><bean:message key="label.navheader.curriculums" /></li>
	<li><html:link page="/curricularPlans/chooseCurricularPlan.faces"><bean:message key="link.consultCurriculum"/></html:link></li>

<%-- 
	<li class="navheader"><bean:message key="label.documentRequestsManagement.documents" /></li>
	<li><html:link page="/generateEnrollmentsDeclaration.do?method=prepare&amp;page=0"><bean:message key="link.student.print.enrollmentsDeclaration"/></html:link></li>
	<li><html:link page="/declarations.do?method=search"><bean:message key="link.declarations" /></html:link></li>
--%>

</ul>
