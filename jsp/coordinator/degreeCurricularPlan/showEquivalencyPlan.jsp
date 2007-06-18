<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<html:xhtml/>

<br/>
<h2><bean:message key="title.equivalency.plan"/></h2>

<logic:notPresent name="degreeCurricularPlan" property="equivalencePlan">
	<div class='simpleblock4'>
		<bean:message key="message.no.equivalency.table.exists"/>
		<br/>
		<bean:message key="label.create.equivalency.table.for.degree.curricular.plan"/>
		<fr:edit name="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan"
				schema="degreeCurricularPlan.createEquivalencyPlan">
		    <fr:layout>
	    	    <fr:property name="classes" value="thtop width8em"/>
	        	<fr:property name="columnClasses" value=",pbottom1,valigntop"/>
		    </fr:layout>
		</fr:edit>
	</div>
</logic:notPresent>
<logic:present name="degreeCurricularPlan" property="equivalencePlan">
	<p class="mvert15">
		<strong>
			<bean:message key="message.equivalency.table.from.degree.curricular.plan"/>
			<bean:write name="degreeCurricularPlan" property="equivalencePlan.sourceDegreeCurricularPlan.presentationName"/>
		</strong>
	</p>
</logic:present>

<logic:present name="degreeCurricularPlan" property="equivalencePlan">
	<bean:define id="degreeCurricularPlan" type="net.sourceforge.fenixedu.domain.DegreeCurricularPlan" name="degreeCurricularPlan"/>
	<bean:define id="equivalencePlan" type="net.sourceforge.fenixedu.domain.EquivalencePlan" name="degreeCurricularPlan" property="equivalencePlan"/>

	<logic:notPresent name="viewTable">
		<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showTable&amp;degreeCurricularPlanID="
				+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
				+ equivalencePlan.getIdInternal() %>">
			<bean:message key="link.equivalency.view.table"/>
		</html:link>
		<br/>
		<br/>
		<logic:present name="degreeCurricularPlan" property="root">
			<bean:define id="degreeModule" name="degreeCurricularPlan" property="root" toScope="request"/>
			<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
			<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
			<jsp:include page="showEquivalencyPlanForDegreeModule.jsp"/>
		</logic:present>
	</logic:notPresent>

	<logic:present name="viewTable">
		<html:link page="<%= "/degreeCurricularPlan/equivalencyPlan.do?method=showPlan&amp;degreeCurricularPlanID="
				+ degreeCurricularPlan.getIdInternal() + "&amp;equivalencePlanID="
				+ equivalencePlan.getIdInternal() %>">
			<bean:message key="link.equivalency.view.plan"/>
		</html:link>
		<br/>
		<br/>
		<logic:present name="curricularCourseEquivalencePlanEntries">
			<bean:define id="curricularCourseEntries" name="curricularCourseEquivalencePlanEntries" toScope="request"/>
			<jsp:include page="showEquivalencyPlanTable.jsp"/>
		</logic:present>
		<logic:present name="courseGroupEquivalencePlanEntries">
			<bean:define id="courseGroupEntries" name="courseGroupEquivalencePlanEntries" toScope="request"/>
			<jsp:include page="showEquivalencyPlanTable.jsp"/>
		</logic:present>
		<logic:notPresent name="curricularCourseEquivalencePlanEntries">
			<logic:notPresent name="courseGroupEquivalencePlanEntries">
				<bean:define id="curricularCourseEntries" name="degreeCurricularPlan" property="equivalencePlan.orderedCurricularCourseEntries" toScope="request"/>
				<bean:define id="courseGroupEntries" name="degreeCurricularPlan" property="equivalencePlan.orderedCourseGroupEntries" toScope="request"/>
				<jsp:include page="showEquivalencyPlanTable.jsp"/>
			</logic:notPresent>
		</logic:notPresent>
	</logic:present>
</logic:present>
