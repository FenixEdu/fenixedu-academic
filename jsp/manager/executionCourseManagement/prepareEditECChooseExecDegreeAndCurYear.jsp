<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<bean:write name="executionPeriodName"/>
<p>
	<html:link module="/manager" module="/manager" action="/createSites" paramId="executionPeriodID" paramName="executionPeriodId">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.create.sites.for.execution.period"/>
	</html:link>
</p>
<html:form action="/editExecutionCourseChooseExDegree">
	<input alt="input.method" type="hidden" name="method" value="prepareEditExecutionCourse"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
	
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseLinked"/>
	</p>
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degree"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegree" property="executionDegree" size="1">
					<html:options collection="<%=SessionConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularYear"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curYear" property="curYear" size="1">
					<html:options collection="<%=SessionConstants.CURRICULAR_YEAR_LIST_KEY%>" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<p class="infoop">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.chooseNotLinked" />
	</p>
	<p>
		<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.executionCoursesNotLinked" property="executionCoursesNotLinked" value="true"/>
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionCoursesNotLinked" />
	</p>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
	</html:submit>
</html:form>