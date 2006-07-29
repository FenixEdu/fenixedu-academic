<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoStudent" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoTeacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEmployee" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>


<bean:define id="student" name="<%= SessionConstants.STUDENT %>" scope="request"/>
<bean:define id="dissertationTitle" name="<%= SessionConstants.DISSERTATION_TITLE %>" />
<bean:define id="attachedCopiesNumber" name="<%= SessionConstants.ATTACHED_COPIES_NUMBER %>" />
<logic:present name="<%= SessionConstants.PROOF_DATE %>" scope="request">
	<bean:define id="proofDate" name="<%= SessionConstants.PROOF_DATE %>" />
</logic:present>
<logic:present name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" scope="request">
	<bean:define id="thesisDeliveryDate" name="<%= SessionConstants.THESIS_DELIVERY_DATE %>" />
</logic:present>



<bean:define id="responsibleEmployee" name="<%= SessionConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= SessionConstants.LAST_MODIFICATION %>" scope="request"/>

				
<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.visualizeProof"/></h2>
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
			</logic:present >				
		
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
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

		<tr>
			<td align="left" colspan="2">
				<bean:message key="label.masterDegree.administrativeOffice.lastModification"/>
				<bean:write name="lastModification" /><br/>
				<bean:message key="label.masterDegree.administrativeOffice.employee"/>
				<bean:write name="responsibleEmployee" property="person.nome" />
			</td>
		</tr>
		
		
		<!-- history -->
		<logic:present name="<%= SessionConstants.MASTER_DEGREE_PROOF_HISTORY %>" scope="request">
	
			<tr>
				<th align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.history"/>
				</th>		
			</tr>
			
			<bean:define id="masterDegreeProofHistory" name="<%= SessionConstants.MASTER_DEGREE_PROOF_HISTORY %>" type="java.util.List"/>
		
			<tr>			
				<th align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.modificationDate"/> - 
					<bean:message key="label.masterDegree.administrativeOffice.employee"/>
				</th>					
			</tr>		
		
			
			<%
				Date modification = null;
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy k:mm:ss");
				String formattedModification = null;
				
				java.util.Hashtable paramsHistory = null;
				InfoStudent infoStudent = (InfoStudent) student;
			%>
			
			<logic:iterate id="masterDegreeProofVersion" name="masterDegreeProofHistory" type="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion" >
				
				
				<%
					modification = new Date(masterDegreeProofVersion.getLastModification().getTime());				
					formattedModification = simpleDateFormat.format(modification);
					
					paramsHistory = new java.util.Hashtable();
					paramsHistory.put("degreeType", infoStudent.getDegreeType().toString());
					paramsHistory.put("studentNumber", infoStudent.getNumber());
					paramsHistory.put("masterDegreeProofVersionID", masterDegreeProofVersion.getIdInternal());
					paramsHistory.put("method", "getStudentAndMasterDegreeProofVersion");
					pageContext.setAttribute("parametersHistory", paramsHistory, PageContext.PAGE_SCOPE);
		
					
				%>
				
				
				<tr>
					<td align="left" colspan="4" >
						<html:link page="/visualizeMasterDegreeProofHistory.do" name="parametersHistory">
							<%= formattedModification %> - <%= masterDegreeProofVersion.getInfoResponsibleEmployee().getPerson().getNome() %>
						</html:link>
					</td>				
				</tr>		
						
			</logic:iterate>	
		
		</logic:present>
		
	</table>



</center>