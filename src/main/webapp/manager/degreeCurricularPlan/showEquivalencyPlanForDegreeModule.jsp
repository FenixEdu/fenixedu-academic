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

<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

<logic:equal name="degreeModule" property="leaf" value="true">

	<bean:define id="curricularCourse" type="net.sourceforge.fenixedu.domain.CurricularCourse" name="degreeModule"/>

	<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
	<bean:define id="width" type="java.lang.String" name="width"/>

	<div class="indent<%= indentLevel %>">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr>
				<td>
					<logic:equal name="degreeModule" property="curricularCourse" value="true">
						<logic:notEmpty name="degreeModule" property="code"><bean:write name="degreeModule" property="code"/> - </logic:notEmpty>
					</logic:equal><bean:write name="degreeModule" property="name"/>
				</td>
				<td class="highlight2 smalltxt" align="center" style="width: 14em;">
					<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
							+ equivalencePlan.getExternalId() + "&amp;degreeModuleID="
							+ curricularCourse.getExternalId() %>">
						<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</td>
				<td class="smalltxt" align="right" style="width: 22em;">
					<%
						java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlanEntry> equivalencePlanEntries = curricularCourse.getNewDegreeModuleEquivalencePlanEntries(equivalencePlan);
						request.setAttribute("equivalencePlanEntries", equivalencePlanEntries);
					%>					
					<bean:size id="numElements" name="equivalencePlanEntries"/>
					<logic:equal name="numElements" value="0">
						<span style="color: #888">
							<bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
						</span>
					</logic:equal>
					<logic:notEqual name="numElements" value="0">
						<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
								+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
								+ equivalencePlan.getExternalId() + "&amp;degreeModuleID="
								+ curricularCourse.getExternalId() %>">
							<bean:message key="link.equivalencies.for.curricular.course.view" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</logic:notEqual>
				</td>
			</tr>
		</table>
	</div>
</logic:equal>
<logic:notEqual name="degreeModule" property="leaf" value="true">

	<bean:define id="courseGroup" type="net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup" name="degreeModule"/>
	<bean:define id="indentLevel" type="java.lang.String" name="indentLevel"/>
	<bean:define id="width" type="java.lang.String" name="width"/>

	<div style="padding-left: <%= indentLevel %>em;">
		<table class="showinfo3 mvert0" style="width: <%= width %>em;">
			<tr class="bgcolor2">
				<th class="aleft">
					<logic:equal name="degreeModule" property="curricularCourse" value="true">
						<logic:notEmpty name="degreeModule" property="code"><bean:write name="degreeModule" property="code"/> - </logic:notEmpty>
					</logic:equal><bean:write name="degreeModule" property="name"/>
				</th>
				<th class="smalltxt" align="center" style="width: 14em;">
					<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
							+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
							+ equivalencePlan.getExternalId() + "&amp;degreeModuleID="
							+ courseGroup.getExternalId() %>">
						<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
					</html:link>
				</th>
				<th class="smalltxt" align="right" style="width: 22em;">
					<%
						java.util.Set<net.sourceforge.fenixedu.domain.EquivalencePlanEntry> equivalencePlanEntries = courseGroup.getNewDegreeModuleEquivalencePlanEntries(equivalencePlan);
						request.setAttribute("equivalencePlanEntries", equivalencePlanEntries);
					%>					
					<bean:size id="numElements" name="equivalencePlanEntries"/>
					<logic:equal name="numElements" value="0">
						<span style="color: #888">
							<bean:message key="message.course.group.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/>
						</span>
					</logic:equal>
					<logic:notEqual name="numElements" value="0">
						<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
								+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
								+ equivalencePlan.getExternalId() + "&amp;degreeModuleID="
								+ courseGroup.getExternalId() %>">
							<bean:message key="link.equivalencies.for.course.group.view" bundle="APPLICATION_RESOURCES"/>
						</html:link>
					</logic:notEqual>
				</th>
			</tr>
		</table>
	</div>

	<logic:iterate id="context" name="degreeModule" property="childContextsSortedByDegreeModuleName">
		<bean:define id="degreeModule" name="context" property="childDegreeModule" toScope="request"/>
		<% 
			Integer newIndentLevel = Integer.valueOf(Integer.parseInt(indentLevel) + 3);
			Integer newWidth = Integer.valueOf(Integer.parseInt(width) - 3);
		%>
		<bean:define id="indentLevel" type="java.lang.String" value="<%= newIndentLevel.toString() %>" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="<%= newWidth.toString() %>" toScope="request"/>
		<jsp:include page="showEquivalencyPlanForDegreeModule.jsp"/>
	</logic:iterate>
</logic:notEqual>
