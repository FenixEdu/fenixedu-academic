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

</style>

<h2>Resultados do Inquérito</h2>

<div class="infoop2" style="font-size: 1.4em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">Semestre: <bean:write name="inquiryResult" property="executionCourse.executionPeriod.name"/> <bean:write name="inquiryResult" property="executionCourse.executionYear.name"/></span></p>
	<p style="margin: 0.75em 0;">Curso: <bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/></span></p>
	<p style="margin: 0.75em 0;">Disciplina: <bean:write name="inquiryResult" property="executionCourse.nome"/></p>
</div>

<bean:define id="result" name="inquiryResult" type="net.sourceforge.fenixedu.domain.inquiries.StudentInquiriesCourseResult" />
<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr>
		<th>Nº de inscritos:</th>
		<td><c:out value="${inquiryResult.numberOfEnrolled}" /></td>
	</tr>
	<tr>
		<th>Avaliados <a href="#" class="help">[?] <span>Nº avaliados / Nº inscritos. Não são contabilizados resultados de épocas especiais e/ou melhorias.</span></a>:</th>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.evaluatedRatio}" /></td>
	</tr>
	<tr>
		<th>Aprovados <a href="#" class="help">[?] <span>Nº aprovados / Nº avaliados . Não são contabilizados resultados de épocas especiais e/ou melhorias.</span></a>:</th>
		<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.approvedRatio}" /></td>
	</tr>
	<tr>
		<th>Média notas <a href="#" class="help">[?] <span>Não são contabilizados resultados de épocas especiais e/ou melhorias.</span></a>:</th>
		<td><c:out value="${inquiryResult.gradeAverage}" /></td>
	</tr>
	<tr>
		<th>Sujeita a inquérito <a href="#" class="help">[?] <span>Algumas UC não foram sujeitas a inquérito, para mais informações ver regulamento QUC e FAQ's.</span></a>:</th>
		<td><bean:message key="<%= "label." + result.getAvailableToInquiry().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
	</tr>
</table>

<logic:equal name="inquiryResult" property="availableToInquiry" value="true">
	<h3 class="mtop15 mbottom0"><strong>Estatística de preenchimento e representatividade</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">%</th>
		</tr>
		<tr>
			<th>Respostas válidas quadro inicial:</th>
			<td><c:out value="${inquiryResult.validInitialFormAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.validInitialFormAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>Respostas válidas inquérito à UC <a href="#" class="help">[?] <span>Respostas válidas - se os valores percentagem de NHTA e NDE não fossem simultaneamente iguais a zero, e a resposta ao inquérito foi submetida após a disponibilização da opção de não responder ao inquérito.</span></a>:</th>
			<td><c:out value="${inquiryResult.validInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.validInquiryAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>Não respostas à UC:</th>
			<td><c:out value="${inquiryResult.noInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.noInquiryAnswersRatio}" /></td>
		</tr>
		<tr>
			<th>Respostas inválidas inquérito à UC:</th>
			<td><c:out value="${inquiryResult.invalidInquiryAnswersNumber}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.invalidInquiryAnswersRatio}" /></td>
		</tr>
	</table>
									
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr>
			<th></th>
			<th class="acenter">Responsáveis pela gestão académica <a href="#" class="help">[?] <span>Representatividade - nº de respostas superior a 5 e a 10% do nº inscritos.</span></a></th>
			<th class="acenter">Comunidade académica IST <a href="#" class="help">[?] <span>Representatividade - nº de respostas superior a 5 e a 50% do nº inscritos.</span></a></th>
		</tr>
		<tr>
			<th>Representatividade para divulgação:</th>
			<td><bean:message key="<%= "label." + result.getInternalDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getPublicDisclosure().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdcenter">
		<tr>
			<th></th>
			<th class="acenter">Organização da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (Discordo) 2 das 4 questões do grupo.</span></a></th>
			<th class="acenter">Avaliação da UC <a href="#" class="help">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (Discordo) a questão e/ou taxa de avaliação <50% e/ou taxa de aprovação <50%.</span></a></th>
			<th class="acenter">Passível de Auditoria <a href="#" class="help">[?] <span>Passível de Auditoria se 2 grupos com resultados a melhorar.</span></a></th>
		</tr>
		<tr>
			<th>Resultados a melhorar:</th>
			<td><bean:message key="<%= "label." + result.getUnsatisfactoryResultsCUOrganization().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getUnsatisfactoryResultsCUEvaluation().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
			<td><bean:message key="<%= "label." + result.getAuditCU().toString() %>" bundle="INQUIRIES_RESOURCES"/></td>
		</tr>
	</table>
