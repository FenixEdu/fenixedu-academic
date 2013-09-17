<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2><strong><bean:message key="link.masterDegree.administrativeOffice.gratuity.penaltyExemption" bundle="APPLICATION_RESOURCES" /></strong></h2>

<fr:edit name="gratuitySituation" schema="registration.gratuitySituation.editPenaltyExemption" >
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle4 thlight thright mtop025" />
		<fr:property name="columnClasses" value="width12em,,tdclear tderror1" />
	</fr:layout>
</fr:edit>