<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:equal name="loggedIsResponsible" value="true">
	<h3 class="mbottom0"><bean:message key="label.teacher"/></h3>											
	<table class="tstyle5">	
		<logic:notEmpty name="summariesManagementBean" property="professorship">
			<bean:define id="professorship" name="summariesManagementBean" property="professorship" />
		 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorship.idInternal" />
			<tr>
		 		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>"/></td>				
		 		<td><bean:write name="professorship" property="teacher.person.name"/></td>
	 		</tr>
		</logic:notEmpty>
		<logic:empty name="summariesManagementBean" property="professorship">
			<bean:define id="professorship" name="summariesManagementBean" property="professorshipLogged" />
		 	<bean:define id="professorshipId" name="summariesManagementBean" property="professorshipLogged.idInternal" />
			<tr>
		 		<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="<%= professorshipId.toString()%>"/></td>				
		 		<td><bean:write name="professorship" property="teacher.person.name"/></td>
	 		</tr>
		</logic:empty>											
		<tr>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="0" /></td>
			<td>
				<bean:message key="label.teacher.in" />
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherNumber" name="summariesManagementForm" property="teacherNumber" size="4" />
				(<bean:message key="label.number" />)
			</td>
		</tr>
		<tr>
			<td><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.teacher" name="summariesManagementForm" property="teacher" value="-1" /></td>
			<td>
				<bean:message key="label.teacher.out" />
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.teacherName" name="summariesManagementForm" property="teacherName" size="40"/>
				(<bean:message key="label.name" />)
			</td>
		</tr>				
	</table>			
</logic:equal>