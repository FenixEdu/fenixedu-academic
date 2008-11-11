<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">
<head>
	<title>.IST</title>
	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/layout.css"  media="screen"  />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/general.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/color.css" media="screen" />
	<link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/CSS/print.css" media="print" />
	
</head>

<body class="survey">


<style>
body.survey {
background: #fff;
margin: 2em;
font-size: 70%;
}
.acenter { text-align: center !important; }
th:first-child {
width: 250px;
}
body.survey table {
}
body.survey table th {
vertical-align: bottom;
}
body.survey table td {
text-align: center;
}
table.td50px td {
width: 60px;
}
body.survey table {
border-top: 4px solid #ddd;
}
.thtop th { vertical-align: top; }
.vatop { vertical-align: top !important; }
.vamiddle { vertical-align: middle !important; }


a.help {
position: relative;
text-decoration: none;
/*color: black !important;*/
border: none !important;
width: 20px;
}
a.help span {
display: none;
}
a.help:hover {
/*background: none;*/ /* IE hack */
z-index: 100;
}
a.help:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: 30px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
a { color: #105c93; }
</style>

<bean:define id="inquiryResult" name="inquiryResult" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesTeachingResult"></bean:define>

<h2>Resultados do Inquérito</h2>

<div class="infoop2" style="font-size: 1.3em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">Semestre: <bean:write name="inquiryResult" property="professorship.executionCourse.executionPeriod.name"/> <bean:write name="inquiryResult" property="professorship.executionCourse.executionYear.name"/></span></p>
	<p style="margin: 0.75em 0;">Curso: <bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/></span></p>
	<p style="margin: 0.75em 0;">Disciplina: <bean:write name="inquiryResult" property="professorship.executionCourse.nome"/></p>
	<p style="margin: 0.75em 0;">Docente: <bean:write name="inquiryResult" property="professorship.teacher.person.name"/></p>
	<p style="margin: 0.75em 0;">Tipo de aula: <bean:message name="inquiryResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></p>
</div>


<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr>
		<th>Respostas válidas ao par Docente / tipo de aula:</th>

		<td><c:out value="${inquiryResult.numberOfAnswers}" /></td>
	</tr>
</table>

<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Responsáveis pela gestão académica <a href="#" class="help">[?] <span>Representatividade - nº de respostas superior a 5 e a 10% do nº estimado de inscritos para o par Docente/Tipo de Aula.</span></a></th>
		<th class="acenter">Comunidade académica IST <a href="#" class="help">[?] <span>Representatividade - nº de respostas superior a 5 e a 50% do nº estimado de inscritos para o par Docente/Tipo de Aula.</span></a></th>
	</tr>
	<tr>
		<th>Representatividade para divulgação:</th>
		<td><bean:message key="<%= "label." + inquiryResult.getInternalDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
		<td><bean:message key="<%= "label." + inquiryResult.getPublicDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Assiduidade dos alunos <a href="#" class="help">[?] <span>Passível de Auditoria se existem, pelo menos, 2 grupos com resultados a melhorar.</span></a></th>
		<th class="acenter">Proveito da aprendizagem presencial <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas, mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 questões do grupo.</span></a></th>

		<th class="acenter">Capacidade pedagógica <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas, mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 4 questões do grupo.</span></a></th>
		<th class="acenter">Interacção com os alunos <a href="#" class="help">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas, mais 25% classifica como abaixo ou igual a 3 (Discordo) as 2 questões do grupo.</span></a></th>
		<th class="acenter">Passível de Auditoria <a href="#" class="help">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (De vez em quando).</span></a></th>
	</tr>

	<tr>
		<th>Resultados a melhorar:</th>
		<td><bean:message key="<%= "label." + inquiryResult.getUnsatisfactoryResultsAssiduity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>		
		<td><bean:message key="<%= "label." + inquiryResult.getUnsatisfactoryResultsPresencialLearning().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>		
		<td><bean:message key="<%= "label." + inquiryResult.getUnsatisfactoryResultsPedagogicalCapacity().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>		
		<td><bean:message key="<%= "label." + inquiryResult.getUnsatisfactoryResultsStudentInteraction().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>		
		<td><bean:message key="<%= "label." + inquiryResult.getUnsatisfactoryResultsAuditable().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>		
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>Proveito da aprendizagem presencial</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>

		<th class="acenter">Média</th>
		<th class="acenter">Desvio padrão</th>
		<th class="acenter">Nunca<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">De vez em quando<br/>3</th>

		<th class="acenter">4</th>
		<th class="acenter">Regularmente<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Sempre<br/>7</th>
	</tr>
	<tr>
		<th>Assiduidade dos alunos a estas aulas:</th>
		<td><c:out value="${inquiryResult.number_P6_1}" /></td>
		<td><c:out value="${inquiryResult.average_P6_1}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P6_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_7}" /></td>
	</tr>

</table>


<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th colspan="2"></th>
		<th class="acenter">Horário</th>
		<th class="acenter">Docente</th>
		<th class="acenter">Conteúdos</th>

		<th class="acenter">Repetente</th>
		<th class="acenter">Outro</th>
	</tr>
	<tr>
		<th rowspan="2" class="vamiddle">Assiduidade dos alunos a estas aulas:</th>
		<td style="background: #f8f8f8;">N</td>
		<td><c:out value="${inquiryResult.p6_1_1_a}" /></td>
		<td><c:out value="${inquiryResult.p6_1_1_b}" /></td>
		<td><c:out value="${inquiryResult.p6_1_1_c}" /></td>
		<td><c:out value="${inquiryResult.p6_1_1_d}" /></td>
		<td><c:out value="${inquiryResult.p6_1_1_e}" /></td>
	</tr>
	<tr>
		<td style="background: #f8f8f8;">%</td>

		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_a}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_b}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_c}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_d}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_1_e}" /></td>
	</tr>

