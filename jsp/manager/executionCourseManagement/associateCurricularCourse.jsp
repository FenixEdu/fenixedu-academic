<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<h2><bean:message key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>
<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	> <bean:write name="executionDegreeName"/>
</logic:notEmpty>
	> <bean:write name="executionCourseName"/>
<p><b><bean:message key="link.manager.executionCourseManagement.associate"/></b></p>
<logic:present name="infoCurricularCourses">
	<html:form action="/editExecutionCourseManageCurricularCourses">
		<input type="hidden" name="method" value="associateCurricularCourses"/>
		<html:hidden property="executionPeriodName" value="<%= pageContext.findAttribute("executionPeriodName").toString() %>" />
		<html:hidden property="executionPeriodId" value="<%= pageContext.findAttribute("executionPeriodId").toString() %>" />
		<html:hidden property="executionDegreeName" value="<%= pageContext.findAttribute("executionDegreeName").toString() %>" />
		<html:hidden property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
	
		<table>
			<logic:notEmpty name="infoCurricularCourses">
				<tr>	 			
					<td colspan="3"><bean:write name="executionDegreeNameForCurricularCourse"/>
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