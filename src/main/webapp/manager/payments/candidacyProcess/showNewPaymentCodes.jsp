<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">


	<h2>Referências criadas</h2>
	
	<fr:view name="newPaymentCodes">
	
		<fr:schema type="net.sourceforge.fenixedu.domain.accounting.paymentCodes.IndividualCandidacyPaymentCode" bundle="MANAGER_RESOURCES" >
			<fr:slot name="code" />
			<fr:slot name="startDate" />
			<fr:slot name="endDate" />
			<fr:slot name="minAmount" />
			<fr:slot name="maxAmount" />
			<fr:slot name="type" />
		</fr:schema>
		
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1" />
		</fr:layout>
	</fr:view>

</logic:present>