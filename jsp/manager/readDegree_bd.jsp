<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table>
<tr>
<td>
<logic:present name="<%= SessionConstants.INFO_DEGREE %>" >
	
		<h3><bean:message key="message.degree.editing" /></h3>
</td>
<td><h2><b><bean:write name="<%= SessionConstants.INFO_DEGREE %>" property="sigla" /> - </b></h2>
</td>
<td><h2><bean:write name="<%= SessionConstants.INFO_DEGREE %>" property="nome" /></h2>
</td>
</logic:present>
</tr>
</table>
<tr>
</tr>