<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.domain.ExecutionYear"%>
<html:xhtml />

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES" /></em>
<h2>
	<bean:message key="label.studentsListByCurricularCourse" bundle="ACADEMIC_OFFICE_RESOURCES" />
</h2>

<html:messages id="message" message="true" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"> <!-- Error messages go here --> <bean:write name="message" />
		</span>
	</p>
</html:messages>

<fr:form action="/studentsListByCurricularCourse.do?method=showActiveCurricularCourseScope">
	<fr:edit id="searchBean" name="searchBean">
		<fr:schema
			type="net.sourceforge.fenixedu.dataTransferObject.academicAdministration.SearchStudentsByCurricularCourseParametersBean"
			bundle="ACADEMIC_OFFICE_RESOURCES">
			<fr:slot name="executionYear" key="label.executionYear" layout="menu-select-postback" required="true">
				<fr:property name="providerClass"
					value="net.sourceforge.fenixedu.presentationTier.renderers.providers.OpenExecutionYearsProvider" />
				<fr:property name="format" value="${year}" />
				<fr:property name="destination" value="executionYearPostBack" />
			</fr:slot>
			<fr:slot name="degreeCurricularPlan" key="label.degreeCurricularPlan" layout="menu-select" required="true">
				<fr:property name="from" value="availableDegreeCurricularPlans" />
				<fr:property name="format" value="${degree.tipoCurso.localizedName} - ${degree.name} - ${name}" />
			</fr:slot>
		</fr:schema>
		<fr:destination name="executionYearPostBack"
			path="/studentsListByCurricularCourse.do?method=chooseExecutionYearPostBack" />
		<fr:destination name="invalid" path="/studentsListByCurricularCourse.do?method=prepareByCurricularCourse" />
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright mtop025 thmiddle" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
	</fr:edit>

	<html:submit>
		<bean:message key="button.search" bundle="ACADEMIC_OFFICE_RESOURCES" />
	</html:submit>
</fr:form>

<br />
<logic:present name="searchBean" property="executionYear">
	<html:link action="/studentsListByCurricularCourse.do?method=downloadStatistics" paramId="executionYearId"
		paramName="searchBean" paramProperty="executionYear.externalId">
		<html:img border="0" src='<%=request.getContextPath() + "/images/excel.gif"%>' altKey="excel" bundle="IMAGE_RESOURCES" />
		<bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="link.download.statistics" />
	</html:link>
</logic:present>

<logic:present name="degreeModuleScopes">

	<bean:define id="executionYear" name="searchBean" property="executionYear.externalId" />

	<table class="tstyle1 thleft thlight">
		<%
		    int semester = 0;
		%>
		<logic:iterate id="degreeModuleScope" name="degreeModuleScopes">
			<bean:define id="semesterI" type="java.lang.Integer" name="degreeModuleScope" property="curricularSemester" />
			<%
			    if (semester != semesterI.intValue()) {
						semester = semesterI.intValue();
			%>
			<tr>
				<th><bean:message key="label.curricularCourseScope.curricularYear" bundle="CURRICULUM_HISTORIC_RESOURCES" /></th>
				<th><bean:message key="label.curricularCourseScope.curricularSemester" bundle="CURRICULUM_HISTORIC_RESOURCES" />
				</th>
				<th><bean:message key="label.curricularCourse" bundle="CURRICULUM_HISTORIC_RESOURCES" /></th>
				<th><bean:message key="label.curricularCourseScope.branch" bundle="CURRICULUM_HISTORIC_RESOURCES" /></th>
			</tr>
			<%
			    }
			%>
			<tr>
				<td class="acenter"><bean:write name="degreeModuleScope" property="curricularYear" /></td>
				<td class="acenter"><bean:write name="degreeModuleScope" property="curricularSemester" /></td>
				<td style="text-align: left"><bean:define id="curricularCourseCode" name="degreeModuleScope"
						property="curricularCourse.externalId" /> <bean:define id="currentSemester" name="degreeModuleScope"
						property="curricularSemester" /> <bean:define id="currentYear" name="degreeModuleScope" property="curricularYear" />
					<html:link
						page="<%="/studentsListByCurricularCourse.do?method=searchByCurricularCourse&amp;curricularCourseCode="
				+ curricularCourseCode + "&amp;semester="
				+ pageContext.findAttribute("currentSemester").toString() + "&amp;year="
				+ pageContext.findAttribute("currentYear").toString() + "&amp;executionYearID="
				+ pageContext.findAttribute("executionYear").toString()%>">
						<bean:write name="degreeModuleScope" property="curricularCourse.name" />
					</html:link></td>
				<td><bean:write name="degreeModuleScope" property="branch" /></td>
			</tr>
		</logic:iterate>
	</table>

</logic:present>
