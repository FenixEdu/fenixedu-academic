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


<logic:present name="infoSiteStudentGroup">



	<logic:empty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">
		<em><bean:message key="title.student.portalTitle"/></em>
		<h2><bean:message key="title.GroupStudentEnrolment"/></h2>
		<p><span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span></p>
	</logic:empty> 



	<logic:notEmpty name="infoSiteStudentGroup" property="infoSiteStudentInformationList">

	<em><bean:message key="title.student.portalTitle"/></em>
	<h2><bean:message key="title.GroupStudentEnrolment"/></h2>
	
	<div class="infoop2">
		<bean:message key="label.student.viewGroupStudentEnrolment.description" />
	</div>
	
	<p class="mtop15"><strong><bean:message key="label.StudentGroup"/></strong></p>


	<table class="tstyle4">
	<tr>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>
	
	 <bean:define id="mailingList" value=""/>
	<logic:iterate id="infoSiteStudentInformation" name="infoSiteStudentGroup" property="infoSiteStudentInformationList">			
		<tr>		
			<td><bean:write name="infoSiteStudentInformation" property="number"/>
			</td>	
			<td><bean:write name="infoSiteStudentInformation" property="name"/>
			</td>		
			<td>
				<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
				<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoSiteStudentInformation" property="email"/></html:link>
			</td>
		</tr>

		  <bean:define id="aux" name="mailingList"/>
			<logic:lessThan name="aux" value="1">
				<bean:define id="mailingList" value="<%= mail.toString() %>"/>	
			</logic:lessThan>
			<logic:greaterThan name="aux" value="0">
				<bean:define id="mailingList" value="<%= aux + ";"+ mail  %>"/>	
			</logic:greaterThan>
			</logic:iterate>
	</table>

</logic:notEmpty>

<p class="mvert15">
	<bean:message key="label.confirmGroupStudentEnrolment" />
</p>

	<table>
		<tr>
			  <td>
			  <html:form action="/groupStudentEnrolment"  style="margin: 0; padding: 0;">
				  <html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message key="button.finalize.enrolment"/>
				  </html:submit>
				  
				  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="enrolment"/>
				  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
				  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />	
				  <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode") %>" />
				  <logic:present name="shiftCode">
				  	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode") %>" />		
				  </logic:present>
					
				</html:form>
			  </td>
				
			<td>
			<html:form action="/viewStudentGroupInformation"  style="margin: 0; padding: 0;">
	
				<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/>                    		         	
				</html:cancel>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>
				<logic:present name="shiftCode">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.shiftCode"  property="shiftCode" value="<%= request.getParameter("shiftCode")%>"/>
				</logic:present>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.studentGroupCode"  property="studentGroupCode" value="<%= request.getParameter("studentGroupCode") %>" />	
				
			</html:form>
			</td>
		</tr>
	</table>
</logic:present>



<logic:notPresent name="infoSiteStudentGroup">
<p>
	<span class="warning0"><bean:message key="message.infoSiteStudentGroupList.not.available" /></span>
</p>
</logic:notPresent>