<%@ page language="java" %>
<%@ page import="ServidorApresentacao.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<table width="100%" cellspacing="0">
	<tr>
		<td class="infoselected"><br /><br /><jsp:include page="examContext.jsp"/></td>
    </tr>
</table>
<br/>
<h2><bean:message key="title.exam.setRooms"/></h2>
<span class="error"><html:errors /></span>
<html:form action="/editExamRooms">
	<html:hidden property="page" value="1"/>
	<logic:present name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		<bean:define id="roomsHashTable" name="<%=SessionConstants.AVAILABLE_ROOMS%>"/>
			<logic:iterate id="infoRoomsOfBuilding" name="roomsHashTable">
				<%int i=0;%>
				<logic:iterate id ="infoRoom" name="infoRoomsOfBuilding">
					<%if(i==0) {%>
					<strong><bean:write name="infoRoom" property="edificio"/></strong><br/>
					<%};%>
					<html:multibox property="selectedRooms">
						<bean:write name="infoRoom" property="idInternal"/>
					</html:multibox>
					<bean:write name="infoRoom" property="nome"/>&nbsp;&nbsp;
					(<bean:write name="infoRoom" property="capacidadeExame"/> lugares)<br/>
					<%i++;%>
				</logic:iterate>
				</br>
			</logic:iterate>
	</logic:present>
	
	<logic:notPresent name="<%=SessionConstants.AVAILABLE_ROOMS%>">
		No rooms available.
	</logic:notPresent>	
<br />
<html:hidden property="method" value="select"/>
<html:submit styleClass="inputbutton">
	<bean:message key="label.choose"/>
</html:submit>
<html:reset value="Limpar" styleClass="inputbutton">
	<bean:message key="label.clear"/>
</html:reset>
</html:form>