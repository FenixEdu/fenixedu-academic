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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<script language="Javascript" type="text/javascript">
<!--
function changeMethod(){
	document.forms[0].method.value="showDistributedTests";
}
// -->
</script>

<logic:present name="canDelete">
	<h2><bean:message key="link.removeTest"/></h2>
	<html:form action="/testDistribution">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteDistributedTest"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%=(pageContext.findAttribute("objectCode")).toString()%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.distributedTestCode" property="distributedTestCode" value="<%=(pageContext.findAttribute("distributedTestCode")).toString()%>"/>
		<br/>
		<logic:equal name="canDelete" value="true">
			<bean:message key="message.confirm.deleteDistributedTest"/>
			<br/>
			<br/>
			<table align="center">
				<tr>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.confirm"/></html:submit></td>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
				</tr>
			</table>
		</logic:equal>

		<logic:notEqual name="canDelete" value="true">
			<bean:message key="message.cantDeleteDistributedTest"/>
			<br/>
			<br/>
			<table align="center">
				<tr>
					<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="changeMethod()"><bean:message key="label.back"/></html:submit></td>
				</tr>
			</table>
		</logic:notEqual>
	</html:form>
</logic:present>