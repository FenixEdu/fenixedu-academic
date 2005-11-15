<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.executionCourse" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/insertExecutionCourse" method="get" >  
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insert"/>
	<html:hidden property="executionPeriodId" value="<%= request.getParameter("executionPeriodId") %>"/>	
	
	<table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.name"/>
			</td>
			<td>
				<html:text size="5" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.code"/>
			</td>
			<td>
				<html:text size="5" property="code" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoreticalHours"/>
			</td>
			<td>
				<html:text size="5" property="theoreticalHours" />
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.praticalHours"/>
			</td>
			<td>
				<html:text size="5" property="praticalHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.theoPratHours"/>
			</td>
			<td>
				<html:text size="5" property="theoPratHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.labHours"/>
			</td>
			<td>
				<html:text size="5" property="labHours" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.execution.course.comment"/>
			</td>
			<td>
				<html:textarea property="comment"
            				   rows="3"
            				   cols="45"/>
			</td>
		</tr>
				
	</table>
	
	<br>
	
	
	
	<html:submit styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>