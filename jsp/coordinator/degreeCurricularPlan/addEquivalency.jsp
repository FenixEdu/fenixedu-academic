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

<p class="mtop2"><bean:message key="message.equivalency.create.for.curricular.course"/>: <strong class="highlight5"><bean:write name="curricularCourse" property="name"/></strong></p>

<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
<table class="mtop0 tdmiddle">
	<tr>
	<td>
		<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.addCurricularCourse" name="curricularCourseEquivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
				schema="CurricularCourseEquivalencePlanEntry.Creator.addCurricularCourse">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		   		<fr:hidden slot="equivalencePlan" name="equivalencePlan"/>
			   	<fr:hidden slot="curricularCourse" name="curricularCourse"/>
		    </fr:layout>
		</fr:edit>
		</td>
		<td>
			<html:submit><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:submit>
		</td>
	</tr>
</table>
</fr:form>


<bean:define id="curricularCourses" name="curricularCourseEquivalencePlanEntryCreator" property="curricularCourses"/>

<logic:notEmpty name="curricularCourses">

	<p class="mtop2"><bean:message key="label.equivalency.to.create"/></p>	
	<div style="background: #fafafa; border: 2px solid #eee; padding: 1em; color: #555;">
		<logic:iterate id="curricularCourseFromList" indexId="i" name="curricularCourses">
			<span style="border-bottom: 1px dotted #aaa;">
				<logic:notEqual name="i" value="0">
					+
				</logic:notEqual>
				<bean:write name="curricularCourseFromList" property="name"/>
			</span>
		</logic:iterate>
		==>
		<span style="border-bottom: 1px dotted #aaa;">
			<bean:write name="curricularCourse" property="name"/>		
		</span>
	</div>

	<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
		<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.create" name="curricularCourseEquivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
				schema="CurricularCourseEquivalencePlanEntry.Creator.create">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		   		<fr:hidden slot="equivalencePlan" name="equivalencePlan"/>
			   	<fr:hidden slot="curricularCourse" name="curricularCourse"/>
		    </fr:layout>
		</fr:edit>
		<p class="mtop2 mbottom0">
			<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
		</p>
	</fr:form>
</logic:notEmpty>
