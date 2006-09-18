<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<h2><bean:message key="title.enrolmentGroup.insertNewGroup"/></h2>

	<div class="infoop">
		<logic:equal name="infoGrouping" property="enrolmentPolicy.type" value="1">
			<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title1" />:</strong><br/>
			<bean:message key="label.student.viewGroupEnrolment.description.1" /></p>
		</logic:equal>
		<logic:equal name="infoGrouping" property="enrolmentPolicy.type" value="2">
			<p><strong><bean:message key="label.student.viewGroupEnrolment.description.title2" />:</strong><br/>
			<bean:message key="label.student.viewGroupEnrolment.description.2" /></p>
		</logic:equal>
	</div>

	<p><strong>Agrupamento:</strong> <bean:write name="infoGrouping" property="name"/><p/>

<%--
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" length="1">
		<bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
	<logic:iterate id="infoExportGrouping" name="infoExportGroupings" offset="1">
		, <bean:write name="infoExportGrouping" property="infoExecutionCourse.nome"/>
	</logic:iterate>
--%>


	<html:form action="/groupEnrolment" method="get" style="margin: 0; padding: 0;">

	<h2><span class="error"><!-- Error messages go here --><html:errors /></span></h2>		 

	<bean:define id="groupNumber" name="groupNumber"/>

		<p><b class="infoop3"><bean:message key="label.GroupNumber"/><bean:write name="groupNumber"/></b></p>
		<p><bean:message key="label.infoStudents.studentsWithoutGroup" /></p>

<logic:present name="infoUserStudent"> 

	<table class="style1" width="80%" cellpadding="0" border="0">	
		<tr>
		
		<th width="5%" class="listClasses-header">
		</th>
		<th width="10%" class="listClasses-header"><bean:message key="label.numberWord" />
		</th>
		<th width="35%" class="listClasses-header"><bean:message key="label.nameWord" />
		</th>
		<th width="20%" class="listClasses-header"><bean:message key="label.emailWord" />
		</th>
		</tr>
			<tr>	
			
				<td class="listClasses">
				<input alt="input.studentsAutomaticallyEnroled" type="checkbox" name="studentsAutomaticallyEnroled" checked disabled>
				</td>	
				
				<td class="listClasses"><bean:write name="infoUserStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoUserStudent" property="infoPerson"/>		
				<td class="listClasses"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
		</table>
	</logic:present>
	
<logic:present name="infoStudents"> 
	<logic:empty name="infoStudents">
	<h2>
	<bean:message key="message.infoStudents.not.available" />
	</h2>
	</logic:empty>
	
	<logic:notEmpty name="infoStudents">
	
	<br/>
	<table class="style1" width="80%" cellpadding="0" border="0">	
		
	
		<logic:iterate id="infoStudent" name="infoStudents">			
			<tr>	
				<td width="5%" class="listClasses">
				<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.studentsNotEnroled" property="studentsNotEnroled">
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson" />
				<bean:write name="infoPerson" property="username"/>
				</html:multibox>
				</td>	
				<td width="10%" class="listClasses"><bean:write name="infoStudent" property="number"/>
				</td>	
				<bean:define id="infoPerson" name="infoStudent" property="infoPerson"/>		
				<td width="35%" class="listClasses"><bean:write name="infoPerson" property="nome"/>
				</td>
				<td width="20%" class="listClasses"><bean:write name="infoPerson" property="email"/>
				</td>
	 		</tr>	
	 	</logic:iterate>
	 

		</table>
		<br/>
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
		<html:form action="/viewShiftsAndGroups" method="get" style="margin: 0; padding: 0;">
		<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" styleClass="inputbutton"><bean:message key="button.cancel"/></html:cancel>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="execute"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseCode"  property="executionCourseCode" value="<%= request.getParameter("executionCourseCode")%>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.groupPropertiesCode"  property="groupPropertiesCode" value="<%= request.getParameter("groupPropertiesCode")%>"/>		
		</html:form>
	</td>
</tr>
</table>
