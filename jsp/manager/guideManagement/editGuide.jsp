<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<h2><bean:message key="label.editGuide" /></h2>

<html:form action="/guideManagement">
	<html:hidden property="method" value="editGuide" />
	<html:hidden property="page" value="1" />
	<html:hidden property="number" />
	<html:hidden property="year" />
	<html:hidden property="version" />
	<html:hidden property="guideID" />
	<html:hidden property="selectedGuideEntryDocumentType" />
	<html:hidden property="selectedGuideEntryID" />
	<html:hidden property="guideSituationID" />

	<bean:define id="paymentTransactions" name="paymentTransactions" type="java.util.ArrayList"/>

	<table>
		<tr>
			<td><b>Número:</b> <bean:write name="guide" property="number" /></td>
		</tr>
		<tr>
			<td><b>Ano:</b> <bean:write name="guide" property="year" /></td>
		</tr>
		<tr>
			<td><b>Versão:</b> <bean:write name="guide" property="version" /></td>
		</tr>		
		<tr>
			<td colspan="5">
				<b>Curso:</b> 
				<html:select property="newDegreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="value"
						labelProperty="label" />
				</html:select>
				 - 
				<html:select property="newExecutionYear">
					<html:options collection="executionYears" property="value"
						labelProperty="label" />
				</html:select>
				<html:submit value="Editar"
				onclick="this.form.method.value='editExecutionDegree'" />
			</td>
		</tr>		
		<tr>
			<td><b>Total:</b> <bean:write name="guide" property="total" /></td>
		</tr>
		<tr>
			<td colspan="4"><b>Nome Aluno:</b> <bean:write name="guide" property="infoPerson.nome" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:define id="behavior" >
					this.form.method.value='deleteGuide';				
				</bean:define>
				<html:submit value="Apagar Versão" onclick="<%= behavior %>" />
			</td>
		</tr>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><h2>Entradas de Guia:</h2></td>
		</tr>
				<tr>
			<td>&nbsp;</td>
		</tr>
	<tr>
		<td><b>Tipo</b></td><td><b>Descrição</b></td><td><b>Quantidade</b></td><td><b>Preço</b></td>
	</tr>
		
			<logic:iterate id="guideEntry" name="guide"
				property="infoGuideEntries" indexId="index">
		<tr>				
				<td>
				<bean:write name="guideEntry" property="documentType.type" /> </td><td>
				<bean:write name="guideEntry" property="description" /> </td> <td>
					<bean:write name="guideEntry" property="quantity" />  </td><td>
					<bean:write name="guideEntry" property="price" /> </td><td>
					
				<bean:define id="behavior" >
					this.form.method.value='deleteGuideEntry';
					this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';					
				</bean:define>
				<html:submit value="Apagar Entrada" onclick="<%= behavior %>" />					
												
				<% if (paymentTransactions.get(index.intValue()) != null){ %>
					Tem Transação
				<% }else{ %>
					<bean:define id="behavior" >
						this.form.method.value='createPaymentTransaction';
						this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';
						this.form.selectedGuideEntryDocumentType.value='<bean:write name="guideEntry" property="documentType.type" />';
					</bean:define>
					<html:submit value="Criar Transação" onclick="<%= behavior %>" />				
				<% }%>	
								
				<br />
		
				</tr>
			</logic:iterate>
		

		<tr>
			<td>&nbsp;</td>
		</tr>

		<tr>
			<td><b>Nova entrada de Guia:</b></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td ><b>Tipo de Documento: </b></td>
			<td><b>Descrição: </b></td>
			<td><b>Quantidade: </b></td>
			<td><b>Preço: </b></td>
		</tr>
		<tr>
			<td ><html:select property="newEntryDocumentType">
				<html:options collection="documentTypes" property="value"
					labelProperty="label" />
			</html:select></td>
			<td><html:text property="newEntryDescription" /></td>
			<td><html:text size="4" property="newEntryQuantity" /></td>
			<td><html:text size="4" property="newEntryPrice" /></td>
		</tr>
				
		<tr>
			<td><html:submit value="Adicionar Entrada"
				onclick="this.form.method.value='addGuideEntry'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><h2>Situações de Guia</h2></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>		
		
		<logic:iterate id="guideSituation" name="guide" property="infoGuideSituations" >
			<tr>
				<bean:define id="behavior" >
					this.form.method.value='deleteGuideSituation';
					this.form.guideSituationID.value='<bean:write name="guideSituation" property="idInternal" />';
				</bean:define>
				
				<td colspan="4"><bean:write name="guideSituation" property="remarks" />
				- <bean:write name="guideSituation" property="idInternal" />
				- <html:submit value="Apagar Situação" onclick="<%= behavior %>" />	</td>	
			</tr>		
		</logic:iterate>
		
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><b>Nova Situação:</b></td>
		</tr>
		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td ><b>Descrição:</b></td>
			<td colspan="2"><b>Data: </b></td>
			<td colspan="2"><b>Situação: </b></td>			
		</tr>
			<td ><html:text size="30" property="newSituationRemarks" /></td>
			<td colspan="2"> 				
				<html:select property="newSituationDay">
					<html:options collection="days" property="value"
						labelProperty="label" />
				</html:select>
				/
				<html:select property="newSituationMonth">
					<html:options collection="months" property="value"
						labelProperty="label" />
				</html:select>				
				/
				<html:select property="newSituationYear">
					<html:options collection="years" property="value"
						labelProperty="label" />
				</html:select>					
			</td>
			<td colspan="2"><html:select property="newSituationType">
				<html:options collection="situationTypes" property="value"
					labelProperty="label" />
			</html:select></td>
		</tr>		
		<tr>
			<td><html:submit value="Adicionar Situação"
				onclick="this.form.method.value='addGuideSituation'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>

	</table>

</html:form>
