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
				<html:select property="coordinatorId">
					<html:options collection="infoTeachersList" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
			<td>
				<bean:message key="label.manager.executionDegree.executionYear"/>
			</td>
			<td>
				<html:select property="executionYear">
					<html:options collection="infoExecutionYearsList" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.executionDegree.temporaryExamMap"/>
			</td>
			<td>
				<html:select property="tempExamMap">
					<html:option key="" value=""/>
					<html:option key="option.manager.true" value="true"/>
    				<html:option key="option.manager.false" value="false"/>
    			</html:select>
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