<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

	<%--<html:form action="/makeSimplePrecedence.do">
		
	</html:form>--%>
<logic:equal name="className" value="RestrictionByNumberOfDoneCurricularCourses">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionDoneCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionDoneCurricularCourse"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionNotDoneCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionNotDoneCurricularCourse"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionDoneOrHasEverBeenEnrolledInCurricularCourse"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionPeriodToApply">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionPeriodToApply"/></h3>
</logic:equal>
