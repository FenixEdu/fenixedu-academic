<%@ page language="java" %>

<%@ page import="java.lang.String" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupMembers"/></h2>


<logic:present name="siteView" property="component"> 
<bean:define id="component" name="siteView" property="component" />

<br/>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.EditStudentGroupMembers.description" />
			</td>
		</tr>
	</table>
	<br/>
	
<span class="error"><!-- Error messages go here --><html:errors /></span>
<br/>
<br/>
<logic:present name="shiftCode">
<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
    	<bean:message key="link.backToGroup"/></html:link><br/>
</logic:present>
<logic:notPresent name="shiftCode"> 
<html:link page="<%="/viewStudentGroupInformation.do?method=viewStudentGroupInformation&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
    	<bean:message key="link.backToGroup"/></html:link><br/>
</logic:notPresent> 

<logic:empty name="component" property="infoSiteStudentInformationList">
<h2><bean:message key="message.infoSiteStudentGroupList.not.available" /></h2>
</logic:empty> 	

<logic:notEmpty name="component" property="infoSiteStudentInformationList">
<html:form action="/deleteStudentGroupMembers" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<bean:message key="message.editStudentGroupMembers.RemoveMembers"/>

<br/>
<br/>		 
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
	
	<logic:iterate id="infoSiteStudentInformation" name="component" property="infoSiteStudentInformationList">			
		<tr>	
			<td class="listClasses">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToRemove" property="studentsToRemove">
			<bean:write name="infoSiteStudentInformation" property="username" />
			</html:multibox>
			</td>	
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			</td>	
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			</td>	
			<td class="listClasses"><bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		
	 	</tr>	
	 </logic:iterate>
 
</table>

<br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.remove"/>                    		         	
</html:submit>
<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br/>
<br/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>

</html:form>
</logic:notEmpty> 	

</logic:present>

<logic:notPresent name="siteView" property="component">
<h2>
<bean:message key="message.infoSiteStudentGroupList.not.available" />
</h2>
</logic:notPresent>


<logic:present name="infoStudentList"> 
		
<html:form action="/insertStudentGroupMembers" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<logic:empty name="infoStudentList"> 
<h2>
<bean:message key="message.editStudentGroupMembers.NoMembersToAdd" />
</h2>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 
<bean:message key="message.editStudentGroupMembers.InsertMembers"/>
<br/>
<br/>

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


	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td class="listClasses">
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToInsert" property="studentsToInsert">
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
			<bean:write name="infoPerson" property="username"/>
			</html:multibox>
			</td>	
			<td class="listClasses"><bean:write name="infoStudent" property="number"/>
			</td>	
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
			<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
			</td>
			<td class="listClasses"><bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
	 

</table>
<br/>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.objectCode"  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>

<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
</html:submit>       

<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</logic:notEmpty>

</html:form>


</logic:present>

<logic:notPresent name="infoStudentList">
<h2>
<bean:message key="message.editStudentGroupMembers.NoMembersToAdd" />
</h2>
</logic:notPresent>