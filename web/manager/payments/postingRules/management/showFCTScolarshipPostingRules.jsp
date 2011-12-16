<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<logic:present role="MANAGER">

	<h2><bean:message key="label.payments.postingRules.degreeCurricularPlan.rulesFor"
		bundle="MANAGER_RESOURCES" /> Bolsas da FCT</h2>
	<br />

	<html:link
		action="/phdPostingRules.do?method=prepareAddFCTPostingRule">
		<bean:message key="label.create.fct.scolarship.pr" bundle="MANAGER_RESOURCES"/>
	</html:link>
	<br/>
	<fr:view name="list" >
		<fr:schema bundle="MANAGER_RESOURCES" type="net.sourceforge.fenixedu.domain.phd.debts.FctScholarshipPhdGratuityContribuitionPR">
			<fr:slot name="startDate"></fr:slot>
			<fr:slot name="endDate"></fr:slot>
		</fr:schema>
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle4 thlight thcenter mtop05" />
			<fr:property name="linkFormat(edit)" value="/postingRules.do?method=prepareEditFCTScolarshipPostingRule&amp;postingRule=${externalId}" />
			<fr:property name="key(edit)" value="label.edit" />
			<fr:property name="bundle(edit)" value="APPLICATION_RESOURCES" />
			
			<fr:property name="linkFormat(delete)" value="/postingRules.do?method=deleteFCTScolarshipPostingRule&amp;postingRule=${externalId}" />
			<fr:property name="key(delete)" value="label.delete" />
			<fr:property name="bundle(delete)" value="APPLICATION_RESOURCES" />
		</fr:layout>
	</fr:view>

</logic:present>