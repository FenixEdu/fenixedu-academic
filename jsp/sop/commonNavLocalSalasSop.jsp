<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<ul>
  <li class="navheader">Gest&atilde;o de Salas</li>
  <li><html:link page="/prepararSalaForm.do?method=prepareCreate"><bean:message key="principalSalas.createSalaLinkName"/></html:link></li>
  <li><html:link page="/prepararSalaForm.do?method=prepareSearch"><bean:message key="principalSalas.manipulateSalasLinkName"/></html:link></li>
  <li><html:link page="/searchEmptyRoomsDA.do?method=prepare"><bean:message key="link.search.empty.rooms"/></html:link></li>
  <li class="navheader"><bean:message key="link.manage.buildings"/></li>
	<li>
		<html:link page="/manageBuildings.do?method=prepare">
			<bean:message key="link.manage.buildings"/>
		</html:link>
	</li>
</ul>