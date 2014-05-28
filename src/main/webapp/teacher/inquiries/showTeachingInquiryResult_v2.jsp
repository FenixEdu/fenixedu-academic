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
<%@ page isELIgnored="true"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>
<html:xhtml />

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" lang="pt-PT" xml:lang="pt-PT">
<head>
	<title>
		<bean:message key="public.QUC.teacherResults" bundle="TITLES_RESOURCES" /> -
		<bean:write name="inquiryResult" property="professorship.executionCourse.nome"/> -
		<bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/> -
		<%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionName()%>
	</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
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
.aright { text-align: right !important; }
.aleft { text-align: left !important; }
th:first-child {
width: 250px;
}
body.survey table { }
body.survey table th { vertical-align: bottom; }
body.survey table td { text-align: center; }
table.td50px td { width: 60px; }
table tr.top th { border-top: 4px solid #ddd; }
table tr.top td { border-top: 4px solid #ddd; }
body.survey table { }
.thtop th { vertical-align: top; }
.vatop { vertical-align: top !important; }
.vamiddle { vertical-align: middle !important; }
.tdright td { text-align: right !important; }

a {
color: #105c93;
}
a.help, a.helpleft {
position: relative;
text-decoration: none;
/*color: black !important;*/
border: none !important;
width: 20px;
}
a.help span, a.helpleft span { display: none; }
a.help:hover, a.helpleft:hover {
/*background: none;*/ /* IE hack */
z-index: 100;
border-bottom-width: 1px;
border-bottom-style: solid;
border-bottom-color: transparent;
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
a.helpleft:hover span {
display: block !important;
display: inline-block;
width: 250px;
position: absolute;
top: 10px;
left: -280px;
text-align: left;
padding: 7px 10px;
background: #48869e;
color: #fff;
border: 3px solid #97bac6;
/*cursor: help;*/
}
a.helpleft[class]:hover span {
right: 20px;
}

table th.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}
table td.separatorright {
border-right: 3px solid #ddd;
padding-right: 8px;
}
</style>

<fmt:setBundle basename="resources.InquiriesResources" var="INQUIRIES_RESOURCES"/>

<bean:define id="inquiryResult" name="inquiryResult" type="net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult"></bean:define>

<p class="mtop0" style="float: right;"><em>Informação do sistema, recolhida a <c:out value="${inquiryResult.resultsDate}" /></em></p>

<h2>QUC - Garantia da Qualidade das UC - Resultados dos inquéritos aos alunos</h2>

<div class="infoop2" style="font-size: 1.3em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">Semestre: 
		<bean:write name="inquiryResult" property="professorship.executionCourse.executionPeriod.semester"/>
		<bean:message bundle="PUBLIC_DEGREE_INFORMATION" locale="pt_PT" key="public.degree.information.label.ordinal.semester.abbr" />
		<bean:message bundle="APPLICATION_RESOURCES" locale="pt_PT" key="of" /> 
		<bean:write name="inquiryResult" property="professorship.executionCourse.executionYear.name"/></span></p>	
	<p style="margin: 0.75em 0;">Curso: <bean:write name="inquiryResult" property="executionDegree.degree.presentationName"/></span></p>
	<p style="margin: 0.75em 0;">Unidade curricular: <bean:write name="inquiryResult" property="professorship.executionCourse.nome"/></p>
	<p style="margin: 0.75em 0;">Docente: 
		<logic:notEmpty name="inquiryResult" property="professorship.teacher">
			<logic:notEmpty name="inquiryResult" property="professorship.teacher.category">
				<bean:write name="inquiryResult" property="professorship.teacher.category.name"/> -
			</logic:notEmpty>
		</logic:notEmpty> 
		<bean:write name="inquiryResult" property="professorship.person.name"/></p>
	<p style="margin: 0.75em 0;">Tipo de aula: <bean:message name="inquiryResult" property="shiftType.name"  bundle="ENUMERATION_RESOURCES"/></p>
</div>


<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr class="top">
		<th>Respostas ao par Docente / tipo de aula</th>

		<td><c:out value="${inquiryResult.valuesMap['N_respostas']}" /> </td>
	</tr>
</table>

<table class="tstyle1 thlight thleft tdcenter">
	<tr class="top">
		<th></th>
		<th class="aright">Responsáveis pela gestão académica <a href="#" class="helpleft">[?] <span>Representatividade - nº de respostas válidas superior a 5 e a 10% do nº estimado de inscritos para o par Docente/Tipo de Aula.</span></a></th>
		<th class="aright">Comunidade académica <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> <a href="#" class="helpleft">[?] <span>Representatividade - nº de respostas válidas superior a 5 e a 50% do nº estimado de inscritos para o par Docente/Tipo de Aula.</span></a></th>
	</tr>
	<tr>
		<th>Representatividade para divulgação</th>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Repres_doc_curso_UC_interna']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Repres_doc_curso_UC_publica']}" /></td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter">
	<tr class="top">
		<th></th>
		<th class="aright">Assiduidade dos alunos <a href="#" class="helpleft">[?] <span>Resultados a melhorar se mais 25% alunos classifica como abaixo ou igual a 3 (De vez em quando).</span></a></th>
		<th class="aright">Proveito da aprendizagem presencial <a href="#" class="helpleft">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 3 questões do grupo. - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
		<th class="aright">Capacidade pedagógica <a href="#" class="helpleft">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas, mais 25% classifica como abaixo ou igual a 3 (Discordo) 2 das 4 questões do grupo.</span></a></th>
		<th class="aright">Interacção com os alunos <a href="#" class="helpleft">[?] <span>Resultados a melhorar se, entre os alunos que frequentaram as aulas, mais 25% classifica como abaixo ou igual a 3 (Discordo) as 2 questões do grupo.</span></a></th>
	</tr>
	<tr>
		<th>Resultados a melhorar</th>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_insatisf_assiduidade']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_insatisf_prov_aprend_pres']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_insatisf_cap_pedag']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_insatisf_int_alunos']}" /></td>
	</tr>
