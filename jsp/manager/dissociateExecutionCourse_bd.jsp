<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/dissociateExecutionCourse" method="get">
	<html:hidden property="method" value="dissociate"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionCourseId" value="<%= request.getParameter("executionCourseId") %>"/>

	<h3><b><bean:message key="label.manager.dissociate.execution.course.title"/></b></h3>

	<br>

	<table border width="80%" cellpadding="0" cellspacing="5" align="center">
		<tr>
			<td>
				<h2><bean:message key="label.manager.executionCourse.name"/></h2>
			</td>
			<td>
				<h2><b><font color="#0066CC"><bean:write name="infoExecutionCourse" property="nome"/></font></b></h2>
			</td>
		</tr>
		<tr>
			<td>
				<h2><bean:message key="label.manager.executionCourse.code"/></h2>
			</td>
			<td>
				<h2><b><font color="#0066CC"><bean:write name="infoExecutionCourse" property="sigla"/></font></b></h2>
			</td>
		</tr>
	    <tr>
			<td>
				<h2><bean:message key="label.manager.executionCourse.executionPeriod"/></h2>
			</td>
			<td>
				<h2><b><font color="#0066CC"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/> - <bean:write name="infoExecutionCourse" property="infoExecutionPeriod.infoExecutionYear.year"/></font></b></h2>
			</td>
		</tr >
	</table>
	
	<br>
	
	<bean:message key="label.manager.dissociate.execution.course.certainty"/>
	
	<br>
	
	<html:submit styleClass="inputbutton">
		<bean:message key="label.manager.dissociate.execution.course"/>
	</html:submit>
	
	<html:reset  styleClass="inputbutton">
		<bean:message key="button.cancel"/>
	</html:reset>
</html:form>