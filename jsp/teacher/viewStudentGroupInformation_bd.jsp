<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	
	<h2><bean:message key="title.StudentGroupInformation"/></h2>
	
	<bean:define id="infoStudentGroup" name="component" property="infoStudentGroup"/>
	<bean:define id="studentGroupCode" name="infoStudentGroup" property="idInternal"/>
	<bean:define id="groupPropertiesCode" name="infoStudentGroup" property="infoGroupProperties.idInternal"/>
	<bean:define id="shiftCode" name="infoStudentGroup" property="infoShift.idInternal"/>
	
	
<logic:empty name="component" property="infoSiteStudentInformationList">
		
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.emptyStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>
	
	<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
	<b><bean:message key="label.groupManagement"/></b>&nbsp
	<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
    	<bean:message key="link.editGroupMembers"/>
    </html:link>&nbsp|&nbsp
 
    
    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.editGroupShift"/></html:link>&nbsp|&nbsp

	<html:link page="<%= "/deleteStudentGroup.do?method=deleteStudentGroup&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;shiftCode=" + shiftCode.toString() +"&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.deleteGroup"/></html:link>
	
	</logic:empty> 
	
	<logic:notEmpty name="component" property="infoSiteStudentInformationList">
	
	<table width="100%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentGroupInformation.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	<br/>	
	<table width="70%" cellpadding="0" border="0">
	<tbody>
	 <% Map sendMailParameters = new TreeMap(request.getParameterMap());
        sendMailParameters.put("method","prepare");
		request.setAttribute("sendMailParameters",sendMailParameters);%>
	<bean:define id="sendMailLinkParameters" type="java.util.Map" name="sendMailParameters"/>
	   <html:link page="/sendMailToAllStudents.do" name="sendMailLinkParameters">
			<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
	   </html:link>
	   
	   
	<tr>
		<td class="listClasses-header" width="15%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="60%"><bean:message key="label.nameWord" />
		</td>
		<td class="listClasses-header" width="25%"><bean:message key="label.emailWord" />
		</td>
	</tr>
	
		
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
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
	<b><bean:message key="label.groupManagement"/></b>&nbsp
	<html:link page="<%="/editStudentGroupMembers.do?method=prepareEditStudentGroupMembers&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString() + "&amp;shiftCode=" + shiftCode.toString()%>">
    	<bean:message key="link.editGroupMembers"/>
    </html:link>&nbsp|&nbsp
 
    
    <html:link page="<%="/editStudentGroupShift.do?method=prepareEditStudentGroupShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;shiftCode=" + shiftCode.toString()+ "&amp;studentGroupCode=" + studentGroupCode.toString()%>">
    	<bean:message key="link.editGroupShift"/></html:link><br/>
  
 </tbody>
</table>   	
  </logic:notEmpty>


  
	

	 
</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h2>
</logic:notPresent>
