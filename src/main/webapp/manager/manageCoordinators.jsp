<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>

<logic:notPresent name="infoExecutionDegree" >
	<bean:message bundle="MANAGER_RESOURCES" key="error.invalidExecutionDegree" />
</logic:notPresent>

<logic:present name="infoExecutionDegree" >
	<bean:define id="executionDegreeId" name="infoExecutionDegree" property="idInternal" />
	<bean:define id="degreeId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.idInternal" />	
	<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal" />
	
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
						<bean:define id="coordinatorId" name="infoCoordinator" property="idInternal"/>	 			
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

