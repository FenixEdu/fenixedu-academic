<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<html:form action="/selectRoomToViewForExams">
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
            <html:hidden property="nome" value='<bean:write name="infoRoom" property="nome"/>'/>
          </td>
          <td><bean:write name="infoRoom" property="nome"/></td>
        </tr>
        <% i++; %>
      </logic:iterate>
    </table>
    <br/>
<html:submit property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.verSalaOperation"/></html:submit>
  </logic:present>
  <logic:notPresent name="publico.infoRooms" scope="request">
    <table>
      <tr>
        <td>
          <span class="error"><bean:message key="message.rooms.none"/></span>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
