<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<h3><bean:message key="label.manager.insert.restriction.confirmation"/></h3>

<logic:equal name="className" value="RestrictionByNumberOfDoneCurricularCourses">
	<b><bean:message key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionDoneCurricularCourse">
	<b><bean:message key="label.manager.insert.restriction.restrictionDoneCurricularCourse"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionNotDoneCurricularCourse">
	<b><bean:message key="label.manager.insert.restriction.restrictionNotDoneCurricularCourse"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionDoneOrHasEverBeenEnrolledInCurricularCourse">
	<b><bean:message key="label.manager.insert.restriction.restrictionDoneOrHasEverBeenEnrolledInCurricularCourse"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse">
	<b><bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrIsCurrentlyEnrolledInCurricularCourse"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse">
	<b><bean:message key="label.manager.insert.restriction.restrictionHasEverBeenOrWillBeAbleToBeEnrolledInCurricularCourse"/></b>
</logic:equal>
<logic:equal name="className" value="RestrictionPeriodToApply">
	<b><bean:message key="label.manager.insert.restriction.restrictionPeriodToApply"/></b>
</logic:equal>

<bean:write name="number" />
