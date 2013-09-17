<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/struts-example-1.0" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>


<logic:present name="infoSiteStudentsAndGroups">

	<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<div class="infoop2">
			<bean:message key="label.teacher.viewAllStudentsAndGroups.description" />
		</div>
	</logic:empty>	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<div class="infoop2">
			<bean:message key="label.teacher.viewAllStudentsAndGroups.description" />
		</div>
	</logic:notEmpty>		
	

	<p>
		<span class="error0"><!-- Error messages go here --><html:errors /></span>
	</p>


	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<ul class="mvert15">
			<li>
				<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
					<bean:message key=""/>
				</html:link>
			</li>
	    </ul>
		<p>
			<span class="error0"><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></span>
		</p>
	</logic:empty> 
	
	
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">

		<ul class="mvert15">
			<li>
				<html:link page="<%="/viewShiftsAndGroups.do?method=viewShiftsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
			    	<bean:message key="link.backToShiftsAndGroups"/>
		    	</html:link>
	    	</li>
	    </ul>
	
	<p class="mtop15 mbottom1">
 		<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
		<bean:message key="label.teacher.NumberOfStudents" /><%= count %>
	</p>
	
	<logic:present name="showPhotos">
		<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;showPhotos=true&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		    	<bean:message key="label.viewPhoto"/>
		</html:link>
	</logic:present>
	<logic:notPresent name="showPhotos">
		<html:link page="<%="/viewAllStudentsAndGroups.do?method=viewAllStudentsAndGroups&amp;objectCode=" + pageContext.findAttribute("objectCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
		    	<bean:message key="label.notViewPhoto"/>
		</html:link>
	</logic:notPresent>
	
	<table class="tstyle4 mtop05">
		<tr>
			<th>
				<bean:message key="label.numberWord" />
			</th>
			<th>
				<bean:message key="label.studentGroupNumber" />
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
			
		<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
			<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>	
			<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
			<tr>	
				<td class="acenter">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td class="acenter">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
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
					<bean:define id="mail" name="infoSiteStudentInformation" property="email"/>
					<html:link href="<%= "mailto:"+ mail %>">
						<bean:write name="infoSiteStudentInformation" property="email"/>
					</html:link>
				</td>
			</tr>				
	 	</logic:iterate>
	</table>

</logic:notEmpty>

</logic:present>
