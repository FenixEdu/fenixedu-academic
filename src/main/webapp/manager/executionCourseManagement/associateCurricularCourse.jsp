<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2>

<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>

<bean:write name="executionPeriodName"/>
<logic:notEmpty name="executionDegreeName">
	&gt; <bean:write name="executionDegreeName"/>
</logic:notEmpty>
&gt; <bean:write name="executionCourseName"/>

<p><b><bean:message bundle="MANAGER_RESOURCES" key="link.manager.executionCourseManagement.associate"/></b></p>
<table>
	<tr>
<logic:present name="infoCurricularCourses">
<logic:notEmpty name="infoCurricularCourses">
	<html:form action="/editExecutionCourseManageCurricularCourses">
		<input alt="input.method" type="hidden" name="method" value="associateCurricularCourses"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear"/>				
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
		
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlan" property="degreeCurricularPlan" value="<%= pageContext.findAttribute("degreeCurricularPlan").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= pageContext.findAttribute("degreeCurricularPlanId").toString() %>"/>
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanName" property="degreeCurricularPlanName" value="<%= pageContext.findAttribute("degreeCurricularPlanName").toString() %>"/>
		<td colspan="3">
		<table>
			<logic:notEmpty name="infoCurricularCourses">
				<tr>	 			
					<td colspan="3">
						<bean:write name="degreeCurricularPlanName"/>
					</td>
 				</tr>
				<tr>
					<th class="listClasses-header">
						&nbsp;
					</th>
					<th class="listClasses-header">
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.curricularCourse" />
					</th>
					<th class="listClasses-header">
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.code" />
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
						<td class="listClasses" style="text-align:left">
							<bean:write name="curricularCourse" property="name"/>
						</td>
						<td class="listClasses">
							<bean:write name="curricularCourse" property="code"/>
						</td>
	 				</tr>
	 			</logic:iterate>						
			</logic:notEmpty>	 	
		</table>
		<br />
		</td>
	</tr>
	<tr>
		<td width="1px">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
			<bean:message bundle="MANAGER_RESOURCES" key="button.manager.executionCourseManagement.continue"/>
		</html:submit>
		</td>
	</html:form>
</logic:notEmpty>
<logic:empty name="infoCurricularCourses">
		<td colspan="2">
			<bean:message bundle="MANAGER_RESOURCES" key="message.manager.executionCourseManagement.noCurricularCourses.notApplicable.degree"
						  arg0="<%= pageContext.findAttribute("degreeCurricularPlanName").toString() %>" />
			<br/>
			<br/>
		</td>
	</tr>
	<tr>
</logic:empty>
</logic:present>
		<bean:define id="executionCoursesNotLinkedValue" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
		<td width="1px">
			<fr:form action="<%="/editExecutionCourseManageCurricularCourses.do?method=prepareAssociateCurricularCourseChooseDegreeCurricularPlan"%>">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= pageContext.findAttribute("executionCourseId").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" value="<%= pageContext.findAttribute("executionPeriod").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseName" property="executionCourseName" value="<%= pageContext.findAttribute("executionCourseName").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlan" property="degreeCurricularPlan" value="<%= pageContext.findAttribute("degreeCurricularPlan").toString() %>"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= pageContext.findAttribute("degreeCurricularPlanId").toString() %>"/>
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanName" property="degreeCurricularPlanName" value="<%= pageContext.findAttribute("degreeCurricularPlanName").toString() %>"/>
				<logic:notEqual name="executionCoursesNotLinkedValue" value="true">				
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear" value="<%= pageContext.findAttribute("curYear").toString() %>" />
				</logic:notEqual>

				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.back"/>
				</html:submit>
			</fr:form>
		</td>
		<td align="left">
			<fr:form action="<%="/editExecutionCourse.do?method=editExecutionCourse&executionCourseId=" + pageContext.findAttribute("executionCourseId").toString() %>">
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod" value="<%= pageContext.findAttribute("executionPeriod").toString() %>" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="0" />
				<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked" value="<%= pageContext.findAttribute("executionCoursesNotLinked").toString() %>" />
				<logic:notEqual name="executionCoursesNotLinkedValue" value="true">
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree" value="<%= pageContext.findAttribute("executionDegree").toString() %>" />
					<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear" value="<%= pageContext.findAttribute("curYear").toString() %>" />
				</logic:notEqual>

				<html:submit>
					<bean:message bundle="MANAGER_RESOURCES" key="label.cancel"/>
				</html:submit>
			</fr:form>
		</td>
	</tr>
</table>