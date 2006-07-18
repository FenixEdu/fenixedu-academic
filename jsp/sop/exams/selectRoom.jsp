<%@ page language="java" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.sop.utils.SessionConstants" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<script language="Javascript" type="text/javascript">
<!--
var select = false;

function invertSelect(){
	if ( select == false ) { 
		select = true; 
	} else { 
		select = false;
	}
	for (var i=0; i<document.roomExamSearchForm.roomsId.length; i++){
		var e = document.roomExamSearchForm.roomsId[i];
		if (select == true) { e.checked = true; } else { e.checked = false; }
	}
}

function cleanSelect() { 
	select = false; 
	document.roomExamSearchForm.roomsId[0].checked = false; 
}
// -->
</script>

<html:form action="/roomExamSearch">
		
<h2>Seleccione as Salas a Consultar</h2>

<span class="error"><html:errors/></span>
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="show"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.page" property="page" value="1"/> 
<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.executionPeriodOID" property="executionPeriodOID"/> 
	
<logic:present name="<%=SessionConstants.ROOMS_LIST%>">		

	<bean:define id="rooms" name="<%=SessionConstants.ROOMS_LIST%>"/>

	<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.roomsId" property="roomsId" onclick="invertSelect()">
	     0 </html:multibox>
	Seleccionar todas 
		
	<table border="1">
		<tr>
			<th></th>
			<th><bean:message key="property.room.name"/></th>
			<th><bean:message key="property.room.type"/></th>
			<th><bean:message key="property.room.building"/></th>
			<th><bean:message key="property.room.floor"/></th>
			<th><bean:message key="property.room.capacity.normal"/></th>
			<th><bean:message key="property.room.capacity.exame"/></th>
		</tr>

		<logic:iterate id ="infoRoom" name="rooms">
			<tr>
				<td>
					<html:multibox bundle="HTMLALT_RESOURCES" altKey="multibox.roomsId" property="roomsId" onclick="cleanSelect()">
						<bean:write name="infoRoom" property="idInternal"/>
					</html:multibox>
				</td>
				<td>
					<bean:write name="infoRoom" property="nome"/>
				</td>
				<td>
					<bean:define id="tipo" name="infoRoom" property="tipo"/>
					<logic:equal name="tipo" value="A">Anfiteatro</logic:equal>
					<logic:equal name="tipo" value="P">Plana</logic:equal>
					<logic:equal name="tipo" value="L">Laborat�rio</logic:equal>
				</td>
				<td>
					<bean:write name="infoRoom" property="edificio"/>
				</td>
				<td>
					<bean:write name="infoRoom" property="piso"/>
				</td>
				<td>
					<bean:write name="infoRoom" property="capacidadeNormal"/> lugares
				</td>
				<td>
					<bean:write name="infoRoom" property="capacidadeExame"/> lugares
				</td>
			</tr>
		</logic:iterate>
	</table>
</logic:present>
<logic:notPresent name="<%=SessionConstants.ROOMS_LIST%>">
	N�o existem salas com as caracteristicas especificadas.<br/>
</logic:notPresent>
<br/>
	<html:submit bundle="HTMLALT_RESOURCES" altKey="submit.submit" styleClass="inputbutton" onclick="cleanSelect()">
		<bean:message key="lable.choose"/>
	</html:submit>
	
<%--
	<html:cancel bundle="HTMLALT_RESOURCES" altKey="cancel.cancel" value="Cancelar" styleClass="inputbutton">
		<bean:message key="label.cancel"/>
	</html:cancel>
--%>
</html:form>	