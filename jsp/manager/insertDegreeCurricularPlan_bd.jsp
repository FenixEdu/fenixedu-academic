<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.manager.insert.degreeCurricularPlan" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/insertDegreeCurricularPlan" method ="get">
	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insert"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<table>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.state"/>
			</td>
			<td>
				<html:select property="state">
					<html:option key="option.editDegreeCP.active" value="1"/>
    				<html:option key="option.editDegreeCP.notActive" value="2"/>
    				<html:option key="option.editDegreeCP.concluded" value="3"/>
    			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.initialDate"/>
			</td>
			<td>
				<html:text size="12" property="initialDate" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.endDate"/>
			</td>
			<td>
				<html:text size="12" property="endDate" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.degreeDuration"/>
			</td>
			<td>
				<html:text size="7" property="degreeDuration" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.minimalYearForOptionalCourses"/>
			</td>
			<td>
				<html:text size="7" property="minimalYearForOptionalCourses" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.neededCredits"/>
			</td>
			<td>
				<html:text size="7" property="neededCredits" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.markType"/>
			</td>
			<td>
				<html:select property="markType">
					<html:option key="option.editDegreeCP.5" value="5"/>
    				<html:option key="option.editDegreeCP.20" value="20"/>
    			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.numerusClausus"/>
			</td>
			<td>
				<html:text size="7" property="numerusClausus" />
			</td>
		</tr>			
		
		<tr>
			<td>
				<bean:message key="message.manager.degree.curricular.plan.anotation"/>
			</td>
			<td>
			<html:textarea property="anotation"
            				   rows="10"
            				   cols="55"/>
			</td>
		</tr>	
		
	</table>

	<br>

	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>			
 </html:form>