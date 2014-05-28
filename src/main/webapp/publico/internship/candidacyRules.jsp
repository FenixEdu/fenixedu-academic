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
<%@page import="org.apache.struts.action.ActionMessages"%>

<script type="text/javascript">
    
    window.onload = function() {
        var frm = document.getElementById("form0");
        frm.onsubmit = function() {
            frm.method.value='confirmCandidacyRules';
            document.getElementById("submitBtn").disabled = true;
        }
    }
    </script>

<html:xhtml />

<h2><bean:message key="label.internationalrelations.internship.candidacy.title" bundle="INTERNATIONAL_RELATIONS_OFFICE" /></h2>


<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="INTERNATIONAL_RELATIONS_OFFICE">
    <p>
        <span class="error0"><!-- Error messages go here --><bean:write name="message" /></span>
    </p>
</html:messages>

<h3>NOTA:</h3>

<ul>
<li>
    <strong>Em meados de FEVEREIRO</strong>, serão afixadas as condições dos estágios obtidos pela
	IAESTE Portugal e que, nessa altura, caso um dos estágios me interesse, <strong>devo fazer
	a minha inscrição.</strong>
</li>
	<li>Caso não obtenha um estágio nesta fase de candidatura, devo
	entrar novamente em contacto com o centro de inscrição IAESTE do meu
	estabelecimento de ensino <strong>a partir do mês de ABRIL</strong>,
	para continuar a ser considerado como candidato para novos estágios que
	apareçam daí em diante.</li>
</ul>

<p>Carregue em "Submeter Candidatura" para concluir o processo de candidatura:</p>

<logic:present name="candidacy">
	<fr:form id="form0" action="/internship.do">
		<input type="hidden" name="method" />
		<fr:edit id="confirm" name="candidacy" visible="false">
	        <fr:destination name="cancel" path="/internship.do?method=backToCandidacy"/>
		</fr:edit>
		<html:cancel><bean:message bundle="COMMON_RESOURCES" key="button.back" /></html:cancel>
		<html:submit styleId="submitBtn">Submeter Candidatura</html:submit>
	</fr:form>
</logic:present>