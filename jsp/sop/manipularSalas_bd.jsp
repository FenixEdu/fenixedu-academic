<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html:form action="/manipularSalas">
  <center> <b> <bean:message key="manipularSalas.titleSuccess"/> </b> </center>
  <br/>
  <span class="error"><html:errors/></span>
  <logic:present name="publico.infoRooms" scope="session">
    <table align="center" border=1 cellpadding='5'>
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
    <br/>
    
    <table align="center">
      <tr align="center">
        <td>
          <html:submit property="operation"><bean:message key="manipularSalas.verSalaOperation"/></html:submit>
        </td>
        <td width="20"> </td>
        <td>
          <html:submit property="operation"><bean:message key="manipularSalas.editarSalaOperation"/></html:submit>
        </td>
        <td width="20"> </td>
         <td>
           <html:submit property="operation"><bean:message key="manipularSalas.apagarSalaOperation"/></html:submit>
         </td>
       </tr>
    </table>
    <br/>
  </logic:present>

  <logic:notPresent name="publico.infoRooms" scope="session">
    <table align="center" border='1' cellpadding='5'>
      <tr align="center">
        <td>
          <font color='red'> <bean:message key="manipularSalas.titleInsuccess"/></font>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
