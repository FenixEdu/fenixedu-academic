<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>


Execution Periods: 
<br />
<table>
	<logic:iterate id="executionPeriod" name="executionPeriods">
		<tr>
			<td>
				<bean:write name="executionPeriod" property="name" />
			</td>
			<td>
				<bean:write name="executionPeriod" property="infoExecutionYear.year" />
			</td>
		</tr>	
	</logic:iterate>
</table>

