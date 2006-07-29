<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>


<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
<bean:define id="attachedCopiesNumber" name="<%= SessionConstants.ATTACHED_COPIES_NUMBER %>" />
<bean:define id="responsibleEmployee" name="<%= SessionConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= SessionConstants.LAST_MODIFICATION %>" scope="request"/>
<logic:present name="<%= SessionConstants.PROOF_DATE %>" scope="request">
	<bean:define id="proofDate" name="<%= SessionConstants.PROOF_DATE %>" />
</logic:present>
<logic:present name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" scope="request">
	<bean:define id="thesisDeliveryDate" name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" />
</logic:present>
			
<h2 align="center">
	<bean:message key="title.masterDegree.administrativeOffice.thesis.visualizeProofHistory"/> - 
	<bean:write name="lastModification" />
</h2>
<h2 align="center">
	<bean:message key="label.masterDegree.administrativeOffice.modifiedBy"/>: 
	<bean:write name="responsibleEmployee" property="person.nome" />
</h2>	
		
<center>
<span class="error"><!-- Error messages go here --><html:errors /></span>

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
		
		<!-- Proof Date -->
		<logic:present name="<%= SessionConstants.PROOF_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
				<td><bean:write name="proofDate"/></td>
			</tr>
		</logic:present>
		<logic:notPresent name="<%= SessionConstants.PROOF_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
				<td><bean:message key="message.masterDegree.administrativeOffice.proofDateNotDefined" /></td>
			</tr>
		</logic:notPresent>
		
		
		<!-- Thesis Delivery Date -->
		<logic:present name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>:&nbsp;</th>
				<td><bean:write name="thesisDeliveryDate"/></td>
			</tr>
		</logic:present>
		<logic:notPresent name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>:&nbsp;</th>
				<td><bean:message key="message.masterDegree.administrativeOffice.thesisDeliveryDateNotDefined" /></td>
			</tr>
		</logic:notPresent>
			
	
	<!-- Final Result -->
		<tr>	
			<th align="left" >
				<bean:message key="label.masterDegree.administrativeOffice.finalResult"/>:&nbsp;
			</th>
			<td>
				<bean:message name="<%= SessionConstants.FINAL_RESULT %>" bundle="ENUMERATION_RESOURCES" />
			</td>
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
	
			<logic:present name="<%= SessionConstants.JURIES_LIST %>" scope="request">
				<tr>
					<th align="left" colspan="2"><bean:message key="label.masterDegree.administrativeOffice.juries"/></th>				
				</tr>
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
				<tr> 
					<td>&nbsp;</td>
				</tr>
			</logic:present >	
			
			
		<!-- External Juries -->
		<logic:present name="<%= SessionConstants.EXTERNAL_JURIES_LIST %>" scope="request">
			<tr>
				<th align="left" colspan="2"><bean:message key="label.masterDegree.administrativeOffice.externalJuries"/></th>				
			</tr>
			<bean:define id="externalJuriesList" name="<%= SessionConstants.EXTERNAL_JURIES_LIST %>" type="java.util.List"/>
			<tr>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>				
			</tr>					
			<logic:iterate id="externalJury" name="externalJuriesList">
				<tr>
					<td align="left" ><bean:write name="externalJury" property="infoPerson.nome"/></td>
					<td align="left"><bean:write name="externalJury" property="infoInstitution.name"/></td>					
				</tr>				
			</logic:iterate>
			<tr> 
				<td>&nbsp;</td>
			</tr>
		</logic:present>	
		
			
<%
	java.util.Hashtable paramsVisuzalize = new java.util.Hashtable();
	InfoStudent infoStudent = (InfoStudent) student;
	
	paramsVisuzalize.put("degreeType", infoStudent.getDegreeType().toString());
	paramsVisuzalize.put("studentNumber", infoStudent.getNumber());
	paramsVisuzalize.put("method", "getStudentAndMasterDegreeProofVersion");
	pageContext.setAttribute("parametersVisuzalize", paramsVisuzalize, PageContext.PAGE_SCOPE);
%>	
	
		<tr> 
			<td  colspan="4"><html:link page="/visualizeMasterDegreeProof.do" name="parametersVisuzalize"><bean:message key="link.masterDegree.administrativeOffice.back" /></html:link></td>
		</tr>				
		
	</table>

</center>