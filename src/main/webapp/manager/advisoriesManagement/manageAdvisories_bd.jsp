<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.util.AdvisoryRecipients" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="link.new.advisory"/></h2>
<br />

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/manageAdvisories" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createAdvisory"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.from"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.sender" property="sender" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.subject"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.subject" property="subject" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.expirationDate"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.experationDate" property="experationDate" size="25"/> (dd/mm/aaaa hh:mm)
			</td>
		</tr>
		<tr>
			<td colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.message"/>
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
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.recipients"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.STUDENTS %>"/>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.recipients.students"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.TEACHERS %>"/>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.recipients.teachers"/>
			</td>
		</tr>
		<tr>
			<td>
			</td>
			<td>
				<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.recipients" property="recipients" value="<%= "" + AdvisoryRecipients.EMPLOYEES %>"/>
				<bean:message bundle="MANAGER_RESOURCES" key="property.advisory.recipients.employees"/>
			</td>
		</tr>
	</table>

	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.create"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>
