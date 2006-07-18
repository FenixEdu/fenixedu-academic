<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<style>
form { margin: 0; padding: 0; }
</style>


<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="shiftsList">
	
<br>

	<div class="infoop2">
		<bean:message key="label.student.EditStudentGroupShift.description" />
	</div>

	<br/>
		
<html:form action="/editStudentGroupShift" method="get">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<span class="error"><html:errors/></span>

<br/>

<span class="error"><html:errors property="shiftType"/></span>

<table width="50%" cellpadding="0" border="0">
	<tr>
		<td><bean:message key="label.editStudentGroupShift.oldShift"/></td>
		<td><bean:write name="shift" property="nome"/></td>
		<td><span class="error"><html:errors property="shiftType"/></span></td>
	</tr>
	<tr>
	 	<td><bean:message key="message.editStudentGroupShift"/></td>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" size="1">
    		<html:options collection="shiftsList" property="value" labelProperty="label"/>
	    	</html:select>
	    </td>
		<td><span class="error"><html:errors property="shiftType"/></span></td>
	</tr>	
</table>



<br/>

<table>
	<tr>
		<td>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.change.enrolment"/></html:submit>  
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
			</html:form>
		</td>
		<td>
			<html:form action="/viewStudentGroupInformation" method="get">
			<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
			</html:form>
		</td>
	</tr>
</table>

</logic:present>