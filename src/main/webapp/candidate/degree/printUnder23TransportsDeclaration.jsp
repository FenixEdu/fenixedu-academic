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

<%@page import="org.fenixedu.commons.i18n.I18N"%>
<%@page import="org.joda.time.LocalDate"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

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
margin: 5px auto;
text-align: left;	
}
div.box p {
margin: 5px 0;
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


<div id="image" style="text-align: center;"><img src="<%= request.getContextPath() %>/images/blazon01.gif"/></div>
<h1 style="text-align: center;">PRESIDÊNCIA DO CONSELHO DE MINISTROS E MINISTÉRIOS DAS FINANÇAS E DA<br/> ADMINISTRAÇÃO PÚBLICA, DAS OBRAS PÚBLICAS, TRANSPORTES E COMUNICAÇÕES E<br/> DA CIÊNCIA, TECNOLOGIA E ENSINO SUPERIOR</h1>

<div id="content">
	
	<h2 style="text-align: center;">PASSE SUB23@SUPERIOR.TP</h2>
	<h2 style="text-align: center;">DECLARAÇÃO de INSCRIÇÃO</h2>
	
	<p style="margin-top: 20px;">Para efeitos de acesso ao passe sub23@superior.tp declara-se que o aluno</p>
	
	<div class="box">
		<p style="margin: 5px 0"><span class="label">Nome:</span> <span class="data"><bean:write name="person" property="name" /></span></p>
		<p style="margin: 5px 0"><span class="label">Cartão do Cidadão/B.I.</span> <span class="data"><bean:write name="person" property="documentIdNumber" /></span> <logic:notEmpty name="person" property="emissionDateOfDocumentIdYearMonthDay"><span class="label">emitido em</span> <span class="data"><bean:define id="emissionDate" name="person" property="emissionDateOfDocumentIdYearMonthDay" type="org.joda.time.YearMonthDay" /> <%= emissionDate.toString("dd/MM/yyyy") %></span></logic:notEmpty></p>
		<p style="margin: 5px 0"><span class="label">Data de nascimento:</span> <span class="data"><bean:define id="birthDate" name="person" property="dateOfBirthYearMonthDay" type="org.joda.time.YearMonthDay" /> <%= birthDate.toString("dd/MM/yyyy") %></span></p>
		<bean:define id="physicalAddress" name="person" property="defaultPhysicalAddress" />
		<p style="margin: 5px 0"><span class="label">Morada:</span> <span class="data"><bean:write name="physicalAddress" property="address" /></span></p>
		<p style="margin: 5px 0"><span class="label">Freguesia:</span> <span class="data"><bean:write name="physicalAddress" property="parishOfResidence" /></span> <span class="label">Concelho:</span> <span class="data"><bean:write name="physicalAddress" property="districtSubdivisionOfResidence" /></span></p>
		<p style="margin: 5px 0"><span class="label">Código Postal:</span> <span class="data"><bean:write name="physicalAddress" property="areaCode" /> <bean:write name="physicalAddress" property="areaOfAreaCode" /></span></p>
	</div>
	
	<p style="margin-top: 30px;">Está inscrito neste estabelecimento de ensino superior, no ano lectivo de <bean:write name="executionYear" property="qualifiedName" /> </p>
	
	<div class="box">
		<logic:equal name="campus" value="Alameda">
			<p style="margin: 5px 0"><span class="label">Estabelecimento de ensino:</span> <span class="data"><%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %></span> </p>
			<p style="margin: 5px 0"><span class="label">Morada das actividades lectivas</span> <span class="data">Avenida Rovisco Pais, I</span> </p>
			<p style="margin: 5px 0"><span class="label">Freguesia:</span> <span class="data">São João de Deus</span> <span class="label">Concelho:</span> <span class="data">Lisboa</span></p>
			<p style="margin: 5px 0"><span class="label">Código Postal:</span> <span class="data">1049-001 Lisboa</span></p>
			<p style="margin: 5px 0"><span class="label">Telefone:</span> <span class="data">21 841 70 00</span></p>
		</logic:equal>
		<logic:equal name="campus" value="Taguspark">
			<p style="margin: 5px 0"><span class="label">Estabelecimento de ensino:</span> <span class="data"><%= net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName().getContent() %></span></p>
			<p style="margin: 5px 0"><span class="label">Morada das actividades lectivas</span> <span class="data">Avenida Professor Cavaco Silva</span></p>
			<p style="margin: 5px 0"><span class="label">Freguesia:</span> <span class="data">Porto Salvo</span> <span class="label">Concelho:</span> <span class="data">Oeiras</span></p>
			<p style="margin: 5px 0"><span class="label">Código Postal:</span> <span class="data">2780-990 Oeiras</span></p>
			<p style="margin: 5px 0"><span class="label">Telefone:</span> <span class="data">21 423 32 00</span></p>
		</logic:equal>
	</div>
	
</div>

<br/>
<table>
	<tr>
		<td class="col1"><%= new LocalDate().toString("dd 'de' MMMM 'de' yyyy", I18N.getLocale()) %></td>
		<td class="col2">
			.........................................................................<br/>
			(assinatura e selo branco ou carimbo)
		</td>
	</tr>
</table>

</body>
</html>
