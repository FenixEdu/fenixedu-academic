<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<html:xhtml />

<bean:write name="inquiryResult" />

<%--

<h2>Resultados do Inquérito</h2>

<div class="infoop2" style="font-size: 1.4em; padding: 0.5em 1em; margin: 1em 0;">
	<p style="margin: 0.75em 0;">2º semestre de 2007/2008</span></p>
	<p style="margin: 0.75em 0;">Ciências de Engenharia - Engenharia Informática e de Computadores - Alameda</span></p>
	<p style="margin: 0.75em 0;">Mecânica e Ondas</p>
</div>


<table class="tstyle1 thlight thleft td50px thbgnone">
	<tr>
		<th>Nº de inscritos:</th>
		<td>57</td>
	</tr>
	<tr>
		<th>Avaliados (%):</th>
		<td>98%</td>
	</tr>
	<tr>
		<th>Aprovados (%):</th>
		<td>95%</td>
	</tr>
	<tr>
		<th>Média notas:</th>
		<td>15</td>
	</tr>
	<tr>
		<th>Sujeita a inquérito:</th>
		<td>Sim</td>
	</tr>
</table>


<h3 class="mtop15 mbottom0"><strong>Estatística de preenchimento e representatividade</strong></h3>

<table class="tstyle1 thlight thleft td50px">
	<tr>
		<th></th>
		<th class="acenter">N</th>
		<th class="acenter">%</th>
	</tr>
	<tr>
		<th>Respostas validas quadro inicial:</th>
		<td>57</td>
		<td>88%</td>
	</tr>
	<tr>
		<th>Respostas validas inquérito à UC:</th>
		<td>52</td>
		<td>91%</td>
	</tr>
	<tr>
		<th>Não respostas à UC:</th>
		<td>3</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Respostas invalidas inquérito à UC:</th>
		<td>0</td>
		<td>0%</td>
	</tr>
</table>
								

<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Responsáveis pela gestão académica</th>
		<th class="acenter">Comunidade académica IST</th>
	</tr>
	<tr>
		<th>Representatividade para divulgação:</th>
		<td>Sim</td>
		<td>Não</td>
	</tr>
</table>


<table class="tstyle1 thlight thleft tdcenter">
	<tr>
		<th></th>
		<th class="acenter">Organização da UC</th>
		<th class="acenter">Avaliação da UC</th>
		<th class="acenter">Passível de Auditoria</th>
	</tr>
	<tr>
		<th>Resultados a melhorar:</th>
		<td>Sim</td>
		<td>Sim</td>
		<td>Sim</td>
	</tr>
</table>


<h3 class="mtop15 mbottom0"><strong>Acompanhamento e carga de trabalho da UC ao longo do semestre</strong></h3>

<table class="tstyle1 thlight thleft td50px">
	<tr>
		<th>Carga Horária da UC:</th>
		<td>-</td>
	</tr>
	<tr>
		<th>Nº ECTS da UC:</th>
		<td>-</td>
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
		<td>48</td>
		<td>6.6</td>
		<td>2.4</td>
	</tr>
	<tr>
		<th>Nº de dias de estudo da UC na época de exames:</th>
		<td>48</td>
		<td>2.2</td>
		<td>2.9</td>
	</tr>
	<tr>
		<th>Nº médio ECTS estimado:</th>
		<td colspan="3">48</td>
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
		<td>47</td>
		<td>17%</td>
		<td>19%</td>
		<td>43%</td>
		<td>17%</td>
		<td>2%</td>
		<td>2%</td>
		<td>0%</td>
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
		<td>19</td>
		<td>39%</td>
	</tr>
	<tr>
		<th>Trabalhos/projectos extensos:</th>
		<td>28</td>
		<td>57%</td>
	</tr>
	<tr>
		<th>Trabalhos/projectos em número elevado:</th>
		<td>11</td>
		<td>22%</td>
	</tr>
	<tr>
		<th>Falta de preparação anterior exigindo mais trabalho/estudo:</th>
		<td>8</td>
		<td>16%</td>
	</tr>
	<tr>
		<th>Extensão do programa face ao nº de aulas previstas:</th>
		<td>1</td>
		<td>2%</td>
	</tr>
	<tr>
		<th>Pouco acompanhamento das aulas ao longo do semestre:</th>
		<td>3</td>
		<td>6%</td>
	</tr>
	<tr>
		<th>Outras razões:</th>
		<td>3</td>
		<td>6%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Aplicação do conhecimento sobre o tema da UC:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Sentido crítico e espírito reflexivo:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Capacidade de cooperação e comunicação:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>A UC encontra-se bem estruturada:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>A bibliografia foi importante:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
	<tr>
		<th>Os materiais de apoio foram bons:</th>
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
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
		<td>49</td>
		<td>2.1</td>
		<td>0.7</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
		<td>5%</td>
	</tr>
</table>

--%>