<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:xhtml/>

<ul>
	<li>
		<html:link page="<%= "/index.do" %>">
			<bean:message key="link.website.listSites"/>
		</html:link>
	</li>
</ul>
