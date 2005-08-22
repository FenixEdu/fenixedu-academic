<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="label.manager.create.competence.course" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/competenceCourseManagement">  
	<html:hidden property="method" value="createCompetenceCourse"/>
	<html:hidden property="competenceCourseID" value="null"/>	
	<table>
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.curricularCourse.code"/>
			</td>
			<td>
				<html:text size="12" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.department"/>
			</td>
			<td>
				<html:select property="departmentID">
					<html:option value="null">&nbsp;</html:option>
					<html:options collection="departments" property="idInternal" labelProperty="name"/>
    			</html:select>
			</td>
		</tr>
	</table>
	
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>