</table>

<table class="tstyle1 thlight thleft tdcenter">
    <tr class="top">
        <th></th>
        <th class="aright">Assiduidade dos alunos <a href="#" class="helpleft">[?] <span>Resultados excelentes se mais se 75% dos alunos classifica como acima ou igual a 3 (de vez em quando).</span></a></th>
        <th class="aright">Proveito da aprendizagem presencial <a href="#" class="helpleft">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas (alunos que responderam igual so superior a 3 na pergunta da assiduidade às aulas, no mínimo de 10), mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo (excepto a questão da assiduidade) e a média de respostas nos outros grupos (Capacidade Pedagógica e Interacção com os alunos) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Capacidade pedagógica <a href="#" class="helpleft">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo e a média de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a questão da assiduidade dos alunos) e Interacção com os alunos) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
        <th class="aright">Interacção com os alunos <a href="#" class="helpleft">[?] <span>Resultados excelentes se, entre os alunos que frequentaram as aulas, mais de 75% classifica como acima ou igual a 7 (Concordo) todas as questões do grupo e a média de respostas nos outros grupos (Proveito da aprendizagem presencial (excepto a questão da assiduidade dos alunos) e Capacidade pedagógica) superior a 7 (Concordo). - conforme revisão do regulamento aprovada em 15 Maio 2009, ver http://quc.ist.utl.pt/_docs_/SSGQUC.pdf</span></a></th>
    </tr>
    <tr>
        <th>Resultados excelentes</th>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_excelentes_assiduidade']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_excelentes_prov_aprend_pres']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_excelentes_cap_pedag']}" /></td>
        <td><fmt:message bundle="${INQUIRIES_RESOURCES}" key="label.colored.boolean.${inquiryResult.valuesMap['Res_excelentes_int_alunos']}" /></td>
    </tr>
</table>


<c:if test="${((empty publicContext || !publicContext) && inquiryResult.valuesMap['Repres_doc_curso_UC_interna'] == '1' || (not empty publicContext && publicContext) && inquiryResult.publicDegreeDisclosure)}" >

<p class="mtop15 mbottom0"><strong>Proveito da aprendizagem presencial</strong></p>

<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th></th>
        <th class="aright">N</th>
        <th class="aright">Média</th>
        <th class="aright separatorright">Desvio padrão</th>
        <th class="aright">Nunca<br/><b>1</b></th>
        <th class="aright"><b>2</b></th>
        <th class="aright">De vez em quando<br/><b>3</b></th>
        <th class="aright"><b>4</b></th>
        <th class="aright">Regularmente<br/><b>5</b></th>
        <th class="aright"><b>6</b></th>
        <th class="aright">Sempre<br/><b>7</b></th>
    </tr>
    <tr>
        <th>Assiduidade dos alunos a estas aulas</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P6_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P6_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P6_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_7']}" /></td>
    </tr>

</table>


<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th colspan="2"></th>
        <th class="aright">Horário</th>
        <th class="aright">Docente</th>
        <th class="aright">Conteúdos</th>

        <th class="aright">Repetente</th>
        <th class="aright">Outro</th>
    </tr>
    <tr>
        <th rowspan="2" class="vamiddle">A fraca assiduidade às aulas deveu-se a:</th>
        <td style="background: #f8f8f8;">N</td>
        <td><c:out value="${inquiryResult.valuesMap['P6_1_1_a']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['P6_1_1_b']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['P6_1_1_c']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['P6_1_1_d']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['P6_1_1_e']}" /></td>
    </tr>
    <tr>
        <td style="background: #f8f8f8;">%</td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_a']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_b']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_c']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_d']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_1_e']}" /></td>
    </tr>
</table>

