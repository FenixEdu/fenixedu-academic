<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr" %>

<h2><bean:message key="label.situations" bundle="CONTRACTS_RESOURCES"/></h2>

<logic:present name="contractSituations">
	<fr:edit name="contractSituations">
		<fr:schema type="net.sourceforge.fenixedu.domain.personnelSection.contracts.ContractSituation" bundle="CONTRACTS_RESOURCES">
			<fr:slot readOnly="true" name="giafId" key="label.giafId" layout="null-as-label"/>
			<fr:slot readOnly="true" name="name" key="label.situation" layout="null-as-label"/>
			<fr:slot readOnly="true" name="endSituation" key="label.endSituation" layout="null-as-label"/>
			<fr:slot name="serviceExemption" key="label.serviceExemption" layout="null-as-label"/>
			<fr:slot name="mustHaveAssociatedExemption" key="label.mustHaveAssociatedExemption" layout="null-as-label"/>
			<fr:slot name="hasMandatoryCredits" key="label.hasMandatoryCredits" layout="null-as-label"/>
			<fr:slot name="giveCredits" key="label.giveCredits" layout="null-as-label"/>
			<fr:slot name="inExercise" key="label.inExercise" layout="null-as-label"/>
		</fr:schema>
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle2 thlight mtop15" />
			<fr:property name="sortBy" value="giafId"/>
		</fr:layout>
	</fr:edit>
</logic:present>
