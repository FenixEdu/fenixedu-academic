<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.manager.edit.executionDegree" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/editExecutionDegree" method ="get">
	    
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="executionDegreeId" value="<%= request.getParameter("executionDegreeId") %>"/>
	
	<table>
		<tr>
			<td>
				<bean:message key="label.manager.executionDegree.coordinator"/>
			</td>
			<td>	
				<html:text property="coordinatorNumber"/>
			</td>
		</tr>
			<td>
				<bean:message key="label.manager.executionDegree.executionYear"/>
			</td>
			<td>
				<html:select property="executionYearId">
					<html:options collection="infoExecutionYearsList" property="idInternal" labelProperty="year"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.executionDegree.campus"/>
			</td>
			<td>
				<html:select property="campusId">
					<html:options collection="infoCampusList" property="idInternal" labelProperty="name"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.executionDegree.temporaryExamMap"/>
			</td>
			<td>
				<html:checkbox property="tempExamMap" value="true"/>
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