<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<style>
th.empty, td.empty {
background: none;
border-top: none;
border-bottom: none;
width: 10px !important;
padding: 0 !important;
}
 
th.col-course { }
th.col-bar { width: 70px; }
th.col-fill { width: 80px; }
th.col-actions { }
 
td.col-course { text-align: left; }
td.col-bar { }
td.col-fill { }
td.col-actions { white-space: nowrap; }
 
div.delegate-resume {
background: #ccc;    
padding: 3px;
margin: 15px 0;
}
 
div.delegate-resume table {
margin: 0;
}
 
 
div.bar-yellow, div.bar-red, div.bar-green, div.bar-blue, div.bar-purple, div.bar-grey {
margin: 2px 0;
}
 
 
td.col-bar div div {
padding: 2px 10px;
-moz-border-radius: 3px;
border-radius: 3px;
text-align: center;
color: #fff;
padding-top: 2px;
font-weight: bold;
margin: auto;
display: inline;
}
 
div.bar-yellow div { background: #DDB75B; }
div.bar-red div { background: #C04439; }
div.bar-green div { background: #478F47; }
div.bar-blue div { background: #3574A5; }
div.bar-purple div { background: #743E8C; }
div.bar-grey div { background: #aaaaaa; }
 
 
ul.legend-general {
list-style: none;
padding: 0;
margin: 10px 0;
color: #444;
}
ul.legend-general li {
display: inline;
padding-right: 10px;
padding: 2px 10px 0 0;
}
span.legend-bar-1,
span.legend-bar-2,
span.legend-bar-3,
span.legend-bar-4,
span.legend-bar-5 {
-moz-border-radius: 3px;
border-radius: 3px;
padding: 1px 4px 0px 4px;
font-size: 8px;
font-weight: bold;
}
span.legend-bar-1 { background: #3574A5; }
span.legend-bar-2 { background: #478F47; }
span.legend-bar-3 { background: #DDB75B; }
span.legend-bar-4 { background: #C04439; }
span.legend-bar-5 { background: #aaaaaa; }
 
</style>

<br/>
<h2><bean:message key="link.yearDelegateInquiries" bundle="DELEGATES_RESOURCES"/></h2>

<p>Bem vindo à nova interface dos QUC para os Delegados! Aqui tens acesso ao quadro com o resumo dos QUC de todas as disciplinas do teu ano. Se os alunos de uma determinada  disciplina indicaram algum problema, é o teu papel, como delegado, explicar o que aconteceu na disciplina que causou o baixo resultado, de forma ao corpo docente corrigir o que correu mal. O número de perguntas que necessita de comentários teus está indicada entre parêntesis. Para além de essas perguntas, podes sempre para cada disciplina preencher comentários opcionais. O que escreves deve ser a opinião da turma, e não apenas a tua opinião pessoal. Fala com os teu colegas!
</p>
<p>
Caso o problema não seja resolvido pelo corpo docente, os teu comentários são muito importantes para que o Coordenador de Curso, os Departamentos, e o Conselho Pedagógico resolvam o problema. Está nas tuas mãos. Participa!
</p>

<logic:present name="executionPeriod" property="delegateInquiryResponsePeriod">
	<logic:notEmpty name="executionPeriod" property="delegateInquiryResponsePeriod.introduction">	
		<div style="border: 1px solid #ddd; padding: 5px 15px; background: #fafafa;">
			<bean:write name="executionPeriod" property="delegateInquiryResponsePeriod.introduction" filter="false"/>
		</div>
	</logic:notEmpty>
</logic:present>

<h3 class="mtop15"><bean:write name="executionDegree" property="degree.sigla"/> (<bean:write name="executionPeriod" property="semester"/>º Semestre <bean:write name="executionPeriod" property="executionYear.year"/>)</h3>

<fr:view name="coursesResultResume">
	<fr:layout name="delegate-inquiry-resume">
	</fr:layout>
</fr:view>