<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
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