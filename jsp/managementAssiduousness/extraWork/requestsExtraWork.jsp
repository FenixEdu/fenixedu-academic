<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.extraWork.requests" /></h2>
<html:form action="/requestsExtraWorkManagement.do" focus="beginDate">
	<html:hidden property="method" value="requestsExtraWork"/>
	<html:hidden property="page" value="1"/>
	<bean:size id="sizeList" name="infoExtraWorkRequestsList" />	
	<html:hidden property="size" value="<%= sizeList.toString() %>" />	
	
	<table border="0">
		<tr><td colspan="2" class="infoop"><bean:message key="info.managementAssiduousness.extraWork.requests"/></td></tr>
		<logic:messagesPresent>	
			<tr><td colspan="2" ><br /><br /></td></tr>
			<tr><td colspan="2"><span class="error"><html:errors/></span></td></tr>
		</logic:messagesPresent>
		<tr><td colspan="2" ><br /><br /></td>
		</tr>
		<tr>
			<td width="1%" nowrap><bean:message key="prompt.dataInicial" />:&nbsp;</td>
			<td width="99%"><html:text property="beginDate" onkeyup="if (this.value.length==2 && window.event.keyCode!=8) this.value = this.value + '/'; if (this.value.length==5 && window.event.keyCode!=8) this.value = this.value + '/';"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<td nowrap><bean:message key="prompt.dataFinal" />:&nbsp;</td>
			<td><html:text property="endDate" onkeyup="if (this.value.length==2 && window.event.keyCode!=8) this.value = this.value + '/'; if (this.value.length==5 && window.event.keyCode!=8) this.value = this.value + '/';"/>&nbsp;(dd/mm/yyyy)</td>
		</tr>
		<tr>
			<td nowrap><bean:message key="label.code.cost.center" />:&nbsp;</td>
			<td><html:text property="cc" /></td>
		</tr>
		<tr>
			<td nowrap><bean:message key="label.money"/>&nbsp;<bean:message key="label.code.cost.center" />:&nbsp;</td>
			<td><html:text property="ccMoney" /></td>
		</tr>
		<tr><td colspan="2" ><br /><br /></td></tr>
		<tr>
			<td colspan="2">
				<table border="0" width="100%">
					<tr>
						<td class="listClasses-header" rowspan="2" width="1%" nowrap><bean:message key="label.employee"/></td>
						<td class="listClasses-header" rowspan="2"><strong>A</strong></td>																													
						<td class="listClasses-header" colspan="2"><strong>B</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>C</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>D</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>E</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>F</strong></td>																																			
					</tr>
					<tr>
						<td class="listClasses-header">+&nbsp;2<br /><bean:message key="prompt.horas"/></td>																													
						<td class="listClasses-header">+&nbsp;120<br /><bean:message key="prompt.horas"/></td>																													
					</tr>	
					<logic:iterate id="infoExtraWorkRequests" name="infoExtraWorkRequestsList" indexId="indice">
						<html:hidden name="infoExtraWorkRequests" property="idInternal" indexed="true" />
						<bean:define id="isPar">
							<%= indice.intValue() % 2  %>
						</bean:define>
						<logic:equal name="isPar" value="0"><tr class="listClasses"></logic:equal>
						<logic:notEqual name="isPar" value="0"><tr class="listClassesWhite"></logic:notEqual>	
							<td class="listClassesWhite"><html:text name="infoExtraWorkRequests" property="infoEmployee.employeeNumber" size="4" indexed="true" /></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option1" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option2" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option3" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option4" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option5" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option6" indexed="true" value="true"/></td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="option7" indexed="true" value="true"/></td>
						</tr>			
					</logic:iterate>	
				</table>
			</td>
		</tr>
		<tr><td colspan="2" ><br /><br /></td></tr>
		<tr><td colspan="2">		
			<html:submit styleClass="inputbutton">
				<bean:message key="botao.avançar"/>
			</html:submit>
		</tr></td>
		<tr><td colspan="2" ><br /><br /></td></tr>
		<tr><td colspan="2"  class="infoop">
			<strong>A</strong>-<bean:message key="label.extrawork.A"/>;<br />		
			<strong>B</strong>-<bean:message key="label.extrawork.B"/>;<br />		
			<strong>C</strong>-<bean:message key="label.extrawork.C"/>;<br />		
			<strong>D</strong>-<bean:message key="label.extrawork.D"/>;<br />		
			<strong>E</strong>-<bean:message key="label.extrawork.E"/>;<br />		
			<strong>F</strong>-<bean:message key="label.extrawork.F"/>;<br />		
		</tr></td>
	</table>		
</html:form>