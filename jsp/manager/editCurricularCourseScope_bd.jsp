<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="infoCurricularCourseScope" name="infoCurricularCourseScope" property="infoBranch"/>
<bean:define id="infoCurricularSemester" name="infoCurricularCourseScope" property="infoCurricularSemester"/>
<bean:define id="infoCurricularYear" name="infoCurricularSemester" property="infoCurricularYear"/>
				

				
<h2><bean:message key="label.manager.edit.curricularCourse" /></h2>
<br/>
<table>
<html:form action="/editCurricularCourseScope" method="get">
	<html:hidden property="page" value="1"/>

	<html:hidden property="method" value="edit"/>
	<html:hidden property="degreeId"/>
	<html:hidden property="degreeCurricularPlanId"/>
	<html:hidden property="curricularCourseId"/>
	<html:hidden property="curricularCourseScopeId"/>
	
	
	<table>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.curricularYear"/>
			</td>
			<td><%--ALTERAR APROPERTY YE VER S POSSO TIRAR AS OPTIONS        --%>
				<html:select name="infoCurricularYear" property="year">
				<html:option key="option.curricular.course.scope.year1" value="1"/>
    			<html:option key="option.curricular.course.scope.year2" value="2"/>
    			<html:option key="option.curricular.course.scope.year3" value="3"/>
    			<html:option key="option.curricular.course.scope.year4" value="4"/>
    			<html:option key="option.curricular.course.scope.year5" value="5"/>
    			</html:select>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.curricularSemester"/>
			</td>
			<td><%--ALTERAR APROPERTY YE VER S POSSO TIRAR AS OPTIONS        --%>
				<html:select name="infoCurricularSemester" property="semester">
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
				<bean:message key="message.manager.message.manager.curricular.course.scope.branchCode"/>
			</td>
			<td><%--nao tenho certeza de property--%>
				<html:text size="60" name="infoBranch" property="code"/>
			</td>
			<td>
				<span class="error"><html:errors name="infoBranch" property="code"/></span>
			</td>
		</tr>

		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.theoreticalHours"/>
			</td>
			<td>
				<html:text size="60" property="theoreticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoreticalHours"/></span>
			</td>
		</tr>
		
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.praticalHours"/>
			</td>
			<td>
				<html:text size="60" property="praticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="praticalHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.theoPratHours"/>
			</td>
			<td>
				<html:text size="60" property="theoPratHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoPratHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.labHours"/>
			</td>
			<td>
				<html:text size="60" property="labHours" />
			</td>
			<td>
				<span class="error"><html:errors property="labHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.maxIncrementNac"/>
			</td>
			<td>
				<html:text size="60" property="maxIncrementNac" />
			</td>
			<td>
				<span class="error"><html:errors property="maxIncrementNac"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.minIncrementNac"/>
			</td>
			<td>
				<html:text size="60" property="minIncrementNac" />
			</td>
			<td>
				<span class="error"><html:errors property="minIncrementNac"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.weight"/>
			</td>
			<td>
				<html:text size="60" property="weight" />
			</td>
			<td>
				<span class="error"><html:errors property="weight"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.message.manager.curricular.course.scope.credits"/>
			</td>
			<td>
				<html:text size="60" property="credits" />
			</td>
			<td>
				<span class="error"><html:errors property="credits"/></span>
			</td>
		</tr>
		
	</table>
	
	<br>

<br />
<html:submit styleClass="inputbutton">
<bean:message key="button.save"/>
</html:submit>
<html:reset  styleClass="inputbutton">
<bean:message key="label.clear"/>
</html:reset>				
</html:form>