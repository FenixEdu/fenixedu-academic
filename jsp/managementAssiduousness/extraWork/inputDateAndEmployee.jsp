<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<h2><bean:message key="titulo.extraWork.sheet" /></h2>
<html:form action="/extraWorkByEmployee.do" focus="employeeNumber">
	<html:hidden property="method" value="readAllByYear"/>
	<html:hidden property="page" value="1"/>
	<table>
		<tr>
			<td colspan="2" class="infoop">
				<bean:message key="info.managementAssiduousness.extraWork.employee"/>
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
				<html:text property="employeeNumber" size="8"/>
			</td>		
		</tr>
		<tr>
			<td>
				<bean:message key="label.moth.year" />:
			</td>
			<td>
				<html:text property="moth" size="2"/>&nbsp;/&nbsp;<html:text property="year" size="4"/>
			</td>		
		</tr>		
		<tr>
			<td>
				<bean:message key="label.todo" />:
			</td>
			<td>
				<html:select property="todo">
					<html:option value="consult">
							<bean:message key="label.consult"/>
					</html:option>
					<html:option value="authorize">
							<bean:message key="label.authorize"/>
					</html:option>
				</html:select>
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