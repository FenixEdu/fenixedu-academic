<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<span class="error"><!-- Error messages go here --><html:errors property="error.default" /></span>
<h2><bean:message key="message.publishment" /></h2>
<br />
<logic:present name="siteView">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.publish.information" /></td>
		</tr>
	</table>
	<html:form action="/marksList">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="publishMarks"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />
		<table>
			<tr>
				<td>
					<bean:message key="message.publishmentMessage"/>:
				</td>
				<td>
					<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.publishmentMessage" rows="2" cols="60" property="publishmentMessage" ></html:textarea>
					<span class="error"><!-- Error messages go here --><html:errors property="publishmentMessage"/></span>
				</td>
			</tr>
			<tr>
				<td>		
					<bean:message key="message.sendSMS"/>		
				</td>
				<td>
					<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.sendSMS" property="sendSMS" disabled="true"/>
					<span class="error"><!-- Error messages go here --><html:errors property="sendSMS"/></span>
				</td>
				<td>
				<span class="error">Serviço Indisponível</span>
				</td>
			</tr>
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
	</html:form>
</logic:present>