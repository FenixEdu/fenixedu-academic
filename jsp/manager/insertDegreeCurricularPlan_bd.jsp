<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.manager.insert.degreeCurricularPlan" /></h2>
<br>

<table>
<html:form action="/insertDegreeCurricularPlan" method ="get">
	    
	<html:hidden property="page" value="1"/>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.name"/>
	</td>
	<td>
		<html:text size="60" property="name" />
	</td>
	<td>
		<span class="error"><html:errors property="name"  /></span>
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
	<td>
		<span class="error"><html:errors property="state" /></span>
	</td>
</tr>
			
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.initialDate"/>
	</td>
	<td>
		<html:text size="60" property="initialDate" />
	</td>
	<td>
		<span class="error"><html:errors property="initialDate" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.endDate"/>
	</td>
	<td>
		<html:text size="60" property="endDate" />
	</td>
	<td>
		<span class="error"><html:errors property="endDate" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.degreeDuration"/>
	</td>
	<td>
		<html:text size="60" property="degreeDuration" />
	</td>
	<td>
		<span class="error"><html:errors property="degreeDuration" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.minimalYearForOptionalCourses"/>
	</td>
	<td>
		<html:text size="60" property="minimalYearForOptionalCourses" />
	</td>
	<td>
		<span class="error"><html:errors property="minimalYearForOptionalCourses" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.neededCredits"/>
	</td>
	<td>
		<html:text size="60" property="neededCredits" />
	</td>
	<td>
		<span class="error"><html:errors property="neededCredits" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.markType"/>
	</td>
	<td>
		<html:select property="markType">
		<html:option value=""/>	
		<html:option key="option.editDegreeCP.5" value="5"/>
    	<html:option key="option.editDegreeCP.20" value="20"/>
    	</html:select>
	</td>
	<td>
		<span class="error"><html:errors property="markType" /></span>
	</td>
</tr>
	
<tr>
	<td>
		<bean:message key="message.manager.degree.curricular.plan.numerusClausus"/>
	</td>
	<td>
		<html:text size="60" property="numerusClausus" />
	</td>
	<td>
		<span class="error"><html:errors property="numerusClausus" /></span>
	</td>
</tr>	
			
</table>

<html:hidden property="method" value="insert"/>
<html:hidden property="degreeId"/>

<br>
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>			

 </html:form>
