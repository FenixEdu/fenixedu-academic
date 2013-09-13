<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

Hello World :o0)
<br />
<bean:write name="strutsExampleForm" property="someValue" />
<br />
<br />

<html:form action="/someStrutsPage">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="showExecutionPeriods" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1" />

		<html:submit>
			List Execution Periods
		</html:submit></td>	
</html:form>
