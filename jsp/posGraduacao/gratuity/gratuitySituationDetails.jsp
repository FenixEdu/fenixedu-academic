<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="DataBeans.InfoGratuitySituation" %>
<%@ page import="DataBeans.transactions.InfoTransaction" %>
<%@ page import="Util.transactions.TransactionType" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>

	
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.gratuitySituationDetails"/></h2>

<center>
	<span class="error"><html:errors/></span>
	
	<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
	<bean:define id="gratuitySituation" name="<%= SessionConstants.GRATUITY_SITUATION %>" scope="request"/>
	<bean:define id="transactionList" name="<%= SessionConstants.TRANSACTION_LIST %>" scope="request"/>
	<bean:define id="transactionsWithoutGuide" name="<%= SessionConstants.TRANSACTIONS_WITHOUT_GUIDE_LIST %>" scope="request" type="java.util.List"/>
		
	
	<table border="0">
		<!-- STUDENT -->
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.studentName"/>
			</th>
			<td align="left">
				<bean:write name="student" property="infoPerson.nome"/>
			</td>	
		</tr>
		<tr>
			<th align="left">
				<bean:message key="label.masterDegree.administrativeOffice.studentNumber"/>
			</th>
			<td align="left">
				<bean:write name="student" property="number"/>
			</td>				
		</tr>	

		<tr><td>&nbsp;</td></tr>
	
		<!-- GRATUITY SITUATION -->		
		<tr align="left">
			<th><bean:message key="label.executionYear"/>: </th>
			<td><bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoExecutionYear.year"/></td>
		</tr>
		<tr align="left">
			<th><bean:message key="label.degree"/>: </th>
			<td><bean:write name="gratuitySituation" property="infoGratuityValues.infoExecutionDegree.infoDegreeCurricularPlan.name"/></td>
		</tr>	
		
		<tr align="left">
			<bean:define id="value" name="gratuitySituation" property="remainingValue" />
			<% 
				String remainingValue = value.toString();
				Double remainingValueDbl = new Double(remainingValue );
				
				if(remainingValueDbl.doubleValue() >= 0)
				{
								
			%>
				<th><bean:message key="label.masterDegree.gratuity.notPayedValue"/>: </th>
				<td><bean:write name="gratuitySituation" property="remainingValue"/></td>
			<% 
				}
				else
				{
			%>								
				<th><bean:message key="label.masterDegree.gratuity.creditValue"/>: </th>
				<td><%= remainingValue.substring(1) %></td>
			<% 
				}
			%>
		</tr>
				
		<tr align="left">
			<th><bean:message key="label.exemptionGratuity.exemption"/>: </th>
			<td><bean:write name="gratuitySituation" property="exemptionPercentage"/> %</td>
		</tr>
		
		
		<tr><td>&nbsp;</td></tr>
		
		<tr  align="center">
			<th><bean:message key="label.transaction.transactionDate"/></th>
			<th><bean:message key="label.transaction.transactionType"/></th>
			<th><bean:message key="label.transaction.paymentType"/></th>
			<th><bean:message key="label.transaction.value"/></th>
			<th>&nbsp;</th>
		</tr>
		
		<% 
			Iterator it =  transactionsWithoutGuide.iterator();
			Integer transactionId = null;
			boolean existsTransactionsWithouGuides = false;
		%>
		<logic:iterate id="transaction" name="transactionList" >
			<tr>
				<td align="left"><bean:write name="transaction" property="transactionDate"/></td>
				
				<bean:define id="transactionType">
						label.transaction.transactionType.<bean:write name="transaction" property="transactionType.name"/>
				</bean:define> 
				<td align="center">
					<bean:message name="transactionType"/>
				</td>
								
				<td align="center"><bean:write name="transaction" property="paymentType"/></td>				

				<td align="right">
					<logic:equal name="transaction" property="transactionType.value" value="10">-</logic:equal>
					<logic:equal name="transaction" property="transactionType.value" value="12">-</logic:equal>
					<bean:write name="transaction" property="value"/>
				</td>
				<td>
				<%
					transactionId = (Integer)it.next();
					if(transactionId != null)
					{ existsTransactionsWithouGuides = true; 
						%> * <%}
				%>
				</td>				
			</tr>			
		</logic:iterate>
			
		
	</table>
	
	<br/>	<br/>
	
	<%
		if(existsTransactionsWithouGuides == true)
		{ %>
			<html:form action="/createGuideFromTransactions.do" >
				<html:hidden property="method" value="chooseContributor"/>
				<html:hidden property="gratuitySituationId" />
				<html:hidden property="studentId" />
				<html:hidden property="contributorNumber" />
				<html:hidden property="page" value="1"/>
				
				<bean:message key="label.transaction.createGuides"/>: 
				
				<html:submit styleClass="inputbutton">
					<bean:message key="button.create"/>
				</html:submit>
			</html:form>
		
			<bean:message key="label.transaction.transactionsWithoutGuidesNote"/> 
	<%}%>
		
</center>