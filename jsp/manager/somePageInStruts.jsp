<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

Hello World :o0)
<br />
<bean:write name="strutsExampleForm" property="someValue" />
<br />
<br />

<html:form action="/someStrutsPage">
	<html:hidden property="method" value="showExecutionPeriods" />
	<html:hidden property="page" value="1" />

		<html:submit>
			List Execution Periods
		</html:submit></td>	
</html:form>
