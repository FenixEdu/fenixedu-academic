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
<logic:present name="executionPeriodNameAndYear" scope="request">
<table>
	<td>
		<h3><bean:message key="label.manager.executionPeriod"/></h3>
	</td>
	<td>
		<h2><bean:write name="executionPeriodNameAndYear"/></h2>
	</td>
</table>
</logic:present>
<%--
<logic:notPresent name="name" scope="request">
	<html:link page="<%= "/insertExecutionCourse.do?method=prepareInsert&executionPeriodId=" + request.getParameter("executionPeriodId") %>"><bean:message key="label.manager.insert.execution.course"/></html:link>
	<br>
	<br>
</logic:notPresent>--%>

<span class="error"><html:errors/></span>

<logic:notEmpty name="infoExecutionCoursesList" scope="request">
	<logic:present name="name" scope="request">
		<html:form action="/associateExecutionCourseToCurricularCourse" method="get">
			<html:hidden property="page" value="1"/>
			<html:hidden property="method" value="associate"/>
			<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
			<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
			<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
			<html:hidden property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>
	
			<b><bean:message key="list.title.execution.course.toAssociate"/><b>
			<br>
			</br>
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
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.site" />
					</td>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
					<bean:define id="infoExecutionCourse" name="infoExecutionCourse"/>		
					<tr>
						<td class="listClasses">
							<html:radio property="executionCourseId" idName="infoExecutionCourse" value="idInternal" />	
						</td>			
						<td class="listClasses" style="text-align:left"><bean:write name="infoExecutionCourse" property="nome"/>
						</td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
						</td>
						<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
						</td>
						<td class="listClasses">
						 <bean:define id="hasSite" name="infoExecutionCourse" property="hasSite"/>
					
						<logic:equal name="hasSite" value="true">
						<bean:message key="label.manager.yes"/>
						</logic:equal>
						<logic:notEqual name="hasSite" value="true">
						<bean:message key="label.manager.no"/>
						</logic:notEqual>
						</td>
					</tr>
				</logic:iterate>	
			</table>
			<html:submit >
				<bean:message key="label.manager.associate.execution.course"/>
			</html:submit>
		</html:form>
	</logic:present>
	
	<logic:notPresent name="name" scope="request">
		<b><bean:message key="list.title.execution.courses"/></b>
		<br>
		</br>
		<html:form action="/deleteExecutionCourses" method="get">
			<html:hidden property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>
			<bean:define id="onclick">
				return confirm('<bean:message key="message.confirm.delete.execution.courses"/>')
		    </bean:define>
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
					<td class="listClasses-header"><bean:message key="label.manager.executionCourse.site" />
					</td>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">		
				<tr>
					<td class="listClasses">
						<html:multibox property="internalIds">
							<bean:write name="infoExecutionCourse" property="idInternal"/>
						</html:multibox>
					</td>	
					<td class="listClasses" style="text-align:left"><bean:write name="infoExecutionCourse" property="nome"/>
					</td>
					<td class="listClasses"><bean:write name="infoExecutionCourse" property="sigla"/>
					</td>
					<td class="listClasses"><bean:write name="infoExecutionCourse" property="infoExecutionPeriod.name"/>
					</td>
				 	<td class="listClasses">
				 		<bean:define id="hasSite" name="infoExecutionCourse" property="hasSite"/>
						<logic:equal name="hasSite" value="true">
							<bean:message key="label.manager.yes"/>
						</logic:equal>
						<logic:notEqual name="hasSite" value="true">
							<bean:message key="label.manager.no"/>
						</logic:notEqual>
					</td>
				</tr>
				</logic:iterate>	
			</table>
			<html:submit onclick='<%=onclick.toString() %>'>
   				<bean:message key="label.delete"/>
  			</html:submit>
		</html:form>
	</logic:notPresent>
</logic:notEmpty>

<logic:empty name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message key="errors.execution.courses.none"/>
	</span>
</logic:empty>