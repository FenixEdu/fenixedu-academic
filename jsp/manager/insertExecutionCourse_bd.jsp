<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.executionCourse" /></h2>

<br/>

<span class="error"><!-- Error messages go here --><html:errors /></span>

<html:form action="/insertExecutionCourse" method="get" >  
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>	
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="5" property="name" />
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
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoreticalHours" size="5" property="theoreticalHours" />
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.praticalHours" size="5" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.theoPratHours" size="5" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.labHours" size="5" property="labHours" />
			</td>
		</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.seminaryHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.seminaryHours" size="5" property="seminaryHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.problemsHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.problemsHours" size="5" property="problemsHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.fieldWorkHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.fieldWorkHours" size="5" property="fieldWorkHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.trainingPeriodHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.trainingPeriodHours" size="5" property="trainingPeriodHours" />
				</td>
			</tr>
			<tr>
				<td>
					<bean:message bundle="MANAGER_RESOURCES" bundle="MANAGER_RESOURCES" key="message.manager.tutorialOrientationHours"/>
				</td>
				<td>
					<html:text bundle="HTMLALT_RESOURCES" altKey="text.tutorialOrientationHours" size="5" property="tutorialOrientationHours" />
				</td>
			</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
			</td>
			<td>
				<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.comment" property="comment"
            				   rows="3"
            				   cols="45"/>
			</td>
		</tr>
				
	</table>
	
	<br/>
	
	
	
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>