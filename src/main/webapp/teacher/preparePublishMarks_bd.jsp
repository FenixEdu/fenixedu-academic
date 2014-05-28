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
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<span class="error"><!-- Error messages go here --><html:errors property="error.default" /></span>
<h2><bean:message key="message.publishment" /></h2>
<br />
<logic:present name="siteView">
	<table width="98%" cellpadding="0" cellspacing="0">
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
				<span class="error"><!-- Error messages go here -->Serviço Indisponível</span>
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