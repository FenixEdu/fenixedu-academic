<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="shiftsList">
	
<br>

	<table width="100%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.student.EditStudentGroupShift.description" />
			</td>
		</tr>
	</table>
	<br>
		
<html:form action="/editStudentGroupShift" method="get">
<html:hidden property="page" value="1"/>
<span class="error"><html:errors/></span>

<br>
<br>		 

<table width="50%" cellpadding="0" border="0">
		
		<tr>
			<td><bean:message key="label.editStudentGroupShift.oldShift"/></td>
			 <logic:present name="shift">
			<td><bean:write name="shift" property="nome"/></td>
			</logic:present>
			 <logic:notPresent name="shift">
			<td><bean:message key="message.NoShift"/></td>
			</logic:notPresent>
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>
		
		<tr>
	 	<td><bean:message key="message.editStudentGroupShift"/></td>
		
		<td>
		<html:select property="shift" size="1">
    	<html:options collection="shiftsList" property="value" labelProperty="label"/>
    	</html:select>
    	</td>
			
			<td><span class="error"><html:errors property="shiftType"/></span></td>
		</tr>	

 
</table>
<br>

<table>
<tr>
	<td><html:submit styleClass="inputbutton"><bean:message key="button.change.enrolment"/>                    		         	
		</html:submit>  
	     
		<html:reset styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

	

<html:hidden property="method" value="edit"/>
<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
<logic:present name="shiftCode">
<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>
<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

</html:form>

	</td>
	<td>
		<html:form action="/viewStudentGroupInformation" method="get">
	
		<html:cancel styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	
		<html:hidden property="method" value="execute"/>
		<html:hidden  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
		<logic:present name="shiftCode">
		<html:hidden  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
		</logic:present>
		<html:hidden  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
		
	</html:form>
	</td>
	
</tr>
</table>


</logic:present>