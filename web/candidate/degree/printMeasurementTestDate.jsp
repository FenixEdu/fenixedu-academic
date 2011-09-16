<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.joda.time.LocalDate"%>
<%@page import="net.sourceforge.fenixedu.domain.candidacy.MeasurementTestRoom"%>
<%@page import="java.util.Locale"%>
<%@page import="net.sourceforge.fenixedu.domain.student.Registration"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />

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
		O IST em associação com a Sociedade Portuguesa de Matemática promove 
		este ano lectivo uma prova de aferição para todos os alunos ingressados no
		1º ano. Esta prova é obrigatória e terá influência na classificação da 
		cadeira de Cálculo Diferencial e Integral I ou Matemática I.
		</p>
		
		<p>
		
		
		A prova realizar-se-á no dia <%= ((Registration)request.getAttribute("registration")).getMeasurementTestRoom().getShift().getDate().toString("dd 'de' MMMM", Language.getLocale())  %> e terá uma duração aproximada de 3 horas, pelo que, neste dia, serão suspensas as aulas do 1º ano. 
		</p>
		
		<p>
		A realização da sua prova de aferição terá lugar na sala <bean:write name="registration" property="measurementTestRoom.name"/>,  onde deverá comparecer  às <%= ((Registration)request.getAttribute("registration")).getMeasurementTestRoom().getShift().getDate().toString("HH:mm", Language.getLocale())  %> horas munido de BI ou cartão de cidadão, lápis e borracha.
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
