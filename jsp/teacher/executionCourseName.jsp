<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table>
<tr>
<td>
<logic:present name="<%= SessionConstants.INFO_SITE %>" >
<bean:define id="executionCourse" name="<%= SessionConstants.INFO_SITE %>" property="infoExecutionCourse" /><h3><bean:message key="message.course.editing" /></h3
</td>
<td><h4><bean:write name="executionCourse" property="nome" /></h4>
</td>
</logic:present>
</tr>
</table>