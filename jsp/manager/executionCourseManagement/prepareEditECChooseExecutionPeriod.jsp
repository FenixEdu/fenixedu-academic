<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/editExecutionCourse">  
		<html:hidden property="method" value="prepareEditECChooseExecDegreeAndCurYear"/>
		<table>
			<tr>
				<td>
					<bean:message key="label.manager.executionCourseManagement.executionPeriod"/>
				</td>
				<td>
					<html:select property="executionPeriodId">
						<html:option value="-1" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message key="button.manager.executionCourseManagement.continue"/></html:submit>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message key="error.manager.executionCourseManagement.noExecutionPeriods"/>
	</span>
</logic:notPresent>