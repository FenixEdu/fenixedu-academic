<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="message.editCurriculum.english" /></h2>

<table>
	<tr>
		<td>
			<html:link module="/manager" page="<%="/editCurriculum.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId").toString() + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId").toString() + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId").toString() %>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.edit.curriculum.portuguese"/></html:link>
		</td>
	</tr>
</table>

<html:form action="/editCurriculum" method="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit" />
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="language" value="<%= request.getParameter("language") %>"/>
	
	<table>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.generalObjectivesEn"/></b>
		</tr>
		<tr>
			<html:textarea property="generalObjectivesEn" rows="8" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.operacionalObjectivesEn"/></b>
		</tr>
		<tr>
			<html:textarea property="operacionalObjectivesEn" rows="8" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.programEn"/></b>
		</tr>
		<tr>
			<html:textarea property="programEn" rows="8" cols="60"/>
		</tr>
<%--		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.evaluationElementsEn"/></b>
		</tr>
		<tr>
			<html:textarea property="evaluationElementsEn" rows="3" cols="60"/>
		</tr>	--%>
	</table>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save.english"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear.english"/>
	</html:reset>			
</html:form>