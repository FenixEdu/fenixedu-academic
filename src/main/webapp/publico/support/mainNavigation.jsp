<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul class="treemenu">
	<li><html:link page="/support.do?method=prepare&amp;page=0">
		<bean:message key="link.support.faq"/>
	</html:link></li>
	<li><html:link page="/supportGlossary.do?method=prepare&amp;page=0">
		<bean:message key="link.support.glossary"/>
	</html:link></li>
</ul>