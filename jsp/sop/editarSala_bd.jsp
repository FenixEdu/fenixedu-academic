<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>

<center><font color='#034D7A' size='5'> <b> <bean:message key="title.editarSala"/> </b> </font></center>
<br/>
<span class="error"><html:errors/></span>
<html:form action="/editarSala">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Name" property="selectRoomCriteria_Name" value="<%= ""+request.getAttribute("selectRoomCriteria_Name")%>"/>            
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Building" property="selectRoomCriteria_Building" value="<%= ""+request.getAttribute("selectRoomCriteria_Building")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Floor" property="selectRoomCriteria_Floor" value="<%= ""+request.getAttribute("selectRoomCriteria_Floor")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Type" property="selectRoomCriteria_Type" value="<%= ""+request.getAttribute("selectRoomCriteria_Type")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityNormal" property="selectRoomCriteria_CapacityNormal" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityNormal")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityExame" property="selectRoomCriteria_CapacityExame" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityExame")%>"/>
	<html:hidden alt="<%= SessionConstants.SELECTED_ROOM_INDEX%>" property="<%= SessionConstants.SELECTED_ROOM_INDEX%>" value="<%= ""+request.getAttribute(SessionConstants.SELECTED_ROOM_INDEX)%>"/>	
	
  <table align="center" cellspacing="10">
    <tr>
      <td align="right" height="40"><bean:message key="sala.name"/></td>
      <td align="left" height="40"><html:text bundle="HTMLALT_RESOURCES" altKey="text.name" property="name" size="11" maxlength="20"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.bulding"/></td>
      <td align="left" height="40">
        <html:select bundle="HTMLALT_RESOURCES" altKey="select.building" property="building" size="1">
          <html:options collection="publico.buildings" property="value" labelProperty="label"/>
        </html:select>
      </td>
    </tr>
    <tr><td align="right" height="40"><bean:message key="sala.floor"/></td>
      <td align="left" height="40"><html:text bundle="HTMLALT_RESOURCES" altKey="text.floor" property="floor" size="2" maxlength="2"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.type"/></td>
      <td align="left" height="40">
        <html:select bundle="HTMLALT_RESOURCES" altKey="select.type" property="type" size="1">
          <html:options collection="publico.types" property="value" labelProperty="label"/>
        </html:select>
      </td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.normalCapacity"/></td>
      <td align="left" height="40"><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/></td>
    </tr>
    <tr>
      <td align="right" height="40"><bean:message key="sala.examinationCapacity"/></td>
      <td align="left" height="40"><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/></td>
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

