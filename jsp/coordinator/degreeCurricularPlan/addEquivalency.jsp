<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>

<em><bean:message key="title.scientificCouncil.portalTitle"/></em>
<h2><bean:message key="link.equivalency.plan.create.equivalence" bundle="APPLICATION_RESOURCES"/></h2>

<p class="mvert15">
	<bean:message key="message.equivalency.table.from.degree.curricular.plan" bundle="APPLICATION_RESOURCES"/>
	<strong class="highlight1">
		<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
	</strong>
</p>

<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">

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
	<p class="mtop05">
		<html:submit><bean:message key="label.set" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>


	<p class="mtop2 mbottom05"><bean:message key="message.origin.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="EquivalencePlanEntry.Creator.addOriginDegreeModule"
						name="equivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
						schema="EquivalencePlanEntry.Creator.addOriginDegreeModule">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
	
	<p class="mtop2 mbottom05"><bean:message key="message.destination.degree.module" bundle="APPLICATION_RESOURCES"/></p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="EquivalencePlanEntry.Creator.addDestinationDegreeModule"
						name="equivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
						schema="EquivalencePlanEntry.Creator.addDestinationDegreeModule">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<logic:notEmpty name="equivalencePlanEntryCreator" property="originDegreeModules">
<logic:notEmpty name="equivalencePlanEntryCreator" property="destinationDegreeModules">

	<p class="mtop2"><bean:message key="label.equivalency.to.create" bundle="APPLICATION_RESOURCES"/></p>	
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #333; line-height: 2em;">
		<logic:iterate id="degreeModuleFromList1" indexId="i1" name="equivalencePlanEntryCreator" property="originDegreeModules">
			<span style="padding: 0 0.5em;">
				<logic:notEqual name="i1" value="0">
					<strong>
						<bean:message name="equivalencePlanEntryCreator" property="originLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
			</span>
			<span style="border-bottom: 1px solid #aaa;">
				<bean:write name="degreeModuleFromList1" property="name"/>
			</span>
		</logic:iterate>
		
		<span style="padding-left: 1em;">==></span>
		
		<logic:iterate id="degreeModuleFromList2" indexId="i2" name="equivalencePlanEntryCreator" property="destinationDegreeModules">
			<span style="padding: 0 0.5em;">	
				<logic:notEqual name="i2" value="0">
					<strong>
						<bean:message name="equivalencePlanEntryCreator" property="destinationLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
			</span>
			<span style="border-bottom: 1px solid #aaa;">
				<bean:write name="degreeModuleFromList2" property="name"/>
			</span>
		</logic:iterate>
		<span style="padding: 0 0.5em;">
			(<strong><bean:write name="equivalencePlanEntryCreator" property="ectsCredits"/></strong> <bean:message key="label.credits" bundle="APPLICATION_RESOURCES"/>)
		</span>
	</div>

	<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
		<fr:edit id="EquivalencePlanEntry.Creator.create" name="equivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
				schema="EquivalencePlanEntry.Creator.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
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
