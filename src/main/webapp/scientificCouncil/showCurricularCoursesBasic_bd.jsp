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
<bean:define id="basicCurricularCourses" name="component" property="basicCurricularCourses"/>
<bean:define id="nonBasicCurricularCourses" name="component" property="nonBasicCurricularCourses"/>
<bean:define id="curricularPlan" name="component" property="infoDegreeCurricularPlan"/>

<span class="error"><!-- Error messages go here --><html:errors /></span>
<table width="98%" border="0" cellpadding="0" cellspacing="0">
          <tr>
            <td class="infoop"><bean:message key="message.basicCurricularCoursesSelection"/></td>
          </tr>
</table>
	<br />
<html:form action="/basicCurricularCourseManager">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="setBasicCurricularCourses"/>
<bean:define id="curricularPlanId" name="curricularPlan" property="externalId"/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularIndex" property="curricularIndex" value="<%= curricularPlanId.toString() %>"/>
<table>
<tr><td>&nbsp;</td><td>&nbsp;</td></tr>
<logic:iterate id="infoCurricularCourse" name="basicCurricularCourses">
<tr>

<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.basicCurricularCourses"  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="externalId"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>
<logic:iterate id="infoCurricularCourse" name="nonBasicCurricularCourses">
<tr>

<td><html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.basicCurricularCourses"  property="basicCurricularCourses"><bean:write name="infoCurricularCourse" property="externalId"/> </html:multibox ></td>
<td><bean:write name="infoCurricularCourse" property="name"/></td>
</tr>
</logic:iterate>


</table>
<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
</html:form>