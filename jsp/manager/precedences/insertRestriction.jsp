<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:equal name="className" value="RestrictionByNumberOfCurricularCourses">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionByNumberOfCurricularCourses"/></h3>
	
	<html:form action="/makeSimplePrecedence.do">
		
	</html:form>
</logic:equal>
<logic:equal name="className" value="RestrictionByNumberOfDoneCurricularCourses">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/></h3>
</logic:equal>
<logic:equal name="className" value="RestrictionDoneCurricularCourse">
	<h3><bean:message key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message key="label.manager.insert.restriction.restrictionDoneCurricularCourse"/></h3>
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
