<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


<logic:present name="infoSiteStudentGroup">
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	<bean:define id="infoStudentGroup" name="infoSiteStudentGroup" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="idInternal"/>
	<bean:define id="infoAttendsSet" name="infoStudentGroup" property="infoAttendsSet"/>
	<bean:define id="groupPropertiesCode" name="infoAttendsSet" property="infoGroupProperties.idInternal"/>
	
	
	
<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
		
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.student.emptyStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	 <span class="error"><html:errors/></span> 	

<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode") + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
	
	<bean:define id="nrOfElements" name="infoSiteStudentGroup" property="nrOfElements"/>
				 			 		
	<b><bean:message key="label.nrOfElements"/> </b><bean:write name="nrOfElements"/>
	<br/>	
	<br/>
	
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	
	
	
			 			
	<b><bean:message key="label.groupOption"/></b>&nbsp
	<logic:lessEqual name="ShiftType" value="2">
	<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>
	<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode") +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString() +"&amp;shiftCode=" +shiftCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    	<bean:message key="link.enrolment"/>
    </html:link>
	</logic:lessEqual>
		
	<logic:greaterEqual name="ShiftType" value="3">
	<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode") +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    	<bean:message key="link.enrolment"/>
    </html:link>
	</logic:greaterEqual>
	
</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.student.viewStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	 <span class="error"><html:errors/></span> 	


	<html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode") + "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>	
	<bean:define id="nrOfElements" name="infoSiteStudentGroup" property="nrOfElements"/>
				 			 		
	<b><bean:message key="label.nrOfElements"/> </b><bean:write name="nrOfElements"/>
	<br/>
	
	<table width="70%" cellpadding="0" border="0">
	<tbody>   
	<tr>
		<td class="listClasses-header" width="15%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="60%"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header" width="25%"><bean:message key="label.emailWord" />
		</td>
	</tr>
	
		
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">			
		<tr>		
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			<td class="listClasses">
				<logic:present name="infoSiteStudentInformation" property="email">
					<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
				</logic:present>
				<logic:notPresent name="infoSiteStudentInformation" property="email">
					&nbsp;
				</logic:notPresent>
				
			</td>
		</tr>
		
				
	 </logic:iterate>

</tbody>
</table>

<br/>
<br/>

<table width="70%" cellpadding="0" border="0">
<tbody>

	<b><bean:message key="label.groupOperations"/></b>&nbsp
	<logic:lessEqual name="ShiftType" value="2">
	<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>
	<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&amp;shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    	<bean:message key="link.enrolment"/>
    </html:link> &nbsp|&nbsp
	</logic:lessEqual>	
	
	<logic:greaterEqual name="ShiftType" value="3">
	<html:link page="<%="/groupStudentEnrolment.do?method=prepareEnrolment&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    	<bean:message key="link.enrolment"/>
    </html:link> &nbsp|&nbsp
	</logic:greaterEqual>	



    	<logic:lessEqual name="ShiftType" value="2">
    	<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>
   <html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+"&amp;shiftCode=" + request.getParameter("shiftCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
   	    <bean:message key="link.removeEnrolment"/>
    </html:link> &nbsp|&nbsp
	</logic:lessEqual>
		    
    <logic:greaterEqual name="ShiftType" value="3">
   <html:link page="<%="/removeGroupEnrolment.do?method=prepareRemove&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>" paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
   	    <bean:message key="link.removeEnrolment"/>
    </html:link> &nbsp|&nbsp
	</logic:greaterEqual>	    
  

<logic:equal name="ShiftType" value="1">
     <bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>
   <html:link page="<%="/editStudentGroupShift.do?method=prepareEdit&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
        <bean:message key="link.editStudentGroupShift"/>
   </html:link>
</logic:equal>


 <logic:equal name="ShiftType" value="3">
    <html:link page="<%="/enrollStudentGroupShift.do?method=prepareEnrollStudentGroupShift&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    	<bean:message key="link.enrollStudentGroupInShift"/>
    </html:link>
	</logic:equal>

  <logic:equal name="ShiftType" value="2">
    <html:link page="<%="/unEnrollStudentGroupShift.do?method=unEnrollStudentGroupShift&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+"&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")%>"  paramId="studentGroupCode" paramName="infoStudentGroup" paramProperty="idInternal">
    <bean:message key="link.unEnrollStudentGroupShift"/>
    </html:link>
	</logic:equal>
 
  
 </tbody>
</table>   	
  </logic:notEmpty>


</logic:present>

<logic:notPresent name="infoSiteStudentGroup">
<h2>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h2>
</logic:notPresent>












