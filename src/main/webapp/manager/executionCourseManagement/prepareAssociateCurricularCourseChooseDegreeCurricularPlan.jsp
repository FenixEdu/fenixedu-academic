<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<bean:write name="executionPeriodName"/>
<logic:present name="executionDegreeName">
	<logic:notEmpty name="executionDegreeName">
		&gt; <bean:write name="executionDegreeName"/>
	</logic:notEmpty>
</logic:present>	
&gt; <bean:write name="executionCourseName"/>

<table>
	<tr>
<html:form action="/editExecutionCourseManageCurricularCourses">
	<input alt="input.method" type="hidden" name="method" value="prepareAssociateCurricularCourse"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear"/>				
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
	
	<b><bean:message bundle="MANAGER_RESOURCES" key="link.manager.executionCourseManagement.associate"/></b>
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseDegree"/>
	</p>
		<td colspan="2">
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message bundle="MANAGER_RESOURCES" key="property.context.degree"/>:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlan" property="degreeCurricularPlan" size="1">
					<html:options collection="<%=PresentationConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
	</table>
	<br />
		</td>
	</tr>
	<tr>
		<td width="1px">
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
	</html:submit>
		</td>
</html:form>
		<td align="left">
			<bean:define id="executionCoursesNotLinkedValue" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
			<fr:form action="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=" + pageContext.findAttribute("executionCourseId").toString() %>">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" value="<%= pageContext.findAttribute("executionPeriod").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
				<logic:notEqual name="executionCoursesNotLinkedValue" value="true">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />	
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear" value="<%= pageContext.findAttribute("curYear").toString() %>" />
				</logic:notEqual>
				
				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.cancel"/>
				</html:submit>
			</fr:form>
 		</td>
	</tr>
</table>