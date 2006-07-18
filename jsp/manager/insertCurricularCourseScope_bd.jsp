<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<h2><bean:message bundle="MANAGER_RESOURCES" key="label.manager.insert.curricularCourseScope" /></h2>
<br />
<span class="error"><html:errors/></span>
<html:form action="/insertCurricularCourseScope" method="get">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="insert"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeId" property="degreeId" value="<%= request.getParameter("degreeId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.degreeCurricularPlanId" property="degreeCurricularPlanId" value="<%= request.getParameter("degreeCurricularPlanId") %>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.curricularCourseId" property="curricularCourseId" value="<%= request.getParameter("curricularCourseId") %>"/>
    <table>
		<tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricular.course.scope.branch"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.branchId" property="branchId">
					<html:options collection="branchesList" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
	    <tr>
			<td>
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.semester"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.curricularSemesterId" property="curricularSemesterId">
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
				<bean:message bundle="MANAGER_RESOURCES" key="label.manager.curricularCourseScope.beginDate"/>
			</td>
			<td>
				<html:select bundle="HTMLALT_RESOURCES" altKey="select.beginDate" property="beginDate">
					<html:options collection="executionPeriodsLabels" property="value" labelProperty="label"/>
				</html:select>				
			</td>
		</tr>
		<tr>
		<td>
			<bean:message bundle="MANAGER_RESOURCES" key="message.manager.degree.curricular.plan.anotation"/>
		</td>
		<td>
			<html:text bundle="HTMLALT_RESOURCES" altKey="text.anotation" property="anotation" size="20" />
		</td>
</tr>
		
	</table>
	<br />
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="button.save"/>
	</html:submit>
	<html:reset bundle="HTMLALT_RESOURCES" altKey="reset.reset"  styleClass="inputbutton">
		<bean:message bundle="MANAGER_RESOURCES" key="label.clear"/>
	</html:reset>
</html:form>