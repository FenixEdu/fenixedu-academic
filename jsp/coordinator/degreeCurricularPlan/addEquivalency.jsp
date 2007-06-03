<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>

<br/>
<h2><bean:message key="title.equivalency.plan"/></h2>

<h3><bean:message key="title.equivalency.plan.for.degree"/> <bean:write name="degreeCurricularPlan" property="presentationName"/></h3>

<div class='simpleblock4'>
	<bean:message key="message.equivalency.table.from.degree.curricular.plan"/>
	<br/>
	<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
</div>

<h4>
	<bean:message key="message.equivalency.create.for.curricular.course"/>:
	<bean:write name="curricularCourse" property="name"/>
</h4>

<div class="dinline forminline">
	<fr:form action="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=prepareAddEquivalency&amp;degreeCurricularPlanID=" + degreeCurricularPlan.getIdInternal() %>">
		<fr:edit id="CurricularCourseEquivalencePlanEntry.Creator.addCurricularCourse" name="curricularCourseEquivalencePlanEntryCreator" type="net.sourceforge.fenixedu.domain.CurricularCourseEquivalencePlanEntry$CurricularCourseEquivalencePlanEntryCreator"
				schema="CurricularCourseEquivalencePlanEntry.Creator.addCurricularCourse">
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle5 thright thlight thmiddle dinline"/>
        		<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		   		<fr:hidden slot="equivalencePlan" name="equivalencePlan"/>
			   	<fr:hidden slot="curricularCourse" name="curricularCourse"/>
		    </fr:layout>
		</fr:edit>
		<html:submit><bean:message key="label.add" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</div>

<br/>
<br/>
<br/>

<bean:define id="curricularCourses" name="curricularCourseEquivalencePlanEntryCreator" property="curricularCourses"/>

<logic:notEmpty name="curricularCourses">

	<h4>
		<bean:message key="label.equivalency.to.create"/>:
	</h4>
	<logic:iterate id="curricularCourseFromList" indexId="i" name="curricularCourses">
		<logic:notEqual name="i" value="0">
			+
		</logic:notEqual>
		<bean:write name="curricularCourseFromList" property="name"/>
	</logic:iterate>
	==>
	<bean:write name="curricularCourse" property="name"/>	

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
		<br/>
		<html:submit><bean:message key="label.create" bundle="APPLICATION_RESOURCES"/></html:submit>
	</fr:form>
</logic:notEmpty>
