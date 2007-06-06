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
	<logic:present name="degreeCurricularPlan" property="root">
		<bean:define id="degreeModule" name="degreeCurricularPlan" property="root" toScope="request"/>
		<bean:define id="indentLevel" type="java.lang.String" value="0" toScope="request"/>
		<bean:define id="width" type="java.lang.String" value="70" toScope="request"/>
		<jsp:include page="showEquivalencyPlanForDegreeModule.jsp"/>
	</logic:present>
</logic:present>