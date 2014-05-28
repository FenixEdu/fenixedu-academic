<%--

    Copyright © 2002 Instituto Superior Técnico

    This file is part of FenixEdu Core.

    FenixEdu Core is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FenixEdu Core is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.

--%>
<%@ page language="java" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>
<%@ taglib uri="http://fenix-ashes.ist.utl.pt/fenix-renderers" prefix="fr"%>

<h2>Depura&ccedil;&atilde;o de Calendarios</h2>
<bean:define id="user" name="user"></bean:define>
<form action="${pageContext.request.contextPath}/manager/calendarDebug.do?method=show" method="post">
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