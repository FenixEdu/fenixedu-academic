<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<h2><bean:message key="label.edit.fct.scolarship.pr" bundle="MANAGER_RESOURCES"/></h2>
<br />

<fr:edit id="bean" name="bean" action="/phdPostingRules.do?method=editFCTScolarshipPostingRule">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle5 thlight thright" />
		<fr:property name="columnClasses" value=",,tdclear tderror1" />
	</fr:layout>
	<fr:schema bundle="MANAGER_RESOURCES"
		type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.PostingRulesManagementDA$FctScolarshipPostingRuleBean">
		<fr:slot name="startDate" key="label.phd.gratuity.startDate"
			required="true" />
		<fr:slot name="endDate" key="label.phd.gratuity.endDate" />
	</fr:schema>
</fr:edit>

