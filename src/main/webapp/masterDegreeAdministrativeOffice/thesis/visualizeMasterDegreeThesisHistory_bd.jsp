<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ page import="net.sourceforge.fenixedu.domain.student.Registration" %>
<%@ page import="net.sourceforge.fenixedu.domain.Teacher" %>
<%@ page import="net.sourceforge.fenixedu.domain.organizationalStructure.ExternalContract" %>
<%@ page import="net.sourceforge.fenixedu.dataTransferObject.InfoEmployee" %>

<bean:define id="student" name="<%= PresentationConstants.STUDENT %>" scope="request"/>
<bean:define id="scpID" name="studentCurricularPlan" property="externalId" scope="request" />
<bean:define id="dissertationTitle" name="<%= PresentationConstants.DISSERTATION_TITLE %>" scope="request"/>
<bean:define id="responsibleEmployee" name="<%= PresentationConstants.RESPONSIBLE_EMPLOYEE %>" scope="request"/>
<bean:define id="lastModification" name="<%= PresentationConstants.LAST_MODIFICATION %>" scope="request"/>

<h2 align="center">
	<bean:message key="title.masterDegree.administrativeOffice.thesis.visualizeHistory"/> - 
	<bean:write name="lastModification" />
</h2>
<h2 align="center">
	<bean:message key="label.masterDegree.administrativeOffice.modifiedBy"/>: 
	<bean:write name="responsibleEmployee" property="person.name" />
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
	<logic:present name="<%= PresentationConstants.GUIDERS_LIST %>" scope="request">
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
	</logic:present >				
	<tr> 
		<td>&nbsp;</td>
	</tr>
	
	<!-- External Guiders -->
	
	<logic:present name="<%= PresentationConstants.EXTERNAL_GUIDERS_LIST %>" scope="request">
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
	</logic:present>
	
	<!-- Assistent Guiders -->
	
	<logic:present name="<%= PresentationConstants.ASSISTENT_GUIDERS_LIST %>" scope="request">
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
	</logic:present>

		
		<!-- External Assistent Guiders -->
	
			
	<logic:present name="<%= PresentationConstants.EXTERNAL_ASSISTENT_GUIDERS_LIST %>" scope="request">
	
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
	</logic:present>
	
<%
	java.util.Hashtable paramsVisuzalize = new java.util.Hashtable();
	Registration infoStudent = (Registration) student;
	
	paramsVisuzalize.put("degreeType", infoStudent.getDegreeType().toString());
	paramsVisuzalize.put("scpID", scpID);
	paramsVisuzalize.put("method", "getStudentAndMasterDegreeThesisDataVersion");
	pageContext.setAttribute("parametersVisuzalize", paramsVisuzalize, PageContext.PAGE_SCOPE);
%>	
	
	<tr> 
		<td  colspan="4"><html:link page="/visualizeMasterDegreeThesis.do" name="parametersVisuzalize"><bean:message key="link.masterDegree.administrativeOffice.back" /></html:link></td>
	</tr>						
</table>

</center>