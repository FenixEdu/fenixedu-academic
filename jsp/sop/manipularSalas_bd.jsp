<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:form action="/manipularSalas">
	<html:hidden property="selectRoomsName" value="<%= ""+request.getAttribute("selectRoomsName")%>"/>            
	<html:hidden property="selectRoomsBuilding" value="<%= ""+request.getAttribute("selectRoomsBuilding")%>"/>
	<html:hidden property="selectRoomsFloor" value="<%= ""+request.getAttribute("selectRoomsFloor")%>"/>
	<html:hidden property="selectRoomsType" value="<%= ""+request.getAttribute("selectRoomsType")%>"/>
	<html:hidden property="selectRoomsCapacityNormal" value="<%= ""+request.getAttribute("selectRoomsCapacityNormal")%>"/>
	<html:hidden property="selectRoomsCapacityExame" value="<%= ""+request.getAttribute("selectRoomsCapacityExame")%>"/>
<h2><bean:message key="manipularSalas.titleSuccess"/></h2>
  <br/>
  <span class="error"><html:errors/></span>
  <logic:present name="publico.infoRooms" scope="request">
    <table border="0" cellpadding="5">
      <%! int i; %>
      <% i = 0; %>
      <logic:iterate id="infoRoom" name="publico.infoRooms">
        <tr align="center">
          <td>
            <html:radio property="index" value="<%= (new Integer(i)).toString()%>"/>
<!--            <html:hidden property="nome" value='<bean:write name="infoRoom" property="nome"/>'/>-->
          </td>
          <td><bean:write name="infoRoom" property="nome"/></td>
        </tr>
        <% i++; %>
      </logic:iterate>
    </table>
    <br/>
    
<html:submit property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.verSalaOperation"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.editarSalaOperation"/></html:submit>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.apagarSalaOperation"/></html:submit>
  </logic:present>
  <logic:notPresent name="publico.infoRooms" scope="request">
    <table>
      <tr>
        <td>
          <span class="error"><bean:message key="manipularSalas.titleInsuccess"/></span>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
