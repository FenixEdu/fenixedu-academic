<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>

<center><font color='#034D7A' size='5'> <b> <bean:message key="title.editarSala"/> </b> </font></center>
<br/>
<span class="error"><html:errors/></span>
<html:form action="/editarSala">
Index: <bean:write name="selectedRoomIndex" scope="request"/>
	<bean:define id="infoRoomOID" name="<%= SessionConstants.ROOM_OID%>" scope="request"/>
	<html:hidden property="<%= SessionConstants.ROOM_OID%>" value="<%=infoRoomOID.toString()%>"/>
	<html:hidden property="selectRoomsName" value="<%= ""+request.getAttribute("selectRoomsName")%>"/>            
	<html:hidden property="selectRoomsBuilding" value="<%= ""+request.getAttribute("selectRoomsBuilding")%>"/>
	<html:hidden property="selectRoomsFloor" value="<%= ""+request.getAttribute("selectRoomsFloor")%>"/>
	<html:hidden property="selectRoomsType" value="<%= ""+request.getAttribute("selectRoomsType")%>"/>
	<html:hidden property="selectRoomsCapacityNormal" value="<%= ""+request.getAttribute("selectRoomsCapacityNormal")%>"/>
	<html:hidden property="selectRoomsCapacityExame" value="<%= ""+request.getAttribute("selectRoomsCapacityExame")%>"/>
	<html:hidden property="selectRoomsIndex" value="<%= ""+request.getAttribute("selectedRoomsIndex")%>"/>	
	
  <table align="center" cellspacing="10">
    <tr>
      <td align="right" height="40"><bean:message key="sala.name"/></td>
      <td align="left" height="40"><html:text property="name" size="11" maxlength="20"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.bulding"/></td>
      <td align="left" height="40">
        <html:select property="building" size="1">
          <html:options collection="publico.buildings" property="value" labelProperty="label"/>
        </html:select>
      </td>
    </tr>
    <tr><td align="right" height="40"><bean:message key="sala.floor"/></td>
      <td align="left" height="40"><html:text property="floor" size="2" maxlength="2"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.type"/></td>
      <td align="left" height="40">
        <html:select property="type" size="1">
          <html:options collection="publico.types" property="value" labelProperty="label"/>
        </html:select>
      </td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.normalCapacity"/></td>
      <td align="left" height="40"><html:text property="capacityNormal" size="3" maxlength="4"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.examinationCapacity"/></td>
      <td align="left" height="40"><html:text property="capacityExame" size="3" maxlength="4"/></td>
    </tr>
  </table>
  <br/>
  <table align="center">
    <tr align="center">
      <td><html:submit>Guardar</html:submit></td>
      <td width="20"></td>
      <td><html:reset>Limpar</html:reset></td>
    </tr>
  </table>
</html:form>

