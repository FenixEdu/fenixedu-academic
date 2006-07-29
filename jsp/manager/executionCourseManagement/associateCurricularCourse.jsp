<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
> <bean:write name="executionCourseName"/>

<p><b><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="link.manager.executionCourseManagement.associate"/></b></p>

<logic:present name="infoCurricularCourses">
	<html:form action="/editExecutionCourseManageCurricularCourses">
		<input alt="input.method" type="hidden" name="method" value="associateCurricularCourses"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear"/>				
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
	
		<table>
			<logic:notEmpty name="infoCurricularCourses">
				<tr>	 			
					<td colspan="3"><bean:write name="degreeCurricularPlanName"/>
					</td>
 				</tr>
				<tr>
					<th class="listClasses-header">&nbsp;
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
					</th>
					<th class="listClasses-header"><bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
					</th>
				</tr>
				<bean:size id="curricularCoursesListSize" name="infoCurricularCourses"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCoursesListSize" property="curricularCoursesListSize" value="<%=curricularCoursesListSize.toString()%>"/>			
				<logic:iterate id="curricularCourse" name="infoCurricularCourses" type="net.sourceforge.fenixedu.dataTransferObject.InfoCurricularCourse">
					<tr>	 			
						<td class="listClasses">
							<bean:define id="internalId" name="curricularCourse" property="idInternal"/>
							<html:checkbox bundle="HTMLALT_RESOURCES" altKey="checkbox.chosen" name="curricularCourse" property="chosen" indexed="true"/>
							<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.idInternal" name="curricularCourse" property="idInternal" indexed="true" value="<%= internalId.toString() %>"/>
						</td>
						<td class="listClasses" style="text-align:left"><bean:write name="curricularCourse" property="name"/>
						</td>
						<td class="listClasses"><bean:write name="curricularCourse" property="code"/>
						</td>
	 				</tr>
	 			</logic:iterate>						
			</logic:notEmpty>	 	
		</table>
		<br />
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
		</html:submit>
	</html:form>
</logic:present>