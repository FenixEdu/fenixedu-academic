<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<html:xhtml/>

<em><bean:message key="administrative.office.services" bundle="STUDENT_RESOURCES"/></em>
<h2><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.eventDetails" /></h2>

<%--
<fr:view name="person" schema="person.view-with-name-and-idDocumentType-and-documentIdNumber">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle2 thlight thright" />
	</fr:layout>
</fr:view>
--%>


<fr:view name="event" schema="AccountingEvent.view-with-amountToPay">
	<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thlight thright tdleftm" />
			<fr:property name="rowClasses" value=",,tdhl1" />
	</fr:layout>
</fr:view>

<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.viewDetails" /></strong></p>
<logic:notEmpty name="entryDTOs">
<fr:view name="entryDTOs" schema="entryDTO.view">
	<fr:layout name="tabular">
		<fr:property name="classes" value="tstyle1 thcenter thlight tdleft mtop025" />
		<fr:property name="columnClasses" value=",," />
	</fr:layout>
</fr:view>
</logic:notEmpty>


<p class="mtop1 mbottom025"><strong><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="label.payments.sibsPayments" /></strong></p>


<logic:notEmpty name="accountingEventPaymentCodes">
	<br/>
	Por questões técnicas o pagamento de propinas encontra-se suspenso temporariamente.
	<br/>
	Por estas questões, os alunos não serão penalizados por não ser cumprido o prazo de pagamento desta 1ª prestação.
	<br/>

	<br/>
	For technical reasons tuition payment is temporarily suspended.
	<br/>
	For this reason, students will not be penalized for not complying with the payment schedule of the 1st installment.
	<br/>
<%--
	<fr:view name="accountingEventPaymentCodes" schema="AccountingEventPaymentCode.view">
		<fr:layout name="tabular">
			<fr:property name="classes" value="tstyle1 thcenter tdcenter thlight mtop025" />
			<fr:property name="columnClasses" value=",," />
		</fr:layout>
	</fr:view>
 --%>
</logic:notEmpty>

<html:form action="/payments.do?method=showEvents">
	<p class="mtop1">
		<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton"><bean:message bundle="ACADEMIC_OFFICE_RESOURCES" key="button.payments.back"/></html:submit>
	</p>
</html:form>

