<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<bean:write name="executionPeriodName"/>

<html:form action="/editExecutionCourseChooseExDegree">
	<input type="hidden" name="method" value="prepareEditExecutionCourse"/>
	<html:hidden property="executionPeriod"/>
	<html:hidden property="page" value="2" />
	
	<p class="infoop">
		<bean:message key="message.manager.executionCourseManagement.chooseLinked"/>
	</p>
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.degree"/>:
			</td>
			<td>
				<html:select property="executionDegree" size="1">
					<html:options collection="<%=SessionConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.curricular.year"/>:
			</td>
			<td>
				<html:select property="curYear" size="1">
					<html:options collection="<%=SessionConstants.CURRICULAR_YEAR_LIST_KEY%>" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<p class="infoop">
		<bean:message key="message.manager.executionCourseManagement.chooseNotLinked" />
	</p>
	<p>
		<html:checkbox property="executionCoursesNotLinked"/>
		<bean:message key="label.manager.executionCourseManagement.executionCoursesNotLinked" />
	</p>
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.manager.executionCourseManagement.continue"/>
	</html:submit>
</html:form>