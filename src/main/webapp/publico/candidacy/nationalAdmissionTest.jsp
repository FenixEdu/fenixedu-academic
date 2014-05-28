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
<%@ page language="java"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>


<html:xhtml/>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>">Licenciaturas</a> &gt;
	Concurso Nacional de Acesso
</div>

<div id="contextual_nav">
<h2 class="brown">Nesta p&aacute;gina</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#con">Ingresso pelo Concurso Nacional de Acesso</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#rec">Recursos</a></li>
		<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#doc">Documentos</a></li>
   </ul>
</div>
<h1>Candidato: Licenciatura (1º Ciclo)</h1>
<p>Os estudantes que pretendam candidatar-se ao <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> com vista a obter uma formação superior deverão optar por um dos seguintes percursos, consoante a área de estudos pretendida:</p>
<ul>
	<li>inscrição em mestrado integrado (ciclo integrado), com a duração de 5 anos e atribuição do grau de licenciado ao fim dos primeiros 3 anos;</li>

	<li>inscrição em licenciatura (1º ciclo), com a duração de 3 anos, seguida da frequência de um mestrado (2º ciclo) de 2 anos, conducente ao grau de mestre.</li>
</ul>
<p>A formação em Arquitectura e em oito cursos de Engenharia está organizada em mestrados integrados. As restantes formações em Engenharia e a formação em Matemática Aplicada seguem também um modelo de dois ciclos.</p>
<p>Nas Engenharias, ao fim dos 3 primeiros anos curriculares (180 ECTS), é conferido o grau de licenciado em Engenharia numa dada especialidade. Este grau atesta uma forte formação em ciências básicas, como Matemática, Física, Química e Programação, e em ciências básicas de engenharia numa dada área, para além de competências transversais, como comunicação oral e escrita, gestão, liderança, trabalho em equipa e empreendedorismo. Esta formação, não tendo objectivos profissionalizantes, confere um conjunto de competências que permitem a empregabilidade em algumas áreas do mercado de trabalho. A formação de 1º ciclo em Arquitectura segue o mesmo modelo conferindo o grau de Licenciado em Estudos de Arquitectura.</p>
<p>A formação completa do Engenheiro e Arquitecto, que capacita para a concepção, a inovação e o desenvolvimento de projectos complexos, só é alcançada ao fim de 5 anos, com a aquisição de competências científicas e tecnológicas avançadas inerentes ao grau de mestre. A Ordem dos Engenheiros e a Ordem dos Arquitectos definiram como condição necessária para admissão nas mesmas a titularidade de uma formação superior de 5 anos.</p>
<h2 id="con">Ingresso pelo Concurso Nacional de Acesso</h2>
<h3>Ano Lectivo 2008/2009</h3>
<p>Após a conclusão do ensino secundário, os estudantes que pretendam obter uma formação superior no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> deverão candidatar-se, através do Concurso Nacional de Acesso, ao ingresso num curso de 1º ciclo ou num curso de ciclo integrado, consoante a área de estudos pretendida. As candidaturas são formuladas junto dos Serviços de Acesso do Ministério da Ciência, Tecnologia e Ensino Superior, tendo em conta as condições de acesso definidas para cada curso.</p>
<h3 class="spaced">Cursos do 1º Ciclo</h3>

	<table class="tab_lay" width="99%" cellspacing="0" summary="Informação para o ingresso nos cursos do 1º Ciclo (ano lectivo 2007-2008)">
		<tr>
			<th>Cursos</th>
			<th>Código</th>
			<th>Campus</th>
			<th>Vagas</th>
			<th style="width: 25%;">Provas de ingresso</th>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/leamb?locale=pt">Licenciatura em Engenharia do Ambiente</a></td>
			<td>1518/9099</td>
			<td>Alameda</td>
			<td>35</td>
			<td>Matemática + Física e Química ou Matem&aacute;tica + Biologia e Geologia</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lean?locale=pt">Licenciatura em Engenharia e Arquitectura Naval</a></td>
			<td>1518/9911</td>
			<td>Alameda</td>
			<td>10</td>
			<td>Matemática + Física e Química</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lee?locale=pt">Licenciatura em Engenharia Electrónica</a></td>
			<td>1519/9912</td>
			<td>Taguspark</td>			
			<td>33</td>
			<td>Matemática + Física e Química</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/legm?locale=pt">Licenciatura em Engenharia Geológica e de Minas</a></td>
			<td>1518/9913</td>
			<td>Alameda</td>
			<td>15</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/legi?locale=pt">Licenciatura em Engenharia e Gestão Industrial</a></td>
			<td>1519/9104</td>
			<td>Taguspark</td>			
			<td>40</td>
			<td>Matemática + Física e Química</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/leic-a?locale=pt">Licenciatura em Engenharia Informática e de Computadores</a></td>
			<td>1518/9121</td>
			<td>Alameda</td>
			<td>170</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/leic-t?locale=pt">Licenciatura em Engenharia Informática e de Computadores</a></td>
			<td>1519/9121</td>
			<td>Taguspark</td>			
			<td>98</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>	
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lemat?locale=pt">Licenciatura em Engenharia de Materiais</a></td>
			<td>1518/9096</td>
			<td>Alameda</td>
			<td>20</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/lerc?locale=pt">Licenciatura em Engenharia de Redes de Comunicações</a></td>
			<td>1519/9746</td>
			<td>Taguspark</td>			
			<td>68</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/lmac?locale=pt">Licenciatura em Matemática Aplicada e Computação</a></td>
			<td>1518/9345</td>
			<td>Alameda</td>			
			<td>30</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>

		</tr>
