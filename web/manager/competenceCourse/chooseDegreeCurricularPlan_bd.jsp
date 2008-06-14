<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.create.competence.course" /></h2>

<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/curricularCoursesCompetenceCourse">  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="readCurricularCourses"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.competenceCourseID" property="competenceCourseID"/>
	<table>
		<tr>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.degreeCurricularPlanID" property="degreeCurricularPlanID">
					<html:options collection="degreeCurricularPlans" property="idInternal" labelProperty="name"/>
				</html:select>
			</td>
		</tr>
	</table>
	<br/>
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.choose"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>	