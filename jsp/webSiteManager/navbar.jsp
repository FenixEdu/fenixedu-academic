<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<ul>
	<li>
		<html:link page="<%= "/announcementsManagement.do?method=start" %>">
			<bean:message key="link.sectionsManagement"/>
		</html:link>
	</li>
</ul>
