<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="100%" cellspacing="0">
	<tr>
    	<td class="infoselected"><p>O curso seleccionado &eacute;:</p>
			<strong><jsp:include page="context.jsp"/></strong>
        </td>
  	</tr>
</table>
<h2><bean:message key="title.editTurno"/></h2>
<br />
<span class="error"><html:errors/></span>
<br />
	<html:form action="/editarTurnoForm">
<table>
  	<tr>
        <td><bean:message key="property.turno.name"/></td>
        <td><html:text property="nome" size="11" maxlength="20"/></td>
   	</tr>
   	<tr>
       	<td><bean:message key="property.turno.capacity"/></td>
       	<td><html:text property="lotacao" size="11" maxlength="20"/></td>
   	</tr>
</table>
<br />
<html:link page="/prepararEditarAulasDeTurno.do"> <bean:message key="link.add.remove.aulas"/></html:link>
<br />
<br />
<html:submit styleClass="inputbutton"><bean:message key="label.save"/></html:submit>
<html:reset styleClass="inputbutton"><bean:message key="label.clear"/></html:reset>
</html:form>