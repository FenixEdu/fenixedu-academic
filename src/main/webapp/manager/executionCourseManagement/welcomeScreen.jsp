<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%><%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message bundle="MANAGER_RESOURCES"  key="label.manager.executionCourseManagement"/></h2>
<logic:messagesPresent message="true" property="success">	<p>		<span class="success0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent><logic:messagesPresent message="true" property="error">	<p>		<span class="error0">			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">				<bean:write name="messages" />			</html:messages>		</span>	</p></logic:messagesPresent>
<table>
	<tr>
		<td class="infoop">
			<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.welcome"/>		
		</td>
	</tr>
</table>
