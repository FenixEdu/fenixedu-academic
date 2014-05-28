<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.domain.accounting.installments.InstallmentForFirstTimeStudents" %>


<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style>
body {
font-size: 76%;
margin-top: 20px;
text-align: center;
line-height: 1.7em;
}
h1 {
font-family: Helvetica, Arial, sans-serif;
font-size: 13px;
}
div#content {
margin-top: 50px;
margin-left: 0px;
font-family: Georgia, serif;
}
div#content h2 {
font-size: 14px;
}
div.box {
border: 1px solid #000;
width: 610px;
margin: 0 auto;
padding: 5px 10px;
}
p {
width: 610px;
margin: 10px auto;
text-align: left;	
}
div.box p {
margin: 10px 0;
font-size: 12px;
font-family: Courier New, monospace;
}
div.box p span.label {
color: #555;
}
div.box p span.data {
font-weight: bold;
}
table {
font-family: Georgia, serif;
margin: 20px auto;
width: 610px;
}
table td {
vertical-align: top;
}
table td.col1 {
text-align: left;
}
table td.col2 {
padding-top: 10px;
text-align: center;
width: 1%;
}
</style>

</head>



<body>

<div id="content">
	
	<h2 style="text-align: center;">REFERÊNCIAS MULTIBANCO PARA PAGAMENTO DAS PROPINAS</h2>
	
	<p style="margin-top: 20px;">A data limite da primeira prestação e a totalidade da propina são de 10 dias a partir da data de inicio da matricula. Após a data limite é cobrado 1% sobre a propina da 1º prestação. Deve optar entre o pagamento da propina na totalidade ou pagamento em três prestações.</p>
	
	<p style="margin-top: 20px;">Taxa de secretaria/Seguro Escolar</p>
	
	<div class="box">
		<logic:present name="administrativeOfficeFeeAndInsurancePaymentCode">
			<bean:define id="administrativeOfficeFeeAndInsurancePaymentCode" name="administrativeOfficeFeeAndInsurancePaymentCode" />
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Entidade: </span> <span class="data"><bean:write name="sibsEntityCode" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Referência: </span> <span class="data"><bean:write name="administrativeOfficeFeeAndInsurancePaymentCode" property="code" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Data limite:</span> <span class="data"> <bean:write name="administrativeOfficeFeeAndInsurancePaymentCode" property="endDate" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Valor:</span> <span class="data"><bean:write name="administrativeOfficeFeeAndInsurancePaymentCode" property="minAmount" /></span></p>
		</logic:present>
	</div>
	
	<logic:present name="firstInstallmentEndDate">
		<bean:define id="firstInstallmentEndDate" name="firstInstallmentEndDate" />
	</logic:present>
	<logic:notPresent name="firstInstallmentEndDate">
		<bean:define id="firstInstallmentEndDate" value="" />
	</logic:notPresent>
	
	<p style="margin-top: 20px;">Propina na totalidade</p>
	<div class="box">
		<logic:present name="totalGratuityPaymentCode">
			<bean:define id="totalGratuityPaymentCode" name="totalGratuityPaymentCode" />
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Entidade: </span> <span class="data"><bean:write name="sibsEntityCode" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Referência: </span> <span class="data"><bean:write name="totalGratuityPaymentCode" property="code" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Data limite:</span> <span class="data"> <bean:write name="firstInstallmentEndDate" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Valor:</span> <span class="data"><bean:write name="totalGratuityPaymentCode" property="minAmount" /></span></p>
		</logic:present>
	</div>

	<p style="margin-top: 20px;">Propina em prestações</p>
	<div class="box">
		<table>
			<tr>
		<logic:iterate id="paymentCode" name="installmentPaymentCodes" indexId="i" type="net.sourceforge.fenixedu.domain.accounting.PaymentCode">
			<% final String style = i > 0 ? "padding-left: 20px; border-left-color: black; border-left-width: thin; border-left-style: solid;" : ""; %>
				<td style="<%= style %>">
			<p style="margin: 0.15em 0; width: auto;"><span class="label"><%= (i + 1)  + "º prestação" %></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Entidade: </span> <span class="data"><bean:write name="sibsEntityCode" /></span></p>
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Referência: </span> <span class="data"><bean:write name="paymentCode" property="code" /></span></p>
			
			<logic:equal name="i" value="0">
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Data limite:</span> <span class="data"> <bean:write name="firstInstallmentEndDate" /></span></p>
			</logic:equal>
			
			<logic:greaterThan name="i" value="0">
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Data limite:</span> <span class="data"> <bean:write name="paymentCode" property="endDate" /></span></p>
			</logic:greaterThan>
			
			<p style="margin: 0.15em 0; width: auto;"><span class="label">Valor:</span> <span class="data"><bean:write name="paymentCode" property="minAmount" /></span></p>
				</td>
		</logic:iterate>
			</tr>
		</table>
	</div>
</div>

</body>
