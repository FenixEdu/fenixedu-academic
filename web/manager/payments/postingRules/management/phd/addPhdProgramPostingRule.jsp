<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<html:xhtml />



	<bean:define id="phdProgramId" name="phdProgram"
		property="externalId" />

<logic:present role="MANAGER">
	<h2><bean:message key="label.payments.postingRules.addGratuityPostingRule" bundle="MANAGER_RESOURCES" /></h2>
	
	<span class="error"><!-- Error messages go here --><html:errors bundle="MANAGER_RESOURCES" /></span>
	
	<br/>
<fr:form action="/phdPostingRules.do">
	<fr:edit id="bean" name="bean" schema="addGratuityPhdPostingRule">
			<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle5 thlight thright" />
			<fr:property name="columnClasses" value=",,tdclear tderror1" />
		</fr:layout>
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PhdPostingRulesManagementDA$CreateGratuityPhdBean">
			<fr:slot name="startDate" key="label.phd.gratuity.startDate" required="true" />
			<fr:slot name="endDate"   key="label.phd.gratuity.endDate" />
			<fr:slot name="gratuity" key="label.phd.gratuity.value" required="true" />
			<fr:slot name="fineRate" key="label.phd.gratuity.fine" required="true" />
		</fr:schema>
	</fr:edit>
	<html:hidden property="method" value="addGratuityPhdPostingRule"/>
	<html:hidden property="phdProgramId" value="<%= phdProgramId.toString() %>"/>
	<p>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit">
		<bean:message bundle="APPLICATION_RESOURCES" key="label.submit"/>
	</html:submit>

	</p>
</fr:form>
</logic:present>