<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="sendMailToAllStudents.do" method="get">
	<table>
		<tr>
			<td>
				<b><bean:message key="label.email.FromName"/></b>
			</td>
			<td>
				<html:text property="fromName" size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.From"/></b>
			</td>
			<td>
				<html:text property="from"  size="50"/>
			</td>
		</tr>
		<tr>
			<td>
				<b><bean:message key="label.email.Subject"/></b>
			</td>
			<td>
				<html:text property="subject"  size="50"/>
			</td>
		</tr>
		<tr valign="top">
			<td  valign="top">
				<b><bean:message key="label.email.Body"/></b>
			</td>
			<td>
				<html:textarea cols="50" rows="10" property="text"/>
			</td>
		</tr>
	</table>
	<html:hidden property="shiftCode"/>
	<html:hidden property="studentGroupCode"/>
	<html:hidden property="objectCode"/>
	<html:hidden property="method" value="send"/>
	<html:submit property="submition"><bean:message key="button.sendMail"/></html:submit>
</html:form>
