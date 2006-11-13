<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>


<logic:iterate id="gratuityLetterDTO" name="gratuityLetterDTOs">

<img src="<%= request.getContextPath() %>/images/LogoIST.gif" alt="<bean:message key="LogoIST" bundle="IMAGE_RESOURCES" />" border="0" /> 

<div style="margin-left: 25em; margin-top: -5em; font-weight: bold; line-height: 125%;">
	<p class="mvert025">
		<em><bean:message key="label.payments.printTemplates.gratuityLetter.to" bundle="ACADEMIC_OFFICE_RESOURCES" /></em><br/>
		<em><bean:write name="gratuityLetterDTO" property="person.name"/></em>
	</p>
	
	<p class="mtop1 mbottom025">
		<em><bean:write name="gratuityLetterDTO" property="person.address"/></em><br/>
		<em><bean:write name="gratuityLetterDTO" property="person.area"/></em><br/>
		<em>
			<bean:write name="gratuityLetterDTO" property="person.areaCode"/> 
			<bean:write name="gratuityLetterDTO" property="person.areaOfAreaCode"/>
		</em>
	</p>
</div>

<bean:define id="year" name="gratuityLetterDTO" property="executionYear.year" />
<h2 class="center" style="margin-top: 4em;"><bean:message key="label.payments.printTemplates.gratuityLetter.title" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%=year.toString()%>"/></h2>


<!-- Price Information -->
<table class="tb02 tbcenter">
	<tr>
		<td>
			<strong><bean:message key="label.payments.printTemplates.gratuityLetter.administrativeOfficeFee" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
			- <bean:write name="gratuityLetterDTO" property="administrativeOfficeFeeAmount.amountAsString"/> <bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</td>
		<td>
			<strong><bean:message key="label.payments.printTemplates.gratuityLetter.insurance" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
			- <bean:write name="gratuityLetterDTO" property="insuranceAmount.amountAsString"/> <bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</td>
		
		<logic:equal name="gratuityLetterDTO" property="anyGratuityDebtAvailable" value="true">
		<td>
			<strong><bean:message key="label.payments.printTemplates.gratuityLetter.grautity" bundle="ACADEMIC_OFFICE_RESOURCES"/> 
			- <bean:write name="gratuityLetterDTO" property="gratuityTotalAmout.amountAsString"/> <bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong>
		</td>
		</logic:equal>
	</tr>
</table>

<!-- End of Price Information -->

<div style="line-height: 125%;">

	<bean:define id="penalty" name="gratuityLetterDTO" property="administrativeOfficeFeePenalty.amountAsString"/>
	
	<p style="text-align: justify;"><bean:message key="label.payments.printTemplates.gratuityLetter.body" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= penalty.toString() %>" /></p>
	
	<h3 class="mvert0" style="text-decoration: underline;"><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode" bundle="ACADEMIC_OFFICE_RESOURCES"/></h3>
	<p class="mvert0" style="text-align: justify;"><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.body" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
	<p class="mbottom05" style="text-align: justify;"><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.instructions" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
	
	
	<ul class="mtop05">
	<li><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.instructions.first" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
	<li><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.instructions.second" bundle="ACADEMIC_OFFICE_RESOURCES"/></li>
	</ul>
</div>


<!-- Insurance and Administrative Office Fee -->
<logic:equal name="gratuityLetterDTO" property="administrativeOfficeAndInsuranceFeeDebtAvailable" value="true">
<table class="tb02 tbcenter mtop2 center">
	<tr>
		<td colspan="2"><strong><bean:message key="label.payments.printTemplates.gratuityLetter.administrativeOfficeFeeAndInsurance" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></td>
	</tr>
	<tr>
		<td colspan="2">
		<bean:define id="administrativeOfficeFeePaymentLimitDate" name="gratuityLetterDTO" property="administrativeOfficeAndInsuranceFeeDebt.limitDate" type="org.joda.time.YearMonthDay"/>
		<bean:message key="label.payments.printTemplates.gratuityLetter.until" bundle="ACADEMIC_OFFICE_RESOURCES"/> <%= administrativeOfficeFeePaymentLimitDate.toString("dd/MM/yyyy") %> (*)
		</td>
	</tr>
	<tr>
		<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.entity" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
		<td><strong><bean:write name="gratuityLetterDTO" property="entityCode" /></strong></td>
	</tr>
	<tr>
		<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.paymentCode" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
		<td><strong><bean:write name="gratuityLetterDTO" property="administrativeOfficeAndInsuranceFeeDebt.code"/></strong></td>
	</tr>
	<tr>
		<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.amount" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
		<td><strong><bean:write name="gratuityLetterDTO" property="administrativeOfficeAndInsuranceFeeDebt.amount.amountAsString"/><bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></td>
	</tr>
</table>
</logic:equal>

<!-- End of Insurance and Administrative Office Fee -->


