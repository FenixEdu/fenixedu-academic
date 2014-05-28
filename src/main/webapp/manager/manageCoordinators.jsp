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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notPresent name="infoExecutionDegree" >
	<bean:message bundle="MANAGER_RESOURCES" key="error.invalidExecutionDegree" />
</logic:notPresent>

<logic:present name="infoExecutionDegree" >
	<bean:define id="executionDegreeId" name="infoExecutionDegree" property="externalId" />
	<bean:define id="degreeId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.externalId" />	
	<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.externalId" />
	
	<table>
		<tr>
			<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.coordinators.modification"/></h3>
		</tr>
		<tr>
				<td>
					<h3><bean:message bundle="MANAGER_RESOURCES" key="label.degree"/></h3>
				</td>
				<td>				
					<h2><b><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.sigla"/>-<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>(<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>)</b></h2>
				</td>	
		</tr>
	</table>
	
	<ul style="list-style-type: square;">
		<li><html:link module="/manager" page="<%= "/insertCoordinator.do?method=prepareInsert&amp;executionDegreeId=" + pageContext.getAttribute("executionDegreeId")%>" >
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.coordinator.by.number"/>
			</html:link>
		</li>
	</ul>
	
	<logic:notPresent name="infoExecutionDegree" property="coordinatorsList">
		<i><bean:message bundle="MANAGER_RESOURCES" key="label.manager.coordinators.nonExisting"/></i>
	</logic:notPresent>
	
	<logic:present name="infoExecutionDegree" property="coordinatorsList">
		<html:form action="/manageCoordinators">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=  executionDegreeId.toString() %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= degreeId.toString() %>"/>	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
			
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit" />
			<table width="80%" cellpadding="0" border="0">
				<tr>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.name" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.teacher.number" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.responsible" />
					</th>	
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.remove" />
					</th>	
				</tr>
				<logic:iterate id="infoCoordinator" name="infoExecutionDegree" property="coordinatorsList">
					<tr>
						<bean:define id="coordinatorId" name="infoCoordinator" property="externalId"/>	 			
						<td class="listClasses"><bean:write name="infoCoordinator" property="infoPerson.nome"/>
						</td>
						<td class="listClasses"><bean:write name="infoCoordinator" property="infoPerson.username"/>
						</td>
						<td class="listClasses">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.responsibleCoordinatorsIds" property="responsibleCoordinatorsIds">
								<bean:write name="coordinatorId"/>
							</html:multibox>
						</td>
						<td class="listClasses">
							<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.deletedCoordinatorsIds" property="deletedCoordinatorsIds">
								<bean:write name="coordinatorId"/>
							</html:multibox>
						</td>						
		 			</tr>
		 		</logic:iterate>						
			</table>
			<br/>
			<html:submit><bean:message bundle="MANAGER_RESOURCES" key="label.manager.save.modifications"/></html:submit>
		</html:form>
	</logic:present>
</logic:present>

