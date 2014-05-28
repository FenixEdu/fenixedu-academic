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
	<%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="<%= request.getContextPath() + "/candidaturas/licenciaturas" %>"><bean:message key="title.degrees" bundle="CANDIDATE_RESOURCES"/></a> &gt;
	<bean:message key="title.application.name.over23" bundle="CANDIDATE_RESOURCES"/>
</div>

<div id="contextual_nav">
<h2 class="brown">Nesta página</h2>
	<ul>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#critsel">Critérios de selecção</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#docnec">Documentos necessários</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#emol">Emolumentos de candidatura</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#vag">Vagas</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#prop">Propinas</a></li>
    	<li><%= pt.ist.fenixWebFramework.servlets.filters.contentRewrite.GenericChecksumRewriter.NO_CHECKSUM_PREFIX %><a href="#praz">Prazos</a></li>
    	<li><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>"><b>Submeter Candidatura</b></a></li>
    	<li><a href="<%= fullPath + "?method=prepareApplicationAccessRecovery" %>"><b>Recuperar Acesso</b></a></li>
   </ul>
</div>



<h1>Candidato: Maiores de 23 anos (1º Ciclo)</h1>

<p>Ao Concurso Especial de Acesso destinado a Maiores de 23 anos podem candidatar-se os indivíduos que, cumulativamente:</p>

<ul>
	<li>Completem 23 anos de idade até ao dia 31 de Dezembro do ano que antecede a candidatura;</li>
	<li>Não sejam titulares de habilitação de acesso ao ensino superior;</li>
	<li>Não sejam titulares de um curso superior, nem tenham frequência anterior do ensino superior.</li>
</ul>

<p><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/reg_maiores23.pdf">Regulamento (PDF, 68KB)</a></p>


<h2 id="critsel">Critérios de selecção</h2>

<p>A avaliação da capacidade para a frequência de um curso de licenciatura ou do 1º ciclo de um curso integrado do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> consta das seguintes componentes:</p>

<ol>
	<li>Apreciação do currículo escolar e profissional do candidato;</li>
	<li>Realização de uma prova escrita de avaliação de conhecimentos em interpretação e expressão escrita;</li>
	<li>Realização de uma prova escrita de avaliação da capacidade científica;</li>
	<li>Avaliação das motivações do candidato através da realização de uma entrevista.</li>
</ol>

<p>A prova a que se referem as alíneas 2) e 3) constará de um conjunto de perguntas, elaboradas por um júri, terá a duração máxima
de três horas e será realizada numa única chamada. As componentes da prova poderão variar consoante o(s) curso(s) a que o
candidato pretenda aceder.</p>

<p><a href="<%= net.sourceforge.fenixedu.domain.Installation.getInstance().getInstituitionURL() %>files/ensino/programas_23anos_20092010.pdf">Programa das provas de avaliação (PDF, 136KB)</a></p>


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


<h2 id="vag">Vagas</h2>

<table class="tab_lay" width="99%" cellspacing="0" summary="Informações sobre vagas por licenciatura para candidatos titulares de Curso Médio e Superior">
	<tr>
		<th>Cursos</th>
		<th>Campus</th>
		<th>Vagas</th>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lean?locale=pt">Licenciatura em Engenharia e Arquitectura Naval</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/legm?locale=pt">Licenciatura em Engenharia Geol&oacute;gica e de Minas</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/leic-a?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Alameda</td>
		<td>9</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lemat?locale=pt">Licenciatura em Engenharia de Materiais</a></td>
		<td>Alameda</td>
		<td>1</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lmac?locale=pt">Licenciatura em Matem&aacute;tica Aplicada e Computa&ccedil;&atilde;o</a></td>
		<td>Alameda</td>
		<td>2</td>	
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/lee?locale=pt">Licenciatura em Engenharia Electr&oacute;nica</a></td>
		<td>Taguspark</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/legi?locale=pt">Licenciatura em Engenharia e Gest&atilde;o Industrial</a></td>
		<td>Taguspark</td>
		<td>1</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/leic-t?locale=pt">Licenciatura em Engenharia Inform&aacute;tica e de Computadores</a></td>
		<td>Taguspark</td>
		<td>5</td>
	</tr>	
	<tr>
		<td><a href="http://fenix.ist.utl.pt/lerc?locale=pt">Licenciatura em Engenharia de Redes de Comunica&ccedil;&otilde;es</a></td>
		<td>Taguspark</td>
		<td>4</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/ma?locale=pt">Mestrado em Arquitectura</a></td>
		<td>Alameda</td>
		<td>3</td>	
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meaer?locale=pt">Mestrado em Engenharia Aeroespacial</a></td>
		<td>Alameda</td>
		<td>3</td>	
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia do Ambiente</a></td>
		<td>Alameda</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mebiol?locale=pt">Mestrado em Engenharia Biol&oacute;gica</a></td>
		<td>Alameda</td>
		<td>3</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/mebiom?locale=pt">Mestrado em Engenharia Biom&eacute;dica</a></td>
		<td>Alameda</td>
		<td>2</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/mec?locale=pt">Mestrado em Engenharia Civil</a></td>
		<td>Alameda</td>
		<td>9</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/meec?locale=pt">Mestrado em Engenharia Electrot&eacute;cnica e de Computadores</a></td>
		<td>Alameda</td>
		<td>10</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meft?locale=pt">Mestrado em Engenharia F&iacute;sica Tecnol&oacute;gica</a></td>
		<td>Alameda</td>
		<td>3</td>
	</tr>
	<tr class="bluecell">
		<td><a href="http://fenix.ist.utl.pt/memec?locale=pt">Mestrado em Engenharia Mec&acirc;nica</a></td>
		<td>Alameda</td>
		<td>8</td>
	</tr>
	<tr>
		<td><a href="http://fenix.ist.utl.pt/meq?locale=pt">Mestrado em Engenharia Qu&iacute;mica</a></td>
		<td>Alameda</td>
		<td>4</td>
	</tr>
</table>

<h2 id="prop">Propinas</h2>
<p>Para o ano lectivo de 2009/2010 a propina para as Licenciaturas de 1º ciclo e 1º ciclo de Mestrados Integrados é de <b>996,85 Euros</b>.</p>


<h2 id="praz">Prazos</h2>

<table class="tab_simpler" summary="Prazos para candidatos em mudanÃ§a ou transferÃªncia de curso" cellspacing="0">
	<tr>
	<td class="align_r"><span class="marker">16 de Abril de 2009</span></td>
	<td>Afixação dos programas das provas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">4 a 29 de Maio de 2009</span></td>
	<td><a href="<%= fullPath + "?method=preparePreCreationOfCandidacy" %>">Apresentação de candidaturas </a></td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">23 de Junho de 2009</span></td>
	<td>Realização dos exames</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">6 de Julho de 2009</span></td>
	<td>Afixação de resultados dos exames e marcação das entrevistas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">13 a 17 de Julho de 2009</span></td>
	<td>Entrevistas</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">17 de Julho de 2009</span></td>
	<td>Afixação dos resultados finais</td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">21 de Julho de 2009</span></td>
	<td>Data limite para apresentação de recursos ao Presidente do <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%></td>
	</tr>
	<tr>
	<td class="align_r"><span class="marker">24 de Julho de 2009</span></td>
	<td>Afixação dos resultados dos recursos</td>
	</tr>
</table>
