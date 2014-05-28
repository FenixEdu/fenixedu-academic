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
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>


<h2><bean:message key="title.insertStudentGroup"/></h2>

<div class="dinline forminline">
<html:form action="/studentGroupManagement" >
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>

<div class="infoop2">
	<bean:message key="label.teacher.CreateStudentGroup.description" />
</div>

	
<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>

<p class="mtop15 mbottom05">
	<bean:message key="message.insertStudentGroupData"/>:
</p>


		 
<table class="tstyle5 thlight thmiddle">
	<tr>
		<th>
			<bean:message key="label.GroupNumber"/>
		</th>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.nrOfElements" size="21" name="infoSiteStudentGroup" property="nrOfElements" />
		</td>
	</tr>
</table>


<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<p>
	<em class="warning0"><bean:message key="message.editStudentGroupMembers.NoMembersToAdd" /></em>
</p>
</logic:empty>

<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
<p>
	<%
		final String urlPhoto = "/studentGroupManagement.do?method=prepareCreateStudentGroup"
				+ "&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")
				+ (request.getParameter("shiftCode") != null ? "&amp;shiftCode=" + request.getParameter("shiftCode") : "")
				+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode");
	%>
	<logic:present name="showPhotos">
		<html:link page="<%= urlPhoto + "&amp;showPhotos=true" %>">
		    <bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%= urlPhoto%>">
		    <bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>
</p>
<table class="tstyle5 thlight dinline">	
	<tr>
		<th>
		</th>
		<th>
			<bean:message key="label.teacher.StudentNumber" />
		</th>
		<logic:notPresent name="showPhotos">
			<th>
				<bean:message key="label.photo" />
			</th>
		</logic:notPresent>
		<th>
			<bean:message key="label.teacher.StudentName" />
		</th>
		<th>
			<bean:message key="label.teacher.StudentEmail" />
		</th>
	</tr>
	
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">				
		<tr>	
			<td>
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentCodes" property="studentCodes">
					<bean:write name="infoSiteStudentInformation" property="username"/>
				</html:multibox>
			</td>	
			<td>
				<bean:write name="infoSiteStudentInformation" property="number"/>
			</td>
			<logic:notPresent name="showPhotos">
				<td class="acenter">
					<bean:define id="personID" name="infoSiteStudentInformation" property="personID"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>	
			<td>
				<bean:write name="infoSiteStudentInformation" property="name"/>
			</td>
			<td>
				<bean:write name="infoSiteStudentInformation" property="email"/>
			</td>
	 	</tr>	
	 </logic:iterate>
</table>
</logic:notEmpty>


<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createStudentGroup"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />

<logic:present name="shiftCode">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>


<br/>

		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.insert"/>                    		         	
		</html:submit>       
	
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/>
		</html:reset>  

	</html:form>

	<html:form action="/studentGroupManagement" >
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="viewShiftsAndGroups"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseID"  property="executionCourseID" value="<%= pageContext.findAttribute("executionCourseID").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
	</html:form>

</div>
