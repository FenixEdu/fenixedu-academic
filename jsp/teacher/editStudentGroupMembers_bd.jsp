<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupMembers"/></h2>

<logic:present name="infoStudentList"> 

<html:form action="/editStudentGroupMembers" method="get">
<html:hidden property="page" value="1"/>
<bean:message key="message.editStudentGroupMembers.RemoveMembers"/>

<br>
<br>		 
<table width="50%" cellpadding="0" border="0">

	
	<tr>
		<td class="listClasses-header">
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentNumber" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentName" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentEmail" />
		</td>
	</tr>
	
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentInformationList">			
		<tr>	
			<td class="listClasses">
			<html:multibox property="studentsToRemove">
			<bean:write name="infoSiteStudentInformation" property="number" />
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

<br>
<html:submit styleClass="inputbutton"><bean:message key="button.delete"/>                    		         	
</html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>
<br>
<br>

<html:hidden property="method" value="prepareEditStudentGroupMembers"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />
</html:form>

<html:form action="/insertStudentGroup" method="get">
<html:hidden property="page" value="1"/>
<bean:message key="message.editStudentGroupMembers.InsertMembers"/>
<br>
<br>
<table width="50%" cellpadding="0" border="0">

	
	<tr>
		<td class="listClasses-header">
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentNumber" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentName" />
		</td>
		<td class="listClasses-header"><bean:message key="label.teacher.StudentEmail" />
		</td>
	</tr>

	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td class="listClasses">
			<html:multibox property="studentCodes">
			<bean:write name="infoStudent" property="idInternal"/>
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
<br>

<html:hidden property="method" value="createStudentGroup"/>
<html:hidden  property="objectCode" value="<%= pageContext.findAttribute("objectCode").toString() %>" />

<html:submit styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
</html:submit>       
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</html:form>
</logic:present>