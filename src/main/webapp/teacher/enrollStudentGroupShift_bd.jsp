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

<h2><bean:message key="title.enrollStudentGroupShift"/></h2>
	
<br/>

	<table width="98%" cellpadding="0" cellspacing="0">
		<tr>
			<td class="infoop">
				<bean:message key="label.teacher.EnrollStudentGroupShift.description" />
			</td>
		</tr>
	</table>
	<br/>
		
<html:form action="/studentGroupManagement" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
<span class="error"><!-- Error messages go here --><html:errors /></span>

<br/>
<br/>		 

<table width="50%" cellpadding="0" border="0">
		
		<tr>
			<td><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
		</tr>
		
		<tr>
	 	<td><bean:message key="message.editStudentGroupShift"/></td>
		
		<td>
		<html:select bundle="HTMLALT_RESOURCES" altKey="select.shift" property="shift" size="1">
    	<html:options collection="shiftsList" property="value" labelProperty="label"/>
    	</html:select>
    	</td>
			
			<td><span class="error"><!-- Error messages go here --><html:errors property="shiftType"/></span></td>
		</tr>	

 
</table>

<table>
<tr>
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.join"/>                    		         	
		</html:submit>
	</td>	
	<td>	
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>
	</td>		
<br/>
<br/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrollStudentGroupShift"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
</html:form>


	<html:form action="/studentGroupManagement" >
	<td>
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
		</html:cancel>
	</td>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewStudentGroupInformation"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
	</html:form>

</tr>
</table>
