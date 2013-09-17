<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


<logic:present name="infoSiteStudentsAndGroups">

<table align="left" width="98%">
<tbody>
<tr>
<td>
	
	<br/>
	<h2><bean:message key="title.viewStudentsAndGroupsWithoutShift"/></h2>
	<br/>
	

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		
	<table width="98%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentsAndGroupsWithoutShift.description" />
				</td>
			</tr>
	</table>
	<br />
	
	
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
	<table width="98%" cellpadding="0" cellspacing="0">
			<tr>
				<td class="infoop">
					<bean:message key="label.teacher.viewStudentsAndGroupsWithoutShift.description" />
				</td>
			</tr>
	</table>
	
	</logic:notEmpty>		
	
<br/>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<br/>
	
	
</td>
</tr>

<tr>
<td>
	



<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>

	<br/>
		
	<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
 	
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
    	<bean:message key="link.backToShiftsAndGroups"/></html:link><br/>
	

	<br/>

	<table width="75%" cellpadding="0" border="0">
	<tbody>
	
	<br/>
 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.teacher.NumberOfStudentsWithoutShift" /><%= count %>
	<br/>
	<logic:present name="showPhotos">
		<html:link page="<%="/viewStudentsAndGroupsWithoutShift.do?method=viewStudentsAndGroupsWithoutShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/viewStudentsAndGroupsWithoutShift.do?method=viewStudentsAndGroupsWithoutShift&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		   	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>
	<br/>
	
	<tr>
		<th class="listClasses-header"><bean:message key="label.numberWord" /></th>
		<th class="listClasses-header"><bean:message key="label.studentGroupNumber" /></th>
		<logic:notPresent name="showPhotos">
			<th class="listClasses-header"><bean:message key="label.photo" /></th>
		</logic:notPresent>
		<th class="listClasses-header"><bean:message key="label.nameWord" /></th>
		<th class="listClasses-header"><bean:message key="label.emailWord" /></th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		
		<tr>
			<td class="listClasses">
				<bean:write name="infoSiteStudentInformation" property="number"/>
			</td>
			<td class="listClasses">
				<bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>
			<logic:notPresent name="showPhotos">
				<td class="listClasses">
					<bean:define id="personID" name="infoSiteStudentInformation" property="personID"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>
			<td class="listClasses">
				<bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		</tr>				
	 </logic:iterate>

</tbody>
</table>

<br/>
<br/>

  </logic:notEmpty>

</td>
  </tr>
 </tbody>
	
</table>
	 
</logic:present>
