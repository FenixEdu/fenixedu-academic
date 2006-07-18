<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

<%@ page import="java.util.TreeMap" %>
<%@ page import="java.util.Map" %>

<logic:present name="infoSiteStudentsAndGroups">


<span class="error"><html:errors/></span>
<br/>

	<logic:empty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		<h2><span class="infoop4"><bean:message key="message.infoSiteStudentsAndGroupsList.not.available" /></span></h2>
		<ul>
			<li><html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
	   		</li>
		</ul>
	</logic:empty>
	
	<logic:notEmpty name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
		
		<h2><bean:message key="title.viewAllStudentsAndGroups"/></h2>
		
		<ul>
			<li><html:link page="<%="/viewShiftsAndGroups.do?method=execute&amp;executionCourseCode=" + request.getParameter("executionCourseCode")+ "&amp;groupPropertiesCode=" + request.getParameter("groupPropertiesCode")%>">
	   		<bean:message key="link.backToShiftsAndGroups"/></html:link> - <bean:message key="link.backToShiftsAndGroups.description"/>
	   		</li>
		</ul>
	
	<p><strong>Agrupamento:</strong> <span class="infoop4"><bean:write name="infoSiteStudentsAndGroups" property="infoGrouping.name"/></span></p>

 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.student.NumberOfStudents" /><%= count %>
	<br/>	
	<br/>
	
<table class="style1" width="75%" cellpadding="0" border="0">
	<tbody>

	<tr>
		<th class="listClasses-header" width="10%"><bean:message key="label.studentGroupNumber" />
		</th>
		<th class="listClasses-header" width="16%"><bean:message key="label.numberWord" />
		</th>
		<th class="listClasses-header" width="53%"><bean:message key="label.nameWord" />
		</th>
		<th class="listClasses-header" width="26%"><bean:message key="label.emailWord" />
		</th>
	</tr>
			
	<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
	
		<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
		<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
		<bean:define id="username" name="UserView" property="utilizador" type="java.lang.String"/>
		<logic:equal name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr class="highlight">
				<td class="listClasses">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:equal>
		<logic:notEqual name="infoSiteStudentInformation" property="username" value="<%= username %>">
			<tr>
				<td class="listClasses">
					<bean:write name="infoStudentGroup" property="groupNumber"/>
				</td>
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="number"/>
				</td>	
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="name"/>
				</td>		
			
				<td class="listClasses">
					<bean:write name="infoSiteStudentInformation" property="email"/>
				</td>
			</tr>
		</logic:notEqual>
	 </logic:iterate>

</tbody>
</table>

<br/>


  </logic:notEmpty>

	 
</logic:present>