</table>

<h3 class="spaced">Cursos Integrados</h3>
<table class="tab_lay" width="99%" cellspacing="0" summary="Informação para o ingresso nos cursos integrads (ano lectivo 2007-2008)">
		<tr>
			<th>Cursos</th>
			<th>Código</th>
			<th>Campus</th>

			<th style="white-space: nowrap;">Vagas</th>
			<th style="width: 25%;">Provas de ingresso</th>
		</tr>

		<tr>
			<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
			<td>1518/9257</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Matemática + Geometria Descritiva ou Desenho + Matemática ou Matemática</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
			<td>1518/9357</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Matemática + Física e Química</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biológica</a></td>
			<td>1518/9358</td>

			<td>Alameda</td>
			<td>65</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biomédica</a></td>
			<td>1518/9359</td>

			<td>Alameda</td>
			<td>50</td>
			<td>Matemática + Física e Química ou Matemática + Biologia e Geologia</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
			<td>1518/9360</td>

			<td>Alameda</td>
			<td>185</td>
			<td>Matemática + Física e Química</td>
		</tr>		
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrotécnica e de Computadores</a></td>
			<td>1518/9367</td>

			<td>Alameda</td>
			<td>205</td>
			<td>Matemática + Física e Química</td>
		</tr>
		<tr>
			<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia Física Tecnológica</a></td>
			<td>1518/9458</td>

			<td>Alameda</td>
			<td>60</td>
			<td>Matemática + Física e Química</td>
		</tr>
		<tr class="bluecell">
			<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mecânica</a></td>	
			<td>1518/9369</td>	
			<td>Alameda</td>	
			<td>165</td>	
			<td>Matemática + Física e Química</td>	
		</tr>

		<tr>
			<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Engenharia Química</a></td>
			<td>1518/9461</td>
			<td>Alameda</td>
			<td>70</td>
			<td>Matemática + Física e Química</td>

		</tr>
</table>
<h3>Classifica&ccedil;&otilde;es M&iacute;nimas de Acesso</h3>
<p>As condi&ccedil;&otilde;es exigidas para a candidatura aos Cursos ministrados no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> s&atilde;o as seguintes (expressas numa escala de 0 a 200 pontos):</p>
<ul>
	<li>Classifica&ccedil;&atilde;o m&iacute;nima de 100 em cada uma das provas de ingresso (exames nacionais do ensino secund&aacute;rio), exceptuando o curso de Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o em que a classifica&ccedil;&atilde;o m&iacute;nima exigida &eacute; de 120, e;</li>

	<li>Classifica&ccedil;&atilde;o m&iacute;nima de 120 na nota de candidatura, exceptuando o curso de Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o em que a classifica&ccedil;&atilde;o m&iacute;nima exigida &eacute; de 140. A nota de candidatura (NC) &eacute; calculada utilizando um peso de 50% para a classifica&ccedil;&atilde;o do Ensino Secund&aacute;rio (MS) e um peso de 50% para a classifica&ccedil;&atilde;o das provas de ingresso (PI). - F&oacute;rmula de C&aacute;lculo da Nota de Candidatura: NC = MS x 50% + PI x 50% (ou seja, m&eacute;dia aritm&eacute;tica da classifica&ccedil;&atilde;o final do Ensino Secund&aacute;rio e da classifica&ccedil;&atilde;o das provas de ingresso).</li>

</ul>
<p>Nota: n&atilde;o se exigem pr&eacute;-requisitos.</p>
<p>Mais informações no <a href="http://www.dges.mctes.pt/DGES/pt">website da Direcção Geral do Ensino Superior (DGES)</a>.</p>
<div class="h_box">
<h2 id="rec">Recursos</h2>
<ul> 
	<li><a href="http://nape.ist.utl.pt/acesso/notas.php">Vagas e notas m&iacute;nimas de seria&ccedil;&atilde;o dos &uacute;ltimos 5 anos lectivos</a></li> 
	<li><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>html/campus/tagus/sat/">Servi&ccedil;os Administrativos do IST-Taguspark (SAT)</a></li> 
	<li><a href="http://guiatecnico.aeist.pt/">Guia do T&eacute;cnico </a> &nbsp;(Publica&ccedil;&atilde;o da responsabilidade da <a href="http://ae.ist.utl.pt/">Associa&ccedil;&atilde;o de Estudantes do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>) </li> 
	<li><a href="http://guialisboa.aeist.pt/">Guia de Lisboa</a>&nbsp;(Publica&ccedil;&atilde;o da responsabilidade da <a href="http://ae.ist.utl.pt/">Associa&ccedil;&atilde;o de Estudantes do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a>) </li> 

</ul>	
</div>
<h2 id="doc">Documentos</h2>
<ul class="material">
	<li class="pdf"><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/reg_1e2ciclo_20082009.pdf">Regulamento do 1º e 2º ciclo para 2008/2009</a> (PDF, 826KB)</li>
	<li class="pdf"><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/propinas_20082009.pdf">Propinas do 1º e 2º ciclo para 2008/2009</a> (PDF, 28KB)</li>
</ul>
