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
			<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.associate.teachers.in.charge"/></h3>
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
	

	<html:form action="/insertCoordinator" focus="number">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert" /> 
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeId" property="executionDegreeId" value="<%=  executionDegreeId.toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= degreeId.toString() %>"/>	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
	
		<table>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" key="message.insert.coordinator.number"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.number" size="5" property="number" />
				</td>
			</tr>
		</table>
		
		<br/>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
		</html:reset>			
	</html:form>
</logic:present>