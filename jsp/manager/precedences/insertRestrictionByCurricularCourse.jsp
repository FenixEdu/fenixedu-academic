<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
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

<span class="error"><html:errors/></span>
<br />
<br />
<bean:define id="classNameChosen" name="className" />
<bean:define id="degreeId" name="degreeId" />
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId" />

<html:form action="/makeSimplePrecedence.do">
	<html:hidden property="page" value="0" />
	<html:hidden property="method" value="insertRestriction" />
	<html:hidden property="className" value="<%= request.getAttribute("className").toString() %>" />	
	<html:hidden property="degreeId" value="<%= request.getAttribute("degreeId").toString() %>" />
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getAttribute("degreeCurricularPlanId").toString() %>" />
			
	<p />
	<table>
		<tr>
			<th colspan="2" align="center"><bean:message key="label.manager.curricularCourseToAddPrecedence" /></th>
			<th colspan="2" align="center"><bean:message key="label.manager.precedentCurricularCourse" /></th>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<logic:iterate id="curricularCourse" name="curricularCoursesList">
			<tr>
				<td><bean:write name="curricularCourse" property="name" /></td>
				<td><html:radio property="curricularCourseToAddPrecedenceID" idName="curricularCourse" value="idInternal"/></td>				
				<td><html:radio property="precedentCurricularCourseID" idName="curricularCourse" value="idInternal"/></td>
				<td align="right"><bean:write name="curricularCourse" property="name" /></td>
			</tr>
		</logic:iterate>
	</table>	
	<p />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.continue"/>
	</html:submit>
</html:form>