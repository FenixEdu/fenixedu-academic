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

<logic:present name="infoExecutionCoursesList" scope="request">

<bean:define id="infoCurricularCourse" name="infoCurricularCourse" property="name"/>
<html:form action="/deleteExecutionDegrees">

	
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="infoCurricularCourse" value="name"/>
</html:form>

<%-- a definir no aplication resourses apenas esta msg
falta uma multibox para escolher para poder associar--%>
	<bean:message key="list.title.execution.courses"/>
	<br />
	<table>
		<tr>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
			</td>
		</tr>
<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
					<tr>	 			
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>
						</td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
						</td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
						</td>
					</tr>
				</logic:iterate>	
		
	</table>
	
</logic:present>

<logic:notPresent name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.period.none"/>
	</span>
</logic:notPresent>
</logic:present>