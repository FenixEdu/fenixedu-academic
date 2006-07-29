<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<logic:present name="siteView"> 
<h2><bean:message key="title.insertStudentGroup"/></h2>

<html:form action="/insertStudentGroup" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>


	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.CreateStudentGroup.description" />
			</td>
		</tr>
	</table>
	<br/>
	
<h2><span class="error"><!-- Error messages go here --><html:errors /></span></h2>
<br/>

<b><bean:message key="message.insertStudentGroupData"/></b>
<br/>
<br/>

		 
<table width="50%" cellpadding="0" border="0">
	<tr>
		<td>
		<bean:message key="label.GroupNumber"/>
		</td>
		<td>
		<html:text bundle="HTMLALT_RESOURCES" altKey="text.nrOfElements" size="21" name="infoSiteStudentGroup" property="nrOfElements" />
		</td>
		
	</tr>

</table>
<br/>

<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<h2>
<bean:message key="message.editStudentGroupMembers.NoMembersToAdd" />
</h2>
</logic:empty>

<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<table width="50%" cellpadding="0" border="0">	
	<tr>
		<th class="listClasses-header">
		</th>
		<th class="listClasses-header"><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th class="listClasses-header"><bean:message key="label.teacher.StudentName" />
		</th>
		<th class="listClasses-header"><bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>
	
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">				
		<tr>	
			<td class="listClasses">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentCodes" property="studentCodes">
			<bean:write name="infoSiteStudentInformation" property="username"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
	 

</table>
</logic:notEmpty>
<br/>



<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudentGroup"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>
<table>
<tr>
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
		</html:submit>       
	</td>
	<td>	
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  
	</td>
</html:form>

		<html:form action="/viewShiftsAndGroups" method="get">
	<td>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewShiftsAndGroups"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</tr>
</table>


</logic:present>