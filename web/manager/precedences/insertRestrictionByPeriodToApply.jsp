<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:equal name="className" value="RestrictionPeriodToApply">
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionPeriodToApply"/></h3>
</logic:equal>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<br />
<br />
<bean:define id="classNameChosen" name="className" />
<bean:define id="degreeId" name="degreeId" />
<bean:define id="degreeCurricularPlanId" name="degreeCurricularPlanId" />

<html:form action="/makeSimplePrecedence.do">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertRestriction" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.className" property="className" value="<%= request.getAttribute("className").toString() %>" />	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getAttribute("degreeId").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getAttribute("degreeCurricularPlanId").toString() %>" />
			
	<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.periodToApplyRestriction" />:</b>&nbsp;
	<html:select bundle="HTMLALT_RESOURCES" altKey="select.number" property="number" >
		<logic:iterate id="periodToApply" name="periodToApplyList">
			<bean:define id="keyPeriod" name="periodToApply" property="name"/>
			<bean:write name="keyPeriod" />
			<bean:define id="valuePeriod" name="periodToApply" property="value"/>
			<bean:write name="valuePeriod" />
			
			<html:option key="<%= keyPeriod.toString() %>" value="<%= valuePeriod.toString() %>"/>
		</logic:iterate>
	</html:select>
	<p />
	<table>
		<tr>
			<th colspan="2"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseToAddPrecedence" /></th>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<logic:iterate id="curricularCourse" name="curricularCoursesList">
			<tr>
				<td><bean:write name="curricularCourse" property="name" /> - <strong><bean:write name="curricularCourse" property="code"/></strong></td>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.curricularCourseToAddPrecedenceID" property="curricularCourseToAddPrecedenceID" idName="curricularCourse" value="idInternal"/></td>				
			</tr>
		</logic:iterate>
	</table>	
	<p />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.continue"/>
	</html:submit>
</html:form>
