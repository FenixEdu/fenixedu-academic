<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.editCurriculum.english" /></h2>

<html:form action="/editCurriculum" method="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit" />
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="language" value="<%= request.getParameter("language") %>"/>
	
	<table>
		<tr>
			<b><bean:message key="message.generalObjectivesEn"/></b>
		</tr>
		<tr>
			<html:textarea property="generalObjectivesEn" rows="3" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message key="message.operacionalObjectivesEn"/></b>
		</tr>
		<tr>
			<html:textarea property="operacionalObjectivesEn" rows="3" cols="60"/>
		</tr>
		<br>
		<tr>
			<b><bean:message key="message.programEn"/></b>
		</tr>
		<tr>
			<html:textarea property="programEn" rows="3" cols="60"/>
		</tr>
<%--		<br>
		<tr>
			<b><bean:message key="message.evaluationElementsEn"/></b>
		</tr>
		<tr>
			<html:textarea property="evaluationElementsEn" rows="3" cols="60"/>
		</tr>	--%>
	</table>
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save.english"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear.english"/>
	</html:reset>			
</html:form>