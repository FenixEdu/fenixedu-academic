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
	<p class="mtop2"><bean:message key="message.origin.curricular.course"/>:</p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.addOriginCurricularCourse"
						name="curricularCourseEquivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
						schema="CurricularCourseEquivalencePlanEntry.Creator.addOriginCurricularCourse">
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
	<p class="mtop2"><bean:message key="message.destination.curricular.course"/>:</p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.addDestinationCurricularCourse"
						name="curricularCourseEquivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
						schema="CurricularCourseEquivalencePlanEntry.Creator.addDestinationCurricularCourse">
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
	<p class="mtop2"><bean:message key="message.set.destination.curricular.course.operator"/>:</p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.setDestinationCurricularCourseOperator"
						name="curricularCourseEquivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
						schema="CurricularCourseEquivalencePlanEntry.Creator.setDestinationCurricularCourseOperator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</td>
			<td>
				<html:submit><bean:message key="label.set" bundle="APPLICATION_RESOURCES"/></html:submit>
			</td>
		</tr>
	</table>
</fr:form>


<logic:notEmpty name="curricularCourseEquivalencePlanEntryCreator" property="originCurricularCourses">
<logic:notEmpty name="curricularCourseEquivalencePlanEntryCreator" property="destinationCurricularCourses">

	<p class="mtop2"><bean:message key="label.equivalency.to.create"/></p>	
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:iterate id="curricularCourseFromList1" indexId="i1" name="curricularCourseEquivalencePlanEntryCreator" property="originCurricularCourses">
			<span style="border-bottom: 1px dotted #aaa;">
				<logic:notEqual name="i1" value="0">
					<strong>
						<bean:message key="AND" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<bean:write name="curricularCourseFromList1" property="name"/>
			</span>
		</logic:iterate>
		==>
		<logic:iterate id="curricularCourseFromList2" indexId="i2" name="curricularCourseEquivalencePlanEntryCreator" property="destinationCurricularCourses">
			<span style="border-bottom: 1px dotted #aaa;">
				<logic:notEqual name="i2" value="0">
					<strong>
						<bean:message name="curricularCourseEquivalencePlanEntryCreator" property="logicOperator.name" bundle="ENUMERATION_RESOURCES"/>
					</strong>
				</logic:notEqual>
				<bean:write name="curricularCourseFromList2" property="name"/>
			</span>
		</logic:iterate>
	</div>

	<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
		<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.create" name="curricularCourseEquivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
				schema="CurricularCourseEquivalencePlanEntry.Creator.create">
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
