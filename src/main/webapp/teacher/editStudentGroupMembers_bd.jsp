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

<%@ page import="java.lang.String" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message key="title.editStudentGroupMembers"/></h2>


<div class="infoop2">
	<bean:message key="label.teacher.EditStudentGroupMembers.description" />
</div>

<p>	
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<logic:present name="shiftCode">
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;shiftCode=" + request.getParameter("shiftCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
		    	<bean:message key="link.backToGroup"/>
		    </html:link>
	    </li>
    </ul>
</logic:present>
<logic:notPresent name="shiftCode"> 
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewStudentGroupInformation&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")+ "&amp;studentGroupCode=" + request.getParameter("studentGroupCode")%>">
		    	<bean:message key="link.backToGroup"/>
		    </html:link>
	    </li>
    </ul>
</logic:notPresent> 

<logic:empty name="studentGroup" property="infoSiteStudentInformationList">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:empty> 	

<logic:notEmpty name="studentGroup" property="infoSiteStudentInformationList">
<html:form action="/studentGroupManagement" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<p class="mbottom05">
	<bean:message key="message.editStudentGroupMembers.RemoveMembers"/>
</p>

<table class="tstyle4 mtop05">
	<tr>
		<th>
		</th>
		<th><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th><bean:message key="label.teacher.StudentName" />
		</th>
		<th><bean:message key="label.teacher.StudentEmail" />
		</th>
		
	</tr>
	
	<logic:iterate id="infoSiteStudentInformation" name="studentGroup" property="infoSiteStudentInformationList">			
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToRemove" property="studentsToRemove">
			<bean:write name="infoSiteStudentInformation" property="username" />
			</html:multibox>
			</td>	
			<td class="acenter"><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
		
	 	</tr>	
	 </logic:iterate>
 
</table>

<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.remove"/>                    		         	
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
	</html:reset>
</p>

<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="deleteStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />
<logic:present name="shiftCode">
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>

</html:form>
</logic:notEmpty> 	

<logic:present name="infoStudentList"> 
		
<html:form action="/studentGroupManagement" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<logic:empty name="infoStudentList"> 
<p>
	<span class="warning0"><bean:message key="message.editStudentGroupMembers.NoMembersToAdd" /></span>
</p>
</logic:empty>

<logic:notEmpty name="infoStudentList"> 

<p class="mbottom05">
	<bean:message key="message.editStudentGroupMembers.InsertMembers"/>
</p>

<table class="tstyle4 mtop05">
	<tr>
		<th>
		</th>
		<th><bean:message key="label.teacher.StudentNumber" />
		</th>
		<th><bean:message key="label.teacher.StudentName" />
		</th>
		<th><bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>


	<logic:iterate id="infoStudent" name="infoStudentList">			
		<tr>	
			<td>
			<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsToInsert" property="studentsToInsert">
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
			<bean:write name="infoPerson" property="username"/>
			</html:multibox>
			</td>	
			<td class="acenter"><bean:write name="infoStudent" property="number"/>
			</td>	
			<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
			<td><bean:write name="infoPerson" property="nome"/>
			</td>
			<td><bean:write name="infoPerson" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
</table>


<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insertStudentGroupMembers"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
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
