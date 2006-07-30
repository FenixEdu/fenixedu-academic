<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<html:form action="/manipularSalas">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Name" property="selectRoomCriteria_Name" value="<%= ""+request.getAttribute("selectRoomCriteria_Name")%>"/>            
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Building" property="selectRoomCriteria_Building" value="<%= ""+request.getAttribute("selectRoomCriteria_Building")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Floor" property="selectRoomCriteria_Floor" value="<%= ""+request.getAttribute("selectRoomCriteria_Floor")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Type" property="selectRoomCriteria_Type" value="<%= ""+request.getAttribute("selectRoomCriteria_Type")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityNormal" property="selectRoomCriteria_CapacityNormal" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityNormal")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityExame" property="selectRoomCriteria_CapacityExame" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityExame")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodId" property="executionPeriodId" value="<%= ""+request.getAttribute("executionPeriodId")%>"/>	
<h2><bean:message key="manipularSalas.titleSuccess"/></h2>
  <br/>
  <span class="error"><!-- Error messages go here --><html:errors /></span>
  <logic:present name="<%= SessionConstants.SELECTED_ROOMS%>" scope="request">
    <table border="0" cellpadding="5">
      <%! int i; %>
      <% i = 0; %>
      <logic:iterate id="infoRoom" name="<%= SessionConstants.SELECTED_ROOMS%>">
        <tr align="center">
          <td>
            <html:radio bundle="HTMLALT_RESOURCES" altKey="radio.index" property="index" value="<%= (new Integer(i)).toString()%>"/>
          </td>
          <td><bean:write name="infoRoom" property="nome"/></td>
        </tr>
        <% i++; %>
      </logic:iterate>
    </table>
    <br/>
    
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.verSalaOperation"/></html:submit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.editarSalaOperation"/></html:submit>
<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.operation" property="operation" styleClass="inputbutton"><bean:message key="manipularSalas.apagarSalaOperation"/></html:submit>
  </logic:present>
  <logic:notPresent name="<%= SessionConstants.SELECTED_ROOMS%>" scope="request">
    <table>
      <tr>
        <td>
          <span class="error"><!-- Error messages go here --><bean:message key="manipularSalas.titleInsuccess"/></span>
        </td>
      </tr>
    </table>
  </logic:notPresent>
</html:form>
