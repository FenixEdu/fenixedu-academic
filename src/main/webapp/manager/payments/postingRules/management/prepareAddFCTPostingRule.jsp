<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><bean:message key="label.create.fct.scolarship.pr" bundle="MANAGER_RESOURCES"/></h2>
<br />

<fr:edit id="bean" name="bean" action="/phdPostingRules.do?method=addFCTScolarshipPostingRule">
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
