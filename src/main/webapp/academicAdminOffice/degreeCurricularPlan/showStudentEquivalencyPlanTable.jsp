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
<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
<bean:define id="selectedDegreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="selectedDegreeCurricularPlan"/>


<logic:present name="equivalencePlanEntryWrappers">
	<table class="tstyle2 mtop05">
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.EquivalencyPlanEntryWrapper" indexId="n" name="equivalencePlanEntryWrappers">
		<tr>
			<td align="center">
					<logic:equal name="entry" property="equivalencePlanEntry.transitiveSource" value="true">
						<bean:message  key="label.transitive" bundle="APPLICATION_RESOURCES"/>
					</logic:equal>
					<logic:notEqual name="entry" property="equivalencePlanEntry.transitiveSource" value="true">
						-
					</logic:notEqual>
				</td>
			<td>
				<bean:define id="equivalencePlanEntry" name="entry" property="equivalencePlanEntry"/>
	
				<% boolean strikeText = false; %>
	
				<logic:equal name="entry" property="removalEntry" value="true">
					<% strikeText = true; %>
				</logic:equal>
	
				<logic:notEqual name="n" value="0">
				</logic:notEqual>
				<% if (strikeText) { %>
					<strike>
				<% } %>
				<logic:iterate id="degreeModuleFromList1" indexId="i1" name="equivalencePlanEntry" property="oldDegreeModules">
					<logic:notEqual name="i1" value="0">
						<strong style="padding: 0 0.5em;">
							<bean:message name="equivalencePlanEntry" property="sourceDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<span class="nowrap" style="border-bottom: 1px solid #aaa;">
						<logic:equal name="degreeModuleFromList1" property="curricularCourse" value="true">
							<logic:notEmpty name="degreeModuleFromList1" property="code"><bean:write name="degreeModuleFromList1" property="code"/> - </logic:notEmpty>
						</logic:equal><bean:write name="degreeModuleFromList1" property="name"/>
					</span>
				</logic:iterate>
			</td>
			<td>
				<span style="padding: 0 0.5em;">==></span>
			</td>
			<td>
				<logic:notEmpty name="equivalencePlanEntry" property="previousCourseGroupForNewDegreeModules">
					<strong><bean:write name="equivalencePlanEntry" property="previousCourseGroupForNewDegreeModules.name"/>:</strong>
				</logic:notEmpty>
				<logic:iterate id="degreeModuleFromList2" indexId="i2" name="equivalencePlanEntry" property="newDegreeModules">
					<logic:notEqual name="i2" value="0">
						<strong style="padding: 0 0.5em;">
							<bean:message name="equivalencePlanEntry" property="newDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
						</strong>
					</logic:notEqual>
					<span class="nowrap" style="border-bottom: 1px solid #aaa;">
						<logic:equal name="degreeModuleFromList2" property="curricularCourse" value="true">
							<logic:notEmpty name="degreeModuleFromList2" property="code"><bean:write name="degreeModuleFromList2" property="code"/> - </logic:notEmpty>
						</logic:equal>
						<bean:write name="degreeModuleFromList2" property="name"/>
					</span>
				</logic:iterate>
				
				<logic:notEmpty name="equivalencePlanEntry" property="ectsCredits">
					<span class="nowrap">(<bean:write name="equivalencePlanEntry" property="ectsCredits"/> <bean:message key="label.credits"/>)</span>
				</logic:notEmpty>
			<% if (strikeText) { %>
				</strike>
			<% } %>
			</td>
			<td>
			<logic:equal name="entry" property="equivalencePlanEntry.equivalencePlan.class.name" value="net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan">
				<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=deleteEquivalency&amp;selectedDegreeCurricularPlanID="
						+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanEntryID="
						+ entry.getEquivalencePlanEntry().getExternalId() + "&amp;studentNumber="
						+ student.getNumber() %>">
					<bean:message key="link.delete" bundle="APPLICATION_RESOURCES"/>
				</html:link>
			</logic:equal>
			<logic:notEqual name="entry" property="equivalencePlanEntry.equivalencePlan.class.name" value="net.sourceforge.fenixedu.domain.StudentCurricularPlanEquivalencePlan">
				<logic:equal name="entry" property="removalEntry" value="true">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=activate&amp;selectedDegreeCurricularPlanID="
							+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanEntryID="
							+ entry.getEquivalencePlanEntry().getExternalId() + "&amp;studentNumber="
							+ student.getNumber() %>">
						<bean:message key="link.activate" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</logic:equal>
				<logic:notEqual name="entry" property="removalEntry" value="true">
					<html:link page="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=deactivate&amp;selectedDegreeCurricularPlanID="
							+ selectedDegreeCurricularPlan.getExternalId() + "&amp;equivalencePlanEntryID="
							+ entry.getEquivalencePlanEntry().getExternalId() + "&amp;studentNumber="
							+ student.getNumber() %>">
						<bean:message key="link.deactivate" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</logic:notEqual>
			</logic:notEqual>
			</td>
		</tr>
		</logic:iterate>
	</table>
</logic:present>
