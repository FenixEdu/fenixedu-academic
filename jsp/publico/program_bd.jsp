<%@ page language="java" %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<logic:present name="program" >
	<table>
		<tr>
			<td>
				<h2><bean:message key="label.program" /></h2>	
			</td>
		</tr>
		<tr>
			<td>
				<bean:write name="program" filter="false"/>
			</td>
		</tr>
	</table>
</logic:present>
<logic:notPresent name="program">
<h2><bean:message key="message.program.not.available"/>
</h2>
</logic:notPresent>