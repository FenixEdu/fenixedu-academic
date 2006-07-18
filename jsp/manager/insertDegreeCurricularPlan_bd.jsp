<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/enum.tld" prefix="e" %>

<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.degreeCurricularPlan" /></h2>

<br>

<span class="error"><html:errors/></span>

<html:form action="/insertDegreeCurricularPlan" method ="get">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<table>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.name"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.name" size="60" property="name" />
			</td>
		</tr>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.state"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState" bundle="ENUMERATION_RESOURCES" excludedFields="PAST"/>  
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.state" property="state">
					<html:options collection="values" property="value" labelProperty="label" />
    			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.initialDate"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.initialDate" size="12" property="initialDate" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.endDate"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.endDate" size="12" property="endDate" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.degreeDuration"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.degreeDuration" size="7" property="degreeDuration" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.minimalYearForOptionalCourses"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.minimalYearForOptionalCourses" size="7" property="minimalYearForOptionalCourses" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.neededCredits"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.neededCredits" size="7" property="neededCredits" />
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.markType"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.markType" property="markType">
					<html:option bundle="MANAGER_RESOURCES" key="option.editDegreeCP.5" value="5"/>
    				<html:option bundle="MANAGER_RESOURCES" key="option.editDegreeCP.20" value="20"/>
    			</html:select>
			</td>
		</tr>	
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.gradeType"/>
			</td>
			<td>
				<e:labelValues id="values" enumeration="net.sourceforge.fenixedu.domain.GradeScale" bundle="ENUMERATION_RESOURCES"/>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.gradeType" property="gradeType">
					<html:option bundle="ENUMERATION_RESOURCES" key="dropDown.Default" value=""/>
					<html:options collection="values" property="value" labelProperty="label"/>
				</html:select>
			</td>
		</tr>				
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.numerusClausus"/>
			</td>
			<td>
				<html:text bundle="HTMLALT_RESOURCES" altKey="text.numerusClausus" size="7" property="numerusClausus" />
			</td>
		</tr>			
		
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.anotation"/>
			</td>
			<td>
			<html:textarea bundle="HTMLALT_RESOURCES" altKey="textarea.anotation" property="anotation"
            				   rows="10"
            				   cols="55"/>
			</td>
		</tr>	
		
	</table>

	<br>

	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>			
 </html:form>