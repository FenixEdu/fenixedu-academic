<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %><%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<html:xhtml/>

<ul>
	<li>
		<html:link page="<%= "/index.do" %>">
			<bean:message key="link.website.listSites"/>
		</html:link>
	</li>
</ul>
