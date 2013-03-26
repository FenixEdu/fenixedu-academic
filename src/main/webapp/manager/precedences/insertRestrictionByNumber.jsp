<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.precedences.management"/>&nbsp;-&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.simple.precedence"/>&nbsp;-&nbsp;<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.restriction.restrictionByNumberOfDoneCurricularCourses"/></h3>

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
			
	<b><bean:message bundle="MANAGER_RESOURCES" key="label.manager.numberOfDoneCurricularCourses" />:</b>&nbsp;<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" property="number" />	
	<p />
	<table>
		<tr>
			<th colspan="2" align="center"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseToAddPrecedence" /></th>
		</tr>
		<tr>
			<td colspan="4">&nbsp;</td>
		</tr>
		<logic:iterate id="curricularCourse" name="curricularCoursesList">
			<tr>
				<td><bean:write name="curricularCourse" property="name"/> - <strong><bean:write name="curricularCourse" property="code"/></strong></td>
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.curricularCourseToAddPrecedenceID" property="curricularCourseToAddPrecedenceID" idName="curricularCourse" value="idInternal"/></td>				
			</tr>
		</logic:iterate>
	</table>	
	<p />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.continue"/>
	</html:submit>
</html:form>
