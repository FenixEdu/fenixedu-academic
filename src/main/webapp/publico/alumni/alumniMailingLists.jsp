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
<html:xhtml />
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<!-- alumniMailingLists.jsp -->

<h1>Inscrição Alumni</h1>

<h2>Mailing Lists</h2>

<p><a href="<%= request.getAttribute("publicAccessUrl").toString() %>">« Registo Público</a></p>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>

<p class="greytxt">
	Ao subscrever as listas abaixo especificadas, estreita o laço comunicacional com a comunidade <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%>: pode trocar mensagens com os restantes membros da lista; receber informação útil e beneficiar do serviço de alertas (agenda de realizações organizadas no <%=net.sourceforge.fenixedu.domain.organizationalStructure.Unit.getInstitutionAcronym()%> e outras notícias de interesse).
</p>

<p class="greytxt">
	Ao efectuar a subscrição, indique o seu nome e nº de aluno(a) ou nº de BI para efeito de autenticação.
</p>

<table class="tab_lay" id="table1" cellpadding="5" cellspacing="0" width="65%">
    <tbody>

        <tr>
            <th>
                Área Ciêntifica
            </th>
            <th>
                Moderador
            </th>
            <th>
                Administrador
            </th>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-ambiente@mlists.ist.utl.pt">Ambiente</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-arquit@mlists.ist.utl.pt">Arquitectura</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-lea@mlists.ist.utl.pt">Engenharia Aeroespacial</a>
            </td>
            <td>
                Fernando Lau
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-biologica@mlists.ist.utl.pt">Engenharia Biológica</a>
            </td>
            <td>
                Helena Pinheiro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-lebm@mlists.ist.utl.pt">Engenharia Biomédica</a>
            </td>
            <td>
                -
            </td>
            <td>
                Engenharia Biomédica / <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-civil@mlists.ist.utl.pt">Engenharia Civil</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:legi.graduados@mlists.ist.utl.pt">Engenharia de Gestão Industrial</a>
            </td>
            <td>
                José Figueiredo
            </td>
            <td>
                José Figueiredo / <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:demat_e@mlists.ist.utl.pt">Engenharia de Materiais</a>
            </td>
            <td>
                José Carlos Pereira<br/>Augusto Moita de Deus
            </td>
            <td>
                Engenharia de Materiais / <a href="mailto:alumni@ist.utl.pt">GCRP</a> 
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-minas@mlists.ist.utl.pt">Engenharia de Minas e Georecursos</a>
            </td>
            <td>
                António Maurício
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-naval@mlists.ist.utl.pt">Engenharia e Arquitectura Naval</a>
            </td>
            <td>
                Yordan Garbatov
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leec@mlists.ist.utl.pt">Engenharia Electrotécnica e Computadores</a>
            </td>
            <td>
                Alexandre Bernardino
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-left@mlists.ist.utl.pt">Engenharia Física e Tecnológica</a>
            </td>
            <td>
                Horácio Fernandes
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leic@mlists.ist.utl.pt">Engenharia Informática e Computadores</a>
            </td>
            <td>
                José Borbinha
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-mec@mlists.ist.utl.pt">Engenharia Mecânica</a>
            </td>
            <td>
                Pedro Coelho
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-eq@mlists.ist.utl.pt">Engenharia Química</a>
            </td>
            <td>
                M. Rosário Ribeiro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-matapcomp@mlists.ist.utl.pt">Matemática Aplicada e Computação</a>
            </td>
            <td>
                Paulo Mateus
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-quimica@mlists.ist.utl.pt">Química</a>
            </td>
            <td>
                M. Rosário Ribeiro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-terr@mlists.ist.utl.pt">Território</a>
            </td>
            <td>
                Luis Castro
            </td>
            <td>
                <a href="mailto:alumni@ist.utl.pt">GCRP</a>
            </td>
        </tr>
    </tbody>
</table>
		
	
	<!-- END CONTENTS -->
</div>