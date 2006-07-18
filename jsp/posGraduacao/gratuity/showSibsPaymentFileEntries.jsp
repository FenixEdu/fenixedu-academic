<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.sibs.InfoSibsPaymentFileEntry" %>

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
				<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sibsPaymentFileEntryId" idName="sibsPaymentFileEntry" property="sibsPaymentFileEntryId" value="idInternal"/>	</td>
				<td><bean:write name="sibsPaymentFileEntry" property="year"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="studentNumber"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="transactionDate"/></td>
				
				<td align="center"><bean:message name="sibsPaymentFileEntry" property="paymentType.name" bundle="ENUMERATION_RESOURCES"/></td>
				
				<bean:define id="paymentStatus">label.<bean:write name="sibsPaymentFileEntry" property="paymentStatus.name"/></bean:define> 
				<td align="center"><bean:message name="paymentStatus"/></td>
				
				<td><bean:write name="sibsPaymentFileEntry" property="payedValue"/></td>
				
			</tr>		
			
		</logic:iterate>
		
	</table>

	<br/><br/>
	
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="fix"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message key="label.sibsPaymentFileEntry.fix"/>
	</html:submit>
	
</html:form>

</center>