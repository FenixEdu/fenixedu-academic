<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<logic:present role="MANAGER">
	
	<bean:define id="bean" name="bean" />
	<bean:define id="type" name="bean" property="type" />
	
	<h2>Criar referências SIBS para <bean:write name="bean" property="type.localizedName" /> </h2>
	
	<fr:form action="/candidacyPaymentCodes.do?method=createPaymentCodes">
		<fr:edit id="bean" name="bean" visible="false" />
				
		<fr:edit id="bean-edit" name="bean">
			<fr:schema type="net.sourceforge.fenixedu.presentationTier.Action.manager.payments.CandidacyProcessPaymentCodeBean" bundle="MANAGER_RESOURCES">
				<fr:slot name="beginDate" required="true" />
				<fr:slot name="endDate" required="true" />
				<fr:slot name="numberOfPaymentCodes" required="true" />
			</fr:schema>
			
			<fr:layout name="tabular">
				<fr:property name="classes" value="tstyle1" />
				<fr:property name="columnClasses" value=",,tdclear tderror" />
			</fr:layout>
			
			<fr:destination name="invalid" path="<%= "/candidacyPaymentCodes.do?method=createPaymentCodesInvalid&type=" + type %>" />			
			<fr:destination name="cancel" path="/candidacyPaymentCodes.do?method=index" />
		</fr:edit>
	
		<p>
			<html:submit>Criar</html:submit>
			<html:submit>Cancelar</html:submit>
		</p>
	</fr:form>
	
</logic:present>