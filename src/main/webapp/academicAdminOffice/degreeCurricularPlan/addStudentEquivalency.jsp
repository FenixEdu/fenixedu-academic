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
<bean:define id="selectedDegreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="selectedDegreeCurricularPlan"/>
<bean:define id="student" type="net.sourceforge.fenixedu.domain.student.Student" name="student"/>

<h2><bean:message key="link.equivalency.plan.create.equivalence" bundle="APPLICATION_RESOURCES"/></h2>

<p class="mvert15">
	<bean:message key="message.equivalency.table.from.degree.curricular.plan" bundle="APPLICATION_RESOURCES"/>
	<strong class="highlight1">
		<bean:write name="selectedDegreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
	</strong>
</p>

<fr:form action="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=prepareAddEquivalency&amp;selectedDegreeCurricularPlanID=" + selectedDegreeCurricularPlan.getExternalId()  
			+ "&amp;studentNumber=" + student.getNumber() %>">
	<p class="mtop2"><bean:message key="message.set.non.list.fields" bundle="APPLICATION_RESOURCES"/>:</p>

	<fr:edit id="StudentEquivalencyPlanEntryCreator.setNonListFields"
			name="studentEquivalencyPlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator"
			schema="StudentEquivalencyPlanEntryCreator.setNonListFields">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
     				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>

<%--
	<p class="mtop05">
		<html:submit><bean:message key="label.set" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
--%>


	<p class="mtop15 mbottom05"><bean:message key="message.origin.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<fr:edit id="StudentEquivalencyPlanEntryCreator.addOrigin"
			name="studentEquivalencyPlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator"
			schema="StudentEquivalencyPlanEntryCreator.addOrigin">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
     				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>

	<p class="mtop15 mbottom05"><bean:message key="message.destination.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<fr:edit id="StudentEquivalencyPlanEntryCreator.addDestination"
			name="studentEquivalencyPlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator"
			schema="StudentEquivalencyPlanEntryCreator.addDestination">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
     				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>

	<p>
		<html:submit><bean:message key="label.update" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>

</fr:form>


<logic:notEmpty name="studentEquivalencyPlanEntryCreator" property="originDegreeModules">
<logic:notEmpty name="studentEquivalencyPlanEntryCreator" property="destinationDegreeModules">

	<p class="mtop2 mbottom05"><bean:message key="label.equivalency.to.create" bundle="APPLICATION_RESOURCES"/></p>	
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #333;">
		<logic:iterate id="degreeModuleFromList1" indexId="i1" name="studentEquivalencyPlanEntryCreator" property="originDegreeModules">
			<logic:notEqual name="i1" value="0">
				<strong style="padding: 0 0.5em;">
					<bean:message name="studentEquivalencyPlanEntryCreator" property="originLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
				</strong>
			</logic:notEqual>
			<span style="border-bottom: 1px solid #aaa;">
				<logic:equal name="degreeModuleFromList1" property="curricularCourse" value="true">
					<logic:notEmpty name="degreeModuleFromList1" property="code"><bean:write name="degreeModuleFromList1" property="code"/> - </logic:notEmpty>
				</logic:equal>
				<bean:write name="degreeModuleFromList1" property="name"/>
			</span>
		</logic:iterate>

		<span style="padding: 0 0.5em;">==></span>

		<logic:notEmpty name="studentEquivalencyPlanEntryCreator" property="destinationDegreeModulesPreviousCourseGroup"><strong><bean:write name="studentEquivalencyPlanEntryCreator" property="destinationDegreeModulesPreviousCourseGroup.name"/>:</strong> </logic:notEmpty>
		<logic:iterate id="degreeModuleFromList2" indexId="i2" name="studentEquivalencyPlanEntryCreator" property="destinationDegreeModules">
			<logic:notEqual name="i2" value="0">
				<strong style="padding: 0 0.5em;">
					<bean:message name="studentEquivalencyPlanEntryCreator" property="destinationLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
				</strong>
			</logic:notEqual>
			<span style="border-bottom: 1px solid #aaa;">
				<logic:equal name="degreeModuleFromList2" property="curricularCourse" value="true">
					<logic:notEmpty name="degreeModuleFromList2" property="code"><bean:write name="degreeModuleFromList2" property="code"/> - </logic:notEmpty>
				</logic:equal>
				<bean:write name="degreeModuleFromList2" property="name"/>
			</span>
		</logic:iterate>
		<span style="padding: 0 0.5em;">
			<logic:notEmpty name="studentEquivalencyPlanEntryCreator" property="ectsCredits">(<bean:write name="studentEquivalencyPlanEntryCreator" property="ectsCredits"/> <bean:message key="label.credits" bundle="APPLICATION_RESOURCES"/>)</logic:notEmpty>
		</span>
	</div>

	<fr:form action="<%= "/degreeCurricularPlan/studentEquivalencyPlan.do?method=showPlan&amp;selectedDegreeCurricularPlanID=" + selectedDegreeCurricularPlan.getExternalId() 
			+ "&amp;studentNumber=" + student.getNumber() %>">
		<fr:edit id="StudentEquivalencyPlanEntryCreator.create"
				name="studentEquivalencyPlanEntryCreator"
				type="net.sourceforge.fenixedu.domain.studentCurricularPlan.equivalencyPlan.StudentEquivalencyPlanEntryCreator"
				schema="StudentEquivalencyPlanEntryCreator.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="dnone"/>
		    </fr:layout>
		</fr:edit>
		
		<p class="mtop15 mbottom0">
			<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:cancel>
		</p>
	</fr:form>
</logic:notEmpty>
</logic:notEmpty>
