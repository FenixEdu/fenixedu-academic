<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
	
	<p style="margin-top: 20px;">A data limite da primeira prestação e a totalidade da propina são de 10 dias a partir da data de inicio da matricula. Após a data limite é cobrado 1% sobre a propina da 1º prestação.</p>
	
	<logic:iterate id="paymentCode" name="paymentCodes" indexId="i" ></logic:iterate>
		<div class="box">
			<bean:define id ="firstInstallmentEndDate" name="firstInstallmentEndDate" type="org.joda.time.YearMonthDay"/> 
			
			<p><span class="label">Entidade:</span> <span class="data"><bean:write name="sibsEntityCode" /></span></p>
			<p><span class="label">Referência: </span> <span class="data"><bean:write name="paymentCode" property="code" /></span></p>
			
			<logic:equal name="paymentCode" property="class.name" value="net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode" >
			<logic:equal name="i" value="0" >
				<p><span class="label">Data limite:</span> <span class="data"> <%= firstInstallmentEndDate.toString("dd/MM/yyyy") %> </span></p>
			</logic:equal>
			</logic:equal>
			
			<logic:equal name="paymentCode" property="class.name" value="net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode" >
				<p><span class="label">Data limite:</span> <span class="data"> <%= firstInstallmentEndDate.toString("dd/MM/yyyy") %> </span></p>
			</logic:equal>
			
			<logic:equal name="paymentCode" property="class.name" value="net.sourceforge.fenixedu.domain.accounting.paymentCodes.InstallmentPaymentCode" >
			<logic:greaterThan value="i" value="0">
				<p><span class="label">Data limite:</span> <span class="data"> <bean:write name="paymentCode" property="endDate" /> </span></p>
			</logic:greaterThan>
			</logic:equal>

			<p><span class="label">Valor:</span> <span class="data"><bean:write name="paymentCode" property="minAmount" /></span></p>
		</div>
</div>