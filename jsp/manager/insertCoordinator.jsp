<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<p><span class="error"><html:errors/></span></p>

<logic:notPresent name="infoExecutionDegree" >
	<bean:message bundle="MANAGER_RESOURCES" key="error.invalidExecutionDegree" />
</logic:notPresent>

<logic:present name="infoExecutionDegree" >
	<bean:define id="executionDegreeId" name="infoExecutionDegree" property="idInternal" />
	<bean:define id="degreeId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.infoDegree.idInternal" />	
	<bean:define id="degreeCurricularPlanId" name="infoExecutionDegree" property="infoDegreeCurricularPlan.idInternal" />

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
		
		<br>
	
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
		</html:submit>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
		</html:reset>			
	</html:form>
</logic:present>