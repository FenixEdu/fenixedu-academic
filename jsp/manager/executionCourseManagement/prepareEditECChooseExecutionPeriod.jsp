<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/editExecutionCourseChooseExPeriod">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareEditECChooseExecDegreeAndCurYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionPeriod"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod">
						<html:option value="" key="label.manager.executionCourseManagement.select">
							<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.select"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/></html:submit>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="error.manager.executionCourseManagement.noExecutionPeriods"/>
	</span>
</logic:notPresent>