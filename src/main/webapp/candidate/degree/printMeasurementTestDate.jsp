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
<%@page import="org.joda.time.LocalDate"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom"%>
<%@page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

<style>
body {
margin-top: 20px;
line-height: 1.7em;
}
div#measurementTestContent {
margin-top: 50px;
margin-left: 0px;
}
</style>

</head>



<body>

<div id="measurementTestContent">
	<div style="text-align: center;"><span style="text-decoration: underline;">PROVA DE AFERIÇÃO</span></div>
	<br/><br/>
	
	<div style="text-align: justify;font-size: 95%;">
	
		<p>
		Caro/a  aluno/a <bean:write name="registration" property="student.name"/>, <bean:write name="registration" property="student.number"/>, 
		</p>
		
		<p>
		O <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> em associação com a Sociedade Portuguesa de Matemática promove 
		este ano lectivo uma prova de aferição para todos os alunos ingressados no
		1º ano. Esta prova é obrigatória e terá influência na classificação da 
		cadeira de Cálculo Diferencial e Integral I ou Matemática I.
		</p>
		
		<p>
		
		
		A prova realizar-se-á no dia <%= ((Registration)request.getAttribute("registration")).getMeasurementTestRoom().getShift().getDate().toString("dd 'de' MMMM", I18N.getLocale())  %> e terá uma duração aproximada de 3 horas, pelo que, neste dia, serão suspensas as aulas do 1º ano. 
		</p>
		
		<p>
		A realização da sua prova de aferição terá lugar na sala <bean:write name="registration" property="measurementTestRoom.name"/>,  onde deverá comparecer  às <%= ((Registration)request.getAttribute("registration")).getMeasurementTestRoom().getShift().getDate().toString("HH:mm", I18N.getLocale())  %> horas munido de BI ou cartão de cidadão, lápis e borracha.
		</p>
		
		<br/>
		<p>
		P'lo Conselho de Gestão<br/>
		Prof. Rogério Colaço
		</p>
	
	</div>
</div>
</body>
</html>
