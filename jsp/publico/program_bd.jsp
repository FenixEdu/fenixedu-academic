<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" >
	<table>
		<tr>
			<td>
				<h2><bean:message key="label.program" /></h2>	
			</td>
		</tr>
		<tr>
			<td>
				<bean:define id="program" name="<%= SessionConstants.EXECUTION_COURSE_CURRICULUM %>" property="program">
				</bean:define> 
				<bean:write name="program" />
			</td>
		</tr>
	</table>
</logic:present>