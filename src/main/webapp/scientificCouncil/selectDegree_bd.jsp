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
<bean:define id="degreeList" name="component" property="degrees"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.public.degree.choose"/></td>
          </tr>
</table>
	<br />
<center><table  >
<tr><th class="listClasses-header"><bean:message key="label.degree"/> </th></tr>
<logic:iterate id="degree" name="degreeList">
<tr>
<bean:define id="degreeId" name="degree" property="externalId"/>
<td class="listClasses" ><html:link page="<%= "/curricularCourseManager.do?method=showDegreeCurricularPlans&index=" + degreeId %>"><bean:write name="degree" property="presentationName"/></html:link></td>
</tr>
</logic:iterate>
</table></center>

 
	
    