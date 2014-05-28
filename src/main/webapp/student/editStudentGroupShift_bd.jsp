<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<h2><bean:message key="title.editStudentGroupShift"/></h2>

<logic:present name="shiftsList">
	


	<div class="infoop2">
		<bean:message key="label.student.EditStudentGroupShift.description" />
	</div>


<div class="dinline forminline">

<html:form action="/editStudentGroupShift"  style="margin: 0; padding: 0;" >
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

<html:form action="/viewStudentGroupInformation"  style="margin: 0; padding: 0; ">
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
</html:form>

</div>

</logic:present>