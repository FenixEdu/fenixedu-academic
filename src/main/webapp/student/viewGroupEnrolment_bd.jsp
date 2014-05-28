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

<h2><bean:message key="title.enrolmentGroup.insertNewGroup"/></h2>

	<div class="infoop2">
		<logic:equal name="infoGrouping" property="enrolmentPolicy.type" value="1">
			<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title1" />:</strong><br/>
			<bean:message key="label.student.viewGroupEnrolment.description.1" /></p>
		</logic:equal>
		<logic:equal name="infoGrouping" property="enrolmentPolicy.type" value="2">
			<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title2" />:</strong><br/>
			<bean:message key="label.student.viewGroupEnrolment.description.2" /></p>
		</logic:equal>
	</div>

	<p>Agrupamento: <bean:write name="infoGrouping" property="name"/></p>

<%--
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" length="1">
		<bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" offset="1">
		, <bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
--%>


	<html:form action="/groupEnrolment"  style="margin: 0; padding: 0;">

	<p class="mvert15"><span class="error"><!-- Error messages go here --><html:errors /></span></p>		 

	<bean:define id="groupNumber" name="groupNumber"/>

		<!--
		<p><b class="infoop2"><bean:message key="label.GroupNumber"/><bean:write name="groupNumber"/></b></p>
		-->
		
		<h3><bean:message key="label.GroupNumber"/><bean:write name="groupNumber"/></h3>
		<p class="mvert05"><bean:message key="label.infoStudents.studentsWithoutGroup" /></p>

<logic:present name="infoUserStudent"> 

	<table class="tstyle4" width="80%" cellpadding="0" border="0">	
		<tr>
		
		<th width="5%">
		</th>
		<th width="10%"><bean:message key="label.numberWord" />
		</th>
		<th width="35%"><bean:message key="label.nameWord" />
		</th>
		<th width="20%"><bean:message key="label.emailWord" />
		</th>
		</tr>
			<tr>	
			
				<td>
				<input alt="input.studentsAutomaticallyEnroled" type="checkbox" name="studentsAutomaticallyEnroled" checked disabled>
				</td>	
				
				<td><bean:write name="infoUserStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoUserStudent" property="infoPerson"/>		
				<td><bean:write name="infoPerson" property="nome"/>
				</td>
				<td><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
		</table>
	</logic:present>
	
<logic:present name="infoStudents"> 
	<logic:empty name="infoStudents">
	<p class="warning0">
		<bean:message key="message.infoStudents.not.available" />
	</p>
	</logic:empty>
	
	<logic:notEmpty name="infoStudents">
	
	<br/>
	
	<table class="tstyle4" width="80%" cellpadding="0" border="0">	
		<logic:iterate id="infoStudent" name="infoStudents">			
			<tr>	
				<td width="5%">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsNotEnroled" property="studentsNotEnroled">
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
				<bean:write name="infoPerson" property="username"/>
				</html:multibox>
				</td>	
				<td width="10%"><bean:write name="infoStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
				<td width="35%"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td width="20%"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
	 	</logic:iterate>
	</table>
	</logic:notEmpty>
</logic:present>


<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrolment"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
<logic:present name="shiftCode"> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />
</logic:present>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupNumber"  property="groupNumber" value="<%= groupNumber.toString() %>" />

<table>
<tr>
	<td>
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/></html:submit>       
	</td>
	
	<td>
		<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset" styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>  
		</html:form>
	</td>
	
	<td>
		<html:form action="/viewShiftsAndGroups"  style="margin: 0; padding: 0;">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
		</html:form>
	</td>
</tr>
</table>
