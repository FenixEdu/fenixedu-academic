<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.Registration" %>
<%@ page import="net.sourceforge.fenixedu.domain.Teacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoMasterDegreeThesisDataVersion" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEmployee" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Date" %>

<bean:define id="student" name="<%= PresentationConstants.STUDENT %>" scope="request"/>
<bean:define id="scpID" name="studentCurricularPlan" property="externalId" scope="request" />
<bean:define id="dissertationTitle" name="<%= PresentationConstants.DISSERTATION_TITLE %>" scope="request"/>
<bean:define id="responsibleEmployee" name="<%= PresentationConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= PresentationConstants.LAST_MODIFICATION %>" scope="request"/>

<h2 align="center"><bean:message key="link.masterDegree.administrativeOffice.thesis.visualize"/></h2>
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
		<th align="left" colspan="2">
			<bean:message key="label.masterDegree.administrativeOffice.dissertationTitle"/>:&nbsp;
			<bean:write name="dissertationTitle"/>			
		</th>
	</tr>
	<tr> 
		<td>&nbsp;</td>
	</tr>
	
</table>


<table border="0" width="100%" cellspacing="3" cellpadding="10">

	<!-- Guiders -->
	<logic:notEmpty name="<%= PresentationConstants.GUIDERS_LIST %>" scope="request">
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.guiders"/></th>				
		</tr>
		
		<bean:define id="guidersList" name="<%= PresentationConstants.GUIDERS_LIST %>" type="java.util.List"/>
		<tr>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherId"/></th>
			<th align="left" width="40%"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
			<td width="30%">&nbsp;</td>
			<td>&nbsp;</td>						
		</tr>					
		<logic:iterate id="guider" name="guidersList">
			<tr>
				<td align="left"><bean:write name="guider" property="teacherId"/></td>
				<td align="left"><bean:write name="guider" property="person.name"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>					
			</tr>				
		</logic:iterate>
	</logic:notEmpty >				
	<tr> 
		<td>&nbsp;</td>
	</tr>
	
	
	<!-- External Guiders -->
	
	<logic:notEmpty name="<%= PresentationConstants.EXTERNAL_GUIDERS_LIST %>" scope="request">
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.externalGuiders"/></th>				
		</tr>
	
		<bean:define id="externalGuidersList" name="<%= PresentationConstants.EXTERNAL_GUIDERS_LIST %>" type="java.util.List"/>
		<tr>
			<td>&nbsp;</td>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
			<td>&nbsp;</td>
			<td>&nbsp;</td>						
		</tr>
		<logic:iterate id="externalGuider" name="externalGuidersList">
			<tr>
				<td>&nbsp;</td>
				<td align="left"><bean:write name="externalGuider" property="person.name"/></td>
				<td align="left"><bean:write name="externalGuider" property="institutionUnit.name"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>					
			</tr>				
		</logic:iterate>
		<tr> 
			<td>&nbsp;</td>
		</tr>			
	</logic:notEmpty>
	
	<!-- Assistent Guiders -->
	
	<logic:notEmpty name="<%= PresentationConstants.ASSISTENT_GUIDERS_LIST %>" scope="request">
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.assistentGuiders"/></th>				
		</tr>
	
		<bean:define id="assistentsGuidersList" name="<%= PresentationConstants.ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
		<tr>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherId"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.teacherName"/></th>
			<td>&nbsp;</td>
			<td>&nbsp;</td>						
		</tr>
		<logic:iterate id="assistentGuider" name="assistentsGuidersList">
			<tr>
				<td align="left"><bean:write name="assistentGuider" property="teacherId"/></td>
				<td align="left"><bean:write name="assistentGuider" property="person.name"/></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>					
			</tr>				
		</logic:iterate>
		<tr> 
			<td>&nbsp;</td>
		</tr>			
	</logic:notEmpty>

		
		<!-- External Assistent Guiders -->
	
			
	<logic:notEmpty name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" scope="request">
	
		<tr>
			<th align="left" colspan="4"><bean:message key="label.masterDegree.administrativeOffice.externalAssistentGuiders"/></th>				
		</tr>		
		<bean:define id="externalAssistentsGuidersList" name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" type="java.util.List"/>
		<tr>
			<td>&nbsp;</td>	
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonName"/></th>
			<th align="left"><bean:message key="label.masterDegree.administrativeOffice.externalPersonInstitution"/></th>
			<td>&nbsp;</td>									
		</tr>			
		<logic:iterate id="externalAssistentGuider" name="externalAssistentsGuidersList">
			<tr>
				<td>&nbsp;</td>
				<td align="left"><bean:write name="externalAssistentGuider" property="person.name"/></td>
				<td align="left"><bean:write name="externalAssistentGuider" property="institutionUnit.name"/></td>
				<td>&nbsp;</td>						
			</tr>				
		</logic:iterate>
		<tr> 
			<td>&nbsp;</td>
		</tr>		
	</logic:notEmpty>
	
	<!-- last modification -->
	<tr>
		<td align="left" colspan="4">
			<bean:message key="label.masterDegree.administrativeOffice.lastModification"/>
			<bean:write name="lastModification" /><br/>
			<bean:message key="label.masterDegree.administrativeOffice.employee"/>
			<bean:write name="responsibleEmployee" property="person.name" />
		</td>
	</tr>			
	<tr> 
		<td>&nbsp;</td>
	</tr>
					
	<!-- history -->
	<logic:notEmpty name="<%= PresentationConstants.MASTER_DEGREE_THESIS_HISTORY %>" scope="request">
	
		<tr>
			<th align="left" colspan="4">
				<bean:message key="label.masterDegree.administrativeOffice.history"/>
			</th>		
		</tr>
		
		<bean:define id="masterDegreeThesisHistory" name="<%= PresentationConstants.MASTER_DEGREE_THESIS_HISTORY %>" type="java.util.List"/>
	
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
		
		<logic:iterate id="masterDegreeThesisDataVersion" name="masterDegreeThesisHistory" type="net.sourceforge.fenixedu.domain.MasterDegreeThesisDataVersion" >
			
			
			<%
				modification = new Date(masterDegreeThesisDataVersion.getLastModification().getTime());				
				formattedModification = simpleDateFormat.format(modification);
				
				paramsHistory = new java.util.Hashtable();
				paramsHistory.put("degreeType", infoStudent.getDegreeType().toString());
				paramsHistory.put("scpID", scpID);
				paramsHistory.put("masterDegreeThesisDataVersionID", masterDegreeThesisDataVersion.getExternalId());
				paramsHistory.put("method", "getStudentAndMasterDegreeThesisDataVersion");
				pageContext.setAttribute("parametersHistory", paramsHistory, PageContext.PAGE_SCOPE);
	
				
			%>
			
			
			<tr>
				<td align="left" colspan="4" >
					<html:link page="/visualizeMasterDegreeThesisHistory.do" name="parametersHistory">
						<%= formattedModification %> - <%= masterDegreeThesisDataVersion.getResponsibleEmployee().getPerson().getName() %>
					</html:link>
				</td>				
			</tr>		
					
		</logic:iterate>	
	
	</logic:notEmpty>
					
</table>

</center>