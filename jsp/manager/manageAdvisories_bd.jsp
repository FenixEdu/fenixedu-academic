<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="Util.AdvisoryRecipients" %>
<h2><bean:message key="title.manage.advisories"/></h2>
<br />
<span class="error">Nota: A validação deste form ainda não está a funcionar.</span>
<html:form action="/manageAdvisories">
	<html:hidden property="method" value="createAdvisory"/>
	<html:hidden property="page" value="1"/>

	<table>
		<tr>
			<td>
				<bean:message key="property.advisory.from"/>
			</td>
			<td>
				<html:text property="sender" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="property.advisory.subject"/>
			</td>
			<td>
				<html:text property="subject" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="property.advisory.expirationDate"/>
			</td>
			<td>
				<html:text property="experationDate" size="25"/> (yyyy/MM/dd hh:mm)
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<bean:message key="property.advisory.message"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
            	<html:textarea property="message"
            				   rows="8"
            				   cols="75"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<bean:message key="property.advisory.recipients"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio property="recipients" value="AdvisoryRecipients.STUDENTS"/>
				<bean:message key="property.advisory.recipients.students"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio property="recipients" value="AdvisoryRecipients.TEACHERS"/>
				<bean:message key="property.advisory.recipients.teachers"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio property="recipients" value="AdvisoryRecipients.EMPLOYEES"/>
				<bean:message key="property.advisory.recipients.employees"/>
			</td>
		</tr>
	</table>

	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="label.create"/>
	</html:submit>
	<html:reset styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>
