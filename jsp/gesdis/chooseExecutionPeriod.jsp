<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<br>
<bean:message key="message.copySite.information" />
<br><br>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/copySiteExecutionCourse">  
		<html:hidden property="method" value="prepareChooseExecDegreeAndCurYear"/>
		<html:hidden property="objectCode"/>
		<html:hidden property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message key="label.copySite.execution.period"/>
				</td>
				<td>
					<html:select property="executionPeriod">
						<html:option value="" key="label.copySite.select.one">
							<bean:message key="label.copySite.select.one"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message key="error.copySite.noExecutionPeriods"/>
	</span>
</logic:notPresent>