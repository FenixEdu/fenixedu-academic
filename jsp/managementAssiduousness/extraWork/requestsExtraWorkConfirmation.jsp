<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html:form action="/requestsExtraWorkManagement.do">
<h2><bean:message key="titulo.extraWork.requests" /></h2>
	<html:hidden property="method" value="readAndDeleteRequests"/>
	<html:hidden property="page" value="0"/>
	<bean:size id="sizeList" name="infoExtraWorkRequestsList" />	
	<html:hidden property="size" value="<%= sizeList.toString() %>" />	
	<html:hidden property="todo" value="apagar"/>
		
	<table border="0">
		<tr><td colspan="2" class="infoop"><bean:message key="info.managementAssiduousness.extraWork.requests.confirmation"/></td></tr>
		<logic:messagesPresent>	
			<tr><td colspan="2" ><br /><br /></td></tr>
			<tr><td colspan="2"><span class="error"><html:errors/></span></td></tr>
		</logic:messagesPresent>
		<tr><td colspan="2" ><br /><br /></td>
		</tr>
		<logic:iterate id="infoExtraWorkRequests" name="infoExtraWorkRequestsList" length="1">
			<tr class="listClassesWhite">
				<td width="1%" nowrap><bean:message key="prompt.dataInicial" />:&nbsp;</td>
				<td align="left" width="99%">
					<bean:define id="beginDate" name="infoExtraWorkRequests" property="beginDate" />
					<% SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
			           simpleDateFormat.setLenient(false); %>
            		<%= simpleDateFormat.format(beginDate)	%>
				</td>
			</tr>
			<tr class="listClasses">
				<td nowrap><bean:message key="prompt.dataFinal" />:&nbsp;</td>
				<td align="left">
					<bean:define id="endDate" name="infoExtraWorkRequests" property="endDate" />
            		<%= simpleDateFormat.format(endDate)	%>
            	</td>
			</tr>
			<tr class="listClassesWhite">
				<td nowrap><bean:message key="label.code.cost.center" />:&nbsp;</td>
				<td  align="left"><bean:write name="infoExtraWorkRequests" property="infoCostCenter.code" />
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenter.departament" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenter.departament" /></logic:notEmpty>
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenter.section1" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenter.section1" /></logic:notEmpty>
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenter.section2" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenter.section2" /></logic:notEmpty>
			</tr>
			<tr class="listClasses">
				<td nowrap><bean:message key="label.money"/>&nbsp;<bean:message key="label.code.cost.center" />:&nbsp;</td>
				<td align="left"><bean:write name="infoExtraWorkRequests" property="infoCostCenterMoney.code" />
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenterMoney.departament" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenterMoney.departament" /></logic:notEmpty>
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenterMoney.section1" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenterMoney.section1" /></logic:notEmpty>
					<logic:notEmpty name="infoExtraWorkRequests" property="infoCostCenterMoney.section2" /><br /><bean:write name="infoExtraWorkRequests" property="infoCostCenterMoney.section2" /></logic:notEmpty>
				</td>
			</tr>
		</logic:iterate>
		<tr><td colspan="2" ><br /><br /></td></tr>
		<tr>
			<td colspan="2">
				<table border="0" width="100%">
					<tr>
						<td class="listClasses-header" rowspan="2" width="1%" nowrap><bean:message key="label.employee"/></td>
						<td class="listClasses-header" rowspan="2" width="20%" nowrap>&nbsp;<bean:message key="prompt.nome"/></td>
						<td class="listClasses-header" rowspan="2"><strong>A</strong></td>																													
						<td class="listClasses-header" colspan="2" width="1%"><strong>B</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>C</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>D</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>E</strong></td>																													
						<td class="listClasses-header" rowspan="2"><strong>F</strong></td>																													
						<td class="listClasses-header" rowspan="2"><bean:message key="label.extrawork.apagar"/></td>																																			
					</tr>
					<tr>
						<td class="listClasses-header">+&nbsp;2<br /><bean:message key="prompt.horas"/></td>																													
						<td class="listClasses-header">+&nbsp;120<br /><bean:message key="prompt.horas"/></td>																													
					</tr>	
					<logic:iterate id="infoExtraWorkRequests" name="infoExtraWorkRequestsList" indexId="indice">
						<html:hidden name="infoExtraWorkRequests" property="idInternal" indexed="true" />
						<bean:define id="requestId" name="infoExtraWorkRequests" property="idInternal"/>
						<bean:define id="isPar">
							<%= indice.intValue() % 2  %>
						</bean:define>
						<logic:equal name="isPar" value="0">
						<tr class="listClasses">
							<td class="listClasses"><bean:write name="infoExtraWorkRequests" property="infoEmployee.employeeNumber" /></td>
							<td class="listClasses"><bean:write name="infoExtraWorkRequests" property="infoEmployee.person.nome" /></td>							
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option1" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option2" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option3" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option4" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option5" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option6" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><logic:equal name="infoExtraWorkRequests" property="option7" value="true"><img src="<%= request.getContextPath() %>/images/correct_bg_grey.gif"</logic:equal>&nbsp;</td>
							<td class="listClasses"><html:checkbox name="infoExtraWorkRequests" property="forDelete" indexed="true" value="true"/></td>
						</tr>
						</logic:equal>
						<logic:notEqual name="isPar" value="0">
						<tr class="listClassesWhite">	
							<td class="listClassesWhite"><bean:write name="infoExtraWorkRequests" property="infoEmployee.employeeNumber" /></td>
							<td class="listClassesWhite"><bean:write name="infoExtraWorkRequests" property="infoEmployee.person.nome" /></td>							
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option1" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option2" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option3" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option4" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option5" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option6" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><logic:equal name="infoExtraWorkRequests" property="option7" value="true"><img src="<%= request.getContextPath() %>/images/correct.gif"</logic:equal>&nbsp;</td>
							<td class="listClassesWhite"><html:checkbox name="infoExtraWorkRequests" property="forDelete" indexed="true" value="true"/></td>
						</tr>			
						</logic:notEqual>
					</logic:iterate>	
				</table>
			</td>
		</tr>
		<tr><td colspan="2" ><br /><br /></td></tr>
		<tr><td colspan="2">		
			<html:submit styleClass="inputbuttonSmall" onclick="this.form.method.value='prepareRequests';">
				&nbsp;&nbsp;&nbsp;<bean:message key="botao.confirmar"/>&nbsp;&nbsp;&nbsp;
			</html:submit>		
			<html:submit styleClass="inputbuttonSmall" onclick="this.form.todo.value='editar';">
				&nbsp;&nbsp;&nbsp;<bean:message key="botao.editar"/>&nbsp;&nbsp;&nbsp;
			</html:submit>
			<html:submit styleClass="inputbuttonSmall" onclick="this.form.todo.value='apagar';">
				<bean:message key="botao.apagar.selecccionados"/>
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