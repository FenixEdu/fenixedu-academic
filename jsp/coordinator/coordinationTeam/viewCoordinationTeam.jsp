<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<logic:present name="coordinators>
<table>
<logic:iterate name="coordinators" id="coordinator">
<tr>
	<td><bean:write name="coordinator" property="infoTeacher.infoPerson.nome" /> 
	<logic:equals name="coordinator" property="responsible" value="true">
	<bean:message key="label.responsible"/>
	</logic:equals>
	</td>
</tr>
</logic:iterate>
</table>
</logic:present>


