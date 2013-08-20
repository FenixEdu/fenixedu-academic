<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %><%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page import="net.sourceforge.fenixedu.presentationTier.Action.resourceAllocationManager.utils.PresentationConstants" %>
<html:xhtml/>
 
<html:form action="/roomSearch">
		
	<logic:iterate indexId="i" id="room" name="availableRooms" type="net.sourceforge.fenixedu.dataTransferObject.InfoRoom">
		<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.availableRoomsId" property="availableRoomsId" value="<%= room.getExternalId().toString() %>" />
	</logic:iterate>

	<em class="printhidden"><bean:message key="link.writtenEvaluationManagement"/></em>
	<h2>Salas livres dia <bean:write name="<%=PresentationConstants.DATE%>"/>
 		das <bean:write name="<%=PresentationConstants.START_TIME%>"/>
 		às <bean:write name="<%=PresentationConstants.END_TIME%>"/></h2>
 
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.method" property="method" value="sort"/>
	
	
	<logic:present name="<%=PresentationConstants.AVAILABLE_ROOMS%>">		
		<bean:define id="availableRooms" name="<%=PresentationConstants.AVAILABLE_ROOMS%>"/>
		<table class="tstyle4 showborder">
			<tr>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="name" onclick="this.form.method.value='sort';this.form.submit();"/> Nome</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="type" onclick="this.form.method.value='sort';this.form.submit();"/> Tipo</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="building" onclick="this.form.method.value='sort';this.form.submit();"/> Edificio</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="floor" onclick="this.form.method.value='sort';this.form.submit();"/> Piso</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="normal" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Normal</th>
				<th><html:radio bundle="HTMLALT_RESOURCES" altKey="radio.sortParameter" property="sortParameter" value="exam" onclick="this.form.method.value='sort';this.form.submit();"/> Capacidade Exame</th>
			</tr>
				<logic:iterate id ="infoRoom" name="availableRooms">
			<tr>
				<td>
					 <bean:write name="infoRoom" property="nome"/>
				</td>
				<td>
					 <bean:write name="infoRoom" property="tipo"/>					 
				</td>
				<td>
					 <bean:write name="infoRoom" property="edificio"/>
				</td>
				<td class="acenter">
					 <bean:write name="infoRoom" property="piso"/>
				</td>
				<td class="aright">
					<bean:write name="infoRoom" property="capacidadeNormal"/> lugares
				</td>
				<td class="aright">
					<bean:write name="infoRoom" property="capacidadeExame"/> lugares
				</td>
			</tr>
				</logic:iterate>
		</table>
	</logic:present>
	<logic:notPresent name="<%=PresentationConstants.AVAILABLE_ROOMS%>">
		Não existem salas disponíveis.
	</logic:notPresent>
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.day" property="day" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.month" property="month" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.year" property="year" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningHour" property="beginningHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.beginningMinute" property="beginningMinute" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endHour" property="endHour" />
	<html:hidden bundle="HTMLALT_RESOURCES" altKey="hidden.endMinute" property="endMinute" />

</html:form> 