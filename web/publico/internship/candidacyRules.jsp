<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
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

<h2><bean:message key="label.internship.candidacy.title" bundle="INTERNSHIP_RESOURCES" /></h2>


<html:messages id="message" property="<%= ActionMessages.GLOBAL_MESSAGE %>" message="true" bundle="INTERNSHIP_RESOURCES">
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
<li>
    Sou obrigado a participar, ou fazer-me representar na reunião de atribuição de estágios a
	realizar em Março.
</li>
<li>
    Caso não obtenha um estágio nessa altura, devo entrar novamente em contacto com a IAESTE <strong>a
	partir do mês de ABRIL</strong>, para continuar a ser considerado como candidato para novos
	estágios que apareçam daí em diante.
</li>
</ul>

<p>Faça "Submeter" para concluir o processo de candidatura:</p>

<logic:present name="candidacy">
	<fr:form id="form0" action="/internship.do">
		<input type="hidden" name="method" />
		<fr:edit id="confirm" name="candidacy" visible="false" />
		<html:submit onclick="this.form.method.value='backToCandidacy';">
			<bean:message bundle="COMMON_RESOURCES" key="button.back" />
		</html:submit>
		<html:submit styleId="submitBtn">
			<bean:message bundle="COMMON_RESOURCES" key="button.submit" />
		</html:submit>
	</fr:form>
</logic:present>