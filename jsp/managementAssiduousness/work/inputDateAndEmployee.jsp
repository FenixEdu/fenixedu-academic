<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.work.employee.sheet" /></h2>
<html:form action="/workByEmployee.do" focus="employeeNumber">
	<html:hidden property="method" value="workSheetByEmployeeAndMoth"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="info.managementAssiduousness.work.employee"/>
			</td>		
		</tr>
		<logic:messagesPresent>	
			<tr>
				<td colspan="2" >
					<br /><br />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<span class="error"><html:errors/></span>
				</td>		
			</tr>
		</logic:messagesPresent>
		<tr>
			<td colspan="2" >
				<br /><br />
			</td>
		</tr>		
		<tr>
			<td width="1%" nowrap>
				<bean:message key="label.employee" />:
			</td>
			<td width="99%">
				<html:text property="employeeNumber" size="8" maxlength="8"/>
			</td>		
		</tr>
		<tr>
			<td width="1%" nowrap>
				<bean:message key="prompt.dataInicial" />:
			</td>
			<td>
				<html:text property="beginDate" size="10" maxlength="10" onkeyup="if (this.value.length==2 && window.event.keyCode!=8) this.value = this.value + '/'; if (this.value.length==5 && window.event.keyCode!=8) this.value = this.value + '/';"/>&nbsp;(dd/mm/aaaa)
			</td>		
		</tr>
		<tr>
			<td width="1%" nowrap>
				<bean:message key="prompt.dataFinal" />:
			</td>
			<td>
				<html:text property="endDate" size="10" maxlength="10" onkeyup="if (this.value.length==2 && window.event.keyCode!=8) this.value = this.value + '/'; if (this.value.length==5 && window.event.keyCode!=8) this.value = this.value + '/';"/>&nbsp;(dd/mm/aaaa)
			</td>		
		</tr>	
		<tr>
			<td colspan="2">
				<br /><br />
			</td>		
		</tr>
	</table>

	<html:submit styleClass="inputbutton">
		<bean:message key="botao.avançar"/>
	</html:submit>
</html:form>