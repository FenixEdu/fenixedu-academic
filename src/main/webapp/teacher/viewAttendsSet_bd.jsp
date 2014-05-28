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
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


	<h2><bean:message key="title.attendsSetInformation"/></h2>

	<bean:define id="attends" name="infoGrouping" property="infoAttends"/>
	<bean:define id="groupPropertiesCode" name="infoGrouping" property="externalId"/>
	<bean:define id="groupingOID" name="infoGrouping" property="externalId"/>

	<logic:empty name="attends">
		<div class="infoop2">
			<bean:message key="label.teacher.emptyAttendsSet.description" />
		</div>
	</logic:empty>	
	
	<logic:notEmpty name="attends">
		<div class="infoop2">
			<bean:message key="label.teacher.viewAttendsSet.description" />
		</div>
	</logic:notEmpty>		
	
<p>
	<span class="error0"><!-- Error messages go here --><html:errors /></span>
</p>	



<logic:empty name="attends">

	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
		    <bean:message key="link.backToShiftsAndGroups"/>
		    </html:link>
	    </li>
    </ul>
		
	<p>
		<em><bean:message key="message.infoAttendsSet.not.available" /></em>
	</p>
	
	<p class="mtop15 mbottom05">
		<b><bean:message key="label.attendsSetManagement"/></b>
	</p>

	<p class="mtop05">
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditAttendsSetMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.editAttendsSetMembers"/>
	    </html:link>
    </p>
 	
</logic:empty> 
	
	
	
<logic:notEmpty name="attends">
	<ul>
		<li>
			<html:link page="<%="/studentGroupManagement.do?method=viewShiftsAndGroups&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()%>">
			<bean:message key="link.backToShiftsAndGroups"/>
			</html:link>
		</li>

		<bean:define id="sendMailLink">/sendMailToWorkGroupStudents.do?method=sendGroupingEmail&amp;groupingCode=<bean:write name="groupingOID"/>&amp;executionCourseID=<bean:write name="executionCourseID"/></bean:define>
		<li>
			<html:link page="<%= sendMailLink %>">
				<bean:message key="link.sendEmailToAllStudents"/><br/><br/>
			</html:link>	
		</li>
	</ul>

	<p class="mbottom05">
		<b><bean:message key="label.attendsSetManagement"/></b>
	</p>
	<p class="mtop05">
		<html:link page="<%="/studentGroupManagement.do?method=prepareEditAttendsSetMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.editAttendsSetMembers"/>
	    </html:link> , 
		<html:link page="<%="/studentGroupManagement.do?method=deleteAttendsSetMembersByExecutionCourse&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.deleteAttendsSetMembersByExecutionCourse"/>
	    </html:link> , 
	    <html:link page="<%="/studentGroupManagement.do?method=deleteAllAttendsSetMembers&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
	    	<bean:message key="link.deleteAllAttendsSetMembers"/>
	    </html:link>
    </p>


 	<bean:size id="count" name="attends"/>
 	<p class="mtop15 mbottom05">
 		<em>
			<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
		</em>
	</p>
	
	<logic:present name="showPhotos">
		<html:link page="<%="/studentGroupManagement.do?method=viewAttendsSet&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/studentGroupManagement.do?method=viewAttendsSet&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID")+ "&amp;groupPropertiesCode=" + groupPropertiesCode.toString()+ "&amp;groupingOID=" + groupingOID.toString()%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>

<table class="tstyle4 mtop05">	
	<tr>
		<th>
			<bean:message key="label.numberWord" />
		</th>
		<logic:notPresent name="showPhotos">
			<th>
				<bean:message key="label.photo" />
			</th>
		</logic:notPresent>
		<th>
			<bean:message key="label.nameWord" />
		</th>
		<th>
			<bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoFrequentaWithAll" name="attends">
	
		<bean:define id="infoStudent" name="infoFrequentaWithAll" property="aluno"/>
		<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>
		<bean:define id="person" name="infoPerson" property="person"/>
		
		<tr>		
			<td class="acenter">
				<bean:write name="infoStudent" property="number"/>
			</td>
			<logic:notPresent name="showPhotos">
				<td class="acenter">
					<bean:define id="personID" name="person" property="externalId"/>
					<html:img src="<%= request.getContextPath() +"/person/retrievePersonalPhoto.do?method=retrieveByID&amp;personCode="+personID.toString()%>" altKey="personPhoto" bundle="IMAGE_RESOURCES" />
				</td>
			</logic:notPresent>
			<td>
				<bean:write name="infoPerson" property="nome"/>
			</td>		
			<td>
				<logic:present name="infoPerson" property="email">
					<bean:define id="mail" name="infoPerson" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>"><bean:write name="infoPerson" property="email"/></html:link>
				</logic:present>
				<logic:notPresent name="infoPerson" property="email">
					&nbsp;
				</logic:notPresent>
			</td>
		</tr>				
	</logic:iterate>
</table>



</logic:notEmpty>