</table>



<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">Média</th>
		<th class="acenter">Desvio padrão</th>

		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">Não concordo nem discordo<br/>5</th>

		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>

		<th>O docente cumpriu regularmente o horário das aulas e outras actividades programadas:</th>
		<td><c:out value="${inquiryResult.number_P6_2}" /></td>
		<td><c:out value="${inquiryResult.average_P6_2}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P6_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_2_9}" /></td>
	</tr>
	<tr>
		<th>O ritmo das aulas foi adequado:</th>
		<td><c:out value="${inquiryResult.number_P6_3}" /></td>
		<td><c:out value="${inquiryResult.average_P6_3}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P6_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P6_3_9}" /></td>
	</tr>
</table>


<p class="mtop15 mbottom0"><strong>Capacidade pedagógica</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">Média</th>
		<th class="acenter">Desvio padrão</th>
		<th class="acenter">Discordo totalmente<br/>1</th>

		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>
		<th class="acenter">Não concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>

		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>
	</tr>
	<tr>
		<th>O docente mostrou-se empenhado:</th>

		<td><c:out value="${inquiryResult.number_P7_1}" /></td>
		<td><c:out value="${inquiryResult.average_P7_1}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P7_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_1_9}" /></td>

	</tr>
	<tr>
		<th>O docente expôs os conteúdos de forma atractiva:</th>
		<td><c:out value="${inquiryResult.number_P7_2}" /></td>
		<td><c:out value="${inquiryResult.average_P7_2}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P7_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_2_9}" /></td>
	</tr>
	<tr>
		<th>O docente demonstrou segurança na exposição:</th>
		<td><c:out value="${inquiryResult.number_P7_3}" /></td>
		<td><c:out value="${inquiryResult.average_P7_3}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P7_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_3_9}" /></td>
	</tr>
	<tr>
		<th>O docente expôs os conteúdos com clareza:</th>

		<td><c:out value="${inquiryResult.number_P7_4}" /></td>
		<td><c:out value="${inquiryResult.average_P7_4}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P7_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P7_4_9}" /></td>

	</tr>
</table>


<p class="mtop15 mbottom0"><strong>Interacção com os alunos</strong></p>

<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">Média</th>

		<th class="acenter">Desvio padrão</th>
		<th class="acenter">Discordo totalmente<br/>1</th>
		<th class="acenter">2</th>
		<th class="acenter">Discordo<br/>3</th>
		<th class="acenter">4</th>

		<th class="acenter">Não concordo nem discordo<br/>5</th>
		<th class="acenter">6</th>
		<th class="acenter">Concordo<br/>7</th>
		<th class="acenter">8</th>
		<th class="acenter">Concordo totalmente<br/>9</th>

	</tr>
	<tr>
		<th>O docente estimulou a participação e discussão:</th>
		<td><c:out value="${inquiryResult.number_P8_1}" /></td>
		<td><c:out value="${inquiryResult.average_P8_1}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P8_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_1_9}" /></td>
	</tr>
	<tr>
		<th>O docente mostrou abertura para o esclarecimento de dúvidas, dentro e fora das aulas:</th>
		<td><c:out value="${inquiryResult.number_P8_2}" /></td>
		<td><c:out value="${inquiryResult.average_P8_2}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P8_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_3}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_4}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_5}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_6}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_7}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_8}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P8_2_9}" /></td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">Média</th>
		<th class="acenter">Desvio padrão</th>
		<th class="acenter">A melhorar<br/>1</th>

		<th class="acenter">Bom<br/>2</th>
		<th class="acenter">Muito bom<br/>3</th>
	</tr>
	<tr>
		<th>Avaliação global do desempenho do docente:</th>
		<td><c:out value="${inquiryResult.number_P9}" /></td>
		<td><c:out value="${inquiryResult.average_P9}" /></td>
		<td><c:out value="${inquiryResult.standardDeviation_P9}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P9_1}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P9_2}" /></td>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P9_3}" /></td>
	</tr>

</table>




</body>
</html>
