<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message key="label.manager.create.competence.course" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/curricularCoursesCompetenceCourse">  
	<html:hidden property="method" value="readCurricularCourses"/>
	<html:hidden property="competenceCourseID"/>
	<table>
		<tr>
			<td>
				<html:select property="degreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="button.choose"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>
</html:form>	