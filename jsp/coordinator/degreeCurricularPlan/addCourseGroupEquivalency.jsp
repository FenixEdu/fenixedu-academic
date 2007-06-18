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

<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
	<p class="mtop2"><bean:message key="message.select.course.groups"/>:</p>
	<table class="mtop0 tdmiddle">
		<tr>
			<td>
				<fr:edit id="CourseGroupEquivalencePlanEntry.Creator"
						name="courseGroupEquivalencePlanEntryCreator"
						type="net.sourceforge.fenixedu.domain.CourseGroupEquivalencePlanEntry$CourseGroupEquivalencePlanEntryCreator"
						schema="CourseGroupEquivalencePlanEntry.Creator">
					<fr:layout name="tabular">
						<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        				<fr:property name="columnClasses" value=",,tdclear tderror1"/>
				    </fr:layout>
				</fr:edit>
			</td>
		</tr>
	</table>
	<p class="mtop2 mbottom0">
		<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
	</p>
</fr:form>
