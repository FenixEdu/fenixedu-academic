
<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView" property="component">
	<bean:define id="component" name="siteView" property="component" />
	<logic:empty name="component" property="infoSiteStudentsAndGroupsList">
	<h2><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></h2>
	</logic:empty> 

	<logic:notEmpty name="component" property="infoSiteStudentsAndGroupsList">
	
	<table align="center" width="85%" cellspacing='1' cellpadding='1'>
	
	<logic:equal name="ShiftChosenType" value="1">
	<h2><bean:message key="title.viewStudentsAndGroupsByShift"/></h2>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="2">
	<h2><bean:message key="title.viewStudentsAndGroupsWithoutShift"/></h2>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="3">
	<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>
	</logic:equal>
	
	
	<br/>	
	<br>
	
 	<bean:size id="count" name="component" property="infoSiteStudentsAndGroupsList"/>
 	
 	<logic:equal name="ShiftChosenType" value="1">
	<bean:message key="label.teacher.NumberOfStudentsInShift" /><%= count %>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="2">
	<bean:message key="label.teacher.NumberOfStudentsWithoutShift" /><%= count %>
	</logic:equal>
	
	<logic:equal name="ShiftChosenType" value="3">
	<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
	</logic:equal>
	
	<br/>	
	<br/>
	
	<tr>
		<td class="listClasses-header" width="10%"><bean:message key="label.studentGroupNumber" />
		</td>
		<td class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</td>
		<td class="listClasses-header" width="53%"><bean:message key="label.nameWord" />
		
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="component" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		
		<tr>		
			<td class="listClasses"><bean:write name="infoStudentGroup" property="groupNumber"/>
			</td>
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			
			
		</tr>				
	 </logic:iterate>


</table>

<br/>
<br/>
	 

	

</logic:notEmpty> 

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoSiteStudentsAndGroupsList.not.available" />
</h2>
</logic:notPresent>