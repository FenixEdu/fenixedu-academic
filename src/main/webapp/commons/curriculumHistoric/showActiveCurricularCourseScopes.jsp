<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants"%>
<%@page import="net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval"%><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<em><bean:message key="link.curriculumHistoric" bundle="CURRICULUM_HISTORIC_RESOURCES"/></em>
<h2>
	<bean:message key="label.curricularPlan"  bundle="CURRICULUM_HISTORIC_RESOURCES"/>
	-
	<bean:write name="degreeCurricularPlan" property="name"/>
</h2>

<p>
	<span class="error0"><!-- Error messages go here --><html:errors bundle="CURRICULUM_HISTORIC_RESOURCES"/></span>
</p>


<logic:present name="degreeModuleScopes">

	<bean:define id="academicInterval" name="academicInterval"/>

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
					<bean:define id="curricularCourseCode" name="degreeModuleScope" property="curricularCourse.externalId"/>
					<bean:define id="currentSemester" name="degreeModuleScope" property="curricularSemester"/>
					<html:link page="<%="/showCurriculumHistoric.do?method=showCurriculumHistoric&amp;curricularCourseCode=" + curricularCourseCode + "&amp;semester=" + pageContext.findAttribute("currentSemester").toString() + "&amp;academicInterval=" + ((AcademicInterval)request.getAttribute(PresentationConstants.ACADEMIC_INTERVAL)).getResumedRepresentationInStringFormat() %>">
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
