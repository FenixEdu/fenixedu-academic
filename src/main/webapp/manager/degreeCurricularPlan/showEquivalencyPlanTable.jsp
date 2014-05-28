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

	<logic:present name="entries">
	<ul class="mtop05">
		<li>
			<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID="
					+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
					+ equivalencePlan.getExternalId() %>">
				<bean:message key="link.equivalency.add" bundle="APPLICATION_RESOURCES"/>
			</html:link>
		</li>
	</ul>
	
	<p class="mtop15 mbottom05">
		<bean:message key="message.degree.module.equivalencies" bundle="APPLICATION_RESOURCES"/>:
	</p>
	
	
	

		<logic:empty name="entries">
			<em><bean:message key="message.curricular.course.has.no.equivalencies" bundle="APPLICATION_RESOURCES"/></em>
		</logic:empty>

		<table class="tstyle2 mtop05">
		<logic:iterate id="entry" type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry" indexId="n" name="entries">
			<tr>
				<td align="center">
					<logic:equal name="entry" property="transitiveSource" value="true">
						<bean:message  key="label.transitive" bundle="APPLICATION_RESOURCES"/>
					</logic:equal>
					<logic:notEqual name="entry" property="transitiveSource" value="true">
						-
					</logic:notEqual>
				</td>
			
			
			<td>
			<logic:notEqual name="n" value="0">
			</logic:notEqual>
			
			<logic:iterate id="degreeModuleFromList1" indexId="i1" name="entry" property="oldDegreeModules">
				<logic:notEqual name="i1" value="0">
					<strong style="padding: 0 0.5em;">
						<bean:message name="entry" property="sourceDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<span class="nowrap" style="border-bottom: 1px solid #ccc;">
					<logic:equal name="degreeModuleFromList1" property="curricularCourse" value="true">
						<logic:notEmpty name="degreeModuleFromList1" property="code"><bean:write name="degreeModuleFromList1" property="code"/> - </logic:notEmpty>
					</logic:equal>
					<bean:write name="degreeModuleFromList1" property="name"/>
				</span>
			</logic:iterate>
			</td>
	
			<td>
				<span>==></span>
			</td>
	
			<td>		
			<logic:iterate id="degreeModuleFromList2" indexId="i2" name="entry" property="newDegreeModules">
				<logic:notEqual name="i2" value="0">
					<strong style="padding: 0 0.5em;">
						<bean:message name="entry" property="newDegreeModulesOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<span class="nowrap" style="border-bottom: 1px solid #ccc;">
					<logic:equal name="degreeModuleFromList2" property="curricularCourse" value="true">
						<logic:notEmpty name="degreeModuleFromList2" property="code"><bean:write name="degreeModuleFromList2" property="code"/> - </logic:notEmpty>
					</logic:equal>
					<bean:write name="degreeModuleFromList2" property="name"/>
				</span>
			</logic:iterate>
			</td>
			
			<td class="acenter nowrap">
			<logic:notEmpty name="entry" property="previousCourseGroupForNewDegreeModules">
				<bean:write name="entry" property="previousCourseGroupForNewDegreeModules.name"/>
			</logic:notEmpty>
			<logic:empty name="entry" property="previousCourseGroupForNewDegreeModules">
				-
			</logic:empty>
			</td>
			
			<td class="acenter nowrap">
			<logic:notEmpty name="entry" property="ectsCredits">
				<bean:write name="entry" property="ectsCredits"/> <bean:message key="label.credits" bundle="APPLICATION_RESOURCES"/>
			</logic:notEmpty>
			<logic:empty name="entry" property="ectsCredits">
				-
			</logic:empty>
			</td>
						
			<td>
			<span>
				<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=deleteEquivalency&amp;degreeCurricularPlanID="
						+ degreeCurricularPlan.getExternalId() + "&amp;equivalencePlanID="
						+ equivalencePlan.getExternalId() + "&amp;equivalencePlanEntryID="
						+ entry.getExternalId() %>">
					<bean:message key="link.delete" bundle="APPLICATION_RESOURCES"/>
				</html:link>
			</span>
			</td>
		</tr>
		</logic:iterate>
	</table>
	</logic:present>
