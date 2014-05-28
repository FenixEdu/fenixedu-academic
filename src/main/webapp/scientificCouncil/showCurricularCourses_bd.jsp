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
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>

<bean:define id="component" name="siteView" property="component"/>
<bean:define id="curricularCourses" name="component" property="curricularCourses"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<center><table>
<tr>
<th class="listClasses-header"><bean:message key="label.name"/></th>
<th class="listClasses-header"><bean:message key="label.curricularCourseType"/></th>
</tr>
<logic:iterate id="infoCurricularCourse" name="curricularCourses">
<tr>
<bean:define id="curricularCourseId" name="infoCurricularCourse" property="externalId"/>
<td class="listClasses">
<html:link page="<%= "/curricularCourseManager.do?method=viewCurriculum&index=" + curricularCourseId %>" >
	<bean:write name="infoCurricularCourse" property="name"/></td>
</html:link>
<td class="listClasses"><bean:write name="infoCurricularCourse" property="ownershipType"/></td>
</tr>
</logic:iterate>
</table></center>