<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th></th>
        <th class="aright">N</th>
        <th class="aright">Média</th>
        <th class="aright separatorright">Desvio padrão</th>
        <th class="aright">Discordo totalmente<br/><b>1</b></th>
        <th class="aright"><b>2</b></th>
        <th class="aright">Discordo<br/><b>3</b></th>
        <th class="aright"><b>4</b></th>
        <th class="aright">Não concordo nem discordo<br/><b>5</b></th>
        <th class="aright"><b>6</b></th>
        <th class="aright">Concordo<br/><b>7</b></th>
        <th class="aright"><b>8</b></th>
        <th class="aright">Concordo totalmente<br/><b>9</b></th>
    </tr>
    <tr>

        <th>O docente cumpriu regularmente o horário das aulas e outras actividades programadas</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P6_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P6_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P6_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_2_9']}" /></td>
    </tr>
    <tr>
        <th>O ritmo das aulas foi adequado</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P6_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P6_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P6_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P6_3_9']}" /></td>        
    </tr>
</table>


<p class="mtop15 mbottom0"><strong>Capacidade pedagógica</strong></p>

<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th></th>
        <th class="aright">N</th>
        <th class="aright">Média</th>
        <th class="aright separatorright">Desvio padrão</th>
        <th class="aright">Discordo totalmente<br/><b>1</b></th>
        <th class="aright"><b>2</b></th>
        <th class="aright">Discordo<br/><b>3</b></th>
        <th class="aright"><b>4</b></th>
        <th class="aright">Não concordo nem discordo<br/><b>5</b></th>
        <th class="aright"><b>6</b></th>
        <th class="aright">Concordo<br/><b>7</b></th>
        <th class="aright"><b>8</b></th>
        <th class="aright">Concordo totalmente<br/><b>9</b></th>
    </tr>
    <tr>
        <th>O docente mostrou-se empenhado</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P7_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P7_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P7_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_1_9']}" /></td>
    </tr>
    <tr>
        <th>O docente expôs os conteúdos de forma atractiva</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P7_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P7_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P7_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_2_9']}" /></td>
    </tr>
    <tr>
        <th>O docente demonstrou segurança na exposição</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P7_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_P7_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P7_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_3_9']}" /></td>
    </tr>
    <tr>
        <th>O docente expôs os conteúdos com clareza</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P7_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_ P7_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_ P7_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P7_4_9']}" /></td>
    </tr>
</table>


<p class="mtop15 mbottom0"><strong>Interacção com os alunos</strong></p>

<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th></th>
        <th class="aright">N</th>
        <th class="aright">Média</th>

        <th class="aright separatorright">Desvio padrão</th>
        <th class="aright">Discordo totalmente<br/><b>1</b></th>
        <th class="aright"><b>2</b></th>
        <th class="aright">Discordo<br/><b>3</b></th>
        <th class="aright"><b>4</b></th>
        <th class="aright">Não concordo nem discordo<br/><b>5</b></th>
        <th class="aright"><b>6</b></th>
        <th class="aright">Concordo<br/><b>7</b></th>
        <th class="aright"><b>8</b></th>
        <th class="aright">Concordo totalmente<br/><b>9</b></th>

    </tr>
    <tr>
        <th>O docente estimulou a participação e discussão</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P8_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_ P8_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_ P8_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_1_9']}" /></td>
    </tr>
    <tr>
        <th>O docente mostrou abertura para o esclarecimento de dúvidas, dentro e fora das aulas</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P8_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_ P8_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_ P8_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P8_2_9']}" /></td>
    </tr>
</table>


<table class="tstyle1 thlight thleft tdright td50px">
    <tr class="top">
        <th></th>
        <th class="aright">N</th>
        <th class="aright">Média</th>
        <th class="aright separatorright">Desvio padrão</th>
        <th class="aright">Muito mau<br/><b>1</b></th>
        <th class="aright"><b>2</b></th>
        <th class="aright">Mau<br/><b>3</b></th>
        <th class="aright"><b>4</b></th>
        <th class="aright">Nem bom nem mau<br/><b>5</b></th>
        <th class="aright"><b>6</b></th>
        <th class="aright">Bom<br/><b>7</b></th>
        <th class="aright"><b>8</b></th>
        <th class="aright">Muito bom<br/><b>9</b></th>
    </tr>
    
    <tr>
        <th>Avaliação global do desempenho do docente</th>
        <td><c:out value="${inquiryResult.valuesMap['N_P9']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['M_ P9']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['dp_P9']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_1']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_2']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_3']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_4']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_5']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_6']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_7']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_8']}" /></td>
        <td><c:out value="${inquiryResult.valuesMap['perc_P9_9']}" /></td>
    </tr>
</table>

</c:if>

<c:if test="${inquiryResult.valuesMap['Repres_doc_curso_UC_interna'] != '1'}" >
<!-- TEXTO 5 -->
</c:if>


</body>
</html>
