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
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<bean:write name="executionPeriodName"/><br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<html:form action="/copySiteExecutionCourse">
	<input alt="input.method" type="hidden" name="method" value="showExecutionCourses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode" property="objectCode"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2" />
	
	<p class="infoop">
		<bean:message key="message.copySite.chooseDegreeAndYear"/>
	</p>
	<table>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.degree"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegree" property="executionDegree" size="1">
					<html:options collection="<%=PresentationConstants.DEGREES%>" property="value" labelProperty="label"/>
				</html:select>
				<br />
			</td>
		</tr>
		<tr>
			<td style="text-align:right">
				<bean:message key="property.context.curricular.year"/>
				:
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curYear" property="curYear" size="1">
					<html:options collection="<%=PresentationConstants.CURRICULAR_YEAR_LIST_KEY%>" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="button.continue"/>
	</html:submit>
</html:form>