<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>

<ul class="treemenu">
	<li><html:link page="/support.do?method=prepare&amp;page=0">
		<bean:message key="link.support.faq"/>
	</html:link></li>
	<li><html:link page="/supportGlossary.do?method=prepare&amp;page=0">
		<bean:message key="link.support.glossary"/>
	</html:link></li>
</ul>