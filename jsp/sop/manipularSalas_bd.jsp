<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<html:form action="/manipularSalas">
	<html:hidden property="selectRoomCriteria_Name" value="<%= ""+request.getAttribute("selectRoomCriteria_Name")%>"/>            
	<html:hidden property="selectRoomCriteria_Building" value="<%= ""+request.getAttribute("selectRoomCriteria_Building")%>"/>
	<html:hidden property="selectRoomCriteria_Floor" value="<%= ""+request.getAttribute("selectRoomCriteria_Floor")%>"/>
	<html:hidden property="selectRoomCriteria_Type" value="<%= ""+request.getAttribute("selectRoomCriteria_Type")%>"/>
	<html:hidden property="selectRoomCriteria_CapacityNormal" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityNormal")%>"/>
	<html:hidden property="selectRoomCriteria_CapacityExame" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityExame")%>"/>
	<html:hidden property="executionPeriodId" value="<%= ""+request.getAttribute("executionPeriodId")%>"/>	
<h2><bean:message key="manipularSalas.titleSuccess"/></h2>
  <br/>
  <span class="error"><html:errors/></span>
  <logic:present name="<%= SessionConstants.SELECTED_ROOMS%>" scope="request">
    <table border="0" cellpadding="5">
      <%! int i; %>
      <% i = 0; %>
      <logic:iterate id="infoRoom" name="<%= SessionConstants.SELECTED_ROOMS%>">
        <tr align="center">
          <td>
            <html:radio property="index" value="<%= (new Integer(i)).toString()%>"/>
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
  <logic:notPresent name="<%= SessionConstants.SELECTED_ROOMS%>" scope="request">
    <table>
      <tr>
        <td>
          <span class="error"><bean:message key="manipularSalas.titleInsuccess"/></span>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
