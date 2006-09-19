<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<h2>
	<bean:message key="label.curricularPlan"  bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	&nbsp;-&nbsp;
	<bean:write name="degreeCurricularPlan" property="name"/>
</h2>

<span class="error"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>

<br />

<logic:present name="degreeModuleScopes">

	<bean:define id="executionYear" name="executionYearID"/>

	<table>
		<%
			int semester = 0;
		%>
		<logic:iterate id="degreeModuleScope" name="degreeModuleScopes">
			<bean:define id="semesterI" type="java.lang.Integer" name="degreeModuleScope" property="curricularSemester"/>
			<%
				if (semester != semesterI.intValue()) {
				    semester = semesterI.intValue();
				    %>
						<tr>
							<th class="listClasses-header">
								<bean:message key="label.curricularCourseScope.curricularYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th class="listClasses-header">
								<bean:message key="label.curricularCourseScope.curricularSemester" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th class="listClasses-header">
								<bean:message key="label.curricularCourse" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th class="listClasses-header">
								<bean:message key="label.curricularCourseScope.branch" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
						</tr>
				    <%
				}
			%>
			<tr>
				<td class="listClasses">
					<bean:write name="degreeModuleScope" property="curricularYear"/>
				</td>
				<td class="listClasses">
					<bean:write name="degreeModuleScope" property="curricularSemester"/>
				</td>
				<td class="listClasses" style="text-align:left">
					<bean:define id="curricularCourseCode" name="degreeModuleScope" property="curricularCourse.idInternal"/>
					<bean:define id="currentSemester" name="degreeModuleScope" property="curricularCourse.idInternal"/>
					<html:link page="<%="/showCurriculumHistoric.do?method=showCurriculumHistoric&amp;curricularCourseCode=" + curricularCourseCode +"&amp;semester=" + pageContext.findAttribute("currentSemester").toString() + "&amp;executionYearID=" + pageContext.findAttribute("executionYear").toString() %>">
						<bean:write name="degreeModuleScope" property="curricularCourse.name"/>
					</html:link>
				</td>
				<td class="listClasses">
					<bean:write name="degreeModuleScope" property="branch"/>
				</td>
			</tr>
		</logic:iterate>
	</table>

</logic:present>
