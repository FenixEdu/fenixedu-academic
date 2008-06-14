<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>

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
	
 	<bean:size id="count" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList"/>
	<bean:message key="label.teacher.NumberOfStudents" /><%= count %>

	<table class="tstyle4">	
		<tr>
			<th width="10%"><bean:message key="label.studentGroupNumber" />
			</th>
			<th width="16%"><bean:message key="label.numberWord" />
			</th>
			<th width="53%"><bean:message key="label.nameWord" />
			</th>
			<th width="26%"><bean:message key="label.emailWord" />
			</th>
		</tr>
		<logic:iterate id="infoSiteStudentAndGroup" name="infoSiteStudentsAndGroups" property="infoSiteStudentsAndGroupsList">
			<bean:define id="infoSiteStudentInformation" name="infoSiteStudentAndGroup" property="infoSiteStudentInformation"/>
			<bean:define id="infoStudentGroup" name="infoSiteStudentAndGroup" property="infoStudentGroup"/>
			<tr>		
				<td><bean:write name="infoStudentGroup" property="groupNumber"/></td>
				<td><bean:write name="infoSiteStudentInformation" property="number"/></td>	
				<td><bean:write name="infoSiteStudentInformation" property="name"/></td>		
				<td><bean:write name="infoSiteStudentInformation" property="email"/></td>
			</tr>				
		</logic:iterate>
	</table>

</logic:notEmpty>

</logic:present>
