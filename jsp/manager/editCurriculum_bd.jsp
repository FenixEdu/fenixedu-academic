<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
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
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit" />
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	
	<table>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.generalObjectives"/></b>
		</tr>
		<tr>
			<html:textarea property="generalObjectives" rows="8" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.operacionalObjectives"/></b>
		</tr>
		<tr>
			<html:textarea property="operacionalObjectives" rows="8" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.program"/></b>
		</tr>
		<tr>
			<html:textarea property="program" rows="8" cols="60"/>
		</tr>
<%--		<br>
		<tr>
			<b><bean:message bundle="MANAGER_RESOURCES" key="message.evaluationElements"/></b>
		</tr>
		<tr>
			<html:textarea property="evaluationElements" rows="8" cols="60"/>
		</tr>	--%>
	</table>
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
</html:form>
