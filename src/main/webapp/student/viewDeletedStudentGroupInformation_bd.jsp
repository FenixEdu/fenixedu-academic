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
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<bean:define id="attends" name="studentGroup" property="attends"/>
	<em><bean:message key="message.evaluationElements" /></em>
	<h2><bean:message key="title.deletedStudentGroup"/></h2>

<p>
		<html:link page="<%="/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;projectID=" + pageContext.findAttribute("projectID") %>">
			<bean:message key="label.return"/>
		</html:link>
</p>

	<p class="mtop15 mbottom1">
	<b><bean:message key="label.GroupNumber"/></b> <bean:write name="studentGroup" property="groupNumber"/></p>
	</p>

<table class="tstyle4">
	<tbody>   
	<tr>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>

<logic:iterate id="attend" name="attends">			
	

<tr>
				<td><bean:write name="attend" property="registration.student.number"/>
				</td>
				<td><bean:write name="attend" property="registration.student.person.name"/>
				</td>		
				<td>
					<logic:present name="attend" property="registration.student.person.email">
						<bean:define id="mail" name="attend" property="registration.student.person.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attend" property="registration.student.person.email"/></html:link>
					</logic:present>
					<logic:notPresent name="attend" property="registration.student.person.email">
						&nbsp;
					</logic:notPresent>
				</td>
			</tr>
</logic:iterate>
	</tbody>
</table>