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
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<span class="error"><!-- Error messages go here --><html:errors property="error.default" /></span>

<h2><bean:message key="label.submit.listMarks" /></h2>
<br /><br />

<logic:present name="siteView">
	<table width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop"><bean:message key="label.submit.information" /></td>
		</tr>
	</table>

<html:form action="/marksList">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>	
<table>
<tr>
	<td>
		<bean:message key="label.data.avaliacao"/>:
	</td>
	<td> 
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.day" property="day" size="2" maxlength="2" />&nbsp;/&nbsp;
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.month" property="month" size="2" maxlength="2"/>&nbsp;/&nbsp;
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.year" property="year" size="4" maxlength="4"/>
	</td>
</tr>
</table>
<br />

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="submitMarks"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.evaluationCode" property="evaluationCode" value="<%= pageContext.findAttribute("evaluationCode").toString() %>" />

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
	<bean:message key="button.save"/>
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>			
</html:form>
</logic:present>