<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<span class="error"><html:errors property="error.default" /></span>
<h2><bean:message key="message.publishment" /></h2>
<br />
<logic:present name="siteView">
	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.publish.information" /></td>
		</tr>
	</table>
	<html:form action="/marksList">
		<html:hidden property="method" value="publishMarks"/>
		<html:hidden property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />
		<table>
			<tr>
				<td>
					<bean:message key="message.publishmentMessage"/>:
				</td>
				<td>
					<html:textarea rows="2" cols="60" property="publishmentMessage" ></html:textarea>
					<span class="error"><html:errors property="publishmentMessage"/></span>
				</td>
			</tr>
			<tr>
				<td>		
					<bean:message key="message.sendSMS"/>		
				</td>
				<td>
					<html:checkbox property="sendSMS"/>
					<span class="error"><html:errors property="sendSMS"/></span>
				</td>
				<td>
				<span class="error">Serviço Indisponível</span>
				</td>
			</tr>
		</table>
		<br />
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
	</html:form>
</logic:present>