<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%><html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="message.editCurriculum.english" /></h2>

<table>
	<tr>
		<td>
			<html:link module="/manager" page="<%="/editCurriculum.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId").toString() + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId").toString() + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId").toString() %>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.curriculum.portuguese"/></html:link>
		</td>
	</tr>
</table>

<html:form action="/editCurriculum" >
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.language" property="language" value="<%= request.getParameter("language") %>"/>

	<table>
		<tr>
			<td>
			<html:select property="executionYearId">
				<html:options collection="executionYears" property="externalId" labelProperty="year"/>
			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.generalObjectivesEn"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectivesEn" property="generalObjectivesEn" rows="8" cols="60"/>
			</td>
		</tr>
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.operacionalObjectivesEn"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectivesEn" property="operacionalObjectivesEn" rows="8" cols="60"/>
			</td>
		</tr>
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.programEn"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.programEn" property="programEn" rows="8" cols="60"/>
			</td>
		</tr>
<%--		<br/>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.evaluationElementsEn"/></b>
		</tr>
		<tr>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.evaluationElementsEn" property="evaluationElementsEn" rows="3" cols="60"/>
		</tr>	--%>
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save.english"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear.english"/>
	</html:reset>			
</html:form>