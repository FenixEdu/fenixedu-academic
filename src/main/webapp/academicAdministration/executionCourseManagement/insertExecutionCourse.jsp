<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.insert.executionCourse"/></h2>
<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<logic:present name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/insertExecutionCourse" focus="name">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertExecutionCourse"/>
		<table>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionPeriod"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="30" property="name" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="5" property="code" />
				</td>
			</tr>			
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
				</td>
				<td>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment" rows="3" cols="45"/>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.entry.phase"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" property="entryPhase">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="entryPhases"/>
					</html:select>
				</td>
			</tr>			
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="label.clear"/></html:reset>
	</html:form>
</logic:present>
<logic:notPresent name="<%= PresentationConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error"><!-- Error messages go here -->
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" key="errors.execution.period.none"/>
	</span>
</logic:notPresent>