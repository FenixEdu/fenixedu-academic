<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="name" scope="request">
<table>
		<td>
			<h2><bean:message key="label.manager.curricularCourse.administrating"/></h2>
		</td>
		<td>
			<h2><b><bean:define id="curricularCourseName" value="<%= request.getParameter("name") %>"/>
					<bean:write name="curricularCourseName"/></b></h2>
		</td>
</table>
</logic:present>
<table>
		<td>
			<h3><bean:message key="label.manager.executionPeriod"/></h3>
		</td>
		<td>
			<h2><bean:write name="executionPeriodNameAndYear"/></h2>
		</td>
</table>


<logic:notPresent name="name" scope="request">
					<html:link page="<%= "/insertExecutionCourse.do?method=prepareInsert&executionPeriodId=" + request.getParameter("executionPeriodId") %>"><bean:message key="label.manager.insert.execution.course"/></html:link>
</logic:notPresent>

<span class="error"><html:errors/></span>
<logic:notEmpty name="infoExecutionCoursesList" scope="request">

<html:form action="/associateExecutionCourseToCurricularCourse" method="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="associate"/>
	
    <logic:present name="name" scope="request">
	<bean:message key="list.title.execution.course.toAssociate"/>
	</logic:present>
	<logic:notPresent name="name" scope="request">
	<bean:message key="list.title.execution.courses"/>
	</logic:notPresent>
	<br>
	</br>
	<table>
		<tr><logic:present name="name" scope="request">
			<td class="listClasses-header">	
			</td>
			</logic:present>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.name" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.code" />
			</td>
			<td class="listClasses-header"><bean:message key="label.manager.executionCourse.executionPeriod" />
			</td>
		</tr>
		<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
		<bean:define id="infoExecutionCourse" name="infoExecutionCourse"/>
							
			<tr>	<logic:present name="name" scope="request">
				<td class="listClasses">
				 
					<html:radio property="executionCourseId" idName="infoExecutionCourse" value="idInternal" />	
							
				</td>
				</logic:present>			
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="nome"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
				</td>
				<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
				</td>
			</tr>
		</logic:iterate>	
	</table>
	<logic:present name="name" scope="request">
	<html:submit >
	<bean:message key="label.manager.associate.execution.course"/>
	</html:submit>
	</logic:present>
</html:form>
</logic:notEmpty>

<logic:empty name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.courses.none"/>
	</span>
</logic:empty>