<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.Registration" %>
<%@ page import="net.sourceforge.fenixedu.domain.Teacher" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeProofVersion" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEmployee" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>


<bean:define id="student" name="<%= PresentationConstants.STUDENT %>" scope="request"/>
<bean:define id="scpID" name="studentCurricularPlan" property="externalId" scope="request" />
<bean:define id="dissertationTitle" name="<%= PresentationConstants.DISSERTATION_TITLE %>" />
<bean:define id="attachedCopiesNumber" name="<%= PresentationConstants.ATTACHED_COPIES_NUMBER %>" />
<logic:present name="<%= PresentationConstants.PROOF_DATE %>" scope="request">
	<bean:define id="proofDate" name="<%= PresentationConstants.PROOF_DATE %>" />
</logic:present>
<logic:present name="<%= PresentationConstants.THESIS_DELIVERY_DATE %>" scope="request">
	<bean:define id="thesisDeliveryDate" name="<%= PresentationConstants.THESIS_DELIVERY_DATE %>" />
</logic:present>



<bean:define id="responsibleEmployee" name="<%= PresentationConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= PresentationConstants.LAST_MODIFICATION %>" scope="request"/>

				
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
				<bean:write name="student" property="person.name"/>
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
		<logic:present name="<%= PresentationConstants.PROOF_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
				<td><bean:write name="proofDate"/></td>
			</tr>
		</logic:present>
		<logic:notPresent name="<%= PresentationConstants.PROOF_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.proofDate"/>:&nbsp;</th>
				<td><bean:message key="message.masterDegree.administrativeOffice.proofDateNotDefined" /></td>
			</tr>
		</logic:notPresent>
		
		
		<!-- Thesis Delivery Date -->
		<logic:present name="<%= PresentationConstants.THESIS_DELIVERY_DATE %>" scope="request">
			<tr>
				<th align="left" ><bean:message key="label.masterDegree.administrativeOffice.thesisDeliveryDate"/>:&nbsp;</th>
				<td><bean:write name="thesisDeliveryDate"/></td>
			</tr>
		</logic:present>
		<logic:notPresent name="<%= PresentationConstants.THESIS_DELIVERY_DATE %>" scope="request">
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
				<bean:message name="<%= PresentationConstants.FINAL_RESULT %>" bundle="ENUMERATION_RESOURCES" />
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

			<logic:present name="<%= PresentationConstants.JURIES_LIST %>" scope="request">
				<tr>
					<th align="left" colspan="2"><bean:message key="label.masterDegree.administrativeOffice.juries"/></th>				
				</tr>
				<bean:define id="juriesList" name="<%= PresentationConstants.JURIES_LIST %>" type="java.util.List"/>
				<tr>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherId"/></th>
					<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>				
				</tr>					
				<logic:iterate id="jury" name="juriesList">
					<tr>
						<td align="left" ><bean:write name="jury" property="teacherId"/></td>
						<td align="left"><bean:write name="jury" property="person.name"/></td>					
					</tr>				
				</logic:iterate>
			</logic:present >				
		
		<tr> 
			<td>&nbsp;</td>
		</tr>
		
		<!-- External Juries -->
		<logic:present name="<%= PresentationConstants.EXTERNAL_JURIES_LIST %>" scope="request">
			<tr>
				<th align="left" colspan="2"><bean:message key="label.masterDegree.administrativeOffice.externalJuries"/></th>				
			</tr>
			<bean:define id="externalJuriesList" name="<%= PresentationConstants.EXTERNAL_JURIES_LIST %>" type="java.util.List"/>
			<tr>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
				<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>				
			</tr>					
			<logic:iterate id="externalJury" name="externalJuriesList">
				<tr>
					<td align="left" ><bean:write name="externalJury" property="person.name"/></td>
					<td align="left">
						<logic:notEmpty name="externalJury" property="institutionUnit" >
							<bean:write name="externalJury" property="institutionUnit.name"/>
						</logic:notEmpty>
						&nbsp;
					</td>					
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
				<bean:write name="responsibleEmployee" property="person.name" />
			</td>
		</tr>
		
		
		<!-- history -->
		<logic:present name="<%= PresentationConstants.MASTER_DEGREE_PROOF_HISTORY %>" scope="request">
	
			<tr>
				<th align="left" colspan="4">
					<bean:message key="label.masterDegree.administrativeOffice.history"/>
				</th>		
			</tr>
			
			<bean:define id="masterDegreeProofHistory" name="<%= PresentationConstants.MASTER_DEGREE_PROOF_HISTORY %>" type="java.util.List"/>
		
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
				Registration infoStudent = (Registration) student;
			%>
			
			<logic:iterate id="masterDegreeProofVersion" name="masterDegreeProofHistory" type="net.sourceforge.fenixedu.domain.MasterDegreeProofVersion" >
				
				
				<%
					modification = new Date(masterDegreeProofVersion.getLastModification().getTime());				
					formattedModification = simpleDateFormat.format(modification);
					
					paramsHistory = new java.util.Hashtable();
					paramsHistory.put("degreeType", infoStudent.getDegreeType().toString());
					paramsHistory.put("scpID", scpID);
					paramsHistory.put("masterDegreeProofVersionID", masterDegreeProofVersion.getExternalId());
					paramsHistory.put("method", "getStudentAndMasterDegreeProofVersion");
					pageContext.setAttribute("parametersHistory", paramsHistory, PageContext.PAGE_SCOPE);
		
					
				%>
				
				
				<tr>
					<td align="left" colspan="4" >
						<html:link page="/visualizeMasterDegreeProofHistory.do" name="parametersHistory">
							<%= formattedModification %> - <%= masterDegreeProofVersion.getResponsibleEmployee().getPerson().getName() %>
						</html:link>
					</td>				
				</tr>		
						
			</logic:iterate>	
		
		</logic:present>
		
	</table>



</center>