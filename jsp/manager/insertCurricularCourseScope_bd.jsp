<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<h2><bean:message key="label.manager.insert.curricularCourseScope" /></h2>

<br>

<html:form action="/insertCurricularCourseScope" method="get">

	<html:hidden property="page" value="1"/>
	<html:hidden property="method" value="insert"/>
	<html:hidden property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
	
    <table>
		<tr>
			<td>
				<bean:message key="label.manager.curricular.course.scope.branch"/>
			</td>
			<td>
				<html:select property="branchId">
					<html:options collection="branchesList" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
	    <tr>
			<td>
				<bean:message key="label.manager.curricular.course.scope.semester"/>
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
				<bean:message key="message.manager.curricular.course.scope.theoreticalHours"/>
			</td>
			<td>
				<html:text size="5" property="theoreticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoreticalHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.praticalHours"/>
			</td>
			<td>
				<html:text size="5" property="praticalHours" />
			</td>
			<td>
				<span class="error"><html:errors property="praticalHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.theoPratHours"/>
			</td>
			<td>
				<html:text size="5" property="theoPratHours" />
			</td>
			<td>
				<span class="error"><html:errors property="theoPratHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.labHours"/>
			</td>
			<td>
				<html:text size="5" property="labHours" />
			</td>
			<td>
				<span class="error"><html:errors property="labHours"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.credits"/>
			</td>
			<td>
				<html:text size="5" property="credits" />
			</td>
			<td>
				<span class="error"><html:errors property="credits"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.maxIncrementNac"/>
			</td>
			<td>
				<html:text size="5" property="maxIncrementNac" />
			</td>
			<td>
				<span class="error"><html:errors property="maxIncrementNac"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.minIncrementNac"/>
			</td>
			<td>
				<html:text size="5" property="minIncrementNac" />
			</td>
			<td>
				<span class="error"><html:errors property="minIncrementNac"/></span>
			</td>
		</tr>
		<tr>
			<td>
				<bean:message key="message.manager.curricular.course.scope.weight"/>
			</td>
			<td>
				<html:text size="5" property="weight" />
			</td>
			<td>
				<span class="error"><html:errors property="weight"/></span>
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