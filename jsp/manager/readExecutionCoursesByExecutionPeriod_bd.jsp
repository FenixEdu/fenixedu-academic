<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<table>
	<tr>
		<td>
			<h3><bean:message key="label.manager.curricularCourse.administrating"/></h3>
		</td>
		<td>
			<h2><b><bean:define id="curricularCourseName" value="<%= request.getParameter("name") %>"/>
					<bean:write name="curricularCourseName"/></b></h2>
		</td>	
	</tr>
</table>

<logic:notEmpty name="infoExecutionCoursesList" scope="request">

<html:form action="/associateExecutionCoursesToCurricularCourse" method="get">
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>

	<bean:message key="list.title.execution.courses.toAssociate"/>
	
	<table>
		<tr>
			<td class="listClasses-header">	
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
			</td>
		</tr>
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
			<tr>	
				<td class="listClasses">
					<html:multibox property="executionCourseId">
						<bean:write name="infoExecutionCourse" property="idInternal"/>
					</html:multibox> 
				</td>			
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
				</td>
			</tr>
		</logic:iterate>	
	</table>
	<html:submit><bean:message key="label.manager.associate.execution.courses"/></html:submit>
</html:form>
</logic:notEmpty>

<logic:empty name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.courses.none"/>
	</span>
</logic:empty>