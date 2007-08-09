<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.studentsListByCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
	</p>
</html:messages>

<fr:form action="/studentsListByCurricularCourse.do?method=showActiveCurricularCourseScope">
	<fr:edit name="searchBean" schema="student.list.searchByCurricularCourse.chooseDegreeCurricularPlan">
		<fr:destination name="executionYearPostBack" path="/studentsListByCurricularCourse.do?method=chooseExecutionYearPostBack"/>	
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thright mtop025"/>
	        <fr:property name="columnClasses" value="width12em,,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>		
</fr:form>

<p/>

<logic:present name="degreeModuleScopes">

	<bean:define id="executionYear" name="searchBean" property="executionYear.idInternal"/>

	<table class="tstyle4 thleft">
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
							<th>
								<bean:message key="label.curricularCourseScope.curricularYear" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th>
								<bean:message key="label.curricularCourseScope.curricularSemester" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th>
								<bean:message key="label.curricularCourse" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
							<th>
								<bean:message key="label.curricularCourseScope.branch" bundle="CURRICULUM_HISTORIC_RESOURCES"/>
							</th>
						</tr>
				    <%
				}
			%>
			<tr>
				<td class="acenter">
					<bean:write name="degreeModuleScope" property="curricularYear"/>
				</td>
				<td class="acenter">
					<bean:write name="degreeModuleScope" property="curricularSemester"/>
				</td>
				<td style="text-align:left">
					<bean:define id="curricularCourseCode" name="degreeModuleScope" property="curricularCourse.idInternal"/>
					<bean:define id="currentSemester" name="degreeModuleScope" property="curricularSemester"/>
					<bean:define id="currentYear" name="degreeModuleScope" property="curricularYear"/>
					<html:link page="<%="/studentsListByCurricularCourse.do?method=searchByCurricularCourse&amp;curricularCourseCode=" + curricularCourseCode +"&amp;semester=" + pageContext.findAttribute("currentSemester").toString() +"&amp;year=" + pageContext.findAttribute("currentYear").toString() + "&amp;executionYearID=" + pageContext.findAttribute("executionYear").toString() %>">
						<bean:write name="degreeModuleScope" property="curricularCourse.name"/>
					</html:link>
				</td>
				<td>
					<bean:write name="degreeModuleScope" property="branch"/>
				</td>
			</tr>
		</logic:iterate>
	</table>

</logic:present>
