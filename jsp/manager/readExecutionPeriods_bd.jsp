<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<logic:present name="name" scope="request">
	<table>
		<tr>
			<td>
				<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.administrating"/></h3>
			</td>
			<td>
				<h2><b><bean:define id="curricularCourseName" value="<%= request.getParameter("name") %>"/>
					<bean:write name="curricularCourseName" /></b></h2>
			</td>	
		</tr>
	</table>
</logic:present>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<logic:present name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">

	<b><bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.periods"/></b>
	<br/>
	</br>
	<table>
		<tr>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.semester" />
			</th>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.year" />
			</th>
			<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.execution.period.state" />
			</th>
			
		</tr>
		<logic:present name="name" scope="request">
			<logic:iterate id="infoExecutionPeriod" name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
				<tr>
					<td class="listClasses">
						<html:link module="/manager" page="<%= "/associateExecutionCourseToCurricularCourse.do?method=prepare&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;name=" + request.getParameter("name") %>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="name" /></html:link>
					</td>
					<td class="listClasses">
						<html:link module="/manager" page="<%= "/associateExecutionCourseToCurricularCourse.do?method=prepare&degreeId=" + request.getParameter("degreeId") + "&degreeCurricularPlanId=" + request.getParameter("degreeCurricularPlanId") + "&curricularCourseId=" + request.getParameter("curricularCourseId") + "&amp;name=" + request.getParameter("name") %>" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /></html:link>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionPeriod" property="state" />
					</td>
				</tr>
			</logic:iterate>
		</logic:present>
		<logic:notPresent name="name" scope="request">
			<logic:iterate id="infoExecutionPeriod" name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>">
				<tr>
					<td class="listClasses">
						<html:link module="/manager" page="/readExecutionCourses.do" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="name" /></html:link>
					</td>
					<td class="listClasses">
						<html:link module="/manager" page="/readExecutionCourses.do" paramId="executionPeriodId" paramName="infoExecutionPeriod" paramProperty="idInternal"><bean:write name="infoExecutionPeriod" property="infoExecutionYear.year" /></html:link>
					</td>
					<td class="listClasses">
						<bean:write name="infoExecutionPeriod" property="state" />
					</td>
				</tr>
			</logic:iterate>
		</logic:notPresent>
	</table>
</logic:present>

<logic:notPresent name="<%= SessionConstants.LIST_EXECUTION_PERIODS %>" scope="request">
	<span class="error">
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" key="errors.execution.period.none"/>
	</span>
</logic:notPresent>