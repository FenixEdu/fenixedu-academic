<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>

<bean:define id="backLink" name="backLink" scope="request"/>

<bean:define id="begin">
	/equivalence.do?backLink=<%= pageContext.findAttribute("backLink").toString() %>
</bean:define>

<ul>
	<li>
		<html:link page="<%= pageContext.findAttribute("begin").toString() %>"><bean:message key="link.enrollment.equivalence.home"/></html:link>
	</li>
	<li>
		<html:link forward="<%= pageContext.findAttribute("backLink").toString() %>"><bean:message key="link.enrollment.equivalence.out"/></html:link>
	</li>
</ul>