</logic:equal>
<logic:notEqual name="inquiryResult" property="availableToInquiry" value="true">
TEXTO 2
</logic:notEqual>


<logic:equal name="inquiryResult" property="internalDisclosure" value="true">
	
	<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr>
			<th>Carga Horária da UC:</th>
			<td><c:out value="${inquiryResult.scheduleLoad}" /></td>
		</tr>
		<tr>
			<th>Nº ECTS da UC:</th>
			<td><c:out value="${inquiryResult.ects}" /></td>
		</tr>
	</table>
	
	<h3 class="mtop15 mbottom0"><strong>Auto-avaliação dos alunos</strong></h3>
	
	<table class="tstyle1 thlight thleft tdcenter td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">Média</th>
			<th class="acenter">Desvio padrão</th>
		</tr>
		<tr>
			<th>Nº médio de horas de trabalho autónomo por semana com a UC:</th>
			<td><c:out value="${inquiryResult.number_perc_NHTA}" /></td>
			<td><c:out value="${inquiryResult.average_perc_weeklyHours}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_perc_NHTA}" /></td>
		</tr>
		<tr>
			<th>Nº de dias de estudo da UC na época de exames:</th>
			<td><c:out value="${inquiryResult.number_NDE}" /></td>
			<td><c:out value="${inquiryResult.average_NDE}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_NDE}" /></td>
		</tr>
		<tr>
			<th>Nº médio ECTS estimado <a href="#" class="help">[?] <span>ECTS ESTIMADO = ((Nº de horas de trabalho autónomo por semana com a UC + Carga Horária da UC)* 14+ Nº de dias de estudo da UC na época de exames * 8)/28.</span></a>:</th>
			<td><c:out value="${inquiryResult.estimatedEctsNumber}" /></td>
			<td><c:out value="${inquiryResult.estimatedEctsAverage}" /></td>
			<td><c:out value="${inquiryResult.estimatedEctsStandardDeviation}" /></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdcenter td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">[10; 12]</th>
			<th class="acenter">[13; 14]</th>
			<th class="acenter">[15; 16]</th>
			<th class="acenter">[17; 18]</th>
			<th class="acenter">[19; 20]</th>
			<th class="acenter">Reprovado</th>
			<th class="acenter">Não avaliado</th>
		</tr>
		<tr>
			<th>Gama de valores da classificação dos alunos:</th>
			<td><c:out value="${inquiryResult.number_P1_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_10_12}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_13_14}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_15_16}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_17_18}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_19_20}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_flunked}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_nonEvaluated}" /></td>
		</tr>
	</table>
	
	
	
	<p class="mtop15 mbottom0"><strong>Carga de trabalho elevada devido a</strong></p>
	
	<table class="tstyle1 thlight thleft td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">%</th>
		</tr>
		<tr>
			<th>Trabalhos/projectos complexos:</th>
			<td><c:out value="${inquiryResult.number_P1_2_a}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_a}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos extensos:</th>
			<td><c:out value="${inquiryResult.number_P1_2_b}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_b}" /></td>
		</tr>
		<tr>
			<th>Trabalhos/projectos em número elevado:</th>
			<td><c:out value="${inquiryResult.number_P1_2_c}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_c}" /></td>
		</tr>
		<tr>
			<th>Falta de preparação anterior exigindo mais trabalho/estudo:</th>
			<td><c:out value="${inquiryResult.number_P1_2_d}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_d}" /></td>
		</tr>
		<tr>
			<th>Extensão do programa face ao nº de aulas previstas:</th>
			<td><c:out value="${inquiryResult.number_P1_2_e}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_e}" /></td>
		</tr>
		<tr>
			<th>Pouco acompanhamento das aulas ao longo do semestre:</th>
			<td><c:out value="${inquiryResult.number_P_1_2_f}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_f}" /></td>
		</tr>
		<tr>
			<th>Outras razões:</th>
			<td><c:out value="${inquiryResult.number_P1_2_g}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc__P1_2_g}" /></td>
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
			<th>Conhecimentos anteriores suficientes para o acompanhamento da UC:</th>
			<td><c:out value="${inquiryResult.number_P1_3}" /></td>
			<td><c:out value="${inquiryResult.average_P1_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P1_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_3_9}" /></td>
		</tr>
	</table>
	
	
	<table class="tstyle1 thlight thleft tdcenter td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">Média</th>
			<th class="acenter">Desvio padrão</th>
			<th class="acenter">Passiva<br/>1</th>
			<th class="acenter">Activa quando solicitada<br/>2</th>
			<th class="acenter">Activa por iniciativa própria<br/>3</th>
		</tr>
		<tr>
			<th>Participação dos alunos na UC:</th>
			<td><c:out value="${inquiryResult.number_P1_4}" /></td>
			<td><c:out value="${inquiryResult.average_P1_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P1_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_4_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_4_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P1_4_3}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>A UC contribuiu para a aquisição e/ou desenvolvimento das seguintes competências</strong></p>
	
	<table class="tstyle1 thlight thleft tdcenter td50px">
		<tr>
			<th></th>
			<th class="acenter">N</th>
			<th class="acenter">Média</th>
			<th class="acenter">Desvio padrão</th>
			<th class="acenter">Não sabe / Não responde / Não aplicável</th>
			<th class="acenter">Não contribuiu<br/>1</th>
			<th class="acenter">Contribuiu<br/>2</th>
			<th class="acenter">Contribuiu muito<br/>3</th>
		</tr>
		<tr>
			<th>Conhecimento e compreensão do tema da UC:</th>
			<td><c:out value="${inquiryResult.number_P2_1}" /></td>
			<td><c:out value="${inquiryResult.average_P2_1}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_1_0}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_1_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_1_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_1_3}" /></td>
		</tr>
		<tr>
			<th>Aplicação do conhecimento sobre o tema da UC:</th>
			<td><c:out value="${inquiryResult.number_P2_2}" /></td>
			<td><c:out value="${inquiryResult.average_P2_2}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_2_0}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_2_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_2_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_2_3}" /></td>
		</tr>
		<tr>
			<th>Sentido crítico e espírito reflexivo:</th>
			<td><c:out value="${inquiryResult.number_P2_3}" /></td>
			<td><c:out value="${inquiryResult.average_P2_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_3_0}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_3_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_3_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_3_3}" /></td>
		</tr>
		<tr>
			<th>Capacidade de cooperação e comunicação:</th>
			<td><c:out value="${inquiryResult.number_P2_4}" /></td>
			<td><c:out value="${inquiryResult.average_P2_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P2_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_4_0}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_4_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_4_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P2_4_3}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Organização da UC</strong></p>
	
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
			<th>O programa previsto foi leccionado:</th>
			<td><c:out value="${inquiryResult.number_P3_1}" /></td>
			<td><c:out value="${inquiryResult.average_P3_1}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_1_9}" /></td>
		</tr>
		<tr>
			<th>A UC encontra-se bem estruturada:</th>
			<td><c:out value="${inquiryResult.number_P3_2}" /></td>
			<td><c:out value="${inquiryResult.average_P3_2}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_2_9}" /></td>
		</tr>
		<tr>
			<th>A bibliografia foi importante:</th>
			<td><c:out value="${inquiryResult.number_P3_3}" /></td>
			<td><c:out value="${inquiryResult.average_P3_3}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_3_9}" /></td>
		</tr>
		<tr>
			<th>Os materiais de apoio foram bons:</th>
			<td><c:out value="${inquiryResult.number_P3_4}" /></td>
			<td><c:out value="${inquiryResult.average_P3_4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P3_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P3_4_9}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Método de avaliação da UC</strong></p>
	
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
			<th>Os métodos de avaliação foram justos e apropriados:</th>
			<td><c:out value="${inquiryResult.number_P4}" /></td>
			<td><c:out value="${inquiryResult.average_P4}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P4_9}" /></td>
		</tr>
	</table>
	
	
	<p class="mtop15 mbottom0"><strong>Avaliação global da UC</strong></p>
	
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
			<th>Avaliação do funcionamento da UC:</th>
			<td><c:out value="${inquiryResult.number_P5}" /></td>
			<td><c:out value="${inquiryResult.average_P5}" /></td>
			<td><c:out value="${inquiryResult.standardDeviation_P5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_1}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_2}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_3}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_4}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_5}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_6}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_7}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_8}" /></td>
			<td><fmt:formatNumber type="percent" maxFractionDigits="1" value="${inquiryResult.perc_P5_9}" /></td>
		</tr>
	</table>
</logic:equal>

<logic:notEqual name="inquiryResult" property="internalDisclosure" value="true">
TEXTO 3
</logic:notEqual>


</body>
</html>
