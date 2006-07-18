<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.util.AdvisoryRecipients" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.new.advisory"/></h2>
<br />

<span class="error"><html:errors/></span>

<html:form action="/manageAdvisories" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createAdvisory"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.from"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.sender" property="sender" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.subject"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.expirationDate"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.experationDate" property="experationDate" size="25"/> (dd/mm/aaaa hh:mm)
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.message"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
            	<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.message" property="message"
            				   rows="8"
            				   cols="75"/>
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.recipients"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.STUDENTS %>"/>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.recipients.students"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.TEACHERS %>"/>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.recipients.teachers"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.EMPLOYEES %>"/>
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="property.advisory.recipients.employees"/>
			</td>
		</tr>
	</table>

	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.create"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>
