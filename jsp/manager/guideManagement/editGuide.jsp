<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.editGuide" /></h2>

<html:form action="/guideManagement">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="editGuide" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.number" property="number" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.version" property="version" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.guideID" property="guideID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGuideEntryDocumentType" property="selectedGuideEntryDocumentType" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectedGuideEntryID" property="selectedGuideEntryID" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.guideSituationID" property="guideSituationID" />
	

	<bean:define id="paymentTransactions" name="paymentTransactions" type="java.util.ArrayList"/>

	<table>
		<tr>
			<td colspan="4"><b>Nome Aluno:</b> <bean:write name="guide" property="infoPerson.nome" />
			</td>
		</tr>
		<tr>
			<td><b>Número:</b> <bean:write name="guide" property="number" /></td>
		</tr>
		<tr>
			<td><b>Ano:</b> <bean:write name="guide" property="year" /></td>
		</tr>
		<tr>
			<td><b>Versï¿½o:</b> <bean:write name="guide" property="version" /></td>
		</tr>		
		<tr>
			<td colspan="5">
				<b>Curso:</b> 
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newDegreeCurricularPlanID" property="newDegreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="value"
						labelProperty="label" />
				</html:select>
				 - 
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newExecutionYear" property="newExecutionYear">
					<html:options collection="executionYears" property="value"
						labelProperty="label" />
				</html:select>
			</td>
		</tr>		
		<tr>
			<td><b>Total:</b> <bean:write name="guide" property="total" /></td>
		</tr>
		<tr>
			<td><b>Tipo Pagamento:</b>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.transactions.PaymentType" bundle="ENUMERATION_RESOURCES"/>
	            <html:select bundle="HTMLALT_RESOURCES" altKey="select.newPaymentType" property="newPaymentType">
	               	<html:option key="dropDown.Default" value=""/>
	                <html:options collection="values" property="value" labelProperty="label"/>
	            </html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:define id="behavior" >
					this.form.method.value='deleteGuide';				
				</bean:define>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Apagar Versï¿½o" onclick="<%= behavior %>" />
				
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Submeter"
				onclick="this.form.method.value='editExecutionDegree'" />
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
		<td><b>Tipo</b></td><td><b>Descriï¿½ï¿½o</b></td><td><b>Quantidade</b></td><td><b>Preï¿½o</b></td>
	</tr>
		
			<logic:iterate id="guideEntry" name="guide"
				property="infoGuideEntries" indexId="index">
		<tr>				
				<td>
					<bean:define id="documentType"><bean:write name="guideEntry" property="documentType"/></bean:define>
					<bean:message bundle="MANAGER_RESOURCES" name="documentType" bundle="ENUMERATION_RESOURCES" />
				</td><td>
				<bean:write name="guideEntry" property="description" /> </td> <td>
					<bean:write name="guideEntry" property="quantity" />  </td><td>
					<bean:write name="guideEntry" property="price" /> </td><td>
					
				<bean:define id="behavior" >
					this.form.method.value='deleteGuideEntry';
					this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';					
				</bean:define>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Apagar Entrada" onclick="<%= behavior %>" />					
												
				<% if (paymentTransactions.get(index.intValue()) != null){ %>
					Tem Transaï¿½ï¿½o
				<% }else{ %>
					<bean:define id="behavior" >
						this.form.method.value='createPaymentTransaction';
						this.form.selectedGuideEntryID.value='<bean:write name="guideEntry" property="idInternal" />';
						this.form.selectedGuideEntryDocumentType.value='<bean:write name="documentType" />';
					</bean:define>
					<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Criar Transaï¿½ï¿½o" onclick="<%= behavior %>" />				
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
			<td><b>Descriï¿½ï¿½o: </b></td>
			<td><b>Quantidade: </b></td>
			<td><b>Preï¿½o: </b></td>
		</tr>
		<tr>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.DocumentType" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newEntryDocumentType" property="newEntryDocumentType">
					<html:option key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label" />
				</html:select>
			</td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.newEntryDescription" property="newEntryDescription" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.newEntryQuantity" size="4" property="newEntryQuantity" /></td>
			<td><html:text bundle="HTMLALT_RESOURCES" altKey="text.newEntryPrice" size="4" property="newEntryPrice" /></td>
		</tr>
				
		<tr>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Adicionar Entrada"
				onclick="this.form.method.value='addGuideEntry'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>
		<tr>
			<td><h2>Situaï¿½ï¿½es de Guia</h2></td>
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
				
				<td colspan="5"><bean:write name="guideSituation" property="remarks" />
				- <bean:write name="guideSituation" property="idInternal" />
				- <bean:write name="guideSituation" property="situation.name" />
				- <bean:write name="guideSituation" property="state" />
				- <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Apagar Situação" onclick="<%= behavior %>" />	</td>	
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
			<td ><b>Descriï¿½ï¿½o:</b></td>
			<td colspan="2"><b>Data: </b></td>
			<td colspan="2"><b>Situação: </b></td>			
		</tr>
			<td ><html:text bundle="HTMLALT_RESOURCES" altKey="text.newSituationRemarks" size="30" property="newSituationRemarks" /></td>
			<td colspan="2"> 				
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newSituationDay" property="newSituationDay">
					<html:options collection="days" property="value"
						labelProperty="label" />
				</html:select>
				/
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newSituationMonth" property="newSituationMonth">
					<html:options collection="months" property="value"
						labelProperty="label" />
				</html:select>				
				/
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.newSituationYear" property="newSituationYear">
					<html:options collection="years" property="value"
						labelProperty="label" />
				</html:select>					
			</td>
			<td colspan="2">
			<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GuideState" bundle="ENUMERATION_RESOURCES"/>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.newSituationType" property="newSituationType">
				<html:option key="dropDown.Default" value=""/>
				<html:options collection="values" property="value" labelProperty="label" />
			</html:select></td>
		</tr>		
		<tr>
			<td><html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" value="Adicionar Situação"
				onclick="this.form.method.value='addGuideSituation'" /></td>
		</tr>

		<tr>
			<td>&nbsp;</td>
		</tr>

	</table>

</html:form>
