<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<p><em>Administra&ccedil;&atilde;o</em></p>
<h2>Depura&ccedil;&atilde;o de Calendarios</h2>
<bean:define id="user" name="user"></bean:define>
<form action="/manager/calendarDebug.do?method=show" method="post">
	<logic:present name="user">
		<input type="text" name="user"  id="user" value="<%= user %>"/>
	</logic:present>
	<logic:notPresent name="user">
		<input type="text" name="user" id="user"/>
	</logic:notPresent>
	
	<button>Mostrar</button>
</form>
<logic:present name="list">
<logic:iterate id="mapEntry" name="list">
  <bean:define id="links" name="mapEntry" property="value"></bean:define>
  <bean:define id="reg" name="mapEntry" property="key"></bean:define>
  
  <h3><bean:write name="reg" property="degreeNameWithDegreeCurricularPlanName"></bean:write></h3>
  <%= links %>
  
</logic:iterate>
</logic:present>