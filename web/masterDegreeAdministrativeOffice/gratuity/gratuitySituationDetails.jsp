<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.transactions.InfoTransaction" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>

	
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.gratuitySituationDetails"/></h2>

<center>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
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

				<td align="center">
					<bean:message name="transaction" property="transactionType.name" bundle="ENUMERATION_RESOURCES"/>
				</td>
								
				<td align="center">
    				<bean:message name="transaction" property="paymentType.name" bundle="ENUMERATION_RESOURCES" />				
				</td>				

				<td align="right">
					<logic:equal name="transaction" property="transactionType.name" value="GRATUITY_REIMBURSEMENT">-</logic:equal>
					<logic:equal name="transaction" property="transactionType.name" value="INSURANCE_REIMBURSEMENT">-</logic:equal>
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
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="chooseContributor"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationId" property="gratuitySituationId" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contributorNumber" property="contributorNumber" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
				
				<bean:message key="label.transaction.createGuides"/>: 
				
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message key="button.create"/>
				</html:submit>
			</html:form>
		
			<bean:message key="label.transaction.transactionsWithoutGuidesNote"/> 
	<%}%>
		
</center>