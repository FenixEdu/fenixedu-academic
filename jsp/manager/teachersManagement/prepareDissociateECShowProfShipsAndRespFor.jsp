<%@page contentType="text/html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<%@ page import="DataBeans.InfoTeacher" %>
<h2><bean:message key="link.manager.teachersManagement.removeECAssociation" /></h2>
<span class="error"><html:errors /></span>
<html:form action="/teachersManagement">
	<html:hidden property="method" value="dissociateProfShipsAndRespFor"/>
	<html:hidden property="teacherNumber"/> 
	<html:hidden property="page" value="0"/>
	<p class="infoop">
		<bean:message key="message.manager.teachersManagement.choosePSorRF"/>
	</p>
	<logic:present name="infoTeacher">
		<b>
			<bean:message key="label.manager.teachersManagement.teacher"/>&nbsp;
			<bean:write name="infoTeacher" property="teacherNumber"/>&nbsp;-&nbsp;
			<bean:write name="infoTeacher" property="infoPerson.nome"/>
		</b>
		<br /><br />	
		<table>
			<bean:size id="professorshipsListSize" name="infoTeacher" property="professorShipsExecutionCourses"/>
			<html:hidden property="professorshipsListSize" value="<%=professorshipsListSize.toString()%>"/>
			<logic:greaterThan name="professorshipsListSize" value="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.teachersManagement.dissociate"/></td>
					<td class="listClasses-header"><bean:message key="label.manager.teachersManagement.professorShips"/></td>
				</tr>
				<logic:iterate id="professorship" name="infoTeacher" property="professorShipsExecutionCourses">			
					<tr>
						<td class="listClasses">
							<bean:define id="internalId" name="professorship" property="idInternal"/>
							<html:checkbox name="professorship" property="toDelete" indexed="true"/>
							<html:hidden name="professorship" property="idInternal" indexed="true" value="<%= internalId.toString() %>"/>
						</td>
						<td class="listClasses"><bean:write name="professorship" property="infoExecutionCourse.nome"/></td>
					</tr>
				</logic:iterate>
			</logic:greaterThan>
			<logic:equal name="professorshipsListSize" value="0">
				<tr>
					<td colspan="2">
						<i><bean:message key="message.manager.teachersManagement.noProfessorships"/></i>
					</td>
				</tr>
			</logic:equal>
		</table>
		<br /><br />
		<table>
			<bean:size id="responsibleForListSize" name="infoTeacher" property="responsibleForExecutionCourses"/>
			<html:hidden property="responsibleForListSize" value="<%=responsibleForListSize.toString()%>"/>			
			<logic:greaterThan name="responsibleForListSize" value="0">
				<tr>
					<td class="listClasses-header"><bean:message key="label.manager.teachersManagement.dissociate"/></td>
					<td class="listClasses-header"><bean:message key="label.manager.teachersManagement.responsibleFor"/></td>
				</tr>
				<logic:iterate id="responsibleFor" name="infoTeacher" property="responsibleForExecutionCourses">			
					<tr>
						<td class="listClasses">
							<bean:define id="internalId" name="responsibleFor" property="idInternal"/>
							<html:checkbox name="responsibleFor" property="toDelete" indexed="true"/>
							<html:hidden name="responsibleFor" property="idInternal" indexed="true" value="<%= internalId.toString() %>"/>
						</td>
						<td class="listClasses"><bean:write name="responsibleFor" property="infoExecutionCourse.nome"/></td>
					</tr>
				</logic:iterate>
			</logic:greaterThan>
			<logic:equal name="responsibleForListSize" value="0">
				<tr>
					<td colspan="2">
						<i><bean:message key="message.manager.teachersManagement.noResponsibleFor"/></i>
					</td>
				</tr>			
			</logic:equal>
		</table>
	</logic:present>
	<p>
		<html:submit styleClass="inputbutton"><bean:message key="button.manager.teachersManagement.dissociate"/>                    		         	
		</html:submit> 
	</p>
</html:form>