<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="ServidorApresentacao.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="DataBeans.sibs.InfoSibsPaymentFileEntry" %>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.gratuity.fixConflicts"/></h2>

<center>

<span class="error"><html:errors/></span>
	
<bean:define id="sibsPaymentFileEntries" name="<%= SessionConstants.SIBS_PAYMENT_FILE_ENTRIES %>" scope="request"/>

	<html:form action="/fixSibsPaymentFileEntries.do" >
	<table>
		
		<tr align="center">
			<th><bean:message key="label.sibsPaymentFileEntry.fix"/></th>
			<th><bean:message key="label.curricular.year"/></th>
			<th><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
			<th><bean:message key="label.transaction.transactionDate"/></th>
			<th><bean:message key="label.transaction.paymentType"/></th>
			<th><bean:message key="label.transaction.status"/></th>
			<th><bean:message key="label.transaction.value"/></th>
		</tr>
			
		<logic:iterate id="sibsPaymentFileEntry" name="sibsPaymentFileEntries">
			
			<tr align="center">
				<td><html:radio idName="sibsPaymentFileEntry" property="sibsPaymentFileEntryId" value="idInternal"/>	</td>
				<td><bean:write name="sibsPaymentFileEntry" property="year"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="studentNumber"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="transactionDate"/></td>
				
				<bean:define id="paymentType">label.<bean:write name="sibsPaymentFileEntry" property="paymentType.name"/></bean:define> 
				<td align="center"><bean:message name="paymentType"/></td>
				
				<bean:define id="paymentStatus">label.<bean:write name="sibsPaymentFileEntry" property="paymentStatus.name"/></bean:define> 
				<td align="center"><bean:message name="paymentStatus"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="payedValue"/></td>
				
			</tr>		
			
		</logic:iterate>
		
	</table>

	<br/><br/>
	
	<html:hidden property="method" value="fix"/>
	<html:hidden property="page" value="1"/>
	<html:submit styleClass="inputbutton">
		<bean:message key="label.sibsPaymentFileEntry.fix"/>
	</html:submit>
	
</html:form>

</center>