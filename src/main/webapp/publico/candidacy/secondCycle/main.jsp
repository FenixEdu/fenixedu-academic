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

<bean:define id="mappingPath" name="mappingPath"/>
<bean:define id="fullPath"><%= request.getContextPath() + "/publico" + mappingPath + ".do" %></bean:define>

<div class="breadcumbs">
	<a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>"><%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> &gt;
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/introducao" %>"><bean:message key="title.candidate" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/>
</div>


<div id="contextual_nav">
<h2 class="brown">Nesta página</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#desc">Descrição</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#vag">Vagas</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#prop">Propinas</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#praz">Prazos</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#critsel">Critérios de selecção</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#docnec">Documentos necessários</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#emol">Emolumentos de candidatura</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#faq">FAQs</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submeter Candidatura</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareApplicationAccessRecovery" %>"><b>Recuperar Accesso</b></a></li>
   </ul>
</div>

<h1><bean:message key="title.application.name.secondCycle" bundle="CANDIDATE_RESOURCES"/></h1>


<h2 id="desc">Descrição</h2>

<p>Podem candidatar-se a um Mestrado de 2º ciclo do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, ou a um 2º ciclo de um Mestrado Integrado do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, os estudantes que estejam nas seguintes condições:</p>

<ul>
	<li>tenham terminado no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> uma Licenciatura de 1º ciclo, ou o 1º ciclo de um Mestrado Integrado, sem <a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/coerencias_cientificas_0910.pdf">coerência científica</a> com o curso de 2º ciclo a que se candidatam;</li>
	<li>sejam titulares de uma formação de 1º ciclo na área de Ciências e Tecnologia (exceptua-se o caso do 2º ciclo em Arquitectura que pressupõe uma formação de 1º ciclo em Arquitectura);</li>
	<li>sejam detentores de um currículo escolar, científico ou profissional, que ateste a sua capacidade para realização do Mestrado a que se candidatam.</li>
</ul>

<p>Os candidatos que se encontrem a terminar uma Licenciatura de 1º ciclo, ou o 1º ciclo de um Mestrado Integrado, poderão submeter a sua candidatura nos prazos estipulados para o efeito, ficando esta condicionada ao término do referido ciclo até ao dia 30 de Setembro de 2009.</p>

<p><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/coerencias_cientificas_0910.pdf">Tabela de Coerência Científica entre cursos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a></p>

<h2 id="vag">Vagas 2º ciclo</h2>

	<table class="tab_lay" width="70%" cellspacing="0" summary="Informações sobre os mestrados (2º ciclo), no âmbito de Bolonha, disponíveis no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>">
	<tr>	
		<th>Curso</th>

		<th>Campus</th>
		<th>Vagas</th>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
		<td>Alameda</td>
		<td>5</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/cursos/mbionano?locale=pt">Mestrado em Bioengenharia e Nanossistemas</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="https://fenix.ist.utl.pt/cursos/mbiotec?locale=pt">Mestrado em Biotecnologia</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
		<td>Alameda</td>

		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meamb?locale=pt">Mestrado em Engenharia do Ambiente</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>

	<tr>
		<td><a href="http://fenix.ist.utl.pt/mean?locale=pt">Mestrado em Engenharia e Arquitectura Naval</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biológica</a></td>

		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biomédica</a></td>
		<td>Alameda</td>
		<td>15</td>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mee?locale=pt">Mestrado em Engenharia Electrónica</a></td>
		<td>Taguspark</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrotécnica e de Computadores</a></td>
		<td>Alameda</td>
		<td>90</td>
	</tr>
	<tr>
		<td><a href="https://fenix.ist.utl.pt/cursos/MEFarm?locale=pt">Mestrado em Engenharia Farmac&ecirc;utica</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia Física Tecnológica</a></td>
		<td>Alameda</td>
		<td>30</td>
	</tr>
	<tr>

		<td><a href="http://fenix.ist.utl.pt/megm?locale=pt">Mestrado em Engenharia Geológica e de Minas</a></td>
		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/megi?locale=pt">Mestrado em Engenharia e Gestão Industrial</a></td>
		<td>Taguskpark</td>
		<td>30</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meic-a?locale=pt">Mestrado em Engenharia Informática e de Computadores</a></td>
		<td>Alameda</td>
		<td>30</td>
	</tr>

	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meic-t?locale=pt">Mestrado em Engenharia Informática e de Computadores</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>
	<tr >
		<td><a href="http://fenix.ist.utl.pt/memat?locale=pt">Mestrado em Engenharia de Materiais</a></td>

		<td>Alameda</td>
		<td>15</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec&acirc;nica</a></td>
		<td>Alameda</td>
		<td>35</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Eng&ordf; Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>20</td>

	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/merc?locale=pt">Mestrado em Engenharia de Redes de Comunica&ccedil;&otilde;es</a></td>
		<td>Taguspark</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mma?locale=pt">Mestrado em Matem&aacute;tica e Aplica&ccedil;&otilde;es</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mq?locale=pt">Mestrado em Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr>
		<td><a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">Mestrado em Sistemas Complexos de Infraestruturas de Transportes (MIT-Portugal)</a></td>
		<td>Alameda</td>
		<td>20</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://www.civil.ist.utl.pt/?locale=pt">Mestrado em Urbanismo e Ordenamento do Território</a></td>
		<td>Alameda</td>
		<td>25</td>
	</tr>
