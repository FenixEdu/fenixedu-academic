<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<logic:present name="coordinators">
<h3>Equipa de Coordenação</h3>
<table>
<tr><td class="listClasses-header">Nome</td>
	<td class="listClasses-header">&nbsp;</td>
</tr>
<logic:iterate name="coordinators" id="coordinator">
<tr>
	<td class="listClasses"><bean:write name="coordinator" property="infoTeacher.infoPerson.nome" /> 
	<logic:equal name="coordinator" property="responsible" value="true">
	<bean:message key="label.responsible"/>
	</logic:equal> 
	</td>
	<td class="listClasses">&nbsp;</td>
</tr>
</logic:iterate>
</table>
</logic:present>


