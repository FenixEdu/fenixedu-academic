<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table width="300" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img height="204" src="<%= request.getContextPath() %>/images/student_01.jpg" alt="<bean:message key="student_01" bundle="IMAGE_RESOURCES" />" width="300" /></td>
  </tr>
  <tr>
    <td><img height="204" src="<%= request.getContextPath() %>/images/student_02.jpg" alt="<bean:message key="student_02" bundle="IMAGE_RESOURCES" />" width="300" /></td>
  </tr>
</table>