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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://jakarta.apache.org/taglibs/datetime-1.0" prefix="dt" %>

<html:messages id="messages" message="true">
	<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<bean:define id="degreeType" name="executionDegree" property="degreeCurricularPlan.degree.degreeType.name" />
	<bean:define id="degreeCurricularPlanID" name="executionDegree" property="degreeCurricularPlan.externalId" />
	<bean:define id="executionDegreeID" name="executionDegree" property="externalId" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="saveCoordinatorsInformation"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= executionDegreeID.toString() %>"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.coordinators.modification"/></h3>
	<h3><b><bean:write name="executionDegree" property="degreeCurricularPlan.degree.sigla"/>-<bean:write name="executionDegree" property="degreeCurricularPlan.degree.nome"/> (<bean:write name="executionDegree" property="executionYear.year"/>)</b></h3>
	
	<ul style="list-style-type: square;">
		<li>
			<html:link module="/manager" action="/executionDegreesManagement.do?method=prepareInsertCoordinator" paramId="executionDegreeID" paramName="executionDegree" paramProperty="externalId">
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.coordinator"/>
			</html:link>
		</li>
	</ul>
		
	<logic:notEmpty name="executionDegree" property="coordinatorsList">
		<table cellpadding='0' border='0'>
			<tr>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.number"/> </th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.name"/> </th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.manager.responsible"/> </th>
				<th class='listClasses-header'> <bean:message bundle="MANAGER_RESOURCES" key="label.remove"/> </th>
			</tr>
			<logic:iterate id="coordinator" name="executionDegree" property="coordinatorsList">
				<tr>
					<td class='listClasses'><bean:write name="coordinator" property="person.username" /></td>
					<td class='listClasses'><bean:write name="coordinator" property="person.name" /></td>
					<td class='listClasses'>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.responsibleCoordinatorsIDs" property="responsibleCoordinatorsIDs">
									<bean:write name="coordinator" property="externalId"/>
						</html:multibox>
					</td>
					<td class='listClasses'>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removeCoordinatorsIDs" property="removeCoordinatorsIDs">
							<bean:write name="coordinator" property="externalId"/>
						</html:multibox>
					</td>
				</tr>
			</logic:iterate>
		</table>
		<br/>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="MANAGER_RESOURCES" key="button.save"/></html:submit>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="this.form.method.value='readExecutionDegrees';">
			<bean:message bundle="MANAGER_RESOURCES" key="label.return"/>
		</html:submit>
	</logic:notEmpty>

</html:form>