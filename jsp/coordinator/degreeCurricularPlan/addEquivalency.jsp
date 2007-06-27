<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>

<h2><bean:message key="link.equivalency.plan.create.equivalence"/></h2>

<p class="mvert15">
	<strong>
		<bean:message key="message.equivalency.table.from.degree.curricular.plan"/>
		<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
	</strong>
</p>

<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
	<p class="mtop2"><bean:message key="message.set.non.list.fields"/>:</p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="EquivalencePlanEntry.Creator.setNonListFields"
						name="equivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.EquivalencePlanEntry$EquivalencePlanEntryCreator"
						schema="EquivalencePlanEntry.Creator.setNonListFields">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</td>
		</tr>
		<tr>
			<td>
				<html:submit><bean:message key="label.set" bundle="APPLICATION_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
	<p class="mtop2"><bean:message key="message.origin.degree.module"/>:</p>
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
	<p class="mtop2"><bean:message key="message.destination.degree.module"/>:</p>
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

	<p class="mtop2"><bean:message key="label.equivalency.to.create"/></p>	
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:iterate id="degreeModuleFromList1" indexId="i1" name="equivalencePlanEntryCreator" property="originDegreeModules">
			<span style="border-bottom: 1px dotted #aaa;">
				<logic:notEqual name="i1" value="0">
					<strong>
						<bean:message name="equivalencePlanEntryCreator" property="originLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<bean:write name="degreeModuleFromList1" property="name"/>
			</span>
		</logic:iterate>
		==>
		<logic:iterate id="degreeModuleFromList2" indexId="i2" name="equivalencePlanEntryCreator" property="destinationDegreeModules">
			<span style="border-bottom: 1px dotted #aaa;">
				<logic:notEqual name="i2" value="0">
					<strong>
						<bean:message name="equivalencePlanEntryCreator" property="destinationLogicOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<bean:write name="degreeModuleFromList2" property="name"/>
			</span>
		</logic:iterate>
		(<bean:write name="equivalencePlanEntryCreator" property="ectsCredits"/> <bean:message key="label.credits"/>)
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
		<p class="mtop2 mbottom0">
			<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
		</p>
	</fr:form>
</logic:notEmpty>
</logic:notEmpty>
