<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<h2><bean:message key="label.manager.executionCourseManagement.insert.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/insertExecutionCourse" focus="name">  
		<html:hidden property="page" value="1"/>
		<html:hidden property="method" value="insertExecutionCourse"/>
		<table>
			<tr>
				<td>
					<bean:message key="label.manager.executionCourseManagement.executionPeriod"/>
				</td>
				<td>
					<html:select property="executionPeriodId">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.execution.course.name"/>
				</td>
				<td>
					<html:text size="30" property="name" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.execution.course.code"/>
				</td>
				<td>
					<html:text size="5" property="code" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.theoreticalHours"/>
				</td>
				<td>
					<html:text size="5" property="theoreticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.praticalHours"/>
				</td>
				<td>
					<html:text size="5" property="praticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.theoPratHours"/>
				</td>
				<td>
					<html:text size="5" property="theoPratHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.labHours"/>
				</td>
				<td>
					<html:text size="5" property="labHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message key="message.manager.execution.course.comment"/>
				</td>
				<td>
					<html:textarea property="comment" rows="3" cols="45"/>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message key="button.save"/></html:submit>
		<html:reset  styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.period.none"/>
	</span>
</logic:notPresent>