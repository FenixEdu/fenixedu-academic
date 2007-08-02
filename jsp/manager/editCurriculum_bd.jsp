<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>


<h2><bean:message bundle="MANAGER_RESOURCES" key="message.editCurriculum" /></h2>

<table>
	<tr>
		<td>
			<html:link module="/manager" page="<%="/editCurriculum.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId").toString() + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId").toString() + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId").toString() + "&amp;language=English"%>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.curriculum.english"/></html:link>
		</td>
	</tr>
</table>

<html:form action="/editCurriculum" method="get">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="edit" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
		
	<table>
		<tr>
			<td>
			<html:select property="executionYearId">
				<html:options collection="executionYears" property="idInternal" labelProperty="year"/>
			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.generalObjectives"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.generalObjectives" property="generalObjectives" rows="8" cols="60"/>
			</td>
		</tr>
		<br/>
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.operacionalObjectives"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.operacionalObjectives" property="operacionalObjectives" rows="8" cols="60"/>
			</td>
		</tr>
		<br/>
		<tr>
			<td>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.program"/></b>
			</td>
		</tr>
		<tr>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.program" property="program" rows="8" cols="60"/>
			</td>
		</tr>
<%--		<br/>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.evaluationElements"/></b>
		</tr>
		<tr>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.evaluationElements" property="evaluationElements" rows="8" cols="60"/>
		</tr>	--%>
	</table>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>
