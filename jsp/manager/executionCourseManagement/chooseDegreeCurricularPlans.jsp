<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:form action="/createExecutionCourses" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createExecutionCourses"/>
	
	<table>
		<tr>
			<th colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.executionPeriod" />: 
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.executionPeriodID" property="executionPeriodID" size="1">
					<html:option value=""></html:option>
					<html:options collection="executionPeriods" property="idInternal" labelProperty="description"/>
				</html:select>
			</th>
		</tr>
		
		<tr><td>&nbsp;</td></tr>	
		
		<tr>
			<th colspan="2">
				<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.degreeCurricularPlans" />: 
			</th>
		</tr>
		<logic:iterate id="degreeCurricularPlan" name="degreeCurricularPlans" >
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.degreeCurricularPlansIDs" property="degreeCurricularPlansIDs">
						<bean:write name="degreeCurricularPlan" property="idInternal"/>
					</html:multibox>
				</td>
				<td>
					<bean:write name="degreeCurricularPlan" property="infoDegree.nome"/> - 
					<bean:write name="degreeCurricularPlan" property="name"/>
				</td>
			</tr>	
		</logic:iterate>

		<tr><td>&nbsp;</td></tr>	
		
		<tr>
			<td>
				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.create"/>
				</html:submit>
			</td>
		</tr>
	</table>
	
</html:form>