<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<span class="error"><html:errors/></span>

<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
> <bean:write name="executionCourseName"/>

<p><b><bean:message key="link.manager.executionCourseManagement.associate"/></b></p>

<logic:present name="infoCurricularCourses">
	<html:form action="/editExecutionCourseManageCurricularCourses">
		<input type="hidden" name="method" value="associateCurricularCourses"/>
		<html:hidden property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
		<html:hidden property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
		<html:hidden property="executionPeriod"/>
		<html:hidden property="executionDegree"/>
		<html:hidden property="curYear"/>				
		<html:hidden property="executionCoursesNotLinked"/>
	
		<table>
			<logic:notEmpty name="infoCurricularCourses">
				<tr>	 			
					<td colspan="3"><bean:write name="degreeCurricularPlanName"/>
					</td>
 				</tr>
				<tr>
					<td class="listClasses-header">&nbsp;
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.curricularCourse" />
					</td>
					<td class="listClasses-header"><bean:message key="label.manager.executionCourseManagement.code" />
					</td>
				</tr>
				<bean:size id="curricularCoursesListSize" name="infoCurricularCourses"/>
				<html:hidden property="curricularCoursesListSize" value="<%=curricularCoursesListSize.toString()%>"/>			
				<logic:iterate id="curricularCourse" name="infoCurricularCourses" type="DataBeans.InfoCurricularCourse">
					<tr>	 			
						<td class="listClasses">
							<bean:define id="internalId" name="curricularCourse" property="idInternal"/>
							<html:checkbox name="curricularCourse" property="chosen" indexed="true"/>
							<html:hidden name="curricularCourse" property="idInternal" indexed="true" value="<%= internalId.toString() %>"/>
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
		<html:submit styleClass="inputbutton">
			<bean:message key="button.manager.executionCourseManagement.continue"/>
		</html:submit>
	</html:form>
</logic:present>