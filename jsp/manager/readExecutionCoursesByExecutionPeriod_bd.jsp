<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<logic:present name="name" scope="request">
	<table>
		<td>
			<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourse.administrating"/></h2>
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
		<h3><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionPeriod"/></h3>
	</td>
	<td>
		<h2><bean:write name="executionPeriodNameAndYear"/></h2>
	</td>
</table>
</logic:present>
<%--
<logic:notPresent name="name" scope="request">
	<html:link module="/manager" page="<%= "/insertExecutionCourse.do?method=prepareInsert&executionPeriodId=" + request.getParameter("executionPeriodId") %>"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.execution.course"/></html:link>
	<br/>
	<br/>
</logic:notPresent>--%>

<span class="error"><html:errors/></span>

<logic:notEmpty name="infoExecutionCoursesList" scope="request">
	<logic:present name="name" scope="request">
		<html:form action="/associateExecutionCourseToCurricularCourse" method="get">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="associate"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>	
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>
	
			<b><bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.course.toAssociate"/><b>
			<br/>
			</br>
			<table>
				<tr>
					<th class="listClasses-header">	
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.name" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.code" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.executionPeriod" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.site" />
					</th>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">
					<bean:define id="infoExecutionCourse" name="infoExecutionCourse"/>		
					<tr>
						<td class="listClasses">
							<html:radio bundle="HTMLALT_RESOURCES" altKey="radio.executionCourseId" property="executionCourseId" idName="infoExecutionCourse" value="idInternal" />	
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
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.yes"/>
						</logic:equal>
						<logic:notEqual name="hasSite" value="true">
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.no"/>
						</logic:notEqual>
						</td>
					</tr>
				</logic:iterate>	
			</table>
			<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" >
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.associate.execution.course"/>
			</html:submit>
		</html:form>
	</logic:present>
	
	<logic:notPresent name="name" scope="request">
		<b><bean:message bundle="MANAGER_RESOURCES" key="list.title.execution.courses"/></b>
		<br/>
		</br>
		<html:form action="/deleteExecutionCourses" method="get">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>
			<bean:define id="onclick">
				return confirm('<bean:message bundle="MANAGER_RESOURCES" key="message.confirm.delete.execution.courses"/>')
		    </bean:define>
			<table>
				<tr>
					<th class="listClasses-header">	
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.name" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.code" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.executionPeriod" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourse.site" />
					</th>
				</tr>
				<logic:iterate id="infoExecutionCourse" name="infoExecutionCoursesList">		
				<tr>
					<td class="listClasses">
						<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.internalIds" property="internalIds">
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
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.yes"/>
						</logic:equal>
						<logic:notEqual name="hasSite" value="true">
							<bean:message bundle="MANAGER_RESOURCES" key="label.manager.no"/>
						</logic:notEqual>
					</td>
				</tr>
				</logic:iterate>	
			</table>
			<html:submit bundle="HTMLALT_RESOURCES" altKey='submit.submit' onclick='<%=onclick.toString() %>'>
   				<bean:message bundle="MANAGER_RESOURCES" key="label.delete"/>
  			</html:submit>
		</html:form>
	</logic:notPresent>
</logic:notEmpty>

<logic:empty name="infoExecutionCoursesList" scope="request">
	<span class="error">
		<html:errors /><bean:message bundle="MANAGER_RESOURCES" key="errors.execution.courses.none"/>
	</span>
</logic:empty>