<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean" %>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<html:xhtml/>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>

<em><bean:message key="title.manage.rooms"/></em>
<h2><bean:message key="title.editarSala"/></h2>

<p>
	<span class="error"><!-- Error messages go here --><html:errors /></span>
</p>

<html:form action="/editarSala">
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Name" property="selectRoomCriteria_Name" value="<%= ""+request.getAttribute("selectRoomCriteria_Name")%>"/>            
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Building" property="selectRoomCriteria_Building" value="<%= ""+request.getAttribute("selectRoomCriteria_Building")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Floor" property="selectRoomCriteria_Floor" value="<%= ""+request.getAttribute("selectRoomCriteria_Floor")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_Type" property="selectRoomCriteria_Type" value="<%= ""+request.getAttribute("selectRoomCriteria_Type")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityNormal" property="selectRoomCriteria_CapacityNormal" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityNormal")%>"/>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.selectRoomCriteria_CapacityExame" property="selectRoomCriteria_CapacityExame" value="<%= ""+request.getAttribute("selectRoomCriteria_CapacityExame")%>"/>
	<html:hidden alt="<%= PresentationConstants.SELECTED_ROOM_INDEX%>" property="<%= PresentationConstants.SELECTED_ROOM_INDEX%>" value="<%= ""+request.getAttribute(PresentationConstants.SELECTED_ROOM_INDEX)%>"/>	
	
  <table class="tstyle5 thlight thright">
    <tr>
      <th><bean:message key="sala.normalCapacity"/></th>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityNormal" property="capacityNormal" size="3" maxlength="4"/></td>
    </tr>
    <tr>
      <th><bean:message key="sala.examinationCapacity"/></th>
      <td><html:text bundle="HTMLALT_RESOURCES" altKey="text.capacityExame" property="capacityExame" size="3" maxlength="4"/></td>
    </tr>
  </table>

<p>
	<html:submit>Guardar</html:submit>
	<html:reset>Limpar</html:reset>
</p>

</html:form>

