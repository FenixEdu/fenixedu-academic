<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%@page import="pt.utl.ist.fenix.tools.util.i18n.Language"%>
<%@page import="org.joda.time.LocalDate"%><html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">

<html:xhtml/>

<head>
<title></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />



</head>



<body>


<p>
Caro/a  aluno/a <bean:write name="registration" property="student.name"/>, <bean:write name="registration" property="student.number"/>, 
</p>

<p>
O IST em associação com a Sociedade Portuguesa de Matemática promove 
este ano lectivo uma prova de aferição para todos os alunos ingressados no
1º ano. Esta prova é obrigatória e poderá ter influência na classificação da 
cadeira de Cálculo Diferencial e Integral I ou Matemática I.
</p>

<p>
A prova realizar-se-á no dia <bean:write name="registration" property="measurementTestRoom.shift.date"/> 23 de Setembro,  pelo que, neste dia,  serão suspensas as aulas do 1º ano no período da tarde. 
</p>

<p>
A realização da sua prova de aferição terá lugar na sala <bean:write name="registration" property="measurementTestRoom.name"/>,  onde deverá comparecer  às <bean:write name="registration" property="measurementTestRoom.shift.date"/> horas munido de BI ou cartão de cidadão.
</p>

<p>
P'lo Conselho de Gestão
Palmira Ferreira da Silva
</p>

</body>
</html>
