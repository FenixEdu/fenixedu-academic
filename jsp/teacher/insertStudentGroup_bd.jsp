<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.insertStudentGroup"/></h2>

<logic:present name="siteView"> 
	

<html:form action="/insertStudentGroup" method="get">
<html:hidden property="page" value="1"/>
<b><bean:message key="message.insertStudentGroupData"/></b>
<br>

<h2><span class="error"><html:errors/></span></h2>
<br>		 
<table width="50%" cellpadding="0" border="0">
	<tr>
		<td>
		<bean:message key="label.GroupNumber"/>
		<%--<h2><span class="error"><html:errors/></span></h2>--%>
		</td>
		<td>
		<html:text size="21" property="groupNumber" />
		</td>
		<%--<td><span class="error"><html:errors property="groupNumber"/></span></td>--%>
	</tr>
	
	<tr>
	
		<td><bean:message key="message.insertStudentGroupShift"/></td>
		
		<td>
		<html:select property="shift" size="1">
    	<html:options collection="shiftsList" property="value" labelProperty="label"/>
    	</html:select>
    	</td>
			
	</tr>	
</table>

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
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />


<html:submit styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
</html:submit>       
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
</html:reset>  

</html:form>
</logic:present>