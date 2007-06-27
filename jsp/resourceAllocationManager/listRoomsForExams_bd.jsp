<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:form action="/selectRoomToViewForExams">
<h2><bean:message key="manipularSalas.titleSuccess"/></h2>
  <br/>
  <span class="error"><!-- Error messages go here --><html:errors /></span>
  <logic:present name="selectedRooms" scope="request">
    <table border="0" cellpadding="5">
      <%! int i; %>
      <% i = 0; %>
      <logic:iterate id="infoRoom" name="selectedRooms">
        <tr align="center">
          <td>
          	<bean:define id="roomId" name="infoRoom" property="idInternal"/>
            <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.index" property="index" value="<%= pageContext.findAttribute("roomId").toString() %>"/>
            <html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.roomId" property="roomId" value="<%= pageContext.findAttribute("roomId").toString() %>"/>
          </td>
          <td><bean:write name="infoRoom" property="nome"/></td>
        </tr>
        <% i++; %>
      </logic:iterate>
    </table>
    <br/>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.verSalaOperation"/></html:submit>
  </logic:present>
  <logic:notPresent name="selectedRooms" scope="request">
    <table>
      <tr>
        <td>
          <span class="error"><!-- Error messages go here --><bean:message key="message.rooms.none"/></span>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
