<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="SCIENTIFIC_COUNCIL">

	<span class="error"><html:errors/></span>
	<html:messages id="message" message="true">
		<span class="error">
			<bean:write name="message"/>
		</span>
	</html:messages>

	<h2><bean:message key="label.edit.credits.period"/></h2>

	<h3 class="mtop2 mbottom05"><bean:message key="label.teacher"/></h3>

	<bean:define id="actionName">
		/defineCreditsPeriods.do?method=beforeShowPeriods&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
	</bean:define>
	
	<fr:edit name="executionPeriod" schema="teacher.credits.period.view" action="<%= actionName %>" service="EditCreditsPeriods">
		<fr:layout>
			<fr:property name="classes" value="mtop0 mbottom1 thlight"/>
			<fr:property name="validatorClasses" value="error0"/>
		</fr:layout>
		<fr:destination name="cancel" path="/defineCreditsPeriods.do?method=beforeShowPeriods"/>
	</fr:edit>
		
</logic:present>