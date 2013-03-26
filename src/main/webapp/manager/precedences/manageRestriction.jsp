<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionByNumberOfDoneCurricularCourses" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionDoneCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionNotDoneCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionNotEnrolledInCurricularCourse" %>
<%@ page import="net.sourceforge.fenixedu.domain.precedences.RestrictionPeriodToApply" %>



<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.simple.precedence"/></h3>

<ul style="list-style-type: square;">
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionByNumberOfDoneCurricularCourses.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionDoneCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionDoneCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionNotDoneCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionNotDoneCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionDoneOrHasEverBeenEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionNotEnrolledInCurricularCourse.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionNotEnrolledCurricularCourse"/>
	</html:link></li>
	<li><html:link module="/manager" page='<%="/makeSimplePrecedence.do?method=chooseRestriction&amp;page=0&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;className=" + RestrictionPeriodToApply.class.getName() %>'>
		<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionPeriodToApply"/>
	</html:link></li>
</ul>
