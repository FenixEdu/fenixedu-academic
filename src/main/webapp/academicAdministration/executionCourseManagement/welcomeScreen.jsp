<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.executionCourseManagement"/></h2>
<logic:messagesPresent message="true" property="success">	<p>		<span class="success0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent><logic:messagesPresent message="true" property="error">	<p>		<span class="error0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent>
<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome1.intro"/><br/>					<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome2.insert"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome3.edit"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome4.merge"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome5.reports"/><br/>			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome6.create"/>
		</td>
	</tr>
</table>
