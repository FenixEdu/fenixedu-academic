<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
	<li>
		<html:link page="<%= "/sectionsManagement.do?method=prepare&amp;objectCode=1" %>">
			<bean:message key="link.sectionsManagement"/>
		</html:link>
	</li>
</ul>
