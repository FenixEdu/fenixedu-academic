<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<logic:present name="infoCurricularCourse">
	<table>
		<tr>
			<td>
				<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b>	<bean:write name="infoCurricularCourse" property="name"/></b></h2>
			</td>	
		</tr>
	</table>

<br />

<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">

<bean:define id="infoCurricularCourse" name="infoCurricularCourse" property="name"/>
<html:form action="/deleteExecutionDegrees">

	
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="infoCurricularCourse" value="name"/>
</html:form>

	<bean:message key="list.title.execution.periods"/>
	<br />
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.manager.execution.period.semester" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.execution.period.year" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.execution.period.state" />
			</td>
			
		</tr>
		<logic:iterate id="infoExecutionPeriod" name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
			<tr>
				<td class="listClasses">
				<html:link page="<%= "/readExecutionCoursesToAssociateCurricularCourseAction.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="name" /></html:link>
				</td>
				<td class="listClasses">
				<html:link page="<%= "/readExecutionCoursesToAssociateCurricularCourseAction.do?degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId")%>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /></html:link>
				</td>
				<td class="listClasses">
					<bean:write name="infoExecutionPeriod" property="state" />
				</td>
			</tr>
		</logic:iterate>
	</table>
	
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.period.none"/>
	</span>
</logic:notPresent>
</logic:present>