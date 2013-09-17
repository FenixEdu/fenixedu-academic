<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>


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

