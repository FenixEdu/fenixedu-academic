<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
	
<logic:present name="genObjectives" >
<table>
<tr>
	<td>
		<h2><bean:message key="label.generalObjectives" />	</h2>
	</td>
</tr>
<tr>
	<td>		 
		<bean:write name="genObjectives" filter="false"/>
	</td>
</tr>
<tr>
	<td>	
		<h2><bean:message key="label.operacionalObjectives" /></h2>
	</td>
</tr>
<tr>
	<td>		
		<bean:write name="opObjectives" filter="false"/>
	</td>
</tr>
</table>
</logic:present>
<logic:notPresent name="genObjectives">
<h2><bean:message key="message.objectives.not.available"/>
</h2>
</logic:notPresent>