<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<p><strong>&raquo; Gest&atilde;o de Salas</strong></p>
<ul>
  <li><html:link page="/prepararSalaForm.do?method=prepareCreate"><bean:message key="principalSalas.createSalaLinkName"/></html:link></li>
  <li><html:link page="/prepararSalaForm.do?method=prepareSearch"><bean:message key="principalSalas.manipulateSalasLinkName"/></html:link></li>
  <li><html:link page="/searchEmptyRoomsDA.do?method=prepare"><bean:message key="link.search.empty.rooms"/></html:link></li>
</ul>
