<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<p><span class="error"><html:errors/></span></p>

<logic:notPresent name="infoExecutionDegree" >
	<bean:message key="error.invalidExecutionDegree" />
</logic:notPresent>

<logic:present name="infoExecutionDegree" >
	<bean:define id="executionDegreeId" name="infoExecutionDegree" property="idInternal" />
	<bean:define id="degreeId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.idInternal" />	
	<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal" />
	
	<table>
		<tr>
			<h3><bean:message key="label.manager.coordinators.modification"/></h3>
		</tr>
		<tr>
				<td>
					<h3><bean:message key="label.degree"/></h3>
				</td>
				<td>				
					<h2><b><bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.sigla"/>-<bean:write name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.nome"/>(<bean:write name="infoExecutionDegree" property="infoExecutionYear.year"/>)</b></h2>
				</td>	
		</tr>
	</table>
	
	<ul style="list-style-type: square;">
		<li><html:link page="<%= "/manageCoordinators.do?method=prepare&amp;view=false&amp;executionDegreeId=" + pageContext.getAttribute("executionDegreeId")%>" >
				<bean:message key="label.manager.insert.coordinator.by.number"/>
			</html:link>
		</li>
	</ul>
	
	<logic:notPresent name="infoExecutionDegree" property="coordinatorsList">
		<i><bean:message key="label.manager.coordinators.nonExisting"/></i>
	</logic:notPresent>
	
	<logic:present name="infoExecutionDegree" property="coordinatorsList">
		<html:form action="/manageCoordinators">
			<html:hidden property="executionDegreeId" value="<%=  executionDegreeId.toString() %>"/>
			<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>	
			<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
			
			<html:hidden property="method" value="edit" />
			<table width="80%" cellpadding="0" border="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.teacher.name" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.teacher.number" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.responsible" />
					</td>	
					<td class="listClasses-header"><bean:message key="label.remove" />
					</td>	
				</tr>
				<logic:iterate id="infoCoordinator" name="infoExecutionDegree" property="coordinatorsList">
					<tr>
						<bean:define id="coordinatorId" name="infoCoordinator" property="idInternal"/>	 			
						<td class="listClasses"><bean:write name="infoCoordinator" property="infoTeacher.infoPerson.nome"/>
						</td>
						<td class="listClasses"><bean:write name="infoCoordinator" property="infoTeacher.teacherNumber"/>
						</td>
						<td class="listClasses">
							<html:multibox property="responsibleCoordinatorsIds">
								<bean:write name="coordinatorId"/>
							</html:multibox>
						</td>
						<td class="listClasses">
							<html:multibox property="deletedCoordinatorsIds">
								<bean:write name="coordinatorId"/>
							</html:multibox>
						</td>						
		 			</tr>
		 		</logic:iterate>						
			</table>
			<br>
			<html:submit><bean:message key="label.manager.save.modifications"/></html:submit>
		</html:form>
	</logic:present>
</logic:present>

