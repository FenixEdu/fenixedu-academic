<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<li><html:link page="<%= "/marksList.do?method=loadFile&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadFileMarks"/></html:link></li>
<li><html:link page="<%= "/marksList.do?method=loadMarksOnline&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadMarksOnline"/></html:link></li>
<li><html:link page="<%= "/marksList.do?method=publishMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.publishMarks"/></html:link></li>
<li><html:link page="<%= "/marksList.do?method=submitMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.submitMarks"/></html:link></li>
