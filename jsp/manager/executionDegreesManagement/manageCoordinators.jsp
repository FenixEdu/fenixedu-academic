<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/taglibs-datetime.tld" prefix="dt" %>

<html:messages id="messages" message="true">
	<span class="error"><!-- Error messages go here --><bean:write name="messages" /></span>
</html:messages>

<html:form action="/executionDegreesManagement">
	<bean:define id="degreeType" name="executionDegree" property="degreeCurricularPlan.degree.degreeType.name" />
	<bean:define id="degreeCurricularPlanID" name="executionDegree" property="degreeCurricularPlan.idInternal" />
	<bean:define id="executionDegreeID" name="executionDegree" property="idInternal" />

	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="saveCoordinatorsInformation"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeType" property="degreeType" value="<%= degreeType.toString() %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanID" property="degreeCurricularPlanID" value="<%= degreeCurricularPlanID.toString() %>" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegreeID" property="executionDegreeID" value="<%= executionDegreeID.toString() %>"/>
	
	<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.coordinators.modification"/></h3>
	<h3><b><bean:write name="executionDegree" property="degreeCurricularPlan.degree.sigla"/>-<bean:write name="executionDegree" property="degreeCurricularPlan.degree.nome"/> (<bean:write name="executionDegree" property="executionYear.year"/>)</b></h3>
	
	<ul style="list-style-type: square;">
		<li>
			<html:link module="/manager" action="/executionDegreesManagement.do?method=prepareInsertCoordinator" paramId="executionDegreeID" paramName="executionDegree" paramProperty="idInternal">
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
					<td class='listClasses'><bean:write name="coordinator" property="teacher.teacherNumber" /></td>
					<td class='listClasses'><bean:write name="coordinator" property="teacher.person.name" /></td>
					<td class='listClasses'>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.responsibleCoordinatorsIDs" property="responsibleCoordinatorsIDs">
									<bean:write name="coordinator" property="idInternal"/>
						</html:multibox>
					</td>
					<td class='listClasses'>
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.removeCoordinatorsIDs" property="removeCoordinatorsIDs">
							<bean:write name="coordinator" property="idInternal"/>
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