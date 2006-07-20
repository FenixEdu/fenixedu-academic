<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.insert.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/insertExecutionCourse" focus="name">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertExecutionCourse"/>
		<table>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionPeriod"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodId" property="executionPeriodId">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="30" property="name" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="5" property="code" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" size="5" property="theoreticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" size="5" property="praticalHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" size="5" property="theoPratHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.labHours" size="5" property="labHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.seminaryHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.seminaryHours" size="5" property="seminaryHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.problemsHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.problemsHours" size="5" property="problemsHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.fieldWorkHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.fieldWorkHours" size="5" property="fieldWorkHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.trainingPeriodHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.trainingPeriodHours" size="5" property="trainingPeriodHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.tutorialOrientationHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.tutorialOrientationHours" size="5" property="tutorialOrientationHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
				</td>
				<td>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment" rows="3" cols="45"/>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.clear"/></html:reset>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="errors.execution.period.none"/>
	</span>
</logic:notPresent>