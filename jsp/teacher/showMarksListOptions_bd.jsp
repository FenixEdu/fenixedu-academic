<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>



<p></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="<%= "/marksList.do?method=loadFile&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadFileMarks"/></html:link></td>
  </tr>
</table>
<p>Este módulo permite fazer o <i>Upload</i> das notas a partir de um ficheiro texto (<strong>txt</strong>). Neste ficheiro deve constar o <strong>Número</strong> e <strong>Nota</strong> dos alunos inscritos na disciplina.  </p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="<%= "/marksList.do?method=loadMarksOnline&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.loadMarksOnline"/></html:link></td>
  </tr>
</table>
<p>Este módulo permite Lançar as notas <i>On Line</i> de todos os alunos inscritos na disciplina.</p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="<%= "/marksList.do?method=preparePublishMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.publishMarks"/></html:link></td>
  </tr>
</table>
<p></p>
<br />

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td nowrap="nowrap" class="infoIcons"><img alt="" height="15" src="<%= request.getContextPath() %>/images/info.gif" width="15"> 
    </td>
    <td class="infoop"><html:link page="<%= "/marksList.do?method=submitMarks&amp;objectCode=" + pageContext.findAttribute("objectCode") + "&amp;examCode=" + pageContext.findAttribute("examCode") %>" ><bean:message key="link.submitMarks"/></html:link></td>
  </tr>
</table>
<p></p>
<br />

