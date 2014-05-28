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

<h2><bean:message key="link.equivalency.plan.create.equivalence" bundle="APPLICATION_RESOURCES"/></h2>

<p class="mvert15">
	<bean:message key="message.equivalency.table.from.degree.curricular.plan" bundle="APPLICATION_RESOURCES"/>
	<strong class="highlight1">
		<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
	</strong>
</p>

<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getExternalId() %>">

	<p class="mtop2 mbottom05"><bean:message key="message.set.non.list.fields" bundle="APPLICATION_RESOURCES"/>:</p>

	<fr:edit id="EquivalencePlanEntry.Creator.setNonListFields"
			name="equivalencePlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
			schema="EquivalencePlanEntry.Creator.setNonListFields">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mvert05"/>
     			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>

	<p class="mtop1 mbottom05"><bean:message key="message.origin.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<fr:edit id="EquivalencePlanEntry.Creator.addOriginDegreeModule"
			name="equivalencePlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
			schema="EquivalencePlanEntry.Creator.addOriginDegreeModule">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
     				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>
	
	
	<p class="mtop1 mbottom05"><bean:message key="message.destination.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<fr:edit id="EquivalencePlanEntry.Creator.addDestinationDegreeModule"
			name="equivalencePlanEntryCreator"
			type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
			schema="EquivalencePlanEntry.Creator.addDestinationDegreeModule">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thright thlight thmiddle mtop05"/>
     				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
	    </fr:layout>
	</fr:edit>
	
	<p>
		<html:submit><bean:message key="label.update" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>

</fr:form>



<logic:notEmpty name="equivalencePlanEntryCreator" property="originDegreeModules">
<logic:notEmpty name="equivalencePlanEntryCreator" property="destinationDegreeModules">

	<p class="mtop2 mbottom05"><bean:message key="label.equivalency.to.create" bundle="APPLICATION_RESOURCES"/></p>	
	<div style="background: #fafaf5; border: 2px solid #eed; padding: 1em; color: #333; line-height: 2em;">
		<logic:iterate id="degreeModuleFromList1" indexId="i1" name="equivalencePlanEntryCreator" property="originDegreeModules">
			<logic:notEqual name="i1" value="0">
				<strong style="padding: 0 0.5em;">
					<bean:message name="equivalencePlanEntryCreator" property="originLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
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

		<logic:notEmpty name="equivalencePlanEntryCreator" property="destinationDegreeModulesPreviousCourseGroup">
			<strong style="padding: 0 0.5em;"><bean:write name="equivalencePlanEntryCreator" property="destinationDegreeModulesPreviousCourseGroup.name"/>:</strong>
		</logic:notEmpty>
		<logic:iterate id="degreeModuleFromList2" indexId="i2" name="equivalencePlanEntryCreator" property="destinationDegreeModules">
			<logic:notEqual name="i2" value="0">
				<strong style="padding: 0 0.5em;">
					<bean:message name="equivalencePlanEntryCreator" property="destinationLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
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
			<logic:notEmpty name="equivalencePlanEntryCreator" property="ectsCredits">(<strong><bean:write name="equivalencePlanEntryCreator" property="ectsCredits"/></strong> <bean:message key="label.credits" bundle="APPLICATION_RESOURCES"/>)</logic:notEmpty>
		</span>
	</div>


	<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getExternalId() %>">
		<fr:edit id="EquivalencePlanEntry.Creator.create" name="equivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
				schema="EquivalencePlanEntry.Creator.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="dnone"/>
		   		<fr:hidden slot="equivalencePlan" name="equivalencePlan"/>
		    </fr:layout>
		</fr:edit>

		<p class="mtop15 mbottom0">
			<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
			<html:cancel><bean:message key="label.cancel" bundle="APPLICATION_RESOURCES"/></html:cancel>
		</p>
	</fr:form>

</logic:notEmpty>
</logic:notEmpty>
