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

<h2><bean:message key="message.credits.masterDegree.title"/></h2>

<html:form action="/masterDegreeCreditsManagement">

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewMasterDegreeCredits"/>

	<table class="tstyle5 thlight thright thmiddle">
		<tr>
			<th><bean:message key="label.credits.choose.ExecutionYear"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionYearID" property="executionYearID" size="1" onchange="this.form.method.value='prepare';this.form.submit();">
					<html:options collection="executionYears" property="externalId" labelProperty="year"/>
				</html:select>
			</td>
		</tr>
		<tr>
			<th><bean:message key="label.credits.choose.masterDegreeCurricularPlan"/>:</th>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionDegreeID" property="executionDegreeID" size="1" >
					<html:options collection="masterDegreeExecutions" property="externalId" labelProperty="degreeCurricularPlan.name"/>
				</html:select>
			</td>
		</tr>
	</table>
	
	<p>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="visualize"/>
		</html:submit>
	</p>
	
</html:form>