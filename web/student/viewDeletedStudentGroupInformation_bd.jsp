<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>


<bean:define id="attends" name="studentGroup" property="attends"/>
	<em><bean:message key="message.evaluationElements" /></em>
	<h2><bean:message key="title.deletedStudentGroup"/></h2>

<p>
		<html:link page="<%="/projectSubmissionsManagement.do?method=viewLastProjectSubmissionForEachGroup&amp;executionCourseID=" + pageContext.findAttribute("executionCourseID") + "&amp;projectID=" + pageContext.findAttribute("projectID") %>">
			<bean:message key="label.return"/>
		</html:link>
</p>

	<p class="mtop15 mbottom1">
	<b><bean:message key="label.GroupNumber"/></b> <bean:write name="studentGroup" property="groupNumber"/></p>
	</p>

<table class="tstyle4">
	<tbody>   
	<tr>
		<th><bean:message key="label.numberWord" />
		</th>
		<th><bean:message key="label.nameWord" />
		</th>
		<th><bean:message key="label.emailWord" />
		</th>
	</tr>

<logic:iterate id="attend" name="attends">			
	

<tr>
				<td><bean:write name="attend" property="registration.student.number"/>
				</td>
				<td><bean:write name="attend" property="registration.student.person.name"/>
				</td>		
				<td>
					<logic:present name="attend" property="registration.student.person.email">
						<bean:define id="mail" name="attend" property="registration.student.person.email"/>
						<html:link href="<%= "mailto:"+ mail %>"><bean:write name="attend" property="registration.student.person.email"/></html:link>
					</logic:present>
					<logic:notPresent name="attend" property="registration.student.person.email">
						&nbsp;
					</logic:notPresent>
				</td>
			</tr>
</logic:iterate>
	</tbody>
</table>