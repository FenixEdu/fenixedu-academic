<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<em><bean:message key="title.student.portalTitle"/></em>
<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="shiftsList">
	


	<div class="infoop2">
		<bean:message key="label.student.EditStudentGroupShift.description" />
	</div>


<div class="dinline forminline">

<html:form action="/editStudentGroupShift" method="get" style="margin: 0; padding: 0;" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<p class="mtop15">
	<span class="error"><!-- Error messages go here --><html:errors /></span>
	<span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span>
</p>

<table class="tstyle5 thright thlight">
	<tr>
		<th><bean:message key="label.editStudentGroupShift.oldShift"/></th>
		<td><bean:write name="shift" property="nome"/></td>
		<td class="tderror1 tdclear"><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
	</tr>
	<tr>
	 	<th><bean:message key="message.editStudentGroupShift"/></th>
		<td>
			<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" size="1">
    		<html:options collection="shiftsList" property="value" labelProperty="label"/>
	    	</html:select>
	    </td>
		<td class="tderror1 tdclear"><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
	</tr>	
</table>


		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.change.enrolment"/></html:submit>  
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
</html:form>

<html:form action="/viewStudentGroupInformation" method="get" style="margin: 0; padding: 0; ">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
</html:form>

</div>

</logic:present>