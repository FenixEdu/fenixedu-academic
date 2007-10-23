<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em class="invisible"><bean:message key="DIRECTIVE_COUNCIL" /></em>
<h2><bean:message key="link.assiduousnessStructure" /></h2>

<p><span class="error0"><html:messages id="message"
	message="true">
	<bean:write name="message" />
</html:messages></span></p>

<logic:present name="assiduousnessPersonFunctionFactory">
	<fr:view name="assiduousnessPersonFunctionFactory"
		schema="show.assiduousnessPersonFunctionParties"/>
	<fr:edit id="assiduousnessPersonFunctionFactory" name="assiduousnessPersonFunctionFactory"
		schema="create.assiduousnessPersonFunctionFactory"
		action="/assiduousnessStructure.do?method=createAssiduousnessPersonFunction">
	</fr:edit>
</logic:present>
