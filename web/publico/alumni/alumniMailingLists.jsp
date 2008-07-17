<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:xhtml />
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<!-- alumniRegistrationResult.jsp -->

<h1>Alumni</h1>

<h2>Mailing Lists</h2>

<p><a href="<%= request.getContextPath() + "/conteudos-publicos/registo-alumni"%>">« Voltar</a></p>

<div class="alumnilogo">
	
	<html:messages id="message" message="true" bundle="ALUMNI_RESOURCES">
		<p><span class="error"><!-- Error messages go here --><bean:write name="message" /></span></p>
	</html:messages>

<p class="greytxt">
	Ao subscrever as listas abaixo especificadas, estreita o laço comunicacional com a comunidade IST: pode trocar mensagens com os restantes membros da lista; receber informação útil e beneficiar do serviço de alertas (agenda de realizações organizadas no IST e outras notícias de interesse).
</p>

<p class="greytxt">
	Ao efectuar a subscrição, indique o seu nome e nº de aluno(a) ou nº de BI para efeito de autenticação.
</p>

<table class="tab_lay" id="table1" cellpadding="5" cellspacing="0" width="65%">
    <tbody>

        <tr>
            <th>
                Área
                Ciêntifica
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
                Betâmio
                Almeida
            </td>
            <td>

                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-arquit@mlists.ist.utl.pt">Arquitectura</a>
            </td>
            <td>
                Betâmio
                Almeida&nbsp;

                <br>
            </td>
            <td>
                GEP
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
                GEP
            </td>
        </tr>
        <tr>
            <td>

                <a href="mailto:alumni-biologica@mlists.ist.utl.pt">Engenharia
                    Biológica</a>
            </td>
            <td>
                M. Rosário
                Ribeiro
                <br>
            </td>
            <td>
                GEP
            </td>

        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-lebm@mlists.ist.utl.pt">Engenharia
                    Biomédica</a>
            </td>
            <td>
                -
            </td>
            <td>

                Engenharia
                Biomédica
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-civil@mlists.ist.utl.pt">Engenharia Civil</a>
            </td>
            <td>
                Betâmio
                Almeida
            </td>

            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:legi.graduados@mlists.ist.utl.pt">Engenharia de Gestão
                    Industrial</a>
            </td>
            <td>

                José
                Figueiredo
                <br>
            </td>
            <td>
                José
                Figueiredo
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-lemat@mlists.ist.utl.pt">Engenharia de Materiais</a>

            </td>
            <td>
                Patrícia Almeida
                Carvalho
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>

                <a href="mailto:alumni-minas@mlists.ist.utl.pt">Engenharia de Minas e
                    Georecursos</a>
            </td>
            <td>
                António
                Maurício
            </td>
            <td>
                GEP
            </td>
        </tr>

        <tr>
            <td>
                <a href="mailto:alumni-naval@mlists.ist.utl.pt">Engenharia e Arquitectura
                    Naval</a>
            </td>
            <td>
                Yordan Garbatov
            </td>
            <td>
                GEP
            </td>

        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leec@mlists.ist.utl.pt">Engenharia
                    Electrotécnica e
                    Computadores</a>
            </td>
            <td>
                Paula Queluz
            </td>
            <td>

                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-left@mlists.ist.utl.pt">Engenharia Física e
                    Tecnológica</a>
            </td>
            <td>
                Horácio
                Fernandes
            </td>

            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-leic@mlists.ist.utl.pt">Engenharia
                    Informática e
                    Computadores</a>
            </td>
            <td>

                José
                Borbinha
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-mec@mlists.ist.utl.pt">Engenharia
                    Mecânica</a>

            </td>
            <td>
                Pedro Coelho
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>

                <a href="mailto:alumni-eq@mlists.ist.utl.pt">Engenharia
                    Química</a>
            </td>
            <td>
                M. Rosário
                Ribeiro
            </td>
            <td>
                GEP
            </td>
        </tr>

        <tr>
            <td>
                <a href="mailto:alumni-matapcomp@mlists.ist.utl.pt">Matemática Aplicada
                    e
                    Computação</a>
            </td>
            <td>
                João Pimentel
                Nunes
                <br>
                Jaime Ramos
                <br>

            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr class="bluecell">
            <td>
                <a href="mailto:alumni-quimica@mlists.ist.utl.pt">Química</a>
            </td>

            <td>
                M. Rosário
                Ribeiro
            </td>
            <td>
                GEP
            </td>
        </tr>
        <tr>
            <td>
                <a href="mailto:alumni-terr@mlists.ist.utl.pt">Território</a>

            </td>
            <td>
                Betâmio
                Almeida
            </td>
            <td>
                GEP
            </td>
        </tr>
    </tbody>
</table>
		
	
	<!-- END CONTENTS -->
</div>