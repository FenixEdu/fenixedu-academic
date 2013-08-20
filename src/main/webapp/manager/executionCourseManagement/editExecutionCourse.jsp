<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<p class="mtop2"><h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.edit.executionCourse"/></h2></p>
<logic:messagesPresent message="true" property="success">
	<p>
		<span class="success0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="success">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>
<span class="error"><!-- Error messages go here --><html:errors /></span>
<logic:messagesPresent message="true" property="error">
	<p>
		<span class="error0">
			<html:messages id="messages" message="true" bundle="MANAGER_RESOURCES" property="error">
				<bean:write name="messages" />
			</html:messages>
		</span>
	</p>
</logic:messagesPresent>


<logic:present name="<%=PresentationConstants.EXECUTION_COURSE%>">
	<bean:define id="executionCourseName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="nome"/>
 	<bean:define id="executionCourseId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="externalId"/>
	<bean:define id="executionPeriodName" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.name"/>
	<bean:define id="executionPeriodId" name="<%=PresentationConstants.EXECUTION_COURSE%>" property="infoExecutionPeriod.externalId"/>
	
	<p><b><bean:write name="executionPeriodName"/>	
	<logic:present name="executionDegreeName">
		<logic:notEmpty name="executionDegreeName">
			&gt; <bean:write name="executionDegreeName"/>
		</logic:notEmpty>
	</logic:present>		
 	&gt; <bean:write name="executionCourseName"/></b></p>
 	<table>
		<html:form action="/editExecutionCourse" focus="name">
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="updateExecutionCourse"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCourseId" property="executionCourseId" value="<%= executionCourseId.toString() %>" />
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriod" property="executionPeriod"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionDegree" property="executionDegree"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curYear" property="curYear"/>				
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionCoursesNotLinked" property="executionCoursesNotLinked"/>
			<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="3"/>

		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="30" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.code" size="5" property="code" />
			</td>
		</tr>							
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment" rows="3" cols="45" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.entry.phase"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" property="entryPhase">
					<html:optionsCollection name="entryPhases"/>
				</html:select>
			</td>
		</tr>						
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.available.grade.submission"/>
			</td>
			<td>
				<html:checkbox property="availableGradeSubmission" value ="true"/>
			</td>
		</tr>
		<tr>
			<td>
				<br/>
				<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
					<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
				</html:submit>
				</html:form>
			</td>
			<td>
				<br/>
				<fr:form action="/editExecutionCourseChooseExPeriod.do?method=listExecutionCourseActions">
					<fr:edit id="sessionBeanJSP" name="sessionBean" visible="false"/>
					<html:submit>
						<bean:message bundle="MANAGER_RESOURCES" key="label.manager.executionCourseManagement.backToCourseList"/>
					</html:submit>
				</fr:form>
			</td>
		</tr>
	</table>
</logic:present>