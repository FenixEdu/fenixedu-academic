<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
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

<jsp:include page="editShift.jsp"/>