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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<p>
	<strong>
		<bean:message key="label.inquiries.chooseCurricularPlan" bundle="INQUIRIES_RESOURCES"/>:
	</strong>	
</p>

<html:form action="/teachingStaff">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="selectExecutionDegree"/>
	<bean:define id="executionYearID" type="java.lang.String" name="executionYear" property="externalId"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionYearID" property="executionYearID" value="<%= executionYearID.toString() %>"/>

	<logic:present name="degreeCurricularPlans">
		<table>
			<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans">
					<tr>
						<td>
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.degreeCurricularPlanID" property="degreeCurricularPlanID" idName="degreeCurricularPlan" value="externalId" />
						</td>
						<td>
							<bean:message name="degreeCurricularPlan" property="infoDegree.degreeType.name" bundle="ENUMERATION_RESOURCES"/>
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="name" />
						</td>
						<td>
							<bean:write name="degreeCurricularPlan" property="infoDegree.nome" />
						</td>
					</tr>
			</logic:iterate>
		</table>
		<p/>
		<html:submit>
			<bean:message key="button.inquiries.choose" bundle="INQUIRIES_RESOURCES"/>
		</html:submit>
	</logic:present>
</html:form>