<!-- Total Gratuity | Installment 1..N -->
<logic:equal name="gratuityLetterDTO" property="anyGratuityDebtAvailable" value="true">
<table class="tb02 tbcenter center" style="margin-top: 1em;">

<tr>
	<td class="tdclearltb"></td>
	<logic:equal name="gratuityLetterDTO" property="totalGratuityDebtAvailable" value="true">
		<td><em><strong><bean:message key="label.payments.printTemplates.gratuityLetter.totalGratuity" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></em></td>
	</logic:equal>
	<bean:size id="colspan" name="gratuityLetterDTO" property="gratuityDebtInstallments" />
	<td colspan="<%= colspan %>"><em><strong><bean:message key="label.payments.printTemplates.gratuityLetter.installmentsGratuity" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></em></td>
</tr>


<tr>
	<td class="tdclearlt"></td>
	<logic:equal name="gratuityLetterDTO" property="totalGratuityDebtAvailable" value="true">
		<td>
			<bean:message key="label.payments.printTemplates.gratuityLetter.until" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<bean:define id="endDateOfTotal" name="gratuityLetterDTO" property="totalGratuityDebt.limitDate" type="org.joda.time.YearMonthDay" /> <%= endDateOfTotal.toString("dd/MM/yyyy") %>
		</td>
	</logic:equal>
	<logic:iterate id="installmentDebt" name="gratuityLetterDTO" property="gratuityDebtInstallments">
		<td>
			<bean:message key="label.payments.printTemplates.gratuityLetter.until" bundle="ACADEMIC_OFFICE_RESOURCES"/>
			<bean:define id="endDateOfInstallment" name="installmentDebt" property="limitDate" type="org.joda.time.YearMonthDay" /> <%= endDateOfInstallment.toString("dd/MM/yyyy") %> (**)
		</td>
	</logic:iterate>
</tr>

<tr>
	<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.entity" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
	<logic:equal name="gratuityLetterDTO" property="totalGratuityDebtAvailable" value="true">
		<td><strong><bean:write name="gratuityLetterDTO" property="entityCode" /></strong></td>
	</logic:equal>
	<logic:iterate id="installmentDebt" name="gratuityLetterDTO" property="gratuityDebtInstallments">
		<td><strong><bean:write name="gratuityLetterDTO" property="entityCode" /></strong></td>
	</logic:iterate>
</tr>

<tr>
	<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.paymentCode" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
	<logic:equal name="gratuityLetterDTO" property="totalGratuityDebtAvailable" value="true">
		<td><strong><bean:write name="gratuityLetterDTO" property="totalGratuityDebt.code" /></strong></td>
	</logic:equal>
	<logic:iterate id="installmentDebt" name="gratuityLetterDTO" property="gratuityDebtInstallments">
		<td><strong><bean:write name="installmentDebt" property="code" /></strong></td>
	</logic:iterate>
</tr>
<tr>
	<td><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.amount" bundle="ACADEMIC_OFFICE_RESOURCES"/></td>
	<logic:equal name="gratuityLetterDTO" property="totalGratuityDebtAvailable" value="true">
		<td><strong><bean:write name="gratuityLetterDTO" property="totalGratuityDebt.amount.amountAsString" /> <bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></td>
	</logic:equal>
	<logic:iterate id="installmentDebt" name="gratuityLetterDTO" property="gratuityDebtInstallments">
		<td><strong><bean:write name="installmentDebt" property="amount.amountAsString" /> <bean:message key="label.currencySymbol" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></td>
	</logic:iterate>
</tr>
</table>
</logic:equal>

<!-- End of Total | Installment 1..N -->


<!-- Footer -->
<div style="line-height: 125%;">
	<ul>
	<li><strong><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.instructions.third" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></li>
	<li><strong><bean:message key="label.payments.printTemplates.gratuityLetter.paymentMode.instructions.fourth" bundle="ACADEMIC_OFFICE_RESOURCES"/></strong></li>
	</ul>
	
	<hr style="margin-top: 2em;"/>
	
	<logic:equal name="gratuityLetterDTO" property="administrativeOfficeAndInsuranceFeeDebtAvailable" value="true" >
		<p class="mvert0">(*)&nbsp;&nbsp;&nbsp;<bean:message key="label.payments.printTemplates.gratuityLetter.footer.first.note" bundle="ACADEMIC_OFFICE_RESOURCES" arg0="<%= penalty.toString() %>" /></p>
	</logic:equal>
	<logic:equal name="gratuityLetterDTO" property="anyGratuityDebtAvailable" value="true">
		<p class="mvert0">(**)&nbsp;<bean:message key="label.payments.printTemplates.gratuityLetter.footer.second.note" bundle="ACADEMIC_OFFICE_RESOURCES"/></p>
	</logic:equal>
</div>	

<!-- End of Footer -->

<div class="break-before"></div>

</logic:iterate>