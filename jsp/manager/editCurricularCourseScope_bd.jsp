<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>			
<h2><bean:message key="label.manager.edit.curricular.course.scope" /></h2>
<br />
<span class="error"><html:errors/></span>
<table>
<html:form action="/editCurricularCourseScope" method="get">
	<html:hidden property="page" value="2"/>
	<html:hidden property="method" value="edit"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	<html:hidden property="curricularCourseScopeId" value="<%= request.getParameter("curricularCourseScopeId") %>"/>
	<table>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.YearAndSemester"/>
			</td>
			<td>
				<html:select property="curricularSemesterId">
				<html:option key="option.curricular.course.scope.semester.1" value="1"/>
    			<html:option key="option.curricular.course.scope.semester.2" value="2"/>
    			<html:option key="option.curricular.course.scope.semester.3" value="3"/>
    			<html:option key="option.curricular.course.scope.semester.4" value="4"/>
    			<html:option key="option.curricular.course.scope.semester.5" value="5"/>
    			<html:option key="option.curricular.course.scope.semester.6" value="6"/>
    			<html:option key="option.curricular.course.scope.semester.7" value="7"/>
    			<html:option key="option.curricular.course.scope.semester.8" value="8"/>
    			<html:option key="option.curricular.course.scope.semester.9" value="9"/>
    			<html:option key="option.curricular.course.scope.semester.10" value="10"/>
    			</html:select>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.branchCode"/>
			</td>
			<td>
				<html:select property="branchId">
					<html:options collection="branchesList" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="label.manager.curricularCourseScope.beginDate"/>
			</td>
			<td>
				<html:select property="beginDate">
					<html:options collection="executionPeriodsLabels" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
		<td>
		  <bean:message key="message.manager.degree.curricular.plan.anotation"/>
		</td>
		<td>
			<html:text property="anotation" size="20" />
		</td>
	</table>
	<br />
	<br />
	<html:submit styleClass="inputbutton">
		<bean:message key="button.save"/>
	</html:submit>
	<html:reset  styleClass="inputbutton">
		<bean:message key="label.clear"/>
	</html:reset>				
</html:form>