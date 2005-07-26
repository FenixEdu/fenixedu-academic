<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<style>@import url(<%= request.getContextPath() %>/CSS/navlateralnew.css);</style> <!-- Import new CSS for this section: #navlateral  -->


<ul>
	<li class="navheader"><bean:message key="label.navheader.student" /></li>
	<li><html:link page="<%="/viewStudentCurriculum.do?method=prepareView"%>"><bean:message key="link.student.curriculum"/></html:link></li>
	<li><html:link page="<%="/viewStudentInformation.do?method=prepareView"%>"><bean:message key="link.student.information"/></html:link></li>
	<li><html:link forward="enrolment"><bean:message key="link.student.enrolment"/></html:link></li>
	<li><html:link page="/changeStudentAreas.do?method=chooseStudent&amp;degreeType=DEGREE"><bean:message key="link.student.areas"/></html:link></li>
	<li class="navheader"><bean:message key="label.navheader.declarations" /></li>
	<li><html:link page="/generateDeclaration.do?method=prepare&amp;page=0"><bean:message key="link.student.print.registrationDeclaration"/></html:link></li>
	<li><html:link page="/generateEnrollmentsDeclaration.do?method=prepare&amp;page=0"><bean:message key="link.student.print.enrollmentsDeclaration"/></html:link></li>
	<li class="navheader"><bean:message key="label.navheader.marksSheet" /></li>
	<li><html:link page="/chooseExecutionYearAndDegreeCurricularPlan.do?method=prepare"><bean:message key="link.consult" /></html:link></li>
	<li><html:link page="/changeDegree/changeDegree.faces"><bean:message key="link.change.degree"/></html:link></li>
</ul>     