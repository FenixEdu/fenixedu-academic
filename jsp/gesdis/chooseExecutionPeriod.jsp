<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<br/>
<bean:message key="message.copySite.information.destination" />
<strong><bean:write name="siteView" property="commonComponent.executionCourse.nome"/></strong>
<bean:message key="message.copySite.information.destination.ofperiod" />
<strong>
<bean:write name="siteView" property="commonComponent.executionCourse.infoExecutionPeriod.name"/>
<bean:write name="siteView" property="commonComponent.executionCourse.infoExecutionPeriod.infoExecutionYear.year"/>
</strong>
<bean:message key="message.copySite.information.whatIsCopied" />
<br/><br/>
<span class="error"><html:errors/></span>
<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<html:form action="/copySiteExecutionCourse">  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="prepareChooseExecDegreeAndCurYear"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
		<table>
			<tr>
				<td>
					<bean:message key="label.copySite.execution.period"/>
				</td>
				<td>
					<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriod" property="executionPeriod">
						<html:option value="" key="label.copySite.select.one">
							<bean:message key="label.copySite.select.one"/>
						</html:option>
						<html:optionsCollection name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>"/>
					</html:select>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.continue"/></html:submit>
	</html:form>
</logic:present>
<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
	<span class="error">
		<html:errors /><bean:message key="error.copySite.noExecutionPeriods"/>
	</span>
</logic:notPresent>