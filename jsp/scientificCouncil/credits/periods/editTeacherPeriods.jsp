<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<logic:present role="SCIENTIFIC_COUNCIL">

	<em><bean:message key="title.teaching"/></em>
	<h2><bean:message key="label.edit.credits.period"/></h2>

	<p><span class="error"><!-- Error messages go here --><html:errors /></span></p>
	<html:messages id="message" message="true">
		<p>
			<span class="error"><!-- Error messages go here -->
				<bean:write name="message"/>
			</span>
		</p>
	</html:messages>

	<h3 class="mtop15 mbottom05"><bean:message key="label.teacher"/></h3>

	<bean:define id="actionName">
		/defineCreditsPeriods.do?method=beforeShowPeriods&amp;executionPeriodId=<bean:write name="executionPeriod" property="idInternal"/>
	</bean:define>
	
	<fr:edit name="executionPeriod" schema="teacher.credits.period.view" action="<%= actionName %>">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright thmiddle mtop05"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
		<fr:destination name="cancel" path="/defineCreditsPeriods.do?method=beforeShowPeriods"/>
	</fr:edit>
		
</logic:present>