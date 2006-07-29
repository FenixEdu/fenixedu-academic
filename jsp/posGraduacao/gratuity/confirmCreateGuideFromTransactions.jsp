<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.masterDegree.utils.SessionConstants" %>
<%@ page import="java.util.Iterator" %>

	
<h2 align="center"><bean:message key="title.transaction.createGuides"/></h2>

<center>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	
	<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
	<bean:define id="contributor" name="<%= SessionConstants.CONTRIBUTOR %>" scope="request"/>
	<bean:define id="transactionList" name="<%= SessionConstants.TRANSACTION_LIST %>" scope="request"  type="java.util.List"/>
	
	<html:form action="/createGuideFromTransactions.do" >
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
			
			<!-- Contributor Number -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorNumber" />: </th>
				<td><bean:write name="contributor" property="contributorNumber"/></td>
			</tr>
			<!-- Contributor Name -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorName" />: </th>
				<td><bean:write name="contributor" property="contributorName"/></td>
			</tr>
			<!-- Contributor Address -->
			<tr align="left">
				<th><bean:message key="label.masterDegree.administrativeOffice.contributorAddress" />: </th>
				<td><bean:write name="contributor" property="contributorAddress"/></td>
			</tr>
          <tr align="left">
            <th><bean:message key="label.person.postCode" /></th>
            <td><bean:write name="contributor" property="areaCode"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.areaOfPostCode" /></th>
            <td><bean:write name="contributor" property="areaOfAreaCode"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.place" /></th>
            <td><bean:write name="contributor" property="area"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressParish" /></th>
            <td><bean:write name="contributor" property="parishOfResidence"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressMunicipality" /></th>
            <td><bean:write name="contributor" property="districtSubdivisionOfResidence"/></td>
          </tr>
          <tr align="left">
            <th><bean:message key="label.person.addressDistrict" /></th>
            <td><bean:write name="contributor" property="districtOfResidence"/></td>
          </tr>
		
			<tr align="left">
				<td>&nbsp;</td>
			</tr>
		
			
			<tr  align="center">
				<th><bean:message key="label.transaction.transactionDate"/></th>
				<th><bean:message key="label.transaction.transactionType"/></th>
				<th><bean:message key="label.transaction.paymentType"/></th>
				<th><bean:message key="label.transaction.value"/></th>
			</tr>
			
			<% 
				Iterator it =  transactionList.iterator();
				Integer transactionId = null;
				
			%>
			<logic:iterate id="transaction" name="transactionList" >
				
				<bean:define id="transactionIdStr" name="transaction" property="idInternal" />
						 
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.transactionsWithoutGuide" property="transactionsWithoutGuide" value="<%= transactionIdStr.toString() %>"/>				
				<tr>
					<td align="left"><bean:write name="transaction" property="transactionDate"/></td>

					<td align="center">
						<bean:message name="transaction" property="transactionType.name" bundle="ENUMERATION_RESOURCES"/>
					</td>
									
					<td align="center">
        				<bean:message name="transaction" property="paymentType.name" bundle="ENUMERATION_RESOURCES" />						
					</td>				
					<td align="left"><bean:write name="transaction" property="value"/></td>
				
				</tr>			
			</logic:iterate>
				
			
		</table>
		
		<br/>	<br/>
	
	
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="create"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.gratuitySituationId" property="gratuitySituationId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentId" property="studentId" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.contributorNumber" property="contributorNumber" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="2"/>
		
		<bean:message key="label.transaction.createGuides"/>: 
		
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message key="button.create"/>
		</html:submit>
	</html:form>
		
</center>