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
			<h3><bean:message key="label.manager.associate.teachers.in.charge"/></h3>
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
	

	<html:form action="/insertCoordinator">
		<html:hidden property="method" value="insert" /> 
		<html:hidden property="page" value="1"/>
		<html:hidden property="executionDegreeId" value="<%=  executionDegreeId.toString() %>"/>
		<html:hidden property="degreeId" value="<%= degreeId.toString() %>"/>	
		<html:hidden property="degreeCurricularPlanId" value="<%= degreeCurricularPlanId.toString() %>"/>
	
		<table>
			<tr>
				<td>
					<bean:message key="message.insert.coordinator.number"/>
				</td>
				<td>
					<html:text size="5" property="number" />
				</td>
			</tr>
		</table>
		
		<br>
	
		<html:submit styleClass="inputbutton">
			<bean:message key="button.save"/>
		</html:submit>
		<html:reset  styleClass="inputbutton">
			<bean:message key="label.clear"/>
		</html:reset>			
	</html:form>
</logic:present>