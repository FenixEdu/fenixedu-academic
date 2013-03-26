<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
<li><html:link page="/showCandidacies.do?seminaryID=6"><bean:message key="label.seminaries.viewSubmissions"/></html:link></li>
<li><html:link page="/selectCandidacies.do?method=prepare&amp;seminaryID=6"><bean:message key="label.seminaries.selectSubmissions"/></html:link></li>
</ul>