<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="message.editCurriculum" /></h2>

<html:link page="<%="/editCurriculum.do?method=prepareEdit&amp;degreeId=" + request.getParameter("degreeId") + "&amp;degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&amp;curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;language=English"%>"><bean:message key="label.manager.edit.curriculum.english"/></html:link>

<html:form action="/editCurriculum" method="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="edit" />
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	
	<table>
		<tr>
			<bean:message key="message.generalObjectives"/>
		</tr>
		<tr>
			<html:text size="60" property="generalObjectives" />
		</tr>
		<tr>
			<bean:message key="message.operacionalObjectives"/>
		</tr>
		<tr>
			<html:text size="60" property="operacionalObjectives"  />
		</tr>
		<tr>
			<bean:message key="message.program"/>
		</tr>
		<tr>
			<html:text size="60" property="program"  />
		</tr>
		<tr>
			<bean:message key="message.evaluationElements"/>
		</tr>
		<tr>
			<html:text size="60" property="evaluationElements"  />
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