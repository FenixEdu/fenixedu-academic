<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<p></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="infoop"><bean:message key="label.marksOnline.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=loadMarksOnline&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadMarksOnline"/></html:link></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
    <td class="infoop"><bean:message key="label.fileUpload.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=loadFile&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadFileMarks"/></html:link></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="infoop"><bean:message key="label.publishMarks.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=preparePublishMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.publishMarks"/></html:link></p>
<br />

<%--
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td class="infoop"><bean:message key="label.submitMarks.information" /></td>
  </tr>
</table>
<p><html:link page="<%= "/marksList.do?method=submitMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.submitMarks"/></html:link></p>
<br />
--%>