<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ page import="DataBeans.InfoStudent" %>
<%@ page import="DataBeans.InfoTeacher" %>


<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
<bean:define id="dissertationTitle" name="<%= SessionConstants.DISSERTATION_TITLE %>" />
<bean:define id="finalResult" name="<%= SessionConstants.FINAL_RESULT %>" />
<bean:define id="attachedCopiesNumber" name="<%= SessionConstants.ATTACHED_COPIES_NUMBER %>" />
<bean:define id="proofDate" name="<%= SessionConstants.PROOF_DATE %>" />
<bean:define id="thesisDeliveryDate" name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" />
<bean:define id="responsibleEmployee" name="<%= SessionConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= SessionConstants.LAST_MODIFICATION %>" scope="request"/>

				
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.visualizeProof"/></h2>
<center>
<span class="error"><html:errors/></span>

	<br/>

	<table border="0" width="100%" cellspacing="3" cellpadding="10">
		<tr>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentNumber"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.studentName"/></th>
		</tr>
	
		<tr>
			<td align="left">
				<bean:write name="student" property="number"/>
			</td>
			<td align="left">
				<bean:write name="student" property="infoPerson.nome"/>
			</td>			
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
	
		<!-- Dissertation Title -->
		<tr>
			<th align="left" >
				<bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:&nbsp;
			</th>
			<th align="left" >
				<bean:write name="dissertationTitle"/>
			</th>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
		<!-- Proof Date -->
		<tr >
			<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
			<td><bean:write name="proofDate"/></td>
		</tr>
		
		<!-- Thesis Delivery Date -->
		<tr >
			<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>S&nbsp;</th>
			<td><bean:write name="thesisDeliveryDate"/></td>
		</tr>
			
	
	<!-- Final Result -->
		<tr>	
			<th align="left" >
				<bean:message key="label.masterDegree.administrativeOffice.finalResult"/>:&nbsp;
			</th>
			<td><bean:write name="finalResult"/></td>
		</tr>
					
		
		<!-- Attached Copies Number -->
		<tr>
			<th align="left" >
				<bean:message key="label.masterDegree.administrativeOffice.attachedCopiesNumber"/>:&nbsp;
			</th>
			<td><bean:write name="attachedCopiesNumber"/></td>
		</tr>
		<tr> 
			<td>&nbsp;</td>
		</tr>
				
		<!-- Juries -->
		<tr>
			<th align="left" colspan="2"><bean:message key="label.masterDegree.administrativeOffice.juries"/></th>				
		</tr>
			<logic:present name="<%= SessionConstants.JURIES_LIST %>" scope="request">
				<bean:define id="juriesList" name="<%= SessionConstants.JURIES_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherNumber"/></th>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>				
				</tr>					
				<logic:iterate id="jury" name="juriesList">
					<tr>
						<td align="left" ><bean:write name="jury" property="teacherNumber"/></td>
						<td align="left"><bean:write name="jury" property="infoPerson.nome"/></td>					
					</tr>				
				</logic:iterate>
			</logic:present >				
		
		<tr> 
			<td>&nbsp;</td>
		</tr>

		<tr>
			<td align="left" colspan="2">
				<bean:message key="label.masterDegree.administrativeOffice.lastModification"/>
				<bean:write name="lastModification" /><br/>
				<bean:message key="label.masterDegree.administrativeOffice.employee"/>
				<bean:write name="responsibleEmployee" property="person.nome" />
			</td>
		</tr>
		
	</table>



</center>