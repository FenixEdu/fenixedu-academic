<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.create.competence.course" /></h2>

<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/createEditCompetenceCourse">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="createCompetenceCourse"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.competenceCourseID" property="competenceCourseID" value=""/>	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="12" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.department"/>
			</td>
			<td>
				<logic:iterate id="department" name="departments">
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.departmentIDs" property="departmentIDs" >
						<bean:write name="department" property="idInternal"/>
					</html:multibox>
					<bean:write name="department" property="realName"/><br/>
				</logic:iterate>
			</td>
		</tr>
	</table>
	
	<br/>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>