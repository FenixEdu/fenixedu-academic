<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3>
	<bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;
	<bean:message key="Criar Precedência Simples"/>
</h3>

<ul style="list-style-type: square;">
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionByNumberOfCurricularCourses"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionByNumberOfCurricularCourses"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionByNumberOfDoneCurricularCourses"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionDoneCurricularCourse"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionDoneCurricularCourse"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionDoneOrHasEverBeenEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link page='<%="/makeSimplePrecedence.do?method=insertRestriction&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=RestrictionPeriodToApply"%>'>
		<bean:message key="label.manager.insert.restriction.restrictionPeriodToApply"/>
	</html:link></li>
</ul>
