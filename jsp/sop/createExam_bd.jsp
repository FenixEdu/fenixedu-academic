<%@ page language="java" %>
<%@ page import="org.apache.struts.action.Action" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td bgcolor="#FFFFFF" class="infoselected"><p>A licenciatura seleccionada
        	&eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
         </td>
    </tr>
</table>
<br/>

<h2><bean:message key="title.exam.create"/></h2>
<span class="error"><html:errors /></span>

 --- TODO ---

Falta por aqui um form

Falta por aqui uma caixa de input para o ano
Falta por aqui uma caixa de input para o mês
Falta por aqui uma caixa de input para o dia
Falta por aqui uma caixa de input para o turno de inicio
Opcionalmente falta por aqui uma caixa opcional de input para a hora de fim/duração

No Futuro breve falta também por aqui uma caixinha para indicar a época.

Falta por aqui um botão submit
Falta por aqui um botão clear