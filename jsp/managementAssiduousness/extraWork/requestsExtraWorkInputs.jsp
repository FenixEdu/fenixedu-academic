<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.extraWork.requests" /></h2>
<html:form action="/requestsExtraWorkManagement.do" focus="beginDate">
	<html:hidden property="method" value="prepareRequests"/>
	<html:hidden property="page" value="1"/>
	
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
		<tr><td colspan="2">		
			<html:submit styleClass="inputbutton">
				<bean:message key="botao.avançar"/>
			</html:submit>
		</tr></td>
	</table>		
</html:form>