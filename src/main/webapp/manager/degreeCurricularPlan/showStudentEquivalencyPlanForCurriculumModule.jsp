<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>
<html:xhtml/>

<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>
<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="equivalencyPlanEntryCurriculumModuleWrapper"/>
<bean:define id="curriculumModule" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule" name="equivalencyPlanEntryCurriculumModuleWrapper" property="curriculumModule"/>
<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
<bean:define id="selectedDegreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="selectedDegreeCurricularPlan"/>
<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="studentCurricularPlanEquivalencePlan"/>
<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
<bean:define id="width" type="java.lang.String" name="width"/>

<logic:equal name="curriculumModule" property="leaf" value="true">
	<logic:equal name="curriculumModule" property="enrolment" value="true">
	
		<bean:define id="enrolment" type="net.sourceforge.fenixedu.domain.Enrolment" name="curriculumModule"/>
	
		<div class="indent<%= indentLevel %>">
			<table class="showinfo3 mvert0" style="width: <%= width %>em;">
				<tr>
					<td>
						<logic:equal name="enrolment" property="enrolment" value="true">
							<logic:notEmpty name="enrolment" property="code"><bean:write name="enrolment" property="code"/> - </logic:notEmpty>
						</logic:equal>
						<bean:write name="enrolment" property="name"/>
					</td>
					<td class="highlight2 smalltxt" align="center" style="width: 14em;">
						<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=prepareAddEquivalency&amp;selectedDegreeCurricularPlanID="
								+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
								+ equivalencePlan.getExternalId() + "&amp;curriculumModuleID="
								+ curriculumModule.getExternalId() + "&amp;studentNumber="
									+ student.getNumber() %>">
							<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</td>
					<td class="smalltxt" align="right" style="width: 22em;">
						<logic:empty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
							<span style="color: #888">
								<bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
							</span>
						</logic:empty>
						<logic:notEmpty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
							<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;selectedDegreeCurricularPlanID="
									+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
									+ equivalencePlan.getExternalId() + "&amp;curriculumModuleID="
									+ curriculumModule.getExternalId() + "&amp;studentNumber="
									+ student.getNumber() 
									%>">
								<bean:message key="link.equivalencies.for.curricular.course.view" bundle="APPLICATION_RESOURCES"/>
							</html:link>
						</logic:notEmpty>
					</td>
				</tr>
			</table>
		</div>
		
	</logic:equal>
</logic:equal>
<logic:notEqual name="curriculumModule" property="leaf" value="true">

	<bean:define id="curriculumGroup" type="net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup" name="curriculumModule"/>

	<div style="padding-left: <%= indentLevel %>em;">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr class="bgcolor2">
				<th class="aleft">
					<bean:write name="curriculumGroup" property="name"/>
				</th>
				<th class="smalltxt" align="center" style="width: 14em;">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=prepareAddEquivalency&amp;selectedDegreeCurricularPlanID="
							+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
							+ equivalencePlan.getExternalId() + "&amp;curriculumModuleID="
							+ curriculumModule.getExternalId() + "&amp;studentNumber="
								+ student.getNumber() %>">
						<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</th>
				<th class="smalltxt" align="right" style="width: 22em;">
					<logic:empty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
						<span style="color: #888">
							<bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
						</span>
					</logic:empty>
					<logic:notEmpty name="equivalencyPlanEntryCurriculumModuleWrapper" property="equivalencePlanEntriesToApply">
						<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showTable&amp;selectedDegreeCurricularPlanID="
								+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
								+ equivalencePlan.getExternalId() + "&amp;curriculumModuleID="
								+ curriculumModule.getExternalId() + "&amp;studentNumber="
								+ student.getNumber() 
								%>">
							<bean:message key="link.equivalencies.for.curricular.course.view" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</logic:notEmpty>
				</th>
			</tr>
		</table>
	</div>

	<logic:iterate id="child" name="equivalencyPlanEntryCurriculumModuleWrapper" property="children">
		<bean:define id="equivalencyPlanEntryCurriculumModuleWrapper" name="child" toScope="request"/>
		<% 
			Integer newIndentLevel = Integer.valueOf(Integer.parseInt(indentLevel) + 3);
			Integer newWidth = Integer.valueOf(Integer.parseInt(width) - 3);
		%>
		<bean:define id="indentLevel" type="java.lang.String" value="<%= newIndentLevel.toString() %>" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="<%= newWidth.toString() %>" toScope="request"/>
		<jsp:include page="showStudentEquivalencyPlanForCurriculumModule.jsp"/>
	</logic:iterate>
</logic:notEqual>
