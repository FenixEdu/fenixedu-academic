<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em class="invisible"><bean:message key="DIRECTIVE_COUNCIL" /></em>
<h2><bean:message key="link.assiduousnessStructure" /></h2>

<p><span class="error0"><html:messages id="message" message="true">
	<bean:write name="message" />
</html:messages></span></p>

<logic:present name="assiduousnessPersonFunctionFactory">
	<logic:equal name="assiduousnessPersonFunctionFactory" property="byPersons" value="true">
		<fr:view name="assiduousnessPersonFunctionFactory" schema="show.assiduousnessPersonFunctionResponsible"/>
		<bean:size id="employeesNumber" name="assiduousnessPersonFunctionFactory"  property="party.allCurrentNonTeacherEmployees"/>
		<logic:equal name="employeesNumber" value="0">
			<bean:message key="message.noEmployees"/>	
		</logic:equal>
		<logic:notEqual name="employeesNumber" value="0">
			<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory"
				schema="create.assiduousnessPersonFunctionFactory.employees"
				action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
			</fr:edit>
		</logic:notEqual>
	</logic:equal>
	<logic:equal name="assiduousnessPersonFunctionFactory" property="byPersons" value="false">
		<fr:view name="assiduousnessPersonFunctionFactory" schema="show.assiduousnessPersonFunctionParties"/>
		<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory"
			schema="create.assiduousnessPersonFunctionFactory"
			action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
		</fr:edit>
	</logic:equal>
</logic:present>
