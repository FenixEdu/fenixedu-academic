<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<em class="invisible"><bean:message key="DIRECTIVE_COUNCIL" /></em>
<h2><bean:message key="link.assiduousnessStructure" /></h2>

<p class="mtop15">
	<span class="error0">
		<html:messages id="message" message="true">
			<bean:write name="message" />
		</html:messages>
	</span>
</p>

<logic:present name="personFunction">
	<bean:define id="personFunctionId" name="personFunction" property="idInternal" />
	<fr:edit id="personFunction" name="personFunction" schema="edit.assiduousnessPersonFunction" action="<%="/assiduousnessStructure.do?method=showAssiduousnessStructure&personFunctionId="+personFunctionId%>">
		<fr:layout>
			<fr:property name="classes" value="tstyle5 thlight thright"/>
	        <fr:property name="columnClasses" value=",,tderror1 tdclear"/>
		</fr:layout>
	</fr:edit>
</logic:present>
