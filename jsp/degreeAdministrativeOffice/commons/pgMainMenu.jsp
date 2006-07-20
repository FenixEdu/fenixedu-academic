<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

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
	<li><html:link page="/changeDegree.do?method=prepare"><bean:message key="link.change.degree"/></html:link></li>
	<li><html:link page="/markSheetManagement.do?method=prepareSearchMarkSheet"><bean:message key="link.markSheet.management"/></html:link></li>
	<li class="navheader"><bean:message key="label.navheader.equivalences" /></li>
	<li><html:link page="/prepareNotNeedToEnroll.do"><bean:message key="link.notNeedToEnroll" /></html:link></li>
	<li><html:link page="/curricularCourseEquivalencies.do?method=prepare"><bean:message key="label.navheader.equivalences" /></html:link></li>
	<li class="navheader"><bean:message key="label.navheader.curriculums" /></li>
	<li><html:link page="/curricularPlans/chooseCurricularPlan.faces"><bean:message key="link.consultCurriculum"/></html:link></li>
	<%-- 
	<li><html:link page="/curricularPlans/curricularPlansManagement.faces"><bean:message key="link.curricularPlansManagement"/></html:link></li>
	
    <li><html:link page="/payments.do?method=prepareSearchPerson"><bean:message key="link.payments" /></html:link></li>
    <li><html:link page="/pricesManagement.do?method=viewPrices"><bean:message key="link.pricesManagement" /></html:link></li>
    --%>
</ul>     