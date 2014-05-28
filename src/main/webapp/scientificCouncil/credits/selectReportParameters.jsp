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

<h2><bean:message key="message.credits.top"/></h2>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<html:form action="/creditsReport">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method"/>


<table class="tstyle5 thlight thright thmiddle">
	<tr>
		<th><bean:message key="label.credits.choose.Period"/>:</th>
		<td>
			<bean:message key="label.credits.from"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.fromExecutionYearID" property="fromExecutionYearID" size="1">
				<html:options collection="executionYears" property="value" labelProperty="label"/>
			</html:select>
			<bean:message key="label.credits.until"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.untilExecutionYearID" property="untilExecutionYearID" size="1">
				<html:options collection="executionYears" property="value" labelProperty="label"/>
			</html:select>
		</td>
	</tr>
	<tr>
		<th>
			<bean:message key="label.credits.choose.Department"/>:
		</th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.departmentID" property="departmentID" size="1">
				<html:option value="0" key="label.allDepartments"/>
				<html:options collection="departments" property="externalId" labelProperty="realName"/>
			</html:select>
		</td>
	</tr>
</table>

	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewDetailedCreditsReport';">
			<bean:message key="button.detailed.credits.report"/>
		</html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" onclick="this.form.method.value='viewGlobalCreditsReport';">
			<bean:message key="button.global.credits.report"/>
		</html:submit>
	</p>
</html:form>	