</table>

<h2 id="prop">Propinas</h2>

<p>Para o ano lectivo de 2009/2010 a propina para os Mestrados de 2º ciclo e 2º ciclo de Mestrados Integrados é de <b>996,85 Euros</b>, com excepção do curso "Mestrado em Engenharia Farmacêutica" em que a propina está fixada no valor de <b>5000 Euros</b>. Relativamente ao "Mestrado em Sistemas Complexos de Infra-estruturas de Transportes" é favor consultar o <a href="http://www.civil.ist.utl.pt/nispt/mit/ctis/">site próprio</a>.</p>


<h2 id="praz">Prazos</h2>
<table class="tab_simpler" summary="Prazos para candidatos do 2º ciclo" cellspacing="0">
	<tr>
		<td class="align_r"><span class="marker">4 de Maio a 15 de Junho de 2009</span></td>
		<td><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>">Apresentação de candidaturas</a></td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 de Junho de 2009</span></td>

		<td>Afixação dos editais de colocação</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 a 3 de Julho de 2009</span></td>
		<td>Matrícula e inscrição</td>

	</tr>
	<tr>
		<td class="align_r"><span class="marker">29 a 3 de Julho de 2009</span></td>
		<td>Reclamação sobre as colocações </td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 de Julho de 2009</span></td>
		<td>Decisão sobre as reclamações</td>
	</tr>
	<tr>
		<td class="align_r"><span class="marker">17 a 21 de Julho de 2009</span></td>
		<td>Matrícula para reclamações atendidas</td>
	</tr>
</table> 

<h2 id="critsel">Critérios de Selecção</h2>

<p>Os candidatos a um Mestrado de 2º ciclo do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, ou a um 2º ciclo de um Mestrado Integrado do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>, serão seriados pela coordenação do curso a que se candidatam tendo em conta os seguintes critérios:</p>
<ol>
	<li>Afinidade entre o curso que possuem e o curso a que se candidatam;</li>
	<li>Natureza do grau que possuem;</li>

	<li>Sucesso escolar no curso que frequentam.</li>
</ol>
<p>Nos casos dos candidatos em que se considere que a formação de 1º ciclo não corresponde às competências necessárias para a formação a que se candidatam, poderá o júri de selecção excluir o candidato ou propor a admissão condicionada à frequência e aprovação num conjunto de unidades curriculares propedêuticas.</p>
<p>O conjunto de unidades curriculares propedêuticas nunca poderá exceder os 30 ECTS e a aprovação nas mesmas condicionará a conclusão do curso. As classificações obtidas nestas unidades curriculares não serão contabilizadas para a classificação final do curso.</p>


<h2 id="docnec">Documentos necessários</h2>
<p>Para completar o processo de candidatura é necessário submeter os seguintes documentos digitalizados:</p>

<ul>
	<li>Foto actual</li>
	<li>Curriculum vitae</li>
	<li>Certificados de habilitações discriminado com média</li>
	<li>Documento de identificação</li>
	<li>Cartão de contribuinte</li>
	<li>Comprovativo de pagamento dos emolumentos de candidatura</li>
</ul>


<h2 id="emol">Emolumentos de candidatura</h2>

<strong>Nome do banco:</strong> Millennium BCP<br />
<strong>NIB:</strong> 0033 0000 00007920342 05<br />

<strong>IBAN:</strong> PT50 0033 0000 00007920342 05<br />
<strong>SWIFT/BIC:</strong> BCOMPTPL<br />
<strong>Montante:</strong> 100 euros

<p/>


<h2 id="faq">FAQ</h2>

<h3>Q1: Sou finalista do 1º ciclo de um curso de Licenciatura (ou Mestrado Integrado) no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. Terei que fazer alguma coisa para prosseguir os estudos no 2º ciclo do mesmo curso?</h3>
<p>A1: Não, nestes casos a transição para o 2º ciclo é automática.</p>

<h3>Q2: Sou finalista do 1º ciclo de um curso de Licenciatura (ou Mestrado Integrado) no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. Em que condições a mudança para outro 2º ciclo diferente é automática?</h3>
<p>A2: É sempre possível mudar de curso e prosseguir estudos noutra área após completar o 1º ciclo desde que esteja garantida a coerência científica entre os dois cursos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. Esta coerência verifica-se sempre que as competências de formação do 1º ciclo respeitem as necessidades de formação para ingresso no 2º ciclo. A <a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/coerencias_cientificas_0910.pdf">Tabela de Coerência Científica entre cursos do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></a> mostra-te em que situações a mudança para um 2º ciclo diferente é automática.</p>

<h3>Q3: Sou finalista do 1º ciclo de um curso de Licenciatura (ou Mestrado Integrado) no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. O que devo fazer se pretender prosseguir os estudos num 2º ciclo do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> sem coerência científica com o curso de 1º ciclo?</h3>
<p>A3: Deves submeter um processo de candidatura ao 2º ciclo de acordo com os prazos e regulamento em vigor, sendo o processo analisado juntamente com as candidaturas de alunos externos que pretendem ingressar num curso de 2º ciclo do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>.</p>

<h3>Q4: Sou titular de uma licenciatura pré-bolonha do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>. Posso candidatar-me a um mestrado de 2º ciclo, na mesma área de formação?</h3>
<p>A4: Sim. </p>

