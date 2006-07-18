<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<table width="300" border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td><img height="204" src="<%= request.getContextPath() + "/images/SOP_entradaFoto01.jpg" %>" alt="<bean:message key="SOP_entradaFoto01" bundle="IMAGE_RESOURCES" />" /></td>
  </tr>
  <tr>
    <td><img height="204" src="<%= request.getContextPath() + "/images/SOP_entradaFoto02.jpg" %>" alt="<bean:message key="SOP_entradaFoto02" bundle="IMAGE_RESOURCES" />" width="300" /></td>
  </tr>
</